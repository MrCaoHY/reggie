package com.example.reggie.filter;

import com.example.reggie.common.BaseContext;
import com.example.reggie.common.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: reggie
 * @description: 检查用户是否登录
 * @author: CaoHaiyang
 * @create: 2022-07-22 19:10
 **/
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //1 获取本次请求URI
        String requestURI = request.getRequestURI();
        //不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/backend/**",
                "/front/**",
                "/swagger-ui/**"
        };
        boolean check = check(urls, requestURI);
        //如果已经登录直接放行
        if (check) {
            filterChain.doFilter(request, response);
            return;
        }
        if (request.getSession().getAttribute("employee") != null) {
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request, response);
            return;
        }

        log.info("用户未登录");
        //如果未登录，通过输出流的方式向客户端响应页面
        response.getWriter().write(new ObjectMapper().writeValueAsString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配判断
     * @param requestURI
     * @return boolean
     * @author caohaiyang
     * @date 2022/7/22 19:24
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
