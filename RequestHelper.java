package com.financingplat.web.util;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class RequestHelper {

	public static String getIpAddress(HttpServletRequest request) { 
	    String ip = request.getHeader("x-forwarded-for");
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		      ip = request.getHeader("X-Real-IP"); 
		}
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("Proxy-Client-IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("WL-Proxy-Client-IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("HTTP_CLIENT_IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getRemoteAddr(); 
	    } 
	    if("0:0:0:0:0:0:0:1".equals(ip)){
	    	ip = "127.0.0.1";
	    }
	    return ip; 
	}
	
	public static String getHost(String urlStr) {
		String host = "";
		try {
			URL url = new URL(urlStr);
			host = url.getHost();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return host;
	}
	
	/**
	 * 判断去向链接为空还是跨域
	 * @param request
	 * @param redirectUrl 去向链接
	 * @return true or false
	 */
	public static Boolean isBlankOrCross(HttpServletRequest request, String redirectUrl) {
	    if(StringUtils.isBlank(redirectUrl)) {
	        return true;
	    }
	    
	    return !RequestHelper.getHost(request.getRequestURL().toString()).equals(RequestHelper.getHost(redirectUrl));
	}
	
	//返回全路径
    public static String getRealDirPath(HttpServletRequest request) {
    	String ctx = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        if(request.getServerPort() == 80) {
            ctx = request.getScheme() + "://" + request.getServerName() + request.getContextPath();
        }
        return ctx;
    }
}
