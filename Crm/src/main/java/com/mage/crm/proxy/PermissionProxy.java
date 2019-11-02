package com.mage.crm.proxy;

import com.mage.crm.RequestPermission;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.vo.Permission;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.List;

@Component
@Aspect
public class PermissionProxy {
    @Resource
    private HttpSession session;

    @Pointcut(value = "@annotation(com.mage.crm.RequestPermission)")
    public void cut(){};

    @Before(value = "cut()")
    public void before(JoinPoint jp){
        //方法前进行拦截
        MethodSignature methodSignature= (MethodSignature) jp.getSignature();
        Method method = methodSignature.getMethod();
        RequestPermission permission = method.getAnnotation(RequestPermission.class);
        if(permission!=null){
            System.out.println("权限值："+permission.aclValue());
            List<Permission> userPermission = (List<Permission>) session.getAttribute("userPermission");
            AssertUtil.isTrue(userPermission==null,"暂无权限");
            //如果不包含对应的权限字符串,则抛出异常
            AssertUtil.isTrue(!userPermission.contains(permission.aclValue()),"暂无权限");
        }
    }
}
