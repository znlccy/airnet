package com.marticles.airnet.mainserver.interceptor;

import com.marticles.airnet.mainserver.model.User;
import com.marticles.airnet.mainserver.model.UserLocal;
import com.marticles.airnet.mainserver.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Marticles
 * @description UserLocalInterceptor
 * @date 2019/1/23
 */
@Component
public class UserLocalInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    UserLocal userLocal;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String jwt_token = getJWT(request);
        if (jwt_token != null) {
            User user = JwtUtil.getUserInfoByJwt(jwt_token);
            userLocal.setUser(user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && userLocal.getUser() != null) {
            modelAndView.addObject("user", userLocal.getUser());
            modelAndView.addObject("isLogin", "true");
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        userLocal.remove();
    }

    private String getJWT(HttpServletRequest request){
        String jwt_token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("jwt_token")) {
                    jwt_token = cookie.getValue();
                }
            }
        }
        return jwt_token;
    }
}
