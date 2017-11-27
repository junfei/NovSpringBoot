package me.dragon.interceptor;

import me.dragon.core.exception.BusinessException;
import me.dragon.utils.PublicUtil;
import me.dragon.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by dragon on 11/27/2017.
 */
@Service
public class AuthInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    @Value("${cookie.passToken}")
    private String passTokenKey;

    private String env;

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        // 登录认证跳过
        if(request.getRequestURL().toString().contains("/auth")){
            return true;
        }
        // 如果设置env=debug,则跳过Jwt认证
        if(this.getEnv()!=null && this.getEnv().equals("debug")){
            return true;
        }
        // 调试跳过
        if (request.getMethod().toUpperCase().equals("OPTIONS")) {
            return true;
        }
        // COOKIE
        String authHeader = request.getHeader("Authorization");
        Cookie[] cookies = request.getCookies();
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // 取cookie如果取不到不允许通过
            if (!PublicUtil.isEmpty(cookies)) {
                for (Cookie cookie : cookies) {
                    if (passTokenKey.equals(cookie.getName())) {
                        authHeader = cookie.getValue();
                        logger.info("cookie的值为：{}",cookie.getValue());
                        break;
                    }
                }
            } else{
                logger.error("==> 解析token失败,权限验证失败!");
                throw new BusinessException("100500", "解析token失败,权限验证失败");
            }
            logger.error("==> 解析token失败,权限验证失败!");
            String errorMsg = "{\"code\":-2,\"description\" :\"Missing or invalid Authorization header\"}";
            return handleException(request, response, errorMsg);
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    private boolean handleException(HttpServletRequest req, ServletResponse res, String msg) throws IOException {
        if(req.getRequestURL().toString().contains("api")){
            // 如果是API进来的处理,则直接返回JSON
            res.resetBuffer();
            res.setContentType("application/json");
            res.getWriter().write(msg);
            res.flushBuffer();
        }
        return false;
    }
}