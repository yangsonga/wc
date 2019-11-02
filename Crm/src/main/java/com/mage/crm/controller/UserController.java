package com.mage.crm.controller;

import com.mage.crm.base.BaseController;
import com.mage.crm.base.CrmConstant;
import com.mage.crm.base.exceptions.ParamsException;
import com.mage.crm.model.MessageModel;
import com.mage.crm.model.UserModel;
import com.mage.crm.query.UserQuery;
import com.mage.crm.service.UserService;
import com.mage.crm.util.CookieUtil;
import com.mage.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    @Resource
    private UserService userService;

    @RequestMapping("/userLogin")
    @ResponseBody //直接将结果返回给前台，不需要视图页面
    public MessageModel userLogin(String userName, String userPwd){
        MessageModel messageModel = new MessageModel();
        try{
            //由于调用userService的login方法可能出现异常，不能抛出给虚拟机，所以捕获
            UserModel userModel = userService.login(userName,userPwd);
            messageModel.setResult(userModel);
        }catch (ParamsException pe){
            pe.printStackTrace();
            messageModel.setCode(CrmConstant.LOGIN_FAILED_CODE);
            messageModel.setMsg(pe.getMsg());
        }catch (Exception e){
            messageModel.setCode(CrmConstant.OPS_FAILED_CODE);
            messageModel.setMsg(CrmConstant.OPS_FAILED_MSG);
        }
        return messageModel;
    }

    @RequestMapping("/updatePwd")
    @ResponseBody
    public MessageModel updatePwd(HttpServletRequest request, String oldPassword, String newPassword,String confirmPassword){
        MessageModel messageModel = new MessageModel();
        String id = CookieUtil.getCookieValue(request, "id");
        try{
            userService.updatePwd(id,oldPassword,newPassword,confirmPassword);
            //具体的提示信息
            messageModel.setMsg("密码修改成功");
        }catch (ParamsException pe){
            messageModel.setCode(CrmConstant.OPS_FAILED_CODE);
            messageModel.setMsg(pe.getMsg());
        }catch (Exception e){
            e.printStackTrace();
            messageModel.setCode(CrmConstant.OPS_FAILED_CODE);
            messageModel.setMsg(CrmConstant.OPS_FAILED_MSG);
        }
        return messageModel;
    }
    @RequestMapping("queryAllCustomerManager")
    @ResponseBody
    public List<User> queryAllCustomerManager(){
       return userService.queryAllCustomerManager();
    }

    @RequestMapping("/index")
    public String index(){
        return "user";
    }

    @RequestMapping("/queryUsersByParams")
    @ResponseBody
    public Map<String,Object> queryUsersByParams(UserQuery userQuery){
        return userService.queryUsersByParams(userQuery);
    }
    @ResponseBody
    @RequestMapping("/insert")
    public MessageModel insert(User user){
        userService.insert(user);
        return createMessageModel("插入用户成功");
    }
    @ResponseBody
    @RequestMapping("/update")
    public MessageModel update(User user){
        userService.update(user);
        return createMessageModel("用户修改成功");
    }
    @ResponseBody
    @RequestMapping("/delete")
    public MessageModel delete(Integer userId){
        userService.delete(userId);
        return createMessageModel("用户修改成功");
    }

}
