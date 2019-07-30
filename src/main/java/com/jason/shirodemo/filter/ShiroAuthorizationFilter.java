package com.jason.shirodemo.filter;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * <一句话简单描述>
 * <详细介绍>
 *
 * @author lihaitao on 2019/7/22
 */
@Slf4j
public class ShiroAuthorizationFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "519");
        jsonObject.put("msg", "onAccessDenied");
        HttpServletResponse res = (HttpServletResponse) response;
        try (PrintWriter out = response.getWriter()) {
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/json");
            out.println(jsonObject);
        } catch (Exception e) {
            log.warn("com.jason.shirodemo.filter.ShiroAuthorizationFilter.onAccessDenied>>>>>>>>>>>>>", e);
        }
        return false;
    }

    @Bean
    public FilterRegistrationBean registration(ShiroAuthorizationFilter shiroFilter) {
        FilterRegistrationBean<ShiroAuthorizationFilter> registration = new FilterRegistrationBean<>(shiroFilter);
        registration.setEnabled(false);
        return registration;
    }
}
