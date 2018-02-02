package com.qq.wechat.utils;

import com.google.common.base.Strings;

import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;


public class RestUtils {
    /**
     * return X-Real-IP,then X-Forwarded-For,last remoteAddr
     *
     * @param request
     * @return
     */
    public static String getRealIp(HttpServletRequest request) {
        if (request == null) return null;

        //强制返回实际IP地址,因为没有设置前端代理,所以不检查请求头的转发地址,防止请求方借此伪造IP进行攻击
        if (true) {
            return request.getRemoteAddr();
        }

        //ref:http://2hei.net/mt/2010/03/nginx-x-forwarded-for.html
        //X-Forwarded-For: client1, proxy1, proxy2，如果有逗号分割则取第一个
        //with nginx,其中real-ip是真是ip地址，forwarded则是转发链，用逗号分割
        //proxy_set_header   X-Real-IP        $remote_addr;
        //proxy_set_header   X-Forwarded-For  $proxy_add_x_forwarded_for;


        String clientIp = request.getHeader("X-Real-IP");
        if (!Strings.isNullOrEmpty(clientIp)) return clientIp;

        clientIp = request.getHeader("X-Forwarded-For");
        if (Strings.isNullOrEmpty(clientIp)) return request.getRemoteAddr();

        String ip = null;
        //via proxy chain
        String[] array = clientIp.split(",");
        if (array.length > 0) {
            ip = array[0].trim();
        }

        if (Strings.isNullOrEmpty(ip)) {
            //bad header?
            return request.getRemoteAddr();
        }

        return ip;
    }
    
    public static String getInputContent(HttpServletRequest request) {
        ServletInputStream in;
        StringBuilder inputContent = new StringBuilder();  
		try {
			in = request.getInputStream();
		        byte[] b = new byte[4096];  
		        for (int n; (n = in.read(b)) != -1;) {  
		            inputContent.append(new String(b, 0, n, "UTF-8"));  
		        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
       
		return inputContent.toString(); 
    }

}
