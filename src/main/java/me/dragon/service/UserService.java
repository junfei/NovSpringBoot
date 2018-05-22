package me.dragon.service;

import me.dragon.domain.User;

import java.util.List;

/**
 * <p>Title: UserService. </p>
 *
 * @author dragon
 * @date 2018/5/22 下午11:25
 */
public interface UserService extends IService<User> {

    /**
     * <p>Title: getUserList. </p>
     * <p>测试获取用户信息 </p>
     *
     * @return User
     * @author dragon
     * @date 2018/5/22 下午11:26
     */
    List<User> getUserList();
}
