package com.primeton.di.trans.steps.canalinput.utils.domain;

import java.io.Serializable;

/**
 * 动作表
 */
public class TableData implements Serializable {

    private String databaseName;
    private String tableName;
    private Object object;
    private String eventType;
    private long logfileOffset;
    private long createTime;

    public long getLogfileOffset() {
        return logfileOffset;
    }

    public void setLogfileOffset(long logfileOffset) {
        this.logfileOffset = logfileOffset;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
