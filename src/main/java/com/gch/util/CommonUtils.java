package com.gch.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class CommonUtils {

	/**
	 * 标准消息封装
	 * 
	 * @param status
	 * @param code
	 * @param msg
	 * @param dataObject
	 * @return
	 */
	public static Map<String, Object> resultMap(int status, String code, String msg, Object dataObject) {
		Map<String, Object> resultMap = new HashMap<>();

		resultMap.put("status", Integer.valueOf(status));
		resultMap.put("code", code == null ? "" : code);
		resultMap.put("msg", msg == null ? "" : msg);
		resultMap.put("data", toJsonString(dataObject));
		return resultMap;
	}

	public static String toJsonString(Object dataWithDate) {
		String result = JSONObject.toJSONString(dataWithDate,
				new SerializerFeature[] { SerializerFeature.WriteMapNullValue,
						SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat });
		return result;
	}

	/**
	 * 生成数字验证码
	 * @param len
	 * @return
	 */
	public static String getCode(int len) {
		String code = "";
		for (int i = 0; i < len; i++) {
			int rand = new Random().nextInt(10);
			code += rand;
		}
		return code;
	}
	
	/**
	 * 获取当前时间(转化为秒)
	 * @return
	 */
	public static int getCurTime() {
		Calendar now = Calendar.getInstance();
		int h = now.get(Calendar.HOUR_OF_DAY);
		int m = now.get(Calendar.MINUTE);
		int s = now.get(Calendar.SECOND);
		return h * 3600 + m * 60 + s;
		
	}
}
