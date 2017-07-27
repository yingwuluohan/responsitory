package com.fang.service.rabbitMq;

import java.io.Serializable;

/**
 * Created by fn on 2017/7/26.
 */
public class MessageEntity implements Serializable {

    private int id ;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
