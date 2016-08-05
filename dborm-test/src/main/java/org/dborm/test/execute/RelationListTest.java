package org.dborm.test.execute;

import org.dborm.android.DbormManager;
import org.dborm.test.utils.BaseTest;
import org.dborm.test.utils.domain.BookInfo;
import org.dborm.test.utils.domain.UserInfo;
import java.util.ArrayList;
import java.util.List;


/**
 * 级联操作的使用
 */
public class RelationListTest extends BaseTest {


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        boolean result = DbormManager.getDborm().insert(getRelationUserInfos());
        assertEquals(true, result);
    }

    
    public void testRelationUpdate() {
        boolean result = DbormManager.getDborm().update(getRelationUserInfos());
        assertEquals(true, result);
    }

    
    public void testRelationDelete() {
        boolean result = DbormManager.getDborm().delete(getRelationUserInfos());
        assertEquals(true, result);
    }

    
    public void testRelationReplace() {
        boolean result = DbormManager.getDborm().replace(getRelationUserInfos());
        assertEquals(true, result);
    }

    
    public void testRelationSaveOrUpdate() {
        boolean result = DbormManager.getDborm().saveOrUpdate(getRelationUserInfos());
        assertEquals(true, result);
    }

    
    public void testRelationSaveOrReplace() {
        boolean result = DbormManager.getDborm().saveOrReplace(getRelationUserInfos());
        assertEquals(true, result);
    }



    private List<UserInfo> getRelationUserInfos() {
        List<UserInfo> userInfos = new ArrayList<UserInfo>();

        UserInfo userInfo = getUserInfo();
        List<BookInfo> bookInfos = new ArrayList<BookInfo>();
        BookInfo bookInfo = getBookInfo();
        bookInfos.add(bookInfo);

        BookInfo bookInfo2 = new BookInfo();
        bookInfo2.setId("222");
        bookInfo2.setUserId("222");
        bookInfo2.setName("《重构》");
        bookInfos.add(bookInfo2);
        userInfo.setBookInfos(bookInfos);
        userInfos.add(userInfo);

        UserInfo userInfo2 = new UserInfo();
        userInfo2.setId("user_222");
        userInfo2.setName("Lucy");
        userInfo2.setNickname("露西");
        List<BookInfo> bookInfos2 = new ArrayList<BookInfo>();
        BookInfo bookInfo3 = new BookInfo();
        bookInfo3.setId("333");
        bookInfo3.setUserId("333");
        bookInfo3.setName("《黑客与画家》");
        bookInfos.add(bookInfo3);

        BookInfo bookInfo4 = new BookInfo();
        bookInfo4.setId("444");
        bookInfo4.setUserId("444");
        bookInfo4.setName("《Java编程思想》");
        bookInfos2.add(bookInfo4);
        userInfo2.setBookInfos(bookInfos2);
        userInfos.add(userInfo2);


        return userInfos;
    }

}
