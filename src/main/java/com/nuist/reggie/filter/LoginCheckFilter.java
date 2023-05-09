package com.nuist.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.nuist.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author 浅梦
 * @Date 2023 05 02 12:05
 * 拦截所有没有登录的请求
 **/
@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获取本次请求的额ulI
        String requestURI = request.getRequestURI();
        log.info("拦截到url{}",requestURI);
        //定义可以放行的路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/login",
                "/user/sendMsg"
        };
        //判断本次请求是否能通过
        boolean check = check(urls, requestURI);
        //如果能通过，放行
        if (check) {
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        //如果不能通过，查看是否登录
        //如果已经登录，放行
        if (request.getSession().getAttribute("employee") != null){
            log.info("已登录，用户id为：{}",request.getSession().getAttribute("employee"));
            filterChain.doFilter(request, response);
            return;
        }

        //判断移动端是否登录
        if (request.getSession().getAttribute("user") != null){
            log.info("已登录，用户id为：{}",request.getSession().getAttribute("user"));
            filterChain.doFilter(request, response);
            return;
        }
        //没有登录，跳转到登录页面
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    private boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                //表示可以放行
                return true;
            }
        }
        return false;
    }
}
