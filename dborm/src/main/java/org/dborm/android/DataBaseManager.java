package org.dborm.android;

import android.database.sqlite.SQLiteDatabase;

import org.dborm.core.api.DbormDataBase;

import java.io.File;

/**
 * Created by shk on 16/7/26.
 */
public class DataBaseManager extends DbormDataBase{


    @Override
    public Object getConnection() {
        File dbFile = new File(DbormContexts.getDbFilePath());
        if (!dbFile.exists()) {
            new File(dbFile.getParent()).mkdirs();
        }
        return SQLiteDatabase.openOrCreateDatabase(DbormContexts.getDbFilePath(), null);
    }

    @Override
    public void closeConnection(Object connection) {
        SQLiteDatabase sqLiteDatabase = (SQLiteDatabase) connection;
        sqLiteDatabase.close();
    }
}
