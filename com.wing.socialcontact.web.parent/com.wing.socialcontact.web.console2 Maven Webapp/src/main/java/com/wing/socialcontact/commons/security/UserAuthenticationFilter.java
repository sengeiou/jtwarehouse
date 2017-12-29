/**  
 * @Title UserAuthenticationFilter.java
 * @date 2016-12-4 上午10:46:12
 * @Copyright: 2016 
 */
package com.wing.socialcontact.commons.security;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.subject.Subject;

import com.wing.socialcontact.common.model.LoginInfo;
import com.wing.socialcontact.common.model.OnLineUser;
import com.wing.socialcontact.util.ServletUtil;

/**
 * 验证用户是否在线
 * 用户已验证通过，并且在全局在线用户列表,否定登录超时
 * @author dijuli
 * @version 1.0
 */
public class UserAuthenticationFilter extends org.apache.shiro.web.filter.authc.UserFilter{

	@Override
	protected boolean isAccessAllowed(ServletRequest request,ServletResponse response, Object mappedValue) {
		HttpServletRequest req=(HttpServletRequest)request;
		System.out.println("验证登录信息=="+req.getRequestURI());
		System.out.println("sessionId=="+req.getSession().getId());
		if (isLoginRequest(request, response)) {
            return true;
        } else {
            Subject subject = getSubject(request, response);
            // 验证是否有登录用户
            if(subject.getPrincipal() != null&&subject.isAuthenticated()){
            	//判断用户是否在全局在线列表
        		String userId=(String)subject.getPrincipal();//获取用户id
            	
            	Map<String,OnLineUser> usersMap =ServletUtil.getOnLineUsers();
    			OnLineUser om=usersMap.get(userId);//获取在线人员列表
    			//判断当前用户是否在在线人员列表里面，如果不在，不能继续访问
            	if(om==null){
            		return false;
            	}else{
            		Map<String,LoginInfo> loginInfos=om.getLoginInfos();
            		if(loginInfos.containsKey(((HttpServletRequest)request).getSession().getId())){
            			return true;
            		}else{
            			return false;
            		}
            	}
            }else{
            	
            	return false;
            }
        }
	}

}
