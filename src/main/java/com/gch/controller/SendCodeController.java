package com.gch.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gch.service.JedisCacheService;
import com.gch.util.CodeConfig;
import com.gch.util.CommonUtils;

@RestController
@RequestMapping("/send")
public class SendCodeController {

	@Autowired
	JedisCacheService jedisCache;

	@RequestMapping("/sendCode")
	public Map<String, Object> sendCode(String number) {
		
		String key = number + CodeConfig.COUNT_SUFFIX;
		// 首先判断key是否存在
		if (jedisCache.existKey(key)) {
			
			String c = jedisCache.get(key);
			Integer count = Integer.valueOf(c);
			count = count + 1;
			// 当前次数加一后重新set
			Integer expireTime = CodeConfig.SECONDS_PER_DAY - CommonUtils.getCurTime();
			jedisCache.setWithExpire(key, String.valueOf(count), expireTime);
		} else {
			// 若不存在,则直接将count设为1并存入到redis中
			jedisCache.set(number + CodeConfig.COUNT_SUFFIX, "1");
		}

		// 获取验证码,写入到redis并返回给前端
		String code = CommonUtils.getCode(CodeConfig.CODE_LEN);
		jedisCache.setWithExpire(number + CodeConfig.PHONE_SUFFIX, code, CodeConfig.CODE_TIMEOUT);
		return CommonUtils.resultMap(1, "", "success", code);
	}
	
	@RequestMapping("/test")
	public Map<String, Object> testRedis() {
		System.out.println(jedisCache.get("k1"));
		return CommonUtils.resultMap(1, "", "success", jedisCache.get("k1"));
	}
	
}
