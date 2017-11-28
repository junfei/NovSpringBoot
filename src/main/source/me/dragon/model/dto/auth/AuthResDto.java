package me.dragon.model.dto.auth;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author dragon
 * @date 11/28/2017
 */
@Data
public class AuthResDto implements Serializable {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户名
     */
    private String userName;
}
