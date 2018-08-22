package com.wl.spacecraft.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class ReturnModel extends GenericReturnModel<Object> {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        Random random = new Random(100);
        Random random2 = new Random(100);
        System.err.println(random.nextInt(10));
        System.err.println(random.nextInt(10));
        System.err.println(random2.nextInt(10));
        System.err.println(random2.nextInt(10));


//        SecureRandom qe = SecureRandom.getInstance("qe");
//        System.err.println(qe);
        double random1 = Math.random();
        System.err.println("qwertyui".substring(0,3));


    }

}
