package com.lxm.chapter_2;

import java.io.Serializable;

/**
 * Created by lxm on 17/2/7.
 */

public class User implements Serializable{

    private static final long serialVersionUID = 1L;

    public int userId;
    public String userName;
    public boolean isMale;

    public User() {
    }

    public User(int userId, String userName , boolean isMale) {
        this.userId = userId;
        this.userName = userName;
        this.isMale = isMale;
    }
}
