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

    /**
     * 获取激活码：
     * @param code
     * @return
     */
    boolean getActive(String code);

    /**
     * 用户登录
     * @param user
     * @return
     */
    User loginUser(User user);
}
