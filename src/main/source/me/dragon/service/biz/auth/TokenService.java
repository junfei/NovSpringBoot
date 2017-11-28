package me.dragon.service.biz.auth;

import me.dragon.model.dto.auth.AuthTokenDto;

/**
 *
 * @author dragon
 * @date 11/28/2017
 */
public interface TokenService {
    AuthTokenDto getAuthTokenDtoByToken(String token);
}
