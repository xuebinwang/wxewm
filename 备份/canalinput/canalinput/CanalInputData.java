package com.primeton.di.trans.steps.canalinput;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Table;

import com.esri.sde.sdk.client.SeColumnDefinition;
import com.esri.sde.sdk.client.SeQuery;
import com.esri.sde.sdk.client.SeSqlConstruct;
import com.esri.sde.sdk.client.SeTable;
import com.primeton.di.core.row.RowMetaInterface;
import com.primeton.di.core.row.ValueMetaInterface;
import com.primeton.di.trans.step.BaseStepData;
import com.primeton.di.trans.step.StepDataInterface;

/**
 * 这个类负责存储一些CanalInput运行时的变量，例如：字段类型数组，数据库连接等
 * @author my
 *
 */
public class CanalInputData extends BaseStepData implements StepDataInterface {

	public RowMetaInterface outputRowMeta;
	public Object[] outputRowData;
	public Connection connection;
//	public SeColumnDefinition[] colDefs;
//	private List rows;
	public ValueMetaInterface valueMeta;
//	public Table table  = null;
//	public Query query = null;
//	public String where="";
//	SeSqlConstruct sqlConstruct = null;
	
	public String oneTableKey;
	public String oneTableName;
	public String[] cols;//单表为字段，多表为表名
	public String[] colsType;
	
	public CanalInputData(){
		super();
//		rows = new ArrayList();
	}
}
