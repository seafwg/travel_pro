package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //①获取登录用户数据
        Map<String, String[]> map = request.getParameterMap();
        //②封装对象
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //③调用service查询：
        UserService userService = new UserServiceImpl();
        User user1 = userService.loginUser(user);
        ResultInfo info = new ResultInfo();
        //④判断用户对象是否为空
        if(user1 == null) {
            info.setFlag(false);
            info.setErrorMsg("用户名或者密码错误");
        }
        //⑤判断用户是否激活
        if(user1 != null && !"Y".equals(user1.getStatus())) {
            //用户尚未激活
            info.setFlag(false);
            info.setErrorMsg("您尚未激活，请激活");
        }
        //⑥判断是否登录成功
        if(user1 != null && "Y".equals(user1.getStatus())) {
            request.getSession().setAttribute("user",user1);//登录成功标记
            //登录成功
            info.setFlag(true);
        }
        //⑦响应数据
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),info);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
