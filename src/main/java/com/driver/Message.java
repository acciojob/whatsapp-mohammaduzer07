package com.driver;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter

public class Message {
    private int id;
    private String content;
    private Date timestamp;

    public Message(int id, String content){
        this.id = id;
        this.content = content;
    }

}
