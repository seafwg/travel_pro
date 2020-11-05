package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

/**
 * @create author: seafwg
 * @create time: 2020
 * @describe: XXX
 * TODO
 **/
public interface UserDao {
    /**
     * 查询用户名是否存在
     * @return
     */
    public User userIsExist(String username);

    /**
     * 保存用户信息
     * @return
     */
    public void saveUserInfo(User user);
}
