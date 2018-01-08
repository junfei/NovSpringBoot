package me.dragon.interceptor;

import me.dragon.core.exception.BusinessException;
import me.dragon.model.dto.auth.AuthResDto;
import me.dragon.model.dto.auth.AuthTokenDto;
import me.dragon.service.biz.auth.AuthService;
import me.dragon.utils.PublicUtil;
import me.dragon.utils.base.ThreadLocalMap;
import me.dragon.utils.base.Wrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 *
 * @author dragon
 * @date 11/27/2017
 */
@Service
public class AuthInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    @Autowired
    private AuthService authService;

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
        // 魔法值
        final String bearer = "Bearer ";
        String authHeader = request.getHeader("Authorization");
        // COOKIE
        Cookie[] cookies = request.getCookies();
        if (authHeader == null || !authHeader.startsWith(bearer)) {
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
        final String token = authHeader.substring(7);
        AuthTokenDto authTokenDto;
        AuthResDto authResDto = null;
        try {
            ThreadLocalMap.put("VIEW_TOKEN", token);
            Wrapper<AuthTokenDto> authTokenDtoWrapper = authService.getAuthTokenDtoByToken(token);
            if(PublicUtil.isNotEmpty(authTokenDtoWrapper) && authTokenDtoWrapper.getCode() == Wrapper.SUCCESS_CODE){
                authTokenDto = authTokenDtoWrapper.getResult();
                authResDto = authTokenDto.getAuthResDto();
                String newToken = authTokenDto.getNewToken();
                if(PublicUtil.isNotEmpty(newToken)){
                    response.setHeader("newToken", newToken);
                }
            }
            if(PublicUtil.isEmpty(authResDto)){
                throw new BusinessException("验签失败,DTO为空");
            }
            ThreadLocalMap.put("TOKEN_USER", authResDto.getUserName());
            ThreadLocalMap.put("TOKEN_AUTH_DTO", authResDto);
        } catch (Exception e) {
            logger.error("==> JWT验签, 出现异常={}", e.getMessage(), e);
            String errorMsg = "{\"code\":-3 ,\"message\" :\"Invalid token\"}";
            return handleException(request, response, errorMsg);
        }
        return true;
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