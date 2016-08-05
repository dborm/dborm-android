package org.dborm.android;


import org.dborm.core.api.DataConverter;
import org.dborm.core.api.Dborm;
import org.dborm.core.api.DbormLogger;
import org.dborm.core.api.SQLExecutor;
import org.dborm.core.framework.DbormHandler;

/**
 * Created by shk
 * 16/4/20 下午2:09
 */
public class DbormManager {


    private static Dborm dborm;

    public static synchronized Dborm getDborm() {
        if (dborm == null) {
            DbormLogger logger = new Logger();
            SQLExecutor sqlExecutor = new SQLExecutorSQLite(logger);
            DataBaseManager dataBaseManager = new DataBaseManager();
            DataConverter dataConverter = new DataConverterSQLite();
            dataBaseManager.setLogger(logger);
            dataBaseManager.setSqlExecutor(sqlExecutor);
            dataBaseManager.setDataConverter(dataConverter);
            dborm = new DbormHandler(dataBaseManager);
        }
        return dborm;
    }

}
