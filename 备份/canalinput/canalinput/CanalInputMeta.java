package com.primeton.di.trans.steps.canalinput;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.esri.sde.sdk.client.SeColumnDefinition;
import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeTable;
import com.primeton.di.core.CheckResultInterface;
import com.primeton.di.core.Const;
import com.primeton.di.core.Counter;
import com.primeton.di.core.database.DatabaseMeta;
import com.primeton.di.core.exception.DataIntegrationException;
import com.primeton.di.core.exception.DataIntegrationStepException;
import com.primeton.di.core.exception.DataIntegrationXMLException;
import com.primeton.di.core.row.RowMeta;
import com.primeton.di.core.row.RowMetaInterface;
import com.primeton.di.core.row.ValueMeta;
import com.primeton.di.core.row.ValueMetaInterface;
import com.primeton.di.core.variables.VariableSpace;
import com.primeton.di.core.xml.XMLHandler;
import com.primeton.di.repository.ObjectId;
import com.primeton.di.repository.Repository;
import com.primeton.di.trans.Trans;
import com.primeton.di.trans.TransMeta;
import com.primeton.di.trans.step.BaseStepMeta;
import com.primeton.di.trans.step.StepDataInterface;
import com.primeton.di.trans.step.StepInterface;
import com.primeton.di.trans.step.StepMeta;
import com.primeton.di.trans.step.StepMetaInterface;
import com.primeton.di.trans.steps.canalinput.utils.DatabaseUtils;
import com.primeton.metastore.api.IMetaStore;

/**
 * 这个类主要用于存储界面设置的元数据信息，可以将设置的信息保存到数据库或文件中，并能够为CanalInput类提供所需要的元数据。
 * 
 * @author my
 *
 */
public class CanalInputMeta extends BaseStepMeta implements StepMetaInterface{
	private String canalServer;
	private int canalPort;
	private String canalInstance;
	private String binlogType; //日志模式选择ROW/Mixed
	private int intervalTime;  //间隔时间配置ms
	
	private String username;
	private String password;
	private String server;
	private int port;
	private String databaseName;
	
	
	private int isOneTableString = 0; //单表多表选择按钮,0为单表，1为多表
	
	private String manyTablesName;//暂时无用，tables代替了
	private String tables; 
	
	private static String oneTableName;
	private String columns; //只在单表模式可配置
	
//	private String where; //条件
//	private int isString	=	1;	//是否转为字符串
//	private boolean variableReplacementActive;
	@Override
	public void setDefault() {
		// TODO Auto-generated method stub
		canalServer = "localhost";
		canalPort = 11111;
		canalInstance = "canal";
		binlogType = "ROW"; //日志模式选择ROW/Mixed
		intervalTime = 5000; //间隔时间配置ms
		
		username = "";
		password = "";
		server = "localhost";
		port = 3306;
		databaseName = "";
		
		isOneTableString = 0; //单表多表选择按钮,0为单表，1为多表
		
		manyTablesName = "";
		tables = "";
		oneTableName = "";
		columns = "";
//		where = "";
//		variableReplacementActive = false;
//		isString = 1;
	}

	@Override
	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
			TransMeta transMeta, Trans trans) {
		// TODO Auto-generated method stub
		return new CanalInput(stepMeta, stepDataInterface, copyNr, transMeta, trans);
	}

	@Override
	public StepDataInterface getStepData() {
		// TODO Auto-generated method stub
		return new CanalInputData();
	}

	/**
	 * 当点击验证的时候会调用该方法验证组件元数据配置是否正确
	 */
	public void check(List<CheckResultInterface> remarks,
			TransMeta businessMeta, StepMeta stepMeta,
			RowMetaInterface prev, String[] input, String[] output,
			RowMetaInterface info) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 在载入组件的时候会调用该方法从文件资源中解析该组件的元数据
	 */
	public void loadXML(Node stepnode, List<DatabaseMeta> databases,
			Map<String, Counter> counters) throws DataIntegrationXMLException {
		readData(stepnode);
		
	}
	
	public void readData(Node stepnode) throws DataIntegrationXMLException {
		try{
		
			canalServer = XMLHandler.getTagValue(stepnode, "canalServer");
			canalPort = Const.toInt(XMLHandler.getTagValue(stepnode, "canalPort"),11111);
			canalInstance = XMLHandler.getTagValue(stepnode, "canalInstance");
			binlogType = XMLHandler.getTagValue(stepnode, "binlogType");
			intervalTime = Const.toInt(XMLHandler.getTagValue(stepnode, "intervalTime"),5000);
			
			username = XMLHandler.getTagValue(stepnode, "username");
			password = XMLHandler.getTagValue(stepnode, "password");
			server = XMLHandler.getTagValue(stepnode, "server");
			databaseName = XMLHandler.getTagValue(stepnode, "databaseName");
			port  = Const.toInt(XMLHandler.getTagValue(stepnode, "port"), 3306);
			
			isOneTableString =  Const.toInt(XMLHandler.getTagValue(stepnode, "isOneTableString"),0);
			
			manyTablesName = XMLHandler.getTagValue(stepnode, "manyTablesName");
			
			oneTableName = XMLHandler.getTagValue(stepnode, "oneTableName");
//			where = XMLHandler.getTagValue(stepnode, "where");
			columns = XMLHandler.getTagValue(stepnode, "columns");
			tables = XMLHandler.getTagValue(stepnode, "tables");
//		    variableReplacementActive = "Y".equals( XMLHandler.getTagValue( stepnode, "variables_active" ) );
//			
//			isString  = Const.toInt(XMLHandler.getTagValue(stepnode, "isString"), 1);
		}
		catch(Exception e){
			throw new DataIntegrationXMLException("Unable to load step info from XML", e);
		}
	}
	
	
	/**
	 * 该方法将组件元数据内容存放到数据库中
	 */
	public void saveRep(Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step)
			throws DataIntegrationException {
		try{
			rep.saveStepAttribute(id_transformation, id_step, "canalServer", canalServer);
			rep.saveStepAttribute(id_transformation, id_step, "canalPort",canalPort);
			rep.saveStepAttribute(id_transformation, id_step, "canalInstance",canalInstance);
			rep.saveStepAttribute(id_transformation, id_step, "binlogType",binlogType);
			rep.saveStepAttribute(id_transformation, id_step, "intervalTime",intervalTime);
			
			rep.saveStepAttribute(id_transformation, id_step, "username", username);
			rep.saveStepAttribute(id_transformation, id_step, "password",password);
			rep.saveStepAttribute(id_transformation, id_step, "server",server);
			rep.saveStepAttribute(id_transformation, id_step, "databaseName",databaseName);
			rep.saveStepAttribute(id_transformation, id_step, "port",port);
			
			rep.saveStepAttribute(id_transformation, id_step, "isOneTableString",isOneTableString);
			
			rep.saveStepAttribute(id_transformation, id_step, "manyTablesName",manyTablesName);
			rep.saveStepAttribute(id_transformation, id_step, "tables",tables);
			
			rep.saveStepAttribute(id_transformation, id_step, "oneTableName",oneTableName);
//			rep.saveStepAttribute(id_transformation, id_step, "where",where);
			rep.saveStepAttribute(id_transformation, id_step, "columns",columns);
			
			rep.saveStepAttribute(id_transformation, id_step, "oneTableName",oneTableName);
//			rep.saveStepAttribute(id_transformation, id_step, "isString",isString);
//		    rep.saveStepAttribute( id_transformation, id_step, "variables_active", variableReplacementActive );
			
		}catch(Exception e){
			throw new DataIntegrationException("Unable to save step information to the repository for id_step="+id_step, e);
		}
	}
	
	/**
	 * 在载入组件的时候会调用该方法从数据库资源中解析该组件的元数据
	 */
	public void readRep(Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases)
			throws DataIntegrationException {
		try{
			
			canalServer = rep.getStepAttributeString(id_step, "canalServer");
			canalPort = (int)rep.getStepAttributeInteger(id_step, "canalPort");
			canalInstance = rep.getStepAttributeString(id_step, "canalInstance");
			binlogType = rep.getStepAttributeString(id_step, "binlogType");
			intervalTime = (int)rep.getStepAttributeInteger(id_step, "intervalTime");
			
			username = rep.getStepAttributeString(id_step, "username");
			password = rep.getStepAttributeString(id_step, "password");
			server = rep.getStepAttributeString(id_step, "server");
			databaseName = rep.getStepAttributeString(id_step, "databaseName");
			port = (int)rep.getStepAttributeInteger(id_step, "port");
			
			isOneTableString = (int)rep.getStepAttributeInteger(id_step, "isOneTableString");
			
			manyTablesName = rep.getStepAttributeString(id_step, "manyTablesName");
			tables = rep.getStepAttributeString(id_step, "tables");
			
			oneTableName = rep.getStepAttributeString(id_step, "oneTableName");

			columns = rep.getStepAttributeString(id_step, "columns");
			
			oneTableName = rep.getStepAttributeString(id_step, "oneTableName");

			
		}
		catch(Exception e)
		{
			throw new DataIntegrationException("Unexpected error reading step information from the repository", e);
		}
	}

	/**
	 * 当下一个步骤点击获取字段时调用这个方法获取该组件的字段元数据
	 * 全部为String字段储存，我们只转hbase存储
	 */
	 public void getFields(RowMetaInterface row, String origin, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space) throws DataIntegrationStepException {
		//0代表是单表
		 if (isOneTableString == 0) {
			 getFieldsOne(row,origin,info,nextStep,space);
		}else if (isOneTableString == 1) {
			//是多表选择，只做表名。。。。先不做处理,tables为逗号隔开的表名，类型为String，把表名当做字段input，
			getFieldsMany(row,origin,info,nextStep,space);
		}
		
	}
	 /**
	  * 单表处理一个字段和类型，需要增加一个rowkey字段:hb_row_key
	  * @param row
	  * @param origin
	  * @param info
	  * @param nextStep
	  * @param space
	  */
		private void getFieldsOne(RowMetaInterface row, String origin, RowMetaInterface[] info, StepMeta nextStep,
				VariableSpace space) {
			 DatabaseUtils.getConnection("mysql", server, port, username, password,databaseName);
			 
//			List<String> ColumnTypes = DatabaseUtils.getColumnTypes(oneTableName);
			String[] cols = columns.split(",");
			ValueMetaInterface v0 = new ValueMeta("hb_row_key".toUpperCase(), ValueMetaInterface.TYPE_STRING);
			v0.setOrigin(origin);
			row.addValueMeta(v0);
			
			for (int i = 0; i < cols.length; i++) {
				ValueMetaInterface v = new ValueMeta(cols[i],
						ValueMetaInterface.TYPE_STRING);
				v.setOrigin(origin);
				row.addValueMeta(v);
			}
			
			
		}
		 /**
		  * 单表处理一个字段和类型，需要增加一个rowkey字段:hb_row_key
		  * @param row
		  * @param origin
		  * @param info
		  * @param nextStep
		  * @param space
		  */
			private void getFieldsMany(RowMetaInterface row, String origin, RowMetaInterface[] info, StepMeta nextStep,
					VariableSpace space) {
				 DatabaseUtils.getConnection("mysql", server, port, username, password,databaseName);
				 
//				List<String> ColumnTypes = DatabaseUtils.getColumnTypes(oneTableName);
				String[] cols = tables.split(",");
				ValueMetaInterface v0 = new ValueMeta("hb_row_key".toUpperCase(), ValueMetaInterface.TYPE_STRING);
				v0.setOrigin(origin);
				row.addValueMeta(v0);
				
				for (int i = 0; i < cols.length; i++) {
					ValueMetaInterface v = new ValueMeta(cols[i],
							ValueMetaInterface.TYPE_STRING);
					v.setOrigin(origin);
					row.addValueMeta(v);
				}
				
				
			}
		

	
	public static final RowMetaInterface buildRow(Connection conn,CanalInputMeta meta,
			List<CheckResultInterface> remarks, String origin, String[] cols) {
//		for(String tableName :DatabaseUtils.getTableNames()) {
//			List<String> Columns = DatabaseUtils.getColumnComments(tableName);
//			System.out.println(tableName);
//			System.out.println(Columns);
//		}
		RowMetaInterface row = new RowMeta();
		List<String> ColumnTypes = DatabaseUtils.getColumnTypes(oneTableName);
//		System.out.println(oneTableName);
//		System.out.println(cols);
//		System.out.println(ColumnTypes);
		for (int i = 0; i < cols.length; i++) {
			ValueMetaInterface v = new ValueMeta(cols[i],
					ValueMetaInterface.TYPE_STRING);
			v.setOrigin(origin);
			row.addValueMeta(v);
		}
	
		return row;
		
	}
	public static final RowMetaInterface buildRowSE(SeConnection conn,CanalInputMeta meta,
		List<CheckResultInterface> remarks, String origin, String[] cols) {
		
//		//0代表是单表
//		 if (isOneTableString == 0) {
//			 buildRowOne(conn,meta,remarks,origin,cols);
//		}else if (isOneTableString == 1) {
//			//是多表选择，只做表名。。。。先不做处理,不清楚怎么做
//		}
		RowMetaInterface row = new RowMeta();
//		Object[] rowData = null;
		try {
			/*SeConnection conn = new SeConnection(meta.getServer(),
					meta.getInstance(), meta.getDatabaseName(),
					meta.getUsername(), meta.getPassword());*/
			SeTable table = new SeTable(conn, meta.getOneTableName());
			SeColumnDefinition[] tableDef = table.describe();
			for (int n = 0; n < cols.length; n++) {
				for (int i = 0; i < tableDef.length; i++) {
	
					int colNum = i;
					if (tableDef[colNum].getName().equalsIgnoreCase(cols[n])) {
						int type = 0;
	
						int dataType = tableDef[colNum].getType();
	
						ValueMetaInterface v = null;
	
						switch (dataType) {
//						case SeColumnDefinition.TYPE_XML:
//							if (meta.getIsString() != 0) {
//								type = ValueMetaInterface.TYPE_STRING;
//							} else {
//								type = ValueMetaInterface.TYPE_OBJECT;
//							}
//							v = new ValueMeta(tableDef[colNum].getName(), type);
//							v.setOrigin(origin);
//							row.addValueMeta(v);
//	
//							// System.out.println(colDefs[colNum].getName()+
//							// rows.getString(colNum));
//	
//							break;
						case SeColumnDefinition.TYPE_BLOB:
							v = new ValueMeta(tableDef[colNum].getName(),
									ValueMetaInterface.TYPE_BINARY);
							v.setOrigin(origin);
							row.addValueMeta(v);
	
							// System.out.println(colDefs[colNum].getName()+
							// rows.getFloat(colNum));
	
							break;
						case SeColumnDefinition.TYPE_NCLOB:
							v = new ValueMeta(tableDef[colNum].getName(),
									ValueMetaInterface.TYPE_STRING);
							v.setOrigin(origin);
							row.addValueMeta(v);
	
							// System.out.println(colDefs[colNum].getName()+
							// rows.getFloat(colNum));
	
							break;
						case SeColumnDefinition.TYPE_INT16:
							v = new ValueMeta(tableDef[colNum].getName(),
									ValueMetaInterface.TYPE_INTEGER);
							v.setOrigin(origin);
							row.addValueMeta(v);
	
							// System.out.println(colDefs[colNum].getName()+
							// rows.getFloat(colNum));
	
							break;
						case SeColumnDefinition.TYPE_NSTRING:
							v = new ValueMeta(tableDef[colNum].getName(),
									ValueMetaInterface.TYPE_STRING);
							v.setOrigin(origin);
							row.addValueMeta(v);
	
							// System.out.println(colDefs[colNum].getName()+
							// rows.getFloat(colNum));
	
							break;
						case SeColumnDefinition.TYPE_UUID:
							v = new ValueMeta(tableDef[colNum].getName(),
									ValueMetaInterface.TYPE_STRING);
							v.setOrigin(origin);
							row.addValueMeta(v);
	
							// System.out.println(colDefs[colNum].getName()+
							// rows.getFloat(colNum));
	
							break;
//						case SeColumnDefinition.TYPE_RASTER:
//							if (meta.getIsString() != 0) {
//								type = ValueMetaInterface.TYPE_STRING;
//							} else {
//								type = ValueMetaInterface.TYPE_OBJECT;
//							}
//							v = new ValueMeta(tableDef[colNum].getName(), type);
//							v.setOrigin(origin);
//							row.addValueMeta(v);
//	
//							// System.out.println(colDefs[colNum].getName()+
//							// rows.getFloat(colNum));
//	
//							break;
						case SeColumnDefinition.TYPE_FLOAT64:
							v = new ValueMeta(tableDef[colNum].getName(),
									ValueMetaInterface.TYPE_NUMBER);
							v.setOrigin(origin);
							row.addValueMeta(v);
	
							// System.out.println(colDefs[colNum].getName()+
							// rows.getFloat(colNum));
	
							break;
						case SeColumnDefinition.TYPE_FLOAT32:
							v = new ValueMeta(tableDef[colNum].getName(),
									ValueMetaInterface.TYPE_NUMBER);
							v.setOrigin(origin);
							row.addValueMeta(v);
	
							// System.out.println(colDefs[colNum].getName()+"="+
							// rows.getFloat(colNum));
	
							break;
						case SeColumnDefinition.TYPE_DATE:
							v = new ValueMeta(tableDef[colNum].getName(),
									ValueMetaInterface.TYPE_DATE);
							v.setOrigin(origin);
							row.addValueMeta(v);
	
							// System.out.println(colDefs[colNum].getName()+"="+
							// rows.getDate(colNum));
	
							break;
						case SeColumnDefinition.TYPE_CLOB:
							v = new ValueMeta(tableDef[colNum].getName(),
									ValueMetaInterface.TYPE_STRING);
							v.setOrigin(origin);
							row.addValueMeta(v);
	
							// System.out.println(colDefs[colNum].getName()+"="+
							// rows.getClob(colNum));
	
							break;
						case SeColumnDefinition.TYPE_INT64:
							v = new ValueMeta(tableDef[colNum].getName(),
									ValueMetaInterface.TYPE_INTEGER);
							v.setOrigin(origin);
							row.addValueMeta(v);
	
							// System.out.println(colDefs[colNum].getName()+"="+
							// rows.getInteger(colNum));
	
							break;
						case SeColumnDefinition.TYPE_INT32:
							v = new ValueMeta(tableDef[colNum].getName(),
									ValueMetaInterface.TYPE_INTEGER);
							v.setOrigin(origin);
							row.addValueMeta(v);
							// System.out.println(colDefs[colNum].getName()+"="+
							// rows.getInteger(colNum));
	
							break;
	
						case SeColumnDefinition.TYPE_STRING:
							v = new ValueMeta(tableDef[colNum].getName(),
									ValueMetaInterface.TYPE_STRING);
							v.setOrigin(origin);
							row.addValueMeta(v);
	
							// System.out.println(colDefs[colNum].getName()+"="+
							// rows.getString(colNum));
	
							break;
	
//						case SeColumnDefinition.TYPE_SHAPE:
//							if (meta.getIsString() != 0) {
//								type = ValueMetaInterface.TYPE_STRING;
//							} else {
//								type = ValueMetaInterface.TYPE_OBJECT;
//							}
//							v = new ValueMeta(tableDef[colNum].getName(), type);
//							v.setOrigin(origin);
//							row.addValueMeta(v);
//	
//							break;
	
						}
					}
	
				}
			}
		} catch (SeException e) {
			e.printStackTrace();
		}
	return row;
}
	
	private static void buildRowOne(SeConnection conn, CanalInputMeta meta, List<CheckResultInterface> remarks,
			String origin, String[] cols) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 保存成文件资源
	 */
	public String getXML(){
        StringBuffer retval = new StringBuffer();
        retval.append("    "+XMLHandler.addTagValue("canalServer",canalServer)+Const.CR);
		retval.append("    "+XMLHandler.addTagValue("canalPort",canalPort)+Const.CR);
		retval.append("    "+XMLHandler.addTagValue("canalInstance",canalInstance)+Const.CR);
		retval.append("    "+XMLHandler.addTagValue("binlogType",binlogType)+Const.CR);
		retval.append("    "+XMLHandler.addTagValue("intervalTime",intervalTime)+Const.CR);
        
        
		retval.append("    "+XMLHandler.addTagValue("username",username)+Const.CR);
		retval.append("    "+XMLHandler.addTagValue("password",password)+Const.CR);
		retval.append("    "+XMLHandler.addTagValue("server",server)+Const.CR);
		retval.append("    "+XMLHandler.addTagValue("databaseName",databaseName)+Const.CR);
		retval.append("    "+XMLHandler.addTagValue("port",port)+Const.CR);
		
		retval.append("    "+XMLHandler.addTagValue("isOneTableString",isOneTableString)+Const.CR);
		retval.append("    "+XMLHandler.addTagValue("manyTablesName",manyTablesName)+Const.CR);
		retval.append("    "+XMLHandler.addTagValue("tables",tables)+Const.CR);
		retval.append("    "+XMLHandler.addTagValue("oneTableName",oneTableName)+Const.CR);
		
//		retval.append("    "+XMLHandler.addTagValue("where",where)+Const.CR);
		retval.append("    "+XMLHandler.addTagValue("columns",columns)+Const.CR);

//		retval.append("    "+XMLHandler.addTagValue("isString",isString)+Const.CR);
//	    retval.append( "    " + XMLHandler.addTagValue( "variables_active", variableReplacementActive ) );
        
		return retval.toString();
	}
	
	/**
	 * @return name
	 */
	public String getName() {
		return "Canal同步mysql输入";
	}
	
	public String getCanalServer() {
		return canalServer;
	}

	public void setCanalServer(String canalServer) {
		this.canalServer = canalServer;
	}

	public int getCanalPort() {
		return canalPort;
	}

	public void setCanalPort(int canalPort) {
		this.canalPort = canalPort;
	}

	public String getCanalInstance() {
		return canalInstance;
	}

	public void setCanalInstance(String canalInstance) {
		this.canalInstance = canalInstance;
	}

	public String getBinlogType() {
		return binlogType;
	}

	public void setBinlogType(String binlogType) {
		this.binlogType = binlogType;
	}

	public int getIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(int intervalTime) {
		this.intervalTime = intervalTime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public int getIsOneTableString() {
		return isOneTableString;
	}

	public void setIsOneTableString(int isOneTableString) {
		this.isOneTableString = isOneTableString;
	}

	public String getManyTablesName() {
		return manyTablesName;
	}

	public void setManyTablesName(String manyTablesName) {
		this.manyTablesName = manyTablesName;
	}

	public String getOneTableName() {
		return oneTableName;
	}

	public void setOneTableName(String oneTableName) {
		this.oneTableName = oneTableName;
	}

	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}

	public String getTables() {
		return tables;
	}

	public void setTables(String tables) {
		this.tables = tables;
	}

	
	
//	public int getIsString() {
//		return isString;
//	}
//
//	public void setIsString(int isString) {
//		this.isString = isString;
//	}
//
//	public boolean isVariableReplacementActive() {
//		return variableReplacementActive;
//	}
//
//	public void setVariableReplacementActive(boolean variableReplacementActive) {
//		this.variableReplacementActive = variableReplacementActive;
//	}

//	public String getWhere() {
//		return where;
//	}
//
//	public void setWhere(String where) {
//		this.where = where;
//	}

	
	
}
