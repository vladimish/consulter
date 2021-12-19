package com.vladimish.consulter.gateway.rabbitmq.holder;

import com.vladimish.consulter.gateway.rabbitmq.models.GetTimetablesReply;
import com.vladimish.consulter.gateway.rabbitmq.models.LoginReply;

import java.util.ArrayList;

public class GetTimetablesHolder {
    static GetTimetablesHolder INSTANCE;
    public ArrayList<GetTimetablesReply> list;

    public GetTimetablesHolder() {
        this.list = new ArrayList<GetTimetablesReply>();
    }

    public static GetTimetablesHolder getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new GetTimetablesHolder();
        }

        return INSTANCE;
    }
}
