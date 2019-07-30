package com.jason.shirodemo.bean;

/**
 * <一句话简单描述>
 * <详细介绍>
 *
 * @author lihaitao on 2019/7/23
 */
public class MockUserInfo extends UserInfo {

    @Override
    public String getUserName() {
        return "zhangsan";
    }

    @Override
    public String getPassword() {
        return "123";
    }
}
