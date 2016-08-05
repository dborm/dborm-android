package org.dborm.android;

import android.database.sqlite.SQLiteDatabase;

import org.dborm.core.api.DbormDataBase;

/**
 * Created by shk on 16/7/26.
 */
public class DataBaseManager extends DbormDataBase{


    @Override
    public Object getConnection() {
        return SQLiteDatabase.openOrCreateDatabase(DbormContexts.getDbFilePath(), null);
    }

    @Override
    public void closeConnection(Object connection) {
        SQLiteDatabase sqLiteDatabase = (SQLiteDatabase) connection;
        sqLiteDatabase.close();
    }
}
