package org.dborm.android;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;

import org.dborm.core.domain.PairDborm;
import org.dborm.core.schema.DbormSchemaInit;
import org.dborm.core.utils.StringUtilsDborm;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * 初始化dborm
 *
 * @author HAIKANG SONG
 * @time 2013-5-7上午10:06:33
 */
public class DbormInit {

    /**
     * 数据库结构本次是否发生变化（创建数据库和升级数据库都认为是发生变化）
     */
    public static boolean dbSchemaHasUpdate = false;


    /**
     * 初始化dborm
     *
     * @author HAIKANG SONG
     * @time 2013-5-7上午10:12:52
     */
    public DbormInit() {
    }

    /**
     * 初始化数据库（用户每次登录的时候都要初始化，因为在初始化dborm的时候会自动初始化dborm的缓存，不同用户的缓存信息可能不同）
     *
     * @param context 环境对象
     * @throws Exception 初始化失败抛出异常
     * @author HAIKANG SONG
     * @time 2013-5-7下午4:23:53
     */
    public void initDb(Context context) throws Exception {
        DbormContexts.context = context;
        initSchema();
        File dbFile = new File(DbormContexts.getDbFilePath());
        if (!dbFile.exists()) {// 如果数据库不存在则创建
            dbSchemaHasUpdate = true;
            createDb();
            createTable();
        }
        upgradeDb();
    }

    /**
     * 根据xml描述信息初始化表结构
     */
    public void initSchema() throws Exception {
        DbormSchemaInit schema = new DbormSchemaInit();
        AssetManager assetManager = DbormContexts.context.getResources().getAssets();
        String[] fileNames = assetManager.list(DbormContexts.schemaPath);
        for (String fileName : fileNames) {
            if (fileName.endsWith(".xml")) {//只加载xml文件
                String filePath = DbormContexts.schemaPath + File.separator + fileName;
                InputStream inputStream = DbormContexts.context.getResources().getAssets().open(filePath);
                schema.initSchema(inputStream);
            }
        }
    }

    /**
     * 创建数据库
     *
     * @author HAIKANG SONG
     * @time 2013-5-6下午4:27:29
     */
    private void createDb() {
        SQLiteDatabase database = getSQLiteDatabase();
        database.setVersion(1);//数据库创建的时候版本号必须设置为1，因为使用的sqlcipher框架强制性要求最小版本号为1
        closeSQLiteDatabase(database);
    }

    private void createTable() throws Exception {
        List<PairDborm<String, List>> dbSql = readDbSqlFile(DbormContexts.versionPath + DbormContexts.dbVersionFilePrefix + 1 + DbormContexts.dbVersionFileSuffix);
        boolean result = DbormManager.getDborm().execSql(dbSql);
        if (!result) {
            throw new Exception("初始化数据库表时出错！");
        }
    }

    /**
     * 获取当前数据库的版本
     *
     * @return 当前数据库文件的版本号
     * @author HAIKANG SONG
     * @time 2013-5-6下午4:32:44
     */
    private int getCurrentDbFileVersion() {
        int currentDbVersion = 0;
        SQLiteDatabase database = getSQLiteDatabase();
        if (database != null) {
            currentDbVersion = database.getVersion();
            closeSQLiteDatabase(database);
        }
        return currentDbVersion;
    }

    /**
     * 修改数据库版本
     *
     * @param version 指定的版本号
     * @author HAIKANG SONG
     * @time 2013-5-6下午4:47:02
     */
    private void updateDbVersion(int version) {
        SQLiteDatabase database = getSQLiteDatabase();
        database.setVersion(version);
        closeSQLiteDatabase(database);
        dbSchemaHasUpdate = true;
    }

    /**
     * 升级数据库
     *
     * @throws Exception
     * @author HAIKANG SONG
     * @time 2013-5-6下午4:34:38
     */
    private void upgradeDb() throws Exception {
        int currentDbVersion = getCurrentDbFileVersion();
        if (DbormContexts.dbVersion > currentDbVersion) {// 如果程序设置的版本号（最新版本）大于当前数据库文件的版本号则升级
            try {
                List<PairDborm<String, List>> execSqlPairList = new ArrayList<PairDborm<String, List>>();
                for (int i = currentDbVersion + 1; i <= DbormContexts.dbVersion; i++) {
                    List<PairDborm<String, List>> dbSql = readDbSqlFile(DbormContexts.versionPath + DbormContexts.dbVersionFilePrefix + i + DbormContexts.dbVersionFileSuffix);
                    execSqlPairList.addAll(dbSql);
                }
                boolean result = DbormManager.getDborm().execSql(execSqlPairList);
                if (!result) {
                    throw new Exception("升级数据库出错！");
                }
                updateDbVersion(DbormContexts.dbVersion);//升级成功之后将数据库的版本号修改为当前版本号
            } catch (Exception e) {// 升级数据库出错的时候不影响程序的使用
                throw new Exception("升级数据库出错！");
            }
        }
    }

    /**
     * 读取数据库的脚本文件
     *
     * @param dbSqlName 脚本名称
     * @return 脚本内容或null
     * @author HAIKANG SONG
     * @time 2013-5-6下午7:00:37
     */
    private List<PairDborm<String, List>> readDbSqlFile(String dbSqlName) {
        List<PairDborm<String, List>> execSqlPairList = new ArrayList<PairDborm<String, List>>();
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        InputStream in = null;
        try {
            in = DbormContexts.context.getResources().getAssets().open(dbSqlName);// 获得assets目录下的文件
            // 经测试如果找不到文件in为null，而不是被catch抓到
            /**
             * PRAGMA foreign_keys=off;
             *
             * BEGIN TRANSACTION;
             *
             * ALTER TABLE "main"."user_info" RENAME TO
             * "_user_info_old_20131023";
             *
             * CREATE TABLE "main"."user_info" ( "user_name" NVARCHAR NOT
             * NULL, "auto_login" INTEGER(16,0), "last_login_time"
             * INTEGER(64,0), "login_server" NVARCHAR(512,0), "login_name"
             * NVARCHAR NOT NULL, "face_url" NVARCHAR(512,0), "corp_code"
             * NVARCHAR, "user_id" NVARCHAR, "password" NVARCHAR NOT NULL,
             * "nick_name" NVARCHAR, "test_column" text, PRIMARY
             * KEY("user_id"), CHECK (LENGTH (login_server)<=512), CHECK
             * (LENGTH (face_url)<=512) );
             *
             * INSERT INTO "main"."user_info" ("user_name", "auto_login",
             * "last_login_time", "login_server", "login_name", "face_url",
             * "corp_code", "user_id", "password", "nick_name") SELECT
             * "user_name", "auto_login", "last_login_time", "login_server",
             * "login_name", "face_url", "corp_code", "user_id", "password",
             * "nick_name" FROM "main"."_user_info_old_20131023";
             *
             * COMMIT;
             *
             * PRAGMA foreign_keys=on;
             */

            br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();
            StringUtilsDborm stringUtils = new StringUtilsDborm();
            while (line != null) {
                if (stringUtils.isNotBlank(line)) {
                    sb.append(line);
                    if (line.endsWith(";")) {// 如果以分号结尾说明是一条完整的SQL结束了
                        PairDborm<String, List> sqlPair = PairDborm.create(sb.toString(), null);
                        execSqlPairList.add(sqlPair);
                        sb = new StringBuffer();
                    } else {
                        sb.append("\n");
                    }
                }
                line = br.readLine();
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("读取文件" + dbSqlName + "的内容时出错!", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException ignored) {
            }
        }
        return execSqlPairList;
    }

    private SQLiteDatabase getSQLiteDatabase() {
        return (SQLiteDatabase) DbormManager.getDborm().getConnection();
    }

    private void closeSQLiteDatabase(SQLiteDatabase database) {
        DbormManager.getDborm().getDataBase().closeConnection(database);
    }

}
