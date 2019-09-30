package com.primeton.
di.trans.steps.canalinput;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.primeton.di.core.CheckResultInterface;
import com.primeton.di.core.exception.DataIntegrationException;
import com.primeton.di.core.exception.DataIntegrationStepException;
import com.primeton.di.trans.Trans;
import com.primeton.di.trans.TransMeta;
import com.primeton.di.trans.step.BaseStep;
import com.primeton.di.trans.step.StepDataInterface;
import com.primeton.di.trans.step.StepInterface;
import com.primeton.di.trans.step.StepMeta;
import com.primeton.di.trans.step.StepMetaInterface;
import com.primeton.di.trans.steps.canalinput.utils.CanalUtil;
import com.primeton.di.trans.steps.canalinput.utils.DatabaseUtils;
import com.primeton.di.trans.steps.canalinput.utils.MD5Utils;


/**
 * 
 * 类主要负责处理数据将空间数据从地理数据库中读取出来，然后放入下一个步骤
 * @author my
 *
 */
public class CanalInput extends BaseStep implements StepInterface{

private static Class<?> PKG = CanalInput.class;
	
	private CanalInputMeta meta;
	
	private CanalInputData data;
	
	private Connection conn = null;
	private CanalConnector connector = null;
	private CanalUtil canalUtil = null;
	
	
	
	public CanalInput(StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr, TransMeta transMeta,
			Trans trans) {
		super(stepMeta, stepDataInterface, copyNr, transMeta, trans);
	}
	
	
	public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
		meta = (CanalInputMeta) smi;
		data = (CanalInputData) sdi;
	
		if (super.init(smi, sdi)) {
			getConn();
			List<CheckResultInterface> remarks = new ArrayList<CheckResultInterface>();

			data.cols = meta.getColumns().split(",");
			data.outputRowMeta = CanalInputMeta.buildRow(conn,meta, remarks, getStepname(),data.cols);
			
			return true;
		}
		return false;
		
	}
	
	public void getConn(){
		if(conn==null){
		 try {
			 conn = DatabaseUtils.getConnection("mysql", meta.getServer(), meta.getPort(), meta.getUsername(), meta.getPassword(), meta.getDatabaseName());
		 }catch (Exception e) {
	           e.printStackTrace();
	       }
		}
		 
	}
	
	/** Stop the running query */
	public synchronized void stopRunning(StepMetaInterface smi, StepDataInterface sdi) throws DataIntegrationException
	{
		meta = (CanalInputMeta) smi;
		data = (CanalInputData) sdi;

        setStopped(true);
     
    	try {
    		if(conn!=null && !conn.isClosed()){
    		conn.close();
    		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	void createOutputValueMapping() throws DataIntegrationException {
	    data.outputRowMeta = getInputRowMeta().clone();
	    meta.getFields( getInputRowMeta(), getStepname(), null, null, this, repository, metaStore );

	  }
	

//	public   static   String   inputStream2String(InputStream   is)   throws   IOException{ 
//        ByteArrayOutputStream   baos   =   new   ByteArrayOutputStream(); 
//        int   i=-1; 
//        while((i=is.read())!=-1){ 
//        baos.write(i); 
//        } 
//       return   baos.toString(); 
//	} 

	public void dispose( StepMetaInterface smi, StepDataInterface sdi ) {
		meta = (CanalInputMeta) smi;
		data = (CanalInputData) sdi;

	    
	    try {
	      if(conn!=null){
	    	  conn.close();
	      }
	    }catch (Exception e) {
			logError("dispose has error ! :", e);
		}

	    super.dispose( smi, sdi );
	  }

	/**
	 * Canal连接
		Canal实例destination数据订阅
		异常处理（失败、超时等）
		ROW模式日志解析、清洗为产品标准数据结构
		Mixed模式日志解析、清洗为产品标准数据结构（优先级低）
		单表模式下，表字段和标准数据匹配逻辑功能
		多表模式下，表名作为属性逻辑匹配功能
		字段元数据获取
		标准数据推送到缓存

	 */

	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) {
		meta = (CanalInputMeta) smi;
		data = (CanalInputData) sdi;
		
		if (first) {						
//			first = false;
			//单表逻辑处理
			if (meta.getIsOneTableString() ==0) {
				processRowOneTable(data,meta);
			}else {
				//多表逻辑处理
			}
			
			return true;
		}else {
			setOutputDone();
			return false;
		}
	}
	
	/**
	 * 单表逻辑处理
	 * @param data
	 */
	private void processRowOneTable(CanalInputData data,CanalInputMeta meta){
		connectorInit();

		//在CanalInputData data中加eventType，从hbaseoutput获取判断
		//rowkey由hbaseoutput指定
		Object[] row = new Object[data.cols.length]; 	
		try {		
				JSONArray canalResult = canalUtil.startSubscribe(connector, meta);
				for (Object object : canalResult) {
					boolean  hasData = false;
					JSONObject resultRow = (JSONObject) object;
					data.eventType = resultRow.getString("eventType");
					if (resultRow.getString("eventType").equalsIgnoreCase("insert") ||
							resultRow.getString("eventType").equalsIgnoreCase("update")) {
						JSONObject columns = resultRow.getJSONObject("columns");				
						for (int i = 0; i < row.length; i++) {
							row[i] = columns.get(data.cols[i]);
							hasData = true;
						}

					}
					if (resultRow.getString("eventType").equalsIgnoreCase("delete")) {
						JSONObject conditions = resultRow.getJSONObject("condition");				
						for (int i = 0; i < row.length; i++) {
							if (conditions.get(data.cols[i]) != null) {
								row[i] = conditions.get(data.cols[i]);
								hasData = true;
							}
							
						}
					}
					
					
					if (hasData) {					
						log.logBasic("hasData *****");
						putRow(data.outputRowMeta, row);
					}
				}

			} catch (DataIntegrationStepException e) {
				connectorClose();
				e.printStackTrace();
			} 
			incrementLinesInput();
			incrementLinesOutput();
		
	}
	
	
	
	
	private void connectorInit(){
		try {
			//创建订阅，开启连接
			if (connector == null) {
				connector = CanalConnectors.newSingleConnector(new InetSocketAddress(meta.getServer(), meta.getCanalPort()), meta.getCanalInstance(), "",
						"");
				connector.connect();
				if (canalUtil == null) {
				canalUtil = new CanalUtil(meta.getBinlogType(), meta.getIntervalTime(), meta.getOneTableName(),
						meta.getManyTablesName(), meta.getDatabaseName());
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("CanalConnectors connect has error! " + 
					meta.getBinlogType()+ meta.getIntervalTime()+ meta.getOneTableName()+
					meta.getManyTablesName()+ meta.getDatabaseName() + "e : " + e
		);
		}
		
		
	}
	
	private void connectorClose(){
		//关闭连接
		if (connector != null) {
			connector.disconnect();
		}
	}
}
