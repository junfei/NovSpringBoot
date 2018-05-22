package me.dragon.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.dragon.domain.User;
import me.dragon.mapper.UserMapper;
import me.dragon.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>Title: UserServiceImpl. </p>
 *
 * @author dragon
 * @date 2018/5/22 下午11:28
 */
@Slf4j
@Service
public class UserServiceImpl extends BaseService<User> implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * <p>Title: getUser. </p>
     * <p>测试获取用户信息 </p>
     *
     * @return User
     * @author dragon
     * @date 2018/5/22 下午11:26
     */
    @Override
    public List<User> getUserList() {
        return userMapper.selectAll();
    }
}
