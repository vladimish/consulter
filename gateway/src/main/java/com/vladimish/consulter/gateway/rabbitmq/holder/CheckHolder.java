package com.vladimish.consulter.gateway.rabbitmq.holder;

import com.vladimish.consulter.gateway.rabbitmq.models.CheckReply;
import com.vladimish.consulter.gateway.rabbitmq.models.GetTimetablesReply;

import java.util.ArrayList;

public class CheckHolder {
    static CheckHolder INSTANCE;
    public ArrayList<CheckReply> list;

    public CheckHolder() {
        this.list = new ArrayList<CheckReply>();
    }

    public static CheckHolder getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new CheckHolder();
        }

        return INSTANCE;
    }
}
