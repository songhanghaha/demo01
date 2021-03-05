package com.example.storage.modules.sys.controller;

import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.storage.common.base.Response;
import com.example.storage.modules.sys.entity.MailValidate;
import com.example.storage.modules.sys.entity.UserInfo;
import com.example.storage.modules.sys.service.IBackService;
import com.example.storage.modules.sys.utils.MailUtil;

import io.swagger.annotations.Api;
@Api(tags = "系统管理-注册管理")
@RestController
@RequestMapping("/user")
public class UserRegisterController {

    @Autowired
    private IBackService backService;

    private Pattern reEmail = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");

    private static Logger logger = LogManager.getLogger(UserRegisterController.class.getName());

    @Autowired
    private MailUtil mailUtil;

    @Value("${sso.email.interval}")
    private String interval;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Response userRegister(UserInfo userInfo) {
        try {
            if (checkUserName(userInfo.getUsername())) {
                logger.warn("this userName already exist , userName is " + userInfo.getUsername());
                return Response.fail();
            }
            if (checkEmail(userInfo.getEmail())) {
                logger.warn("this email already exist , email is " + userInfo.getEmail());
                return Response.fail();
            }
            Integer flag = backService.userRegister(userInfo);
            if (flag == 1) {
                MailValidate mailValidate = mailUtil.activateMail(userInfo);
                backService.insertMailValidate(mailValidate);
                logger.info("register success , userInfo is " + userInfo);
                return Response.ok();
            } else {
                return Response.fail();
            }
        } catch (Exception e) {
            logger.error("register error : ", e);
            return Response.systemError();
        }
    }

    private boolean checkUserName(String username) {
        return backService.selectUserByUserName(username) != null;
    }

    private boolean checkEmail(String email) {
        return backService.selectUserByEmail(email) != null;
    }
}
