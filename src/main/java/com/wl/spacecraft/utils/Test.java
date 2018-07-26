package com.wl.spacecraft.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Calendar;
import java.util.Date;

public class Test {
    public static void main(String[] args) {

        System.out.println(new Date().getTime());
        System.err.println(RandomStringUtils.random(4,"0123456789"));

    }
}
