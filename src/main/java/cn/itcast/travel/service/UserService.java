package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

/**
 * @create author: seafwg
 * @create time: 2020
 * @describe: XXX
 * TODO
 **/
public interface UserService {
    /**
     * 用户注册
     * @param user
     * @return
     */
    boolean register(User user);
}
