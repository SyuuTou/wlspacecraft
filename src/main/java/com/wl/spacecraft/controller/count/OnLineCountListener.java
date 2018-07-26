package com.wl.spacecraft.controller.count;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;

@RestController
public class OnLineCountListener {

        @GetMapping("test/count")
        public String count(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
            HttpSession session = httpServletRequest.getSession();
            Object count=session.getServletContext().getAttribute("count");
            return "count : "+count;
        }

        @GetMapping("online/count")
        public String onLineount(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try{  //把sessionId记录在浏览器
            Cookie c = new Cookie("JSESSIONID", URLEncoder.encode(httpServletRequest.getSession().getId(), "utf-8"));
            c.setPath("/");
            //先设置cookie有效期为2天，不用担心，session不会保存2天
            c.setMaxAge( 48*60 * 60);
            httpServletResponse.addCookie(c);
        }catch (Exception e){
            e.printStackTrace();
        }

        HttpSession session = httpServletRequest.getSession();
        Object count=session.getServletContext().getAttribute("count");
        return "count : "+count;
    }

}
