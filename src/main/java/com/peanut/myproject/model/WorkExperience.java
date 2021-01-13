package com.peanut.myproject.model;

import java.io.Serializable;

/**
 * @Author HuaZhongmin
 * @Date 2021/1/1
 * @Time 17:16
 * @Week 周五
 **/
public class WorkExperience implements Serializable {

    private final static long SERIALIZE = 1L;

    private String id;
    private int workYear;
    private double salaryYear;
    private String place;
    private int level;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getWorkYear() {
        return workYear;
    }

    public void setWorkYear(int workYear) {
        this.workYear = workYear;
    }

    public double getSalaryYear() {
        return salaryYear;
    }

    public void setSalaryYear(double salaryYear) {
        this.salaryYear = salaryYear;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public WorkExperience() {

    }

    public WorkExperience(String id, int workYear, double salaryYear, String place, int  level) {
        this.id = id;
        this.workYear = workYear;
        this.salaryYear = salaryYear;
        this.place = place;
        this.level = level;
    }

    @Override
    public String toString() {
        return "WorkExperience{" +
                "id='" + id + '\'' +
                ", workYear='" + workYear + '\'' +
                ", salaryYear='" + salaryYear + '\'' +
                ", place='" + place + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}
