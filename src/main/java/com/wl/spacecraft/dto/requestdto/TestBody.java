package com.wl.spacecraft.dto.requestdto;

public class TestBody {
    public String name;
    public Integer age;

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "requestBody{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
