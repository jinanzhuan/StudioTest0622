package com.example.shuwei.quickindex;

import java.io.Serializable;

/**
 * Created by shuwei on 2016/6/25.
 */
public class Person implements Serializable,Comparable<Person> {
    private String name;
    private String pinYin;

    public Person(String name) {
        this.name = name;
        this.pinYin=PinYinUtils.getPinYin(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinYin() {
        return pinYin;
    }

    public void setPinYin(String pinYin) {
        this.pinYin = pinYin;
    }

    @Override
    public int compareTo(Person another) {
        return this.getPinYin().compareTo(another.getPinYin());
    }
}
