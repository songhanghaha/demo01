package com.example.storage.modules.sys.token;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.example.storage.common.base.Response;
import com.example.storage.common.base.ReturnCode;
import com.example.storage.common.config.enums.REDIS_DB;
import com.example.storage.common.utils.HttpUtils;
import com.example.storage.common.utils.JwtUtil;
import com.example.storage.common.utils.UserThreadLocal;
import com.example.storage.modules.sys.utils.RedisUtil;

/**
 * 拦截器实现
 *
 */
public class TokenInterceptor implements HandlerInterceptor{
	@Autowired
	RedisUtil redisUtil;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		if (null == redisUtil){
			WebApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
		    redisUtil = (RedisUtil) webApplicationContext.getBean("redisUtil");
		}
		
		response.setCharacterEncoding(HttpUtils.charset);
		//从http请求头中取出 token
		String token = request.getHeader("App-Token");
		
		if (StringUtils.isBlank(token)) {	
			response.getWriter().write(getResponse(ReturnCode.TOKEN_NOT_EXIST.getCode(), ReturnCode.TOKEN_NOT_EXIST.getMsg()));
			return false;
		}
		
		boolean verify = JwtUtil.verifyToken(token);
		if (!verify) {
			response.getWriter().write(getResponse(ReturnCode.TOKEN_EXPIRED.getCode(), ReturnCode.TOKEN_EXPIRED.getMsg()));
			return false;
	    }
		
		String userId = JwtUtil.getTokenUserId(token);
		Object tokenOfRedis = redisUtil.get(userId, REDIS_DB.USER_TOKEN.getDbIndex());
		if (tokenOfRedis == null) {
			response.getWriter().write(getResponse(ReturnCode.TOKEN_NOT_GET_FROM_REDIS.getCode(), ReturnCode.TOKEN_NOT_GET_FROM_REDIS.getMsg()));
			return false;
		}
		
		if (!token.equals(tokenOfRedis.toString())) {
			response.getWriter().write(getResponse(ReturnCode.TOKEN_AUTH_ERROR.getCode(), ReturnCode.TOKEN_AUTH_ERROR.getMsg()));
			return false;
		}
		
		UserThreadLocal.set(userId);
		return true;
		
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
						   ModelAndView modelAndView) throws Exception {
		//log.info("postHandle 被调用");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//log.info("afterCompletion 被调用");
	}
	
	public String getResponse(String code, String message) {
		Response response=new Response();
		response.setCode(code);
		response.setMessage(message);
	    String result = JSON.toJSONString(response);
		return result;
	}
}
