package com.gch.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gch.service.JedisCacheService;
import com.gch.util.CodeConfig;
import com.gch.util.CommonUtils;

@RestController
@RequestMapping("/check")
public class CheckCodeController {

	@Autowired
	JedisCacheService jedisCache;

	@RequestMapping("/canSend")
	public Map<String, Object> canSend(String number) {
		if (!StringUtils.isEmpty(number)) { // 后端二次校验手机号是否为空
			
			String countKey = number + CodeConfig.COUNT_SUFFIX;
			if (jedisCache.existKey(countKey)) { // 判断是否存在key
				
				String count = jedisCache.get(countKey);
				if (Integer.valueOf(count) == CodeConfig.COUNT_TIMES_1DAY) {
					return CommonUtils.resultMap(0, "", "今日已达到上限验证次数,请明天重试", "");
				}
				return CommonUtils.resultMap(1, "", "success", "");
			}
			
			// 若不存在表示可以发送
			return CommonUtils.resultMap(1, "", "success", "");
		}
		
		return CommonUtils.resultMap(0, "", "手机号填写为空", "");
	}
	
	@RequestMapping("/checkCode")
	public Map<String, Object> checkCode(String number, String code) { // 先不做number和code的二次校验了
		
		String phoneKey = number + CodeConfig.PHONE_SUFFIX;
		// 首先校验验证码是否已经过期
		if (jedisCache.existKey(phoneKey)) { 
			String value = jedisCache.get(phoneKey);
			if (value.equals(code)) {
				jedisCache.del(phoneKey);
				return CommonUtils.resultMap(1, "", "验证成功", "");
			} 
			return CommonUtils.resultMap(0, "", "验证码错误", "");
		} 
		return CommonUtils.resultMap(0, "", "验证码错误或者已过期", "");
	}
}
