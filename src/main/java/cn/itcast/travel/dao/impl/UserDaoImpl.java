package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @create author: seafwg
 * @create time: 2020
 * @describe: XXX
 * TODO
 **/
public class UserDaoImpl implements UserDao {
    //声明jdbcTemplate:
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
    /**
     * 查询数据库用户名是否被占用
     * @return
     */
    @Override
    public User userIsExist(String username) {
        User user = null;
        try {
            String sql = "SELECT * FROM tab_user WHERE username = ?";
            user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
        } catch (DataAccessException e) {

        }

        return user;
    }

    /**
     * 保存用户信息
     * @return
     */
    @Override
    public void saveUserInfo(User user) {
        String sql = "INSERT INTO tab_user " +
                "(username,password,name,birthday,sex,telephone,email,status,code) " +
                "VALUES(?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql,
                user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getBirthday(),
                user.getSex(),
                user.getTelephone(),
                user.getEmail(),
                user.getStatus(),
                user.getCode()
        );
    }

    /**
     * 获取激活码：
     * @param code
     * @return
     */
    @Override
    public User findActiveCode(String code) {
        User user = null;
        try {
            String sql = "SELECT * FROM tab_user where code = ?";
            user = jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),code);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * 更新激活状态码：
     * @param user
     */
    @Override
    public void updateActiveCode(User user) {
        String sql = "UPDATE tab_user SET status = 'Y' WHERE uid=?";
        jdbcTemplate.update(sql, user.getUid());
    }

    /**
     * 查询数据库中是否存在用户名和密码：
     * @param username
     * @param password
     * @return
     */
    @Override
    public User findUsernameAndPassword(String username, String password) {
        User user = null;
        try {
            String sql = "SELECT * FROM tab_user WHERE username = ? and password = ?";
            user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username, password);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return user;
    }
}
