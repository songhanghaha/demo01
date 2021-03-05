package com.example.storage.modules.sys.utils;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.springframework.stereotype.Component;

import com.example.storage.common.base.Response;
import com.example.storage.common.base.ReturnCode;
import com.example.storage.common.base.exception.BusException;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TenCentMessageUtil {
	 final int appId = 1400297148;     //短信应用SDK AppID
     final String appkey = "2cc10cd41ea1ef3ef90a267c5a0f03d7";   //短信应用SDK AppKey
     final String smsSign ="project";// 签名内容
     
     public Response sendSms(String phoneNumber, int templateId, ArrayList<String> params) {
    	log.info("Begin tencent sms, phoneNumber is [{}]", phoneNumber);
    	SmsSingleSender  sender = new SmsSingleSender(appId, appkey);
        SmsSingleSenderResult result = null;
		try {
			result = sender.sendWithParam("86", phoneNumber, templateId, params, smsSign, "", "");
			if (result.result == 0) {
				log.info("End tencent sms");
				return Response.ok();
			}
			log.warn("err: "+result.errMsg);
			return Response.returnCode(String.valueOf(result.result), result.errMsg);
		} catch (JSONException | HTTPException | IOException e) {
			e.printStackTrace();
			throw new BusException(ReturnCode.SMS_SERVER_ERROR);
		}
     }
}
