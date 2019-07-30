package com.jason.shirodemo.service;

import com.jason.shirodemo.bean.SysPermission;
import com.jason.shirodemo.bean.SysRole;
import com.jason.shirodemo.bean.UserInfo;

/**
 * <一句话简单描述>
 * <详细介绍>
 *
 * @author lihaitao on 2019/7/23
 */
public interface ShiroService {
    /**
     * 通过用户名称查询角色列表
     *
     * @param userName
     * @return
     */
    SysRole[] findSysRoleListByUsername(String userName);

    /**
     * 根据角色id查询权限列表
     *
     * @param id
     * @return
     */
    SysPermission[] findSysPermissionListByRoleId(int id);

    /**
     * 通过用户名查询用户信息
     *
     * @param username
     * @return
     */
    UserInfo selectUserInfoByUsername(String username);
}
