package me.dragon.service.biz.auth;

import me.dragon.model.dto.auth.AuthTokenDto;
import me.dragon.utils.base.Wrapper;
import org.springframework.stereotype.Service;

/**
 * @author dragon
 * @date 11/28/2017
 */
public interface AuthService {
    Wrapper<AuthTokenDto> getAuthTokenDtoByToken(String token);
}
