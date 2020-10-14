package com.ediary.domain;

public class Message {

    private String title;
    private String content;

    public enum Status{
        SENT, READ
    }

    //ManyToOne:    uzytkownik jako nadawca
    //ManyToMany:   uzytkownik jako odbiorcy
}
