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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

//:访问路径  /user/方法名
@WebServlet("/user/*")
public class UserServlet extends HttpServlet {
    //声明UserServlet业务对象：
    private UserService userService = new UserServiceImpl();

    /**
     * 用户注册方法：
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("3");
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
        //UserService userService = new UserServiceImpl();
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

    /**
     * 用户登录方法：
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        // UserService userService = new UserServiceImpl();
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

    /**
     * 获取当前用户名方法：
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getCurUserName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //①从session中获取登录用户
        Object user = request.getSession().getAttribute("user");
        //②将user协会客户端
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),user);
    }

    /**
     * 邮件激活方法：
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void activeEmial(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //①获取激活码
        String activeCode = request.getParameter("code");
        if (activeCode != null) {
            UserService service = new UserServiceImpl();
            boolean activeFlag = service.getActive(activeCode);
            String msg = null;
            if (activeFlag) {
                //激活成功：
                msg = "激活成功，请<a href='login.html'>登录</a>";
            }else {
                msg = "激活失败，请联系管理员!";
            }
            //设置相应信息
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }
    public void exitLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //①销毁session
        request.getSession().invalidate();
        //②跳转得到登陆页面
        response.sendRedirect(request.getContextPath()+"/login.html");
    }
}
