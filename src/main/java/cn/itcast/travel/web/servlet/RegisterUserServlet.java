package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @create author: seafwg
 * @create time: 2020
 * @describe: XXX
 * TODO
 **/
@WebServlet("/registerUserServlet")
public class RegisterUserServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        //①校验验证码：
        //请求过来的check
        String check = request.getParameter("check");
        //从session中获取CHECKCODE_SERVER
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");//强制转换成字符串
        //忽略大小写判断是否相等，第一次checkcode_server是空的
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)) {
            //验证码错误
            //创建返回数据格式的ResultInfo类：
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("验证码错误...");
            //将resultInfo序列化为json对象
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(resultInfo);
            //写回数据
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);

            return;
        }

        //②：获取表单数据：
        Map<String, String[]> map = request.getParameterMap();
        //封装对象
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //③调用service
        UserService userService = new UserServiceImpl();
        boolean flag = userService.register(user);
        //④根据返回来的数据响应结果
        ResultInfo info = new ResultInfo();
        if (flag) {
            //注册成功
            info.setFlag(true);
        } else {
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("注册失败");
        }
        //将info对象序列化json传输
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(info);

        //写回给客户端
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        this.doPost(request, response);
    }
}
