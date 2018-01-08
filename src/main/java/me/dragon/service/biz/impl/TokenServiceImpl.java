package me.dragon.service.biz.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import me.dragon.core.exception.BusinessException;
import me.dragon.model.dto.auth.AuthResDto;
import me.dragon.model.dto.auth.AuthTokenDto;
import me.dragon.model.enums.ResultCodeEnum;
import me.dragon.service.biz.auth.TokenService;
import me.dragon.utils.JacksonUtil;
import me.dragon.utils.MD5Utils;
import me.dragon.utils.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 *
 * @author dragon
 * @date 11/28/2017
 */
@Service
public class TokenServiceImpl implements TokenService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    // TOKEN过期剩余时间,根据此变量设置续租
    @Value("${auth.expiredRemainMinutes}")
    private Integer expiredRemainMinutes;

    // TOKEN过期分钟数
    @Value("${auth.expiredMinutes}")
    private Integer expiredMinutes;

    @Override
    public AuthTokenDto getAuthTokenDtoByToken(String token) {
        AuthTokenDto authTokenDto;
        try {
            // 续租处理
            authTokenDto = new AuthTokenDto();
            String tokenKey = getViewPrivateKey();
            final Claims claims = Jwts.parser().setSigningKey(tokenKey).parseClaimsJws(token).getBody();
            Date expiration = claims.getExpiration();
            String authResDtoString = (String) claims.get("authResDto");
            DateTime nowDate = new DateTime();
            DateTime expireDate = new DateTime(expiration);
            AuthResDto authResDto = JacksonUtil.parseJson(authResDtoString, AuthResDto.class);
            String newToken = null;
            if (nowDate.plusMinutes(expiredRemainMinutes).isAfter(expireDate)) {
                // 如果当前时间到过期时间小于20分钟,则需要续租
                newToken = this.encodeToken(authResDto);
            }
            authTokenDto.setNewToken(newToken);
            authTokenDto.setAuthResDto(authResDto);
        } catch (Exception e) {
            logger.error("TOKEN解密失败=token={}", token);
            logger.error("TOKEN解密失败={}", e.getMessage(), e);
            throw new BusinessException(ResultCodeEnum.ES000006.code(), ResultCodeEnum.ES000006.msg());
        }
        return authTokenDto;
    }

    public String encodeToken(AuthResDto authResDto) {
        logger.error("encodeToken - 生成TOKEN. authResDto={}", authResDto);
        String token;
        try {
            String authResDtoString = JacksonUtil.toJson(authResDto);
            //将用户信息放入Token
            DateTime nowDate = new DateTime();
            DateTime laterDate = nowDate.plusMinutes(expiredMinutes);
            String tokenKey = getViewPrivateKey();
            token = Jwts.builder().setSubject(authResDto.getUserName()).claim("authResDto", authResDtoString).setIssuedAt(nowDate.toDate()).setExpiration(laterDate.toDate()).signWith(SignatureAlgorithm.HS256, tokenKey).compact();
        } catch (Exception e) {
            logger.error("生成TOKEN, 出现异常={}", e.getMessage(), e);
            throw new BusinessException("生成TOKEN失败!");
        }
        logger.error("encodeToken - 生成TOKEN. authResDto={}, [OK]", authResDto);
        return token;
    }

    /**
     * 获取Token密钥
     */
    public String getViewPrivateKey() {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String decodeKey = ops.get("TOKEN_KEY");
        if (StringUtils.isEmpty(decodeKey)) {
            decodeKey = StringUtils.getUUID();
            setTokenKey(decodeKey);
        }
        return decodeKey;
    }

    public void setTokenKey(String tokenKey) {
        if (StringUtils.isEmpty(tokenKey)) {
            logger.error("tokenKey is null");
            throw new BusinessException("tokenKey为空");
        }
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        // 加密后存入
        ops.set("TOKEN_KEY", MD5Utils.encodeByAES(tokenKey));
    }
}
