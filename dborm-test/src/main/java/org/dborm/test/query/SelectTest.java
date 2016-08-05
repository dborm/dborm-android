package org.dborm.test.query;

import org.dborm.android.DbormManager;
import org.dborm.core.domain.QueryResult;
import org.dborm.core.framework.SQLExecutorHandler;
import org.dborm.test.utils.BaseTest;
import org.dborm.test.utils.domain.BookInfo;
import org.dborm.test.utils.domain.UserInfo;
import org.dborm.core.api.DbormDataBase;
import org.dborm.core.utils.DbormLoggerHandler;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;


/**
 * 查询相关的测试用例
 * Created by shk
 */
public class SelectTest extends BaseTest {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        UserInfo userInfo = getUserInfo();
        DbormManager.getDborm().insert(userInfo);
        BookInfo bookInfo = getBookInfo();
        bookInfo.setUserId(userInfo.getId());
        DbormManager.getDborm().insert(bookInfo);
    }


    public void testGetEntity() {
        String sql = "SELECT * FROM user_info where id = ? ";
        String[] bindArgs = new String[]{USER_ID};
        UserInfo user = DbormManager.getDborm().getEntity(UserInfo.class, sql, USER_ID);
        assertEquals(USER_NICKNAME, user.getNickname());
    }

    public void testGetEntities() {
        String sql = "SELECT * FROM user_info";
        List<UserInfo> user = DbormManager.getDborm().getEntities(UserInfo.class, sql);
        assertEquals(USER_NICKNAME, user.get(0).getNickname());
    }

    public void testGetEntitiesByExample() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(USER_ID);
        userInfo.setNickname(USER_NICKNAME);
        List<UserInfo> userList = DbormManager.getDborm().getEntitiesByExample(userInfo, true);
        assertEquals(1, userList.size());
    }

    public void testGetEntityCount() {
        long count = DbormManager.getDborm().getEntityCount(UserInfo.class);
        assertEquals(1, count);
    }

    public void testGetCount() {
        String sql = "SELECT COUNT(*) FROM user_info where id = ? ";
        long count = DbormManager.getDborm().getCount(sql, USER_ID);
        assertEquals(1, count);
    }

    public void testUseResultSet() {
        String sql = "SELECT id, nickname, age FROM user_info where id = ? ";
        List<String> selectionArgs = new ArrayList<String>();
        selectionArgs.add(USER_ID);
        DbormDataBase dbormDataBase = DbormManager.getDborm().getDataBase();
        Connection conn = null;
        try {
            conn = (Connection) dbormDataBase.getConnection();
            List<QueryResult> queryResults = new SQLExecutorHandler(new DbormLoggerHandler()).query(sql, selectionArgs, conn);
            for (QueryResult queryResult : queryResults) {
                assertEquals(USER_NICKNAME, queryResult.getObject("nickname"));
                assertEquals(USER_ID, queryResult.getObject(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                dbormDataBase.closeConnection(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}
