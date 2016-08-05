package org.dborm.test.upgrade;


import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import org.dborm.android.DbormContexts;
import org.dborm.android.DbormInit;
import org.dborm.test.utils.MainActivity;

import java.io.File;

public class UpgradeTest extends ActivityInstrumentationTestCase2<MainActivity> {


    public UpgradeTest() {
        super("org.dborm.test", MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testUpgrade() {
        String dbPath = Environment.getExternalStorageDirectory() + File.separator + "dborm" + File.separator + "db_test.db";
        DbormContexts.setDbFilePath(dbPath);
        DbormContexts.setShowSql(true);
        DbormContexts.dbVersion = 1;

        initDb();

        DbormContexts.dbVersion = 3;// 在项目中升级数据库只需修改版本号即可，不需要再次调用下面的初始化数据库操作

        initDb();

    }

    private void initDb() {
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


}
