package com.primeton.di.trans.steps.canalinput.utils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.Message;
import com.primeton.di.trans.steps.canalinput.CanalInputMeta;
import com.primeton.di.trans.steps.canalinput.utils.domain.TableData;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;

/**
 * 测试canal-1.1.0 binlog 为 row、mixed
 */
public class CanalUtil {

	private static CanalInputMeta canalInputMeta;
	private String binlogType; // 日志模式选择ROW/Mixed
	private int intervalTime; // 间隔时间配置ms


	private String tableName;
	private String tableNames;
	private String databaseName;

	public CanalUtil( String binlogType, int intervalTime,String tableName, String tableNames, String databaseName) {
		this.binlogType = binlogType;
		this.intervalTime = intervalTime;

		this.tableName = tableName;
		this.tableNames = tableNames;
		this.databaseName = databaseName;
		
	}

	public JSONArray startSubscribe(CanalConnector connector,CanalInputMeta meta) {			
	
		canalInputMeta = meta;
		JSONArray result = new JSONArray();
		// 第三步：循环订阅

		// 获取指定数量的数据,一次获取完了sleep时间段的数据，非阻塞，使用1s来解决非阻塞的cpu性能问题
		Message message = connector.getWithoutAck(1000, (long) 1, TimeUnit.SECONDS);
		long batchId = message.getId();
		int size = message.getEntries().size();
		try {
			if (batchId == -1 || size == 0) {
				System.out.println("no message !");
				Thread.sleep(intervalTime);
			} else {
				result = getEntry(message.getEntries());
			}

		} catch (InterruptedException e) {
			System.out.println("Error is having :" + e.getMessage());
			connector.rollback(batchId); // 处理失败, 回滚数据
			connector.disconnect();
		}
		connector.ack(batchId); // 提交确认
		return result;
	}

	private JSONArray getEntry(List<Entry> entrys) {

		JSONArray resultArray = new JSONArray();
		for (Entry entry : entrys) {
			// 事物数据不处理
			if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN
					|| entry.getEntryType() == EntryType.TRANSACTIONEND) {
				continue;
			}
			RowChange rowChange = null;
			try {
				rowChange = RowChange.parseFrom(entry.getStoreValue());
			} catch (Exception e) {
				throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),
						e);
			}
			
			//过滤库
			if (!entry.getHeader().getSchemaName().equalsIgnoreCase(databaseName)) continue;
			//不是多表，判断是否是对应单表,全否return
			if (tableNames == null || ("").equals(tableNames)){			
				if (!entry.getHeader().getTableName().equalsIgnoreCase(tableName)) continue;
				//是多表，判断是否包含,不包含 return
			}else if (!tableNames.toLowerCase().contains(entry.getHeader().getTableName().toLowerCase())) continue;
			
			TableData tableData = new TableData();
			tableData.setDatabaseName(entry.getHeader().getSchemaName());
			tableData.setTableName(entry.getHeader().getTableName());
			tableData.setEventType(rowChange.getEventType().name());
			tableData.setLogfileOffset(entry.getHeader().getLogfileOffset());
			tableData.setCreateTime(System.currentTimeMillis());
			
			JSONObject result = new JSONObject();
			result.put("databaseName", tableData.getDatabaseName());
			result.put("tableName", tableData.getTableName());
			result.put("eventType", tableData.getEventType());
			result.put("logfileOffset", tableData.getLogfileOffset());
			result.put("createTime", tableData.getCreateTime());
			
			
			//ddl or hasSql
			
			if (rowChange.getSql() != null && !("").equals(rowChange.getSql())) {
				System.out.println("rowChange.getSql ----->" + rowChange.getSql());
				if (rowChange.getIsDdl()) {
					//ddl操作[是否是ddl变更操作，比如create table/drop table] 操作no
			
				}else {
					//mixed模式的statement数据，解析sql语句
					resultArray.add(resultStatement(rowChange.getSql(), result, result.getString("eventType")));
				}
				continue;
			}

			for (RowData rowData : rowChange.getRowDatasList()) {
				resultArray.add(resultRow(rowData, result, result.getString("eventType")));
			}

		}
		return resultArray;
	}

	/**
	 *
	 * statement模式数据解析、格式封装
	 * @return JSONObject
	 */
	private JSONObject resultStatement(String rowSql, JSONObject result, String eventType) {
		
        String sql = rowSql.replace("`", "").toLowerCase();
        System.out.println(sql);
        
		if (eventType.equals(EventType.INSERT.name())){
			JSONObject columns = getInsertData(sql,result.getString("tableName"));
			result.put("columns", columns);
		}

		//update找到where 条件主键，反查库
		if (eventType.equals(EventType.UPDATE.name())){
			JSONObject columns = getUpdateData(sql,result.getString("tableName"));
			result.put("columns", columns);
		}
		//delete 直接封装，返回
		// DELETE FROM `cap_role` WHERE (`ROLE_ID`='2')
		if (eventType.equals(EventType.DELETE.name())){
			JSONObject condition = getDeleteData(sql);
			result.put("condition", condition);
		}

		return result;
	}
	
	
    private static JSONObject getUpdateData(String sqlUpdate, String tableName) {
	        
	        String newQuerySql =" select * from " + tableName + sqlUpdate.substring(sqlUpdate.indexOf("where")-1);
	        System.out.println("newQuerySql :  "+ newQuerySql);
	        
	        DatabaseUtils.getConnection("mysql", canalInputMeta.getCanalServer(), canalInputMeta.getPort(), 
	        		canalInputMeta.getUsername(), canalInputMeta.getPassword(),canalInputMeta.getDatabaseName());
	        List<String> columnNames = DatabaseUtils.getColumnNames(tableName);
	        
	        DatabaseUtils.getConnection("mysql", canalInputMeta.getCanalServer(), canalInputMeta.getPort(), 
	        		canalInputMeta.getUsername(), canalInputMeta.getPassword(),canalInputMeta.getDatabaseName());
	        JSONObject row = DatabaseUtils.queryResultById(newQuerySql,tableName,columnNames);
	        return row;
	    }

    
    //delete from canal_role_test where (role_id='4')
    private static JSONObject getDeleteData(String sqlDelete) {
        String whereSql = sqlDelete.substring(sqlDelete.indexOf("where")+6);
        JSONObject row = new JSONObject();
        if (whereSql.contains("(")) {
        	whereSql = whereSql.replace("(", "").replace(")", "");
		}
        
        if (whereSql.contains("and")){
            String[] ands = whereSql.split("and");
            for (int i = 0; i < ands.length; i++) {
                String[] data = ands[i].split("=");
                row.put(data[0].trim(),data[1].replace("'","").trim());
            }
        }else {
            String[] data = whereSql.split("=");
            row.put(data[0].trim(),data[1].replace("'","").trim());
        }
        System.out.println(row);
        return row;
    }



    private static JSONObject getInsertData(String sqlInsert, String tableName) {
    	 String[] data = sqlInsert.split("values");
         String[] cloumns = data[0].substring(data[0].indexOf("(")+1,data[0].indexOf(")")).split(",");
         String[] values = data[1].substring(data[1].indexOf("(")+1,data[1].indexOf(")")).split(",");

         StringBuffer whereSql = new StringBuffer().append(" where 1=1 ");
         for (int i = 0; i < cloumns.length; i++) {
             whereSql.append(" and ").append(cloumns[i]).append(" = ").append(values[i]);
         }
         System.out.println(whereSql);

         String newQuerySql =" select * from " + tableName + whereSql.toString();
         System.out.println(newQuerySql);

         DatabaseUtils.getConnection("mysql", canalInputMeta.getCanalServer(), canalInputMeta.getPort(), 
	        		canalInputMeta.getUsername(), canalInputMeta.getPassword(),canalInputMeta.getDatabaseName());
         List<String> columnNames = DatabaseUtils.getColumnNames(tableName);
         
         DatabaseUtils.getConnection("mysql", canalInputMeta.getCanalServer(), canalInputMeta.getPort(), 
	        		canalInputMeta.getUsername(), canalInputMeta.getPassword(),canalInputMeta.getDatabaseName());


         JSONObject row = DatabaseUtils.queryResultById(newQuerySql,tableName,columnNames);

         return row;
    }

	/**
	 *
	 * Row模式数据解析、格式封装
	 * @return JSONObject
	 */
	public JSONObject resultRow(RowData rowData, JSONObject result, String eventType) {
		// 存放变化后的字段
		JSONObject columns = new JSONObject();
		result.put("columns", columns);

		// 条件
		JSONObject condition = new JSONObject();
		result.put("condition", condition);

		// insert,update 处理after数据
		if (eventType.equals(EventType.INSERT.name())
				|| eventType.equals(EventType.UPDATE.name())) {

			for (Column column : rowData.getAfterColumnsList()) {
				columns.put(column.getName(), column.getValue());
			}
		}

		// delete处理before数据，只加condition条件
		// a. isKey,直接删除，无key则以其他所有数据为条件删除
		if (eventType.equals(EventType.DELETE.name())) {
			for (Column column : rowData.getBeforeColumnsList()) {
				if (column.hasIsKey()) {
					condition.put(column.getName(), column.getValue());
					break;
				} else {
					columns.put(column.getName(), column.getValue());
				}
			}
		}

		// update、对比before和after数据
		// a. isKey,直接以before的key来更新after后的数据，这个key也有可能修改 ，所以取消
		// 无key则以其他update为false的数据为条件更新，取消
		// 全更新 字段则以before数据作为条件更新 绝对条件update
		if (eventType.equals(EventType.UPDATE.name())) {
			for (Column column : rowData.getBeforeColumnsList()) {
				condition.put(column.getName(), column.getValue());
			}

		}
		return result;
	}
}
