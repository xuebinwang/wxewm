package com.primeton.di.ui.trans.steps.canalinput;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.primeton.di.core.Const;
import com.primeton.di.i18n.BaseMessages;
import com.primeton.di.trans.TransMeta;
import com.primeton.di.trans.step.BaseStepMeta;
import com.primeton.di.trans.step.StepDialogInterface;
import com.primeton.di.trans.steps.canalinput.CanalInputMeta;
import com.primeton.di.trans.steps.canalinput.utils.DatabaseUtils;
import com.primeton.di.ui.core.widget.ColumnInfo;
import com.primeton.di.ui.core.widget.StyledTextComp;
import com.primeton.di.ui.core.widget.TableView;
import com.primeton.di.ui.trans.step.BaseStepDialog;

import com.primeton.di.ui.trans.steps.tableinput.SQLValuesHighlight;

import swing2swt.layout.BorderLayout;
import swing2swt.layout.FlowLayout;

public class CanalInputDialog extends BaseStepDialog implements StepDialogInterface{
	private static Class<?> PKG = CanalInputMeta.class;
	private CanalInputMeta input;
	
	private Text canalServerText;
	private Text canalPortText;
	private Text canalInstanceText;
	private Text binlogTypeText;
	private Text intervalTimeText;
	private Text usernameText;
	private Text passwordText;
	private Text serverText;
	private Text portText;
	private Text databaseNameText;
	
	private Button isOneTableStringButton;
	
//	private TableView selectTableList; //多表
//	private Text oneTableNameText;
//	private TableView selectColumnList;  //选中的多字段
	private TableView selectColumnOrTableList;
	
	
	private Combo selectTableCombo; //选中的单表

	private Button isStringButton;
	private StyledTextComp  whereText;
	private SQLValuesHighlight lineStyler = new SQLValuesHighlight();
	
	private Label wlVariables;
	private Button wVariables;
	


	
	
	public CanalInputDialog(Shell parent, Object in, TransMeta transMeta, String stepname) {
		super(parent, (BaseStepMeta)in, transMeta, stepname);
		input = (CanalInputMeta) in;
		// TODO Auto-generated constructor stub
	}

	@Override
	public String open() {
		
		Shell perent = getParent();
		Display display = perent.getDisplay();
		shell = new Shell(perent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.MIN);
		props.setLook(shell);
		setShellImage(shell, input);
		
		shell.setLayout(new BorderLayout(0, 0));
		shell.setText(BaseMessages.getString( PKG,"CanalInputDialog.titlename")); //$NON-NLS-1$
		 final ModifyListener lsMod = new ModifyListener() {
		      public void modifyText( ModifyEvent e ) {
		    	  input.setChanged();
		      }
		    };
			Composite composite = new Composite(shell, SWT.NONE);
			
			composite.setSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			
			composite.setLayoutData(BorderLayout.CENTER);
			GridLayout gl_composite = new GridLayout(3, false);
			gl_composite.marginLeft = 20;
			composite.setLayout(gl_composite);
			
			Label nameLabel = new Label(composite, SWT.NONE);
			nameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
			nameLabel.setText(BaseMessages.getString( PKG,"CanalInputDialog.nameLabel"));
			
			wStepname = new Text(composite, SWT.BORDER);
			wStepname.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
			wStepname.setText(BaseMessages.getString( PKG,"CanalInputDialog.nameText"));
			wStepname.addModifyListener(lsMod);
			
			Label canalServerLabel = new Label(composite, SWT.NONE);
			canalServerLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
			canalServerLabel.setText(BaseMessages.getString( PKG,"CanalInputDialog.canalServerLabel"));
			
			canalServerText = new Text(composite, SWT.BORDER);
			canalServerText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
			canalServerText.addModifyListener(lsMod);
			
			Label canalPortLabel = new Label(composite, SWT.RIGHT);
			canalPortLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
			canalPortLabel.setText(BaseMessages.getString( PKG,"CanalInputDialog.canalPortLabel"));
			
			canalPortText = new Text(composite, SWT.BORDER);
			canalPortText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
			canalPortText.addModifyListener(lsMod);
			
			Label canalInstanceLabel = new Label(composite, SWT.RIGHT);
			canalInstanceLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
			canalInstanceLabel.setText(BaseMessages.getString( PKG,"CanalInputDialog.canalInstanceLabel"));
			
			canalInstanceText = new Text(composite, SWT.BORDER);
			canalInstanceText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
			canalInstanceText.addModifyListener(lsMod);
			
			Label binlogTypeLabel = new Label(composite, SWT.RIGHT);
			binlogTypeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
			binlogTypeLabel.setText(BaseMessages.getString( PKG,"CanalInputDialog.binlogTypeLabel"));
			
			binlogTypeText = new Text(composite, SWT.BORDER);
			binlogTypeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
			binlogTypeText.addModifyListener(lsMod);
			
			Label intervalTimeLabel = new Label(composite, SWT.RIGHT);
			intervalTimeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
			intervalTimeLabel.setText(BaseMessages.getString( PKG,"CanalInputDialog.intervalTimeLabel"));
			
			intervalTimeText = new Text(composite, SWT.BORDER);
			intervalTimeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
			intervalTimeText.addModifyListener(lsMod);
			
			Label userNameLabel = new Label(composite, SWT.RIGHT);
			userNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
			userNameLabel.setText(BaseMessages.getString( PKG,"CanalInputDialog.userNameLabel"));
			
			usernameText = new Text(composite, SWT.BORDER);
			usernameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
			usernameText.addModifyListener(lsMod);
			
			Label passwordLabel = new Label(composite, SWT.NONE);
			passwordLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
			passwordLabel.setText(BaseMessages.getString( PKG,"CanalInputDialog.passwordLabel"));
			
			passwordText = new Text(composite, SWT.BORDER|SWT.PASSWORD|SWT.SINGLE);
			passwordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
			passwordText.addModifyListener(lsMod);
			
			Label serverLabel = new Label(composite, SWT.NONE);
			serverLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
			serverLabel.setText(BaseMessages.getString( PKG,"CanalInputDialog.serverLabel"));
			
			serverText = new Text(composite, SWT.BORDER);
			serverText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
			serverText.addModifyListener(lsMod);
			
			Label protLabel = new Label(composite, SWT.NONE);
			protLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
			protLabel.setText(BaseMessages.getString( PKG,"CanalInputDialog.protLabel"));
			
			portText = new Text(composite, SWT.BORDER);
			portText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
			portText.addModifyListener(lsMod);
			
			Label databaseLabel = new Label(composite, SWT.NONE);
			databaseLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
			databaseLabel.setText(BaseMessages.getString( PKG,"CanalInputDialog.databaseLabel"));
			
			databaseNameText = new Text(composite, SWT.BORDER);
			databaseNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			databaseNameText.addModifyListener(lsMod);
			
			
			
			Button testConnDabaseButton = new Button(composite, SWT.NONE);
			testConnDabaseButton.setText(BaseMessages.getString( PKG,"CanalInputDialog.testConnDabase"));
			
			
			Label selectTable = new Label(composite, SWT.NONE);
			selectTable.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
			selectTable.setText(BaseMessages.getString( PKG,"CanalInputDialog.selectTable"));
			
			selectTableCombo = new Combo(composite, SWT.NONE);
			GridData gd_selectTableCombo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1);
			gd_selectTableCombo.widthHint = 150;
			selectTableCombo.setLayoutData(gd_selectTableCombo);
			selectTableCombo.addModifyListener(lsMod);

			new Label(composite, SWT.NONE | SWT.ARROW_RIGHT);
			isOneTableStringButton = new Button(composite, SWT.RADIO|SWT.LEFT);	
			isOneTableStringButton.setText(BaseMessages.getString( PKG,"CanalInputDialog.isOneTableStringButton"));
			
			isOneTableStringButton.setSelection(true);
			new Label(composite, SWT.NONE | SWT.ARROW_RIGHT);
			
			new Label(composite, SWT.NONE | SWT.ARROW_RIGHT);

			Button isaManyTableStringButton = new Button(composite, SWT.RADIO|SWT.LEFT);	
			isaManyTableStringButton.setText(BaseMessages.getString( PKG,"CanalInputDialog.isManyTableStringButton"));
			new Label(composite, SWT.NONE | SWT.ARROW_RIGHT);
			
			Composite composite_2 = new Composite(composite, SWT.NONE);
			composite_2.setLayout(new GridLayout(4, false));
			composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
			
			new Label(composite_2, SWT.NONE | SWT.ARROW_RIGHT);
			
			Button selectColumnButton = new Button(composite_2, SWT.NONE);
			selectColumnButton.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1));
			selectColumnButton.setText(BaseMessages.getString( PKG,"CanalInputDialog.selectColumnButton"));
			selectTableCombo.addModifyListener(lsMod);
			
				
			int UpInsCols=1;
			int UpInsRows= (input.getColumns()!=null?input.getColumns().split(",").length:1);
			
			ColumnInfo[] ciReturn = new ColumnInfo[UpInsCols];
			ciReturn[0]=new ColumnInfo("名称",  ColumnInfo.COLUMN_TYPE_TEXT,false); //$NON-NLS-1$

			selectColumnOrTableList = new TableView(transMeta, composite_2, 
					  SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL, 
					  ciReturn, 
					  UpInsRows,  
					  lsMod,
					  props);
			
					
			Listener selectIsOneTableStringButton = new Listener() {
				public void handleEvent(Event e) { 			
					GridData gd_selectColumnList = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
					selectColumnOrTableList.setLayoutData(gd_selectColumnList);
														
				}};
					
			isOneTableStringButton.addListener(SWT.Selection, selectIsOneTableStringButton);
			
			Listener selectColumnButtonListener = new Listener() {
				public void handleEvent(Event e) {
					if(isOneTableStringButton.getSelection()) {
						selectColumn();   
					}else {
						selectTables();
					}
				  
				}};
			selectColumnButton.addListener(SWT.Selection, selectColumnButtonListener);	
			new Label(composite, SWT.NONE | SWT.ARROW_RIGHT);
			
			
			Composite composite_1 = new Composite(shell, SWT.NONE);
			composite_1.setLayoutData(BorderLayout.SOUTH);
			composite_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			
			wOK = new Button(composite_1, SWT.NONE);
			wOK.setText(BaseMessages.getString( PKG,"CanalInputDialog.wOK"));
			
			wCancel = new Button(composite_1, SWT.NONE);
			wCancel.setText(BaseMessages.getString( PKG,"CanalInputDialog.wCancel"));
			
			// Add listeners
			
			Listener testConnListener  = new Listener() { public void handleEvent(Event e) { testConnDataBase();     }};
			
			testConnDabaseButton.addListener    (SWT.Selection, testConnListener    );
			
			
			lsOK       = new Listener() { public void handleEvent(Event e) { ok();     }};
			lsCancel   = new Listener() { public void handleEvent(Event e) { cancel(); }};
			
			wOK.addListener    (SWT.Selection, lsOK    );
			wCancel.addListener(SWT.Selection, lsCancel);
			
			
			shell.addShellListener(	new ShellAdapter() { public void shellClosed(ShellEvent e) { cancel(); } } );
				
			getData();
			shell.setSize(500, 580);
			shell.open();
			while (!shell.isDisposed())
			{
					if (!display.readAndDispatch()) display.sleep();
			}
			
			return stepname;
		
	}
	
	/*
	 * Copy information from the meta-data input to the dialog fields.
	 * 
	 */ 
	public void getData(){

		if(input.getCanalServer()!=null){
			canalServerText.setText(input.getCanalServer());
		}
		
		if(input.getCanalPort() != 0){
			canalPortText.setText(String.valueOf(input.getCanalPort()));  //默认11111
		}
				
		if(input.getCanalInstance()!=null){
			canalInstanceText.setText(input.getCanalInstance());
		}
		if(input.getBinlogType()!=null){
			binlogTypeText.setText(input.getBinlogType()); //默认日志模式选择ROW
		}

		if(input.getIntervalTime() != 0){
			intervalTimeText.setText(String.valueOf(input.getIntervalTime())); //间隔时间配置5000ms
		}	
		
		if(input.getUsername()!=null){
			usernameText.setText(input.getUsername());
		}
		if(input.getPassword()!=null){
			passwordText.setText(input.getPassword());
		}
		if(input.getServer()!=null){
			serverText.setText(input.getServer());
		}
		if(input.getPort() != 0){
			portText.setText(String.valueOf(input.getPort()));
		}
		
		
		if(input.getDatabaseName()!=null){
			databaseNameText.setText(input.getDatabaseName());
		}
				

		//单表多表选择按钮,0为单表，1为多表,默认0
		if (input.getIsOneTableString()==0) {
			
			
			if(input.getOneTableName()!=null){
				selectTableCombo.setText(input.getOneTableName());
			}
			
			if (input.getColumns()!=null){
				String[] columns=input.getColumns().split(",");
				selectColumnOrTableList.table.setItemCount(columns.length);
				for (int i=0;i<columns.length;i++){
					TableItem item = selectColumnOrTableList.table.getItem(i);
					if (columns[i]!=null) item.setText(1, columns[i]);
				}
			}
		}else {
			if (input.getTables()!=null){
				String[] tables = input.getTables().split(",");
				selectColumnOrTableList.table.setItemCount(tables.length);
				for (int i = 0; i < tables.length; i++) {
					TableItem item = selectColumnOrTableList.table.getItem(i);
					if (tables[i]!=null) {
						item.setText(1, tables[i]);
					}
				
				}
			}
		}

		wStepname.setText(stepname);
	}
	/**
	 * 测试连接数据库
	 */
	public void testConnDataBase(){
		String username = null;
		String password = null;
		String server = null;
		String database = null;
		int port = 3306;
		if(usernameText.getText().trim().length()>0){
			username=usernameText.getText().trim();
    	}
    	
    	if(passwordText.getText().trim().length()>0){
    		password=passwordText.getText().trim();
    	}
    	if(databaseNameText.getText().trim().length()>0){
    		database=databaseNameText.getText().trim();
    	}
    	if(serverText.getText().trim().length()>0){
    		server=serverText.getText().trim();
    	}
    	
    	if(portText.getText().trim().length()>0){
    		port=Integer.parseInt(portText.getText().trim());
    	}
    	
    	try {
    		DatabaseUtils.getConnection("mysql", server, port, username, password, database);
    		
    		List<String> tableList = DatabaseUtils.getTableNames();
    		String[] tables = new String[tableList.size()];
    		for(int i=0;i<tableList.size();i++){
    			tables[i]=tableList.get(i);
    		}
    		
    		if(tables!=null) {			
				selectTableCombo.setItems(tables);
				selectTableCombo.select(0);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getStackTrace();
		}
	}
	/**
	 * 测试连接canal
	 */
	public void testConnCanal(){
		try {
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.getStackTrace();
		}
	}

	/**
	 * 取消
	 * 
	 */
	private void cancel(){
		input.setChanged(changed);
		dispose();
	}
	
	/**
	 * 保存时，调用
	 */
	private void ok() {
		if (Const.isEmpty(wStepname.getText())) return;

		stepname = wStepname.getText(); // return value
		// copy info to TextFileInputMeta class (input)
        
        getInfo(input);
        
        /*
         * if (input.getDatabaseMeta()==null)
		{
			MessageBox mb = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR );
			mb.setMessage(BaseMessages.getString( PKG,"CanalInputDialog.SelectValidConnection")); //$NON-NLS-1$
			mb.setText(BaseMessages.getString( PKG,"CanalInputDialog.DialogCaptionError")); //$NON-NLS-1$
			mb.open();
		}*/
		
		dispose();
	}
	
	/**
	 * 
	 * 
	 * 从界面类中获取页面数据。赋值到元数据
	 * 
	 * @param meta
	 */
	 private void getInfo(CanalInputMeta meta){

			if(canalServerText.getText().trim().length()>0){
	    		meta.setCanalServer(canalServerText.getText().trim());
	    	}
			if(canalPortText.getText().trim().length()>0){
	    		meta.setCanalPort(Integer.parseInt(canalPortText.getText().trim()));
	    	}
			if(canalInstanceText.getText().trim().length()>0){
	    		meta.setCanalInstance(canalInstanceText.getText().trim());
	    	}
			if(binlogTypeText.getText().trim().length()>0){
	    		meta.setBinlogType(binlogTypeText.getText().trim());
	    	}
			if(intervalTimeText.getText().trim().length()>0){
	    		meta.setIntervalTime(Integer.parseInt(intervalTimeText.getText().trim()));
	    	}
	    	
	    	if(usernameText.getText().trim().length()>0){
	    		meta.setUsername(usernameText.getText().trim());
	    	}
	    	if(passwordText.getText().trim().length()>0){
	    		meta.setPassword(passwordText.getText().trim());
	    	}
	    	if(serverText.getText().trim().length()>0){
	    		meta.setServer(serverText.getText().trim());
	    	}
	    	if(portText.getText().trim().length()>0){
	    		meta.setPort(Integer.parseInt(portText.getText().trim()));
	    	}
	    	if(databaseNameText.getText().trim().length()>0){
	    		meta.setDatabaseName(databaseNameText.getText().trim());
	    	}
	    	
	    	
	    	int nrfields = selectColumnOrTableList.nrNonEmpty();
	    	StringBuffer columnOrTableBuffer = new StringBuffer();
	    	for (int i=0 ; i<nrfields ; i++){
				TableItem item  = selectColumnOrTableList.getNonEmpty(i);
				
				columnOrTableBuffer.append(item.getText(1));
	    		if(i!=nrfields-1){
	    			columnOrTableBuffer.append(",");
	    		}
			}
	    	//选择为单表，不选择多表选择按钮,0为单表，1为多表
			if(isOneTableStringButton.getSelection()){
	    		meta.setIsOneTableString(0);
	    		if(selectTableCombo.getText().trim().length()>0){
		    		meta.setOneTableName(selectTableCombo.getText().trim());
		    	}
	    		meta.setColumns(columnOrTableBuffer.toString());
	    	
	    	}else {
	    		meta.setIsOneTableString(1);    		
		    	meta.setTables(columnOrTableBuffer.toString());
			}
			
	   

	    	stepname=wStepname.getText();
	    }
	    
	 /**
	  * 返回所选择数据库的表字段信息
	  */
	public void selectColumn(){
		String username = null;
		String password = null;
		String server = null;
		String database = null;
		String tablename=null;
		int port=3306;
		if(usernameText.getText().trim().length()>0){
			username=usernameText.getText().trim();
    	}
    	
    	if(passwordText.getText().trim().length()>0){
    		password=passwordText.getText().trim();
    	}
    	if(databaseNameText.getText().trim().length()>0){
    		database=databaseNameText.getText().trim();
    	}
    	if(serverText.getText().trim().length()>0){
    		server=serverText.getText().trim();
    	}
    	if(selectTableCombo.getText().trim().length()>0){
    		tablename=selectTableCombo.getText().trim();
    	}
    	
    	if(portText.getText().trim().length()>0){
    		port=Integer.parseInt(portText.getText().trim());
    	}
		try {		
	    	DatabaseUtils.getConnection("mysql", server, port, username, password, database);
			List<String> columsList = DatabaseUtils.getColumnNames(tablename);
			String[] selectColumns = new String[columsList.size()];
    		for(int i=0 ; i<columsList.size(); i++){
    			selectColumns[i]=columsList.get(i);
    		}
    		selectColumnOrTableList.table.clearAll();
    		selectColumnOrTableList.table.setItemCount(selectColumns.length);
				
				for(int i=0;i<selectColumns.length;i++){
					selectColumnOrTableList.table.getItem(i).setText(1, selectColumns[i]);;
				}
				selectColumnOrTableList.setRowNums();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 返回所选择数据库的表信息
	 */
	public void selectTables(){
		String username = null;
		String password = null;
		String server = null;
		String database = null;
		int port=5151;
		if(usernameText.getText().trim().length()>0){
			username=usernameText.getText().trim();
    	}
    	
    	if(passwordText.getText().trim().length()>0){
    		password=passwordText.getText().trim();
    	}
    	if(databaseNameText.getText().trim().length()>0){
    		database=databaseNameText.getText().trim();
    	}
    	if(serverText.getText().trim().length()>0){
    		server=serverText.getText().trim();
    	}
    	
    	if(portText.getText().trim().length()>0){
    		port=Integer.parseInt(portText.getText().trim());
    	}
		try {			
	    	DatabaseUtils.getConnection("mysql", server, port, username, password, database);
			List<String> tablesList = DatabaseUtils.getTableNames();
			String[] selectTables = new String[tablesList.size()];
    		for(int i=0;i<tablesList.size();i++){
    			selectTables[i]=tablesList.get(i);
    		}			
    		selectColumnOrTableList.table.clearAll();
    		selectColumnOrTableList.table.setItemCount(selectTables.length);
				
				for(int i=0;i<selectTables.length;i++){
					selectColumnOrTableList.table.getItem(i).setText(1, selectTables[i]);;
				}
				selectColumnOrTableList.setRowNums();

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
