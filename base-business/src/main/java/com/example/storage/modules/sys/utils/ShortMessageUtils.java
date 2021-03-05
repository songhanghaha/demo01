package com.example.storage.modules.sys.utils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.storage.common.base.KSSConstants;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ShortMessageUtils {
    @Autowired
	RedisUtil redisUtil;
	
	/**
	 * 生成随机的a位数：短信验证码
	 * @return
	 */
    public String getMsgCode(int a) {
    	StringBuilder sb = new StringBuilder();
    	Random random = new Random();
    	for (int i = 0; i < a; i++) {
			sb.append(Integer.valueOf(random.nextInt(10)));
		}
    	return sb.toString();
    }
    
    /**
     * redis 缓存限制短信发送验证码时间间隔
     * @param phone
     * @param minutes
     * @param redisDB
     */
    public void sendSmsTimeIntervalLimit(String phone, int minutes, int indexdb) {
 	   Calendar calendar = Calendar.getInstance();
       calendar.add(Calendar.MINUTE, minutes);
       long intervalTime = calendar.getTimeInMillis();
       String intervalString = String.valueOf(intervalTime);
 	   redisUtil.set(phone, intervalString, KSSConstants.sixtySeconds*minutes, indexdb);
    }
    
    /**
     * redis 缓存验证码和验证码过期时间
     * @param phone
     * @param code
     * @param minutes
     * @param redisDB
     */
    public void saveCodeAndExpiredDate(String phone, String code, int minutes, int indexdb) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, minutes);
		long expiredTime = calendar.getTimeInMillis();
		String value = code + "#" + expiredTime;//拼接验证码和验证码过期时间
		redisUtil.set(phone, value, KSSConstants.sixtySeconds*minutes, indexdb);
    }
    
    /**
     * redis 判断短信发送验证码是否受到时间限制
     * @param phone
     * @param redisDB
     * @return
     * @throws ParseException
     */
    public Boolean isLimited(String phone, int indexdb) {
		if (!redisUtil.exists(phone, indexdb)) {
			return false;
		}
		String intervalString = redisUtil.get(phone, indexdb).toString();
		try {
			long intervalTime = Long.parseLong(intervalString);	
			if (System.currentTimeMillis() < intervalTime) {
				log.warn("The verification code has been sent, please send it later");
				return true;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return false;
    }
    
    /**
     * 校验手机号格式
     * @param phone
     * @return
     */
    public Boolean isMatch(String phoneNumber) {
       String regex = "^1\\d{10}$";
   	   Pattern p = Pattern.compile(regex);
   	   Matcher m = p.matcher(phoneNumber);
   	   Boolean isMatch = m.matches();
   	   return isMatch;
    }

}
