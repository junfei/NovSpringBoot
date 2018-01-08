package me.dragon.service.biz.impl;

import me.dragon.core.exception.BusinessException;
import me.dragon.model.dto.auth.AuthTokenDto;
import me.dragon.service.biz.auth.AuthService;
import me.dragon.service.biz.auth.TokenService;
import me.dragon.utils.base.WrapMapper;
import me.dragon.utils.base.Wrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 * @author dragon
 * @date 11/28/2017
 */
@Service
public class AuthServiceImpl implements AuthService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private TokenService tokenService;

    @Override
    public Wrapper<AuthTokenDto> getAuthTokenDtoByToken(String token) {
        logger.info("GET_Token:{}", token);
        AuthTokenDto authTokenDto;
        try {
            authTokenDto = tokenService.getAuthTokenDtoByToken(token);
        } catch (BusinessException ex) {
            logger.error("==>GET_Token,出现异常={}", ex.getMessage());
            return WrapMapper.wrap(Wrapper.ERROR_CODE, ex.getMessage());
        } catch (Exception e) {
            logger.error("==>GET_Token,出现异常={}", e.getMessage());
            return WrapMapper.error();
        }
        return WrapMapper.wrap(Wrapper.SUCCESS_CODE, "操作成功", authTokenDto);
    }
}
