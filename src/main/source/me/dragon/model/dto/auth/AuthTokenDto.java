package me.dragon.model.dto.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by dragon on 11/28/2017.
 */
@Data
@ApiModel(value = "AuthTokenDto")
public class AuthTokenDto {
    @ApiModelProperty(value = "登陆人信息")
    private AuthResDto authResDto;

    @ApiModelProperty(value = "新TOKEN")
    private String newToken;
}
