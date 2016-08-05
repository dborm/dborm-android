package org.dborm.test.execute;

import org.dborm.android.DbormManager;
import org.dborm.test.utils.BaseTest;
import org.dborm.test.utils.domain.UserInfo;
import java.util.List;

/**
 * 多个实体对象的增删改操作
 */
public class EntityListTest extends BaseTest {


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        List<UserInfo> userInfos = getUserInfos(10);
        boolean result = DbormManager.getDborm().insert(userInfos);
        assertEquals(true, result);
    }

    public void testUpdate() {
        List<UserInfo> userInfos = getUserInfos(10);
        boolean result = DbormManager.getDborm().update(userInfos);
        assertEquals(true, result);
    }

    public void testReplace() {
        List<UserInfo> userInfos = getUserInfos(10);
        boolean result = DbormManager.getDborm().replace(userInfos);
        assertEquals(true, result);
    }

    public void testSaveOrReplace() {
        List<UserInfo> userInfos = getUserInfos(10);
        boolean result = DbormManager.getDborm().replace(userInfos);
        assertEquals(true, result);
    }

    public void testSaveOrUpdate() {
        List<UserInfo> userInfos = getUserInfos(10);
        boolean result = DbormManager.getDborm().replace(userInfos);
        assertEquals(true, result);
    }

    public void testDelete() {
        List<UserInfo> userInfos = getUserInfos(10);
        boolean result = DbormManager.getDborm().delete(userInfos);
        assertEquals(true, result);
        userInfos = DbormManager.getDborm().getEntities(UserInfo.class, "select * from user_info");
        assertEquals(0, userInfos.size());
    }




}
