package org.dborm.test.execute;

import org.dborm.android.DbormManager;
import org.dborm.test.utils.BaseTest;
import org.dborm.test.utils.domain.UserInfo;

/**
 * 事务的使用
 */
public class TransactionTest extends BaseTest {


    
    
    public void testTransactionSuccess() {
        UserInfo user = getUserInfo();
        DbormManager.getDborm().beginTransaction();
        DbormManager.getDborm().insert(user);

        user.setName("Jack");
        DbormManager.getDborm().update(user);
        boolean result = DbormManager.getDborm().commit();
        assertEquals(true, result);
    }

    
    public void testTransactionError() {
        //如需测试请将如下注释打开

//        UserInfo user = getUserInfo();
//        DbormHandler.getDborm().beginTransaction();
//        DbormHandler.getDborm().insert(user);
//
//        user.setName("Jack");
//        DbormHandler.getDborm().insert(user);//因主键相同所以第二次新增将会出错
//        boolean result = DbormHandler.getDborm().commit();
//        assertEquals(true, result);
//
//        List<UserInfo> userInfos = DbormHandler.getDborm().getEntities(UserInfo.class, "select * from user_info");
//        assertEquals(0, userInfos.size());//因事务具有原子性,所以以上两条记录都没有添加成功
    }


}
