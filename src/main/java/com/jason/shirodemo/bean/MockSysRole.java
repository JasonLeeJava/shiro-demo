package com.jason.shirodemo.bean;

/**
 * <一句话简单描述>
 * <详细介绍>
 *
 * @author lihaitao on 2019/7/23
 */
public class MockSysRole extends SysRole {
    @Override
    public int getId() {
        return 11;
    }

    @Override
    public String getRoleName() {
        return "super";
    }
}
