package org.dborm.test.execute;

import org.dborm.android.DbormManager;
import org.dborm.test.utils.BaseTest;
import org.dborm.test.utils.domain.BookInfo;
import org.dborm.test.utils.domain.UserInfo;

/**
 * 级联操作的使用
 */
public class RelationTest extends BaseTest {


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        boolean result = DbormManager.getDborm().insert(getRelationUserInfo());
        assertEquals(true, result);
    }

    
    public void testRelationUpdate() {
        boolean result = DbormManager.getDborm().update(getRelationUserInfo());
        assertEquals(true, result);
    }

    
    public void testRelationDelete() {
        boolean result = DbormManager.getDborm().delete(getRelationUserInfo());
        assertEquals(true, result);
    }

    
    public void testRelationReplace() {
        boolean result = DbormManager.getDborm().replace(getRelationUserInfo());
        assertEquals(true, result);
    }

    
    public void testRelationSaveOrUpdate() {
        boolean result = DbormManager.getDborm().saveOrUpdate(getRelationUserInfo());
        assertEquals(true, result);
    }

    
    public void testRelationInsertAndUpdate() {
        UserInfo userInfo = getUserInfo();
        BookInfo bookInfo = new BookInfo();
        bookInfo.setId("111");
        bookInfo.setUserId("222");
        bookInfo.setName("《重构》");
        userInfo.setBookInfo(bookInfo);
        boolean result = DbormManager.getDborm().saveOrUpdate(userInfo);//因bookInfo的主键并不存在,所以userInfo做新增操作,bookInfo做修改操作
        assertEquals(true, result);
    }

    
    public void testRelationSaveOrReplace() {
        boolean result = DbormManager.getDborm().saveOrReplace(getRelationUserInfo());
        assertEquals(true, result);
    }



    private UserInfo getRelationUserInfo() {
        UserInfo userInfo = getUserInfo();
        BookInfo bookInfo = getBookInfo();
        userInfo.setBookInfo(bookInfo);
        return userInfo;
    }

}
