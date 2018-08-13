package com.lhjl;

import java.util.HashMap;

public class LambdaTest {
    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<>();
        map.put("q","w");
        map.put("a","s");
        map.put("z","d");
        map.forEach((K,V)->{
            System.err.println(K);
            System.err.println(V);
        });

    }
}
