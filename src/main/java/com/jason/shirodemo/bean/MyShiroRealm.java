package com.jason.shirodemo.bean;

import com.jason.shirodemo.service.ShiroService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * <一句话简单描述>
 * <详细介绍>
 *
 * @author lihaitao on 2019/7/23
 */
public class MyShiroRealm extends AuthorizingRealm {
    private static final Logger logger = LoggerFactory.getLogger(MyShiroRealm.class);
    @Resource
    private ShiroService shiroService;

    /**
     * 权限授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        UserInfo userInfo = (UserInfo) principals.getPrimaryPrincipal();
        for (SysRole sysRole : shiroService.findSysRoleListByUsername(userInfo.getUserName())) {
            authorizationInfo.addRole(sysRole.getRoleName());
            logger.info(sysRole.toString());
            for (SysPermission sysPermission : shiroService.findSysPermissionListByRoleId(sysRole.getId())) {
                logger.info(sysPermission.toString());
                authorizationInfo.addStringPermission(sysPermission.getUrl());
            }
        }
        ;
        return authorizationInfo;
    }

    /**
     * 主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        //获取用户的输入的账号.
        String username = (String) token.getPrincipal();
        logger.info("对用户[{}]进行登录验证..验证开始", username);
        //通过username从数据库中查找 User对象，如果找到，没找到.
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        UserInfo userInfo = shiroService.selectUserInfoByUsername(username);
        if (userInfo == null) {
            // 抛出 帐号找不到异常
            throw new UnknownAccountException();
        }
        return new SimpleAuthenticationInfo(userInfo, userInfo.getPassword(), getName());
    }
}
