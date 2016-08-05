package org.dborm.android;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.dborm.core.api.DataConverter;
import org.dborm.core.api.DbormLogger;
import org.dborm.core.api.SQLExecutor;
import org.dborm.core.domain.PairDborm;
import org.dborm.core.domain.QueryResult;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by shk on 16/7/26.
 */
public class SQLExecutorSQLite implements SQLExecutor {

    public DbormLogger logger;
    public DataConverterSQLite dataConverterSQLite = new DataConverterSQLite();

    public SQLExecutorSQLite(DbormLogger logger) {
        this.logger = logger;
    }


    @Override
    public void execSQL(String sql, List bindArgs, Object connection) throws Exception {
        showSql(getSql(sql, bindArgs));
        SQLiteDatabase database = (SQLiteDatabase) connection;
        database.execSQL(sql);
    }

    @Override
    public void execSQLUseTransaction(Collection<PairDborm<String, List>> execSqlPairList, Object connection) throws Exception {
        SQLiteDatabase database = (SQLiteDatabase) connection;
        String currentSql = "";
        try {
            database.beginTransaction();
            StringBuilder sqlBuffer = new StringBuilder();
            for (PairDborm<String, List> pair : execSqlPairList) {
                currentSql = getSql(pair.first, pair.second);
                sqlBuffer.append(currentSql);
                sqlBuffer.append(";\n");
                if (pair.second == null) {
                    database.execSQL(pair.first, new Object[]{});
                } else {
                    database.execSQL(pair.first, pair.second.toArray());
                }
            }
            showSql(sqlBuffer.toString());
            database.setTransactionSuccessful();// 设置事务标志为成功，当结束事务时就会提交事务
        } catch (Exception e) {
            throw new Exception("出异常的SQL如下:\n" + currentSql, e);
        } finally {
            database.endTransaction();
        }
    }

    @Override
    public List<QueryResult> query(String sql, List bindArgs, Object connection) throws Exception {
        List<QueryResult> queryResults = new ArrayList<QueryResult>();
        showSql(getSql(sql, bindArgs));
        SQLiteDatabase database = (SQLiteDatabase) connection;
        Cursor cursor = null;
        try {
            if (bindArgs == null) {
                cursor = database.rawQuery(sql, new String[]{});
            } else {
                cursor = database.rawQuery(sql, objectArrayToString(bindArgs));
            }
            while (cursor.moveToNext()) {
                QueryResult queryResult = new QueryResult();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String columnLabel = cursor.getColumnName(i);
                    Object value = cursor.getString(i);
                    queryResult.putResult(columnLabel, value);
                    queryResult.addResult(value);
                }
                queryResults.add(queryResult);
            }
        } finally {
            cursor.close();
        }
        return queryResults;
    }

    @Override
    public List<QueryResult> query(String sql, List bindArgs, Map<String, Field> fields, Object connection) throws Exception {
        List<QueryResult> queryResults = new ArrayList<QueryResult>();
        showSql(getSql(sql, bindArgs));
        SQLiteDatabase database = (SQLiteDatabase) connection;
        Cursor cursor = null;
        try {
            if (bindArgs == null) {
                cursor = database.rawQuery(sql, new String[]{});
            } else {
                cursor = database.rawQuery(sql, objectArrayToString(bindArgs));
            }
            while (cursor.moveToNext()) {
                QueryResult queryResult = new QueryResult();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String columnLabel = cursor.getColumnName(i);
                    Object value = dataConverterSQLite.columnValueToFieldValue(cursor, columnLabel, fields.get(columnLabel));
                    queryResult.putResult(columnLabel, value);
                    queryResult.addResult(value);
                }
                queryResults.add(queryResult);
            }
        } finally {
            cursor.close();
        }
        return queryResults;
    }


    private String[] objectArrayToString(List bindArgs) {
        String[] args = new String[bindArgs.size()];
        for (int i = 0; i < bindArgs.size(); i++) {
            Object obj = bindArgs.get(i);
            if (obj != null) {
                args[i] = obj.toString();
            } else {
                args[i] = null;
            }
        }
        return args;
    }

    /**
     * 检查SQL语句并做日志记录
     *
     * @param sql      sql语句
     * @param bindArgs sql语句所绑定的参数
     * @author COCHO
     * @time 2013-5-7上午10:55:38
     */
    private String getSql(String sql, List bindArgs) {
        if (sql != null && bindArgs != null) {
            for (Object bindArg : bindArgs) {
                String arg;
                if (bindArg == null) {
                    arg = "null";
                } else if (bindArg instanceof Date) {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss SSS");
                    arg = "'" + format.format((Date) bindArg) + "'";
                } else if (bindArg instanceof String) {
                    arg = "'" + bindArg + "'";
                } else {
                    arg = bindArg.toString();
                }
                sql = sql.replaceFirst("[?]", arg);
            }
        }
        return sql;
    }

    private void showSql(String sql) {
        if (DataBaseManager.showSql) {
            logger.debug(sql);
        }
    }





}
