package com.ediary.domain;

import org.apache.tomcat.jni.Address;

import java.util.Date;

public class Event {

    private String description;
    private Date createDate;
    private Date date;

    public enum Type{
        HOMEWORK, EXAM, TEST, OTHER
    }
}
