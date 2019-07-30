package com.jason.shirodemo.service.impl;

import com.jason.shirodemo.bean.*;
import com.jason.shirodemo.service.ShiroService;
import org.springframework.stereotype.Service;

/**
 * <一句话简单描述>
 * <详细介绍>
 *
 * @author lihaitao on 2019/7/23
 */
@Service
public class ShiroServiceImpl implements ShiroService {

    @Override
    public SysRole[] findSysRoleListByUsername(String userName) {
        return new SysRole[]{new MockSysRole()};
    }

    @Override
    public SysPermission[] findSysPermissionListByRoleId(int id) {
        return new SysPermission[]{new MockSysPermission()};
    }

    @Override
    public UserInfo selectUserInfoByUsername(String username) {
        return new MockUserInfo();
    }
}
