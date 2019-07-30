package com.jason.shirodemo.config;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <一句话简单描述>
 * <详细介绍>
 *
 * @author lihaitao on 2019/7/24
 */
public class UserRealm extends AuthorizingRealm {
    private Logger logger = LoggerFactory.getLogger(UserRealm.class);

    private final String SUPER_USER = "USER196555";
    private final String COMMON_ADMIN_USER = "USER196556";
    private final String LEGAL_USERNAME = "USER";


    @Override
    @SuppressWarnings("unchecked")
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Session session = SecurityUtils.getSubject().getSession();
        //查询用户的权限
        Subject subject = SecurityUtils.getSubject();
        String s = subject.getPrincipal().toString();
        logger.warn(">>>>>>>>>>>>>>>" + s);

        //为当前用户设置角色和权限
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRole(session.getAttribute("role") == null ? "" : session.getAttribute("role").toString());
        authorizationInfo.addStringPermission(session.getAttribute("userPermission") == null ? "" : session.getAttribute("userPermission").toString());
        return authorizationInfo;
    }

    /**
     * 验证当前登录的Subject
     * LoginController.login()方法中执行Subject.login()时 执行此方法
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        String loginName = (String) authcToken.getPrincipal();
        // 获取用户密码
        String password = new String((char[]) authcToken.getCredentials());
        JSONObject user = new JSONObject();
        user.put("username", loginName);
        user.put("password", password);
        // 此处应该去查询是否有这个员工。。。
        if (!loginName.startsWith(LEGAL_USERNAME)) {
            //没找到帐号
            throw new UnknownAccountException();
        }

        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getString("username"),
                user.getString("password"),
                //ByteSource.Util.bytes("salt"), salt=username+salt,采用明文访问时，不需要此句
                getName()
        );
        //session中不需要保存密码
        user.remove("password");
        Session session = SecurityUtils.getSubject().getSession();
        if (SUPER_USER.equals(loginName)) {
            session.setAttribute("role", "super");
            session.setAttribute("userPermission", "testAuth");
        } else if (COMMON_ADMIN_USER.equals(loginName)) {
            session.setAttribute("role", "common");
            session.setAttribute("userPermission", "testAuth");
        }
        //将用户信息放入session中
        session.setAttribute("userInfo", user);
        return authenticationInfo;
    }
}