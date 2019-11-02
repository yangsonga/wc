package com.mage.crm.interceptors;

import com.mage.crm.base.CrmConstant;
import com.mage.crm.service.UserService;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.util.Base64Util;
import com.mage.crm.util.CookieUtil;
import com.mage.crm.util.StringUtil;
import com.mage.crm.vo.User;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Resource
    private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取cookie中的id，判断是否为空
        String id = CookieUtil.getCookieValue(request, "id");
        AssertUtil.isTrue(StringUtil.isEmpty(id),CrmConstant.LOGIN_NO_CODE,CrmConstant.LOGIN_NO_MSG);
        User user =userService.queryUserById(Base64Util.deCode(id));
        //判断用户是否为空
        AssertUtil.isTrue(user==null,CrmConstant.LOGIN_FAILED_CODE,CrmConstant.LOGIN_FAILED_MSG);
        //判断用户是否可用
        AssertUtil.isTrue("0".equals(user.getIsValid()),"该用户为失效用户");
        return true;
    }
}
