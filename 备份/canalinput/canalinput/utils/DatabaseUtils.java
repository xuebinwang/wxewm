package com.primeton.di.trans.steps.canalinput.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.jfree.util.Log;

import com.alibaba.fastjson.JSONObject;


public class DatabaseUtils {
 
    private static String driver;
    private static String url;
    private static String user;
    private static String pwd;
    private static Connection conn;
//    private static final String SQL = "SELECT 1 FROM ";// 数据库操作
    private static final String SQL = "SELECT * FROM ";
    private static final String SQLEND = "  limit 0 , 1";
    
    /**
     * 获取数据库连接
     *
     * @return
     */
    public static Connection getConnection(String access, String server,int port, String userName,String password,String database) {
    	 if (access.trim().equalsIgnoreCase("mysql")) {
 			driver = "com.mysql.jdbc.Driver";
 		}    
     	 try {     
              Class.forName(driver);
          } catch (ClassNotFoundException e) {
        	  throw new RuntimeException( "Class Driver has error ! :", e );         
          }
     	 
     	 user = userName;
     	 pwd = password;
     	 url = "jdbc:"+access.trim()+"://"+server.trim()+":"+port+"/"+database+"?useUnicode=true&characterEncoding=utf8";
        try {
       	
            conn = DriverManager.getConnection(url, user, pwd);
        } catch (SQLException e) {        
        	 e.printStackTrace();
        	 Log.error( "Class conn has error ! : url :"+ url+" user : "+ user +"+ pwd : "+ pwd );         
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     * @param conn
     */
    public static void closeConnection(Connection conn) {
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取数据库下的所有表名
     */
    public static List<String> getTableNames() {
        List<String> tableNames = new ArrayList<>();
//        Connection conn = getConnection();
        ResultSet rs = null;
        try {
            //获取数据库的元数据
            DatabaseMetaData db = conn.getMetaData();
            //从元数据中获取到所有的表名
            rs = db.getTables(null, null, null, new String[] { "TABLE" });
            while(rs.next()) {
                tableNames.add(rs.getString(3));
            }
        } catch (SQLException e) {
           e.printStackTrace();
        } finally {
            try {
                rs.close();
                closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return tableNames;
    }

    /**
     * 获取表中所有字段名称
     * @param tableName 表名
     * @return
     */
    public static List<String> getColumnNames(String tableName) {
        List<String> columnNames = new ArrayList<>();
        //与数据库的连接
//        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName +SQLEND;
        try {
            pStemt = conn.prepareStatement(tableSql);
            //结果集元数据
            ResultSetMetaData rsmd = pStemt.getMetaData();
            //表列数
            int size = rsmd.getColumnCount();
            for (int i = 0; i < size; i++) {
                columnNames.add(rsmd.getColumnName(i + 1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pStemt != null) {
                try {
                    pStemt.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                   e.printStackTrace();
                }
            }
        }
        return columnNames;
    }

    /**
     * 获取表中所有字段类型
     * @param tableName
     * @return
     */
    public static List<String> getColumnTypes(String tableName) {
        List<String> columnTypes = new ArrayList<>();
        //与数据库的连接
//        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName + SQLEND;
        try {
            pStemt = conn.prepareStatement(tableSql);
            //结果集元数据
            ResultSetMetaData rsmd = pStemt.getMetaData();
            //表列数
            int size = rsmd.getColumnCount();
            for (int i = 0; i < size; i++) {
                columnTypes.add(rsmd.getColumnTypeName(i + 1));
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        } finally {
            if (pStemt != null) {
                try {
                    pStemt.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                	e.printStackTrace();
                }
            }
        }
        return columnTypes;
    }

    /**
     * 获取表中字段的所有注释
     * @param tableName
     * @return
     */
    public static List<String> getColumnComments(String tableName) {
        List<String> columnTypes = new ArrayList<>();
        //与数据库的连接
//        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName + SQLEND;
        List<String> columnComments = new ArrayList<>();//列名注释集合
        ResultSet rs = null;
        try {
            pStemt = conn.prepareStatement(tableSql);
            rs = pStemt.executeQuery("show full columns from " + tableName);
            while (rs.next()) {
                columnComments.add(rs.getString("Comment"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                	e.printStackTrace();
                }
            }
        }
        return columnComments;
    }
    
    /*
     * 
     * 返回json的      字段：value 数组
     */
    public static JSONObject queryResultById(String sql, String tableName, List<String> columnNames){
   	
        PreparedStatement pStemt = null;
        ResultSet rs = null;
        JSONObject resultQuery = new JSONObject();
        try {
            pStemt = conn.prepareStatement(sql);
            rs = pStemt.executeQuery();
            while (rs.next()) {             
                for (int i = 0; i < columnNames.size(); i++) {
                    resultQuery.put(columnNames.get(i),rs.getObject(columnNames.get(i)).toString());
                }      
                return resultQuery;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultQuery;
    }
}
