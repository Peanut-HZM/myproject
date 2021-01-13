package com.peanut.myproject.model;

import java.io.Serializable;

/**
 * @Author HuaZhongmin
 * @Date 2021/1/1
 * @Time 17:14
 * @Week 周五
 **/
public class UserInfo implements Serializable {

    private final long serialize = 1L;

    private String id;
    private String name;
    private double height;
    private double weight;
    private String address;
    private long phone;
    private String sex;
    private boolean isMarried;
    private boolean isGraduated;
    private String school;
    private String company;
    private WorkExperience workExperience;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public boolean getIsMarried() {
        return isMarried;
    }

    public void setIsMarried(boolean isMarried) {
        this.isMarried = isMarried;
    }

    public boolean getIsGraduated() {
        return isGraduated;
    }

    public void setIsGraduated(boolean isGraduated) {
        this.isGraduated = isGraduated;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public WorkExperience getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(WorkExperience workExperience) {
        this.workExperience = workExperience;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", sex='" + sex + '\'' +
                ", isMarried='" + isMarried + '\'' +
                ", isGraduated='" + isGraduated + '\'' +
                ", school='" + school + '\'' +
                ", company='" + company + '\'' +
                ", workExperience='" + workExperience + '\'' +
                '}';
    }
}
