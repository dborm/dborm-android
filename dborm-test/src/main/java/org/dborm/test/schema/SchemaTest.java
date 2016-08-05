package org.dborm.test.schema;

import org.dborm.android.DbormManager;
import org.dborm.test.utils.BaseTest;
import org.dborm.test.utils.domain.User;

import java.util.Date;

/**
 * Created by shk
 * 16/5/16 13:57
 */
public class SchemaTest extends BaseTest {



    public void testSchema() {
        User user = new User();//User类没有添加任何注解,仍然可以操作,是因为用xml（resources/dborm/schema/schema.schema）描述了该类
        user.setId(USER_ID);
        user.setName("Tom");
        user.setNickname(USER_NICKNAME);
        user.setAge(10);
        user.setCreateTime(new Date());
        boolean result = DbormManager.getDborm().insert(user);
        assertEquals(true, result);
    }




}
