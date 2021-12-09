package com.vladimish.consulter.gateway.rabbitmq.holder;

import com.vladimish.consulter.gateway.rabbitmq.models.RegisterReply;

import java.util.ArrayList;

public class RegisterHolder {
    static RegisterHolder INSTANCE;
    public ArrayList<RegisterReply> list;

    public RegisterHolder() {
        this.list = new ArrayList<RegisterReply>();
    }

    public static RegisterHolder getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new RegisterHolder();
        }

        return INSTANCE;
    }
}
