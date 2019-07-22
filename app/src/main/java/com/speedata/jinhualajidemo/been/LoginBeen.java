package com.speedata.jinhualajidemo.been;

public class LoginBeen {

    /**
     * gender : 1
     * name : test
     * count : 1
     */

    private int gender;
    private String name;
    private int count;

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ContentBeen{" +
                "gender=" + gender +
                ", name='" + name + '\'' +
                ", count=" + count +
                '}';
    }
}
