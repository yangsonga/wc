package com.mage.crm;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})//表明可以注解在方法和类上
@Retention(RetentionPolicy.RUNTIME)//表明该注解是运行期的行为
@Documented
public @interface RequestPermission {
    String aclValue();
}
