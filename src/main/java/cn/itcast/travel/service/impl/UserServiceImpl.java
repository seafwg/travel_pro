package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

/**
 * @create author: seafwg
 * @create time: 2020
 * @describe: XXX
 * TODO
 **/
public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();
    /**
     * 注册：
     * @param user
     * @return
     */
    @Override
    public boolean register(User user) {
        //①查询用户名是否被占用：
        User isExist = userDao.userIsExist(user.getUsername());
        if(isExist != null) {
            return false;
        }
        //邮件部分，添加未激活状态，添加激活码
        user.setStatus("N");
        user.setCode(UuidUtil.getUuid());
        //②用户名没有占用：保存用户信息
        userDao.saveUserInfo(user);

        //③给注册用户发送邮件：
        String content = "<a href='http://localhost/travel/activeUserServlet?code="+user.getCode()+"'>点击激活【黑马旅游网】</a>";
        MailUtils.sendMail(user.getEmail(),content,"激活邮件，请您激活...");

        return true;
    }

    /**
     * 获取激活码：激活邮件
     * @param code
     * @return
     */
    @Override
    public boolean getActive(String code) {
        User user = userDao.findActiveCode(code);
        if(user != null) {
            //调用DAO层更新激活状态
            userDao.updateActiveCode(user);
            return true;
        }else{
            return false;
        }
    }
}
