package com.lhjl;

import org.apache.commons.codec.binary.Base64;

import java.util.Arrays;

public class Base64Test {
    public static void main(String[] args) {

        String a="a";
        byte[] bytes = a.getBytes();

        System.err.println(Arrays.toString(bytes));

        byte[] bytes1 = Base64.encodeBase64(bytes);
        String s = Base64.encodeBase64String(bytes);
//        String s2 = java.util.Base64.getEncoder().encodeToString(bytes);
        String s1 = Base64.encodeBase64URLSafeString(bytes);
        byte[] bytes2 = Base64.encodeBase64Chunked(bytes);

        System.err.println(Arrays.toString(bytes1));
        System.err.println(s);
//        System.err.println(s2);
        System.err.println(s1);
        System.err.println(Arrays.toString(bytes2));

    }
}
