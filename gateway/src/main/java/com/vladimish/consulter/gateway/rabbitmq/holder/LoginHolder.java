package com.vladimish.consulter.gateway.rabbitmq.holder;

import com.vladimish.consulter.gateway.rabbitmq.models.LoginReply;
import java.util.ArrayList;

public class LoginHolder {
    static LoginHolder INSTANCE;
    public ArrayList<LoginReply> list;

    public LoginHolder() {
        this.list = new ArrayList<LoginReply>();
    }

    public static LoginHolder getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new LoginHolder();
        }

        return INSTANCE;
    }
}
