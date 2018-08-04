package com.wl.spacecraft.utils;


import org.apache.http.util.TextUtils;

import java.util.ArrayList;

public class Test{
    public static void main(String[] args) {
        boolean qwe = TextUtils.isEmpty(" ");
        System.err.println(qwe);

        String s = String.valueOf(" "+"qwe");
        System.err.println(s);
        ArrayList<String> strings = new ArrayList<>();
        strings.add("qwe");
        System.err.println(strings.contains("qwe"));;
    }

}