package org.dborm.test.utils;

import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;

import org.dborm.android.DbormContexts;
import org.dborm.android.DbormInit;
import org.dborm.android.DbormManager;
import org.dborm.test.utils.domain.BookInfo;
import org.dborm.test.utils.domain.UserInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BaseTest extends ActivityInstrumentationTestCase2<MainActivity> {


    public static final String USER_ID = "USER_ID";
    public static final String USER_NICKNAME = "汤姆";
    public static final String BOOK_ID = "BOOK_ID";

    public BaseTest() {
        super("org.dborm.test", MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        initDborm();
        cleanTable();
    }


    public void initDborm() {
        initContexts();
        initDb();
    }

    public void initContexts() {
        String dbPath = Environment.getExternalStorageDirectory() + File.separator + "dborm" + File.separator + "db_test.db";
        DbormContexts.setDbFilePath(dbPath);
        DbormContexts.setShowSql(true);
        DbormContexts.dbVersion = 1;
    }

    public void initDb() {
        File oldFile = new File(DbormContexts.getDbFilePath());
        oldFile.delete();
        try {
            new DbormInit().initDb(this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (DbormInit.dbSchemaHasUpdate) {
            System.out.println("数据库的数据结构本次发生了改变");
        }
        File file = new File(DbormContexts.getDbFilePath());
        assertEquals(true, file.exists());
    }


    public static UserInfo getUserInfo() {
        UserInfo user = new UserInfo();
        user.setId(USER_ID);
        user.setName("Tom");
        user.setNickname(USER_NICKNAME);
        user.setAge(10);
        user.setCreateTime(new Date());
        return user;
    }

    public static BookInfo getBookInfo() {
        BookInfo bookInfo = new BookInfo();
        bookInfo.setId(BOOK_ID);
        bookInfo.setUserId(USER_ID);
        bookInfo.setName("《代码简洁之道》");
        bookInfo.setPrice(55.0);
        bookInfo.setLooked(true);
        bookInfo.setReadTime(15l);
        return bookInfo;
    }

    public static List<UserInfo> getUserInfos(int num) {
        List<UserInfo> userInfos = new ArrayList<UserInfo>();
        for (int i = 0; i < num; i++) {
            UserInfo user = new UserInfo();
            user.setId(USER_ID + i);
            user.setName("Tom");
            user.setCreateTime(new Date());
            userInfos.add(user);
        }
        return userInfos;
    }


    public static void cleanTable() {
        boolean result = DbormManager.getDborm().execSql("delete from user_info");
        assertEquals(true, result);
        result = DbormManager.getDborm().execSql("delete from book_info");
        assertEquals(true, result);
    }

//    @Override
//    protected void tearDown() throws Exception {
//        cleanTable();
//    }

}
