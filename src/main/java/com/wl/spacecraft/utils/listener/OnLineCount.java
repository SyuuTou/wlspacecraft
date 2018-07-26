package com.wl.spacecraft.utils.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


@WebListener
public class OnLineCount implements HttpSessionListener {
    private int count;
    @Override
    public synchronized  void sessionCreated(HttpSessionEvent arg0) {
        System.out.println("【HttpSessionListener监听器】count++  增加");
        count++;
        arg0.getSession().getServletContext().setAttribute("count", count);

    }

    @Override
    public synchronized void sessionDestroyed(HttpSessionEvent arg0) {
        System.out.println("【HttpSessionListener监听器】count--  减少");
        count--;
        arg0.getSession().getServletContext().setAttribute("count", count);

    }
}
