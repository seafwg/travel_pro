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

    /**
     * 获取激活码：
     * @param code
     * @return
     */
    public User findActiveCode(String code);

    /**
     * 更新用户的激活状态码：
     * @param user
     */
    void updateActiveCode(User user);
}
