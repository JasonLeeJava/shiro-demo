package com.jason.shirodemo.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.Set;

/**
 * <一句话简单描述>
 * <详细介绍>
 *
 * @author lihaitao on 2019/7/22
 */
@Controller
@Slf4j
public class ShiroController {

    @PostMapping(value = "/login")
    @ResponseBody
    public JSONObject login(@RequestBody JSONObject request) {
        log.warn(">>>>>>>>>>>> login success!{}", request);
        JSONObject info = new JSONObject();
        String password = request.getString("password");
        String username = request.getString("username");
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new InvalidParameterException();
        }
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            currentUser.login(token);
            info.put("result", "success");
        } catch (AuthenticationException e) {
            info.put("result", "fail");
        }
        return info;
    }

    @RequestMapping("/")
    public String loginPage() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return "index";
        }
        return "login";
    }

    @GetMapping(value = "index")
    @ResponseBody
    public String index() {
        log.warn(">>>>>>>>>>>> index success!");
        Subject subject = SecurityUtils.getSubject();
        PrincipalCollection principals = subject.getPrincipals();
        return "当前登录的人是:" + principals.toString();
    }

    @GetMapping(value = {"/logout"})
    public String logout() {

        Subject currentUser = SecurityUtils.getSubject();
        try {
            String username = currentUser.getPrincipal().toString();
            log.warn(username + "退出登录啦~~~~~~~~~~~~~~~~~~");
            currentUser.logout();
        } catch (AuthenticationException e) {
            log.warn(">>>>>>>>>>>> logout, {}", e);
        }
        return "login";
    }

    @RequiresPermissions("testAuth")
    @RequiresAuthentication
    @RequiresRoles("super")
    @GetMapping("/test-auth-annotation")
    @ResponseBody
    public String testAuth() {
        log.warn("testAuth执行~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        return "com.jason.shirodemo.controller.ShiroController.testAuth";
    }

}
