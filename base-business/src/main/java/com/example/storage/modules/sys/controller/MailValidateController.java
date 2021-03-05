package com.example.storage.modules.sys.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.storage.common.base.Response;
import com.example.storage.common.base.ReturnCode;
import com.example.storage.modules.sys.entity.MailValidate;
import com.example.storage.modules.sys.entity.UserInfo;
import com.example.storage.modules.sys.service.IBackService;
import com.example.storage.modules.sys.utils.MailUtil;

import io.swagger.annotations.Api;
@Api(tags = "系统管理-邮件管理")
@RestController
@RequestMapping("/mail/validate")
public class MailValidateController {
	
	@Autowired
    private  IBackService userService;

    @Autowired
    private MailUtil mailUtil;

    private Logger logger = LoggerFactory.getLogger(MailValidateController.class);
    /**
     * 用户点击链接验证邮箱
     */
    @RequestMapping(value = "/activatemail", method = RequestMethod.GET)
    public Response mailboxVerification(String email, String token) {
        try {
            if (StringUtils.isEmpty(email)) {
                throw new NullPointerException("Incomplete email");
            }
            if (StringUtils.isEmpty(token)) {
                throw new NullPointerException("Incomplete token");
            }
            Long nowTime = System.currentTimeMillis();
            MailValidate mailValidate = userService.selectMailValidateByEmail(email);
            UserInfo userInfo = userService.selectUserByEmail(email);
            Long expiryTime = Long.parseLong(mailValidate.getExpiryTime());
            if (!StringUtils.isEmpty(mailValidate)) {
                if (mailValidate.getState() == 0) {
                    if (expiryTime < nowTime) {//时间比对，判断是否过期
                        MailValidate mailValidate1 = mailUtil.activateMail(userInfo);
                        userService.updateMailValidateByEmail(mailValidate1);
                        return Response.fail();
                    } else {
                        if (mailValidate.getToken().equals(token)) {//token比对，反应是否合法
                            userService.updateUserStateByValidate(userInfo);
                            userService.updateMailValidateStateByEmail(mailValidate);
                            return Response.ok();
                        } else {
                        	return Response.returnCode(ReturnCode.TOKEN_NOT_EXIST.getCode(), ReturnCode.TOKEN_NOT_EXIST.getMsg());
                        }
                    }
                } else {
                	return Response.returnCode(ReturnCode.USER_NOT_ACTIVATE.getCode(), ReturnCode.USER_NOT_ACTIVATE.getMsg());
                }
            } else {
            	return Response.returnCode(ReturnCode.USER_NOT_EXIST.getCode(), ReturnCode.USER_NOT_ACTIVATE.getMsg());
            }
        } catch (Exception e) {
            logger.error("mailboxVerification error : ", e);
            return Response.systemError();
        }
    }
}
