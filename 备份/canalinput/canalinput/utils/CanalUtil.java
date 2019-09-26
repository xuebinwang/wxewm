package com.primeton.di.trans.steps.canalinput.utils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.Message;
import com.primeton.di.trans.steps.canalinput.utils.domain.TableData;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;

/**
 * 测试canal-1.1.0 binlog 为 row
 */
public class CanalUtil {

	private String binlogType; // 日志模式选择ROW/Mixed
	private int intervalTime; // 间隔时间配置ms


	private String tableName;
	private String tableNames;
	private String databaseName;

	public CanalUtil( String binlogType, int intervalTime,String tableName, String tableNames, String databaseName) {
//		this.canalServer = canalServer;
//		this.canalPort = canalPort;
//		this.canalInstance = canalInstance;
		this.binlogType = binlogType;
		this.intervalTime = intervalTime;

		this.tableName = tableName;
		this.tableNames = tableNames;
		this.databaseName = databaseName;
		
	}

	public JSONArray startSubscribe(CanalConnector connector) {			
		// 第二步：开启全部订阅
		connector.connect();
		/**
		 * #(1)这里黑白名单可以覆盖defaultDatabaseName。 #(2)如果Client端
		 * 配置了connector.subscribe则会覆盖黑白名单配置 #表过滤--白名单 只监听库表
		 * #如testDB\..*只监听testDB数据库,testDB\.test_1 只监听testDB库中test_1表。多个用逗号分开
		 * #此过滤条件只针对row模式的数据有效(ps. mixed/statement因为不解析sql，所以无法准确提取tableName进行过滤)
		 * #canal.instance.filter.regex = .*\\..*
		 */
		connector.subscribe(".*\\..*");

		JSONArray result = new JSONArray();
		// 第三步：循环订阅

		// 获取指定数量的数据,一次获取完了sleep时间段的数据，非阻塞，使用1s来解决非阻塞的cpu性能问题
//            Message message = connector.getWithoutAck(batchSize);
		Message message = connector.getWithoutAck(1000, (long) 1, TimeUnit.SECONDS);
		long batchId = message.getId();
		int size = message.getEntries().size();
		try {
			if (batchId == -1 || size == 0) {
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
			
			
			//ddl or hasSql
			if (rowChange.getSql() != null && !("").equals(rowChange.getSql())) {
				if (rowChange.getIsDdl()) {
					//ddl操作[是否是ddl变更操作，比如create table/drop table] 
					
					
				}else {
					//mixed模式的statement数据，解析sql语句
					
				}
				continue;
			}
			
			
			TableData tableData = new TableData();
			tableData.setDatabaseName(entry.getHeader().getSchemaName());
			tableData.setTableName(entry.getHeader().getTableName());
			tableData.setEventType(rowChange.getEventType().name());
			tableData.setLogfileOffset(entry.getHeader().getLogfileOffset());
			tableData.setCreateTime(System.currentTimeMillis());

			for (RowData rowData : rowChange.getRowDatasList()) {
				resultArray.add(resultData(rowData, tableData));
			}

		}
		return resultArray;
	}

	/**
	 *
	 * @param rowData
	 * @param tableData
	 * @return json格式的string
	 */
	public JSONObject resultData(RowData rowData, TableData tableData) {

		JSONObject result = new JSONObject();
		result.put("databaseName", tableData.getDatabaseName());
		result.put("tableName", tableData.getTableName());
		result.put("eventType", tableData.getEventType());
		result.put("logfileOffset", tableData.getLogfileOffset());
		result.put("createTime", tableData.getCreateTime());

		// 存放变化后的字段
		JSONObject columns = new JSONObject();
		result.put("columns", columns);

		// 条件
		JSONObject condition = new JSONObject();
		result.put("condition", condition);

		// insert,update 处理after数据
		if (tableData.getEventType().equals(EventType.INSERT.name())
				|| tableData.getEventType().equals(EventType.UPDATE.name())) {

			for (Column column : rowData.getAfterColumnsList()) {
				columns.put(column.getName(), column.getValue());
			}
		}

		// delete处理before数据，只加condition条件
		// a. isKey,直接删除，无key则以其他所有数据为条件删除
		if (tableData.getEventType().equals(EventType.DELETE.name())) {
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
		if (tableData.getEventType().equals(EventType.UPDATE.name())) {
			for (Column column : rowData.getBeforeColumnsList()) {
				condition.put(column.getName(), column.getValue());
			}

		}
		return result;
	}
}
