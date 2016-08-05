package org.dborm.android;


import android.content.Context;

import java.io.File;

/**
 * Created by shk on 16/6/29.
 */
public class DbormContexts extends org.dborm.core.utils.DbormContexts {


    public static Context context;


    /**
     * 当前dborm的版本（初始化dborm的时候会根据该版本号决定是否要重建数据库）
     */
    public static int dbVersion = 1;

    /**
     * 表结构信息的文件路径((在assets目录下的相对位置，目录尾不能添加/)
     */
    public static String schemaPath = "db" + File.separator + "schema";

    /**
     * 数据库版本升级的脚本文件位置(在assets目录下的相对位置)
     */
    public static String versionPath = "db" + File.separator + "version" + File.separator;

    /**
     * 版本文件名字的前缀
     */
    public static String dbVersionFilePrefix = "version_";


    /**
     * 版本文件名字的后缀
     */
    public static String dbVersionFileSuffix = ".sql";


    /**
     * 数据库存放路径（包含数据库名称）
     */
    private static String dbFilePath;


    /**
     * 获得数据库文件的路径
     *
     * @return 数据库路径
     * @author HAIKANG SONG
     * @time 2013-5-2下午4:45:10
     */
    public static String getDbFilePath() {
        return dbFilePath;
    }

    public static void setDbFilePath(String dbFilePath) {
        DbormContexts.dbFilePath = dbFilePath;
    }


    public static boolean isShowSql() {
        return DataBaseManager.showSql;
    }

    public static void setShowSql(boolean showSql) {
        DataBaseManager.showSql = showSql;
    }
}
