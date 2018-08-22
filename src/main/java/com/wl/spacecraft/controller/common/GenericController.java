package com.wl.spacecraft.controller.common;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 所有自定义Controller的顶级接口,封装常用的与session和response、request相关的操作
 * <p>
 * Created by syuutou on 2018/8/19 0019.
 */
public abstract class GenericController {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
    /**
     * 客户端返回JSON字符串
     *
     * @param response
     * @param object
     * @return
     */
    protected String renderString(HttpServletResponse response, Object object) {
        return renderString(response, new Gson().toJson(object), "application/json");
    }

    /**
     * 客户端返回字符串
     *
     * @param response 响应和
     * @param string json字符串
     * @return
     */
    protected String renderString(HttpServletResponse response, String string, String type) {
        try {
            response.reset();
            //设置传输类型
            response.setContentType(type);
            //设置相应字符编码
            response.setCharacterEncoding("utf-8");
            //解决跨域问题
            response.setHeader("Access-Control-Allow-Origin", "*");

            response.getWriter().print(string);
            return null;
        } catch (IOException e) {
            return null;
        }
    }
}
