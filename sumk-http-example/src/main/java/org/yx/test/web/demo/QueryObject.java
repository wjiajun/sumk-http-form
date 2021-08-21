package org.yx.test.web.demo;

import org.yx.util.SumkDate;

import java.util.Map;

/**
 * @author : wjiajun
 * @description:
 */
public class QueryObject {

    private String name;

    private int age;

    private SumkDate date;

    private Map<String, Object> map;

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

    public SumkDate getDate() {
        return date;
    }

    public void setDate(SumkDate date) {
        this.date = date;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}
