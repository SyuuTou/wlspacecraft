package com.wl.spacecraft.utils;


import java.rmi.ServerError;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

public class TestStream {
    public static void main(String[] args) {
        List<Person> list = new ArrayList<>();
        list.add(new Person("jack", 20));
        list.add(new Person("mike", 25));
        list.add(new Person("tom", 30));
        ;

//        list = list.stream()
//                .filter(person -> person.getAge() == 20)
//                .collect(toList());

//        list = list.stream()
//                .sorted((p1, p2) -> p1.getAge() - p2.getAge())
//                .collect(toList());

//        list = list.stream()
//                .sorted(Comparator.comparingInt(Person::getAge))
//                .collect(toList());

//        list = list.stream()
//                .limit(2)
//                .collect(toList());
        List<String> newlist =
                list.stream().map(Person::getName).collect(toList());


        System.err.println(newlist);
        System.err.println(null != new Integer(1));


        Instant now     = Instant.now();
        Instant later   = now.plusSeconds(3);
        Instant earlier = now.minusSeconds(3);
        System.err.println(now.toString());
        System.err.println(later);
        System.err.println(earlier);

    }

}