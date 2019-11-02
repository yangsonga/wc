package com.mage.crm.base.exceptions;

import com.alibaba.fastjson.JSON;
import com.mage.crm.base.CrmConstant;
import com.mage.crm.model.MessageModel;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

@Component
public class GlobalException implements HandlerExceptionResolver {

    /**
     *  1：未登录异常
     *  2：json格式异常
     *  3：视图异常
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param e 异常类型
     * @return
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        ModelAndView modelAndView = createDefaultModelAndView(httpServletRequest);
        //声明一个自定义异常
        ParamsException paramsException;
        //1:未登录异常
        if(o instanceof HandlerMethod){
          //判断是否是未登录异常
            if(e instanceof ParamsException){
                paramsException = (ParamsException)e;
                if(paramsException.getCode()==CrmConstant.LOGIN_NO_CODE){
                    //是未登录异常
                    modelAndView.addObject("code",paramsException.getCode());
                    modelAndView.addObject("msg",paramsException.getMsg());
                    return modelAndView;
                }
            }
            //2：json异常
            HandlerMethod handlerMethod = (HandlerMethod) o;
            Method method = handlerMethod.getMethod();
            //获取该方法的注解
            ResponseBody responseBody = method.getAnnotation(ResponseBody.class);
            if(responseBody!=null){
                //为json异常
                MessageModel messageModel = new MessageModel();
                messageModel.setCode(CrmConstant.OPS_FAILED_CODE);
                messageModel.setMsg(CrmConstant.OPS_FAILED_MSG);
                if(e instanceof ParamsException){
                    paramsException = (ParamsException)e;
                    messageModel.setCode(paramsException.getCode());
                    messageModel.setMsg(paramsException.getMsg());
                }
                httpServletResponse.setContentType("application;json;charset=utf-8");
                httpServletResponse.setCharacterEncoding("utf-8");
                PrintWriter printWriter = null;
                try {
                    printWriter = httpServletResponse.getWriter();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }finally {
                    if(printWriter!=null){
                        printWriter.write(JSON.toJSONString(messageModel));
                        printWriter.flush();
                        printWriter.close();
                    }
                }
                return null;
            }else{
                //3：视图异常
                if(e instanceof ParamsException){
                    paramsException = (ParamsException)e;
                    modelAndView.addObject("code",paramsException.getCode());
                    modelAndView.addObject("msg",paramsException.getMsg());
                    return modelAndView;
                }else{
                    return modelAndView;
                }
            }

        }
        return null;
    }

    /**
     *  该方法用于设置默认错误视图模型对象
     * @param request 用于存储作用域
     * @return
     */
    public  ModelAndView createDefaultModelAndView(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("code", CrmConstant.OPS_FAILED_MSG);
        modelAndView.addObject("msg",CrmConstant.OPS_FAILED_MSG);
        modelAndView.addObject("uri",request.getRequestURI());
        modelAndView.addObject("ctx",request.getContextPath());
        return modelAndView;
    }
}
