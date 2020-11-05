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
                "(username,password,name,birthday,sex,telephone,email) " +
                "VALUES(?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql,
                user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getBirthday(),
                user.getSex(),
                user.getTelephone(),
                user.getEmail()
        );
    }
}
