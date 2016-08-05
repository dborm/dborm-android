package org.dborm.test.init;

import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;

import org.dborm.android.DbormContexts;
import org.dborm.android.DbormInit;
import org.dborm.android.DbormManager;
import org.dborm.core.api.Dborm;
import org.dborm.test.utils.MainActivity;
import org.dborm.test.utils.domain.UserInfo;

import java.io.File;
import java.util.Date;


public class InitTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public String dbPath;
    Dborm dborm;

    public InitTest() {
        super("org.dborm.test", MainActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testInit() {
        initContexts();
        initDb();
        check();
    }

    public void initContexts() {
        dbPath = Environment.getExternalStorageDirectory() + File.separator + "tbc" + File.separator + "db" + File.separator
                + "db_test.db";
        DbormContexts.setDbFilePath(dbPath);
        DbormContexts.setShowSql(true);
        DbormContexts.dbVersion = 1;
    }

    public void initDb() {
        File oldFile = new File(DbormContexts.getDbFilePath());
        oldFile.delete();
        try {
            dborm = DbormManager.getDborm();
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

    private void check() {
        UserInfo user = new UserInfo();
        user.setId("USID1");
        user.setName("Tom");
        user.setAge(10);
        user.setCreateTime(new Date());
        boolean result = dborm.insert(user);//添加一条数据测试一下数据库是否可用
        assertEquals(true, result);
        result = dborm.delete(user);
        assertEquals(true, result);
    }


}
