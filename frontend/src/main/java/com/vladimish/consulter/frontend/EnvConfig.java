package com.vladimish.consulter.frontend;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnvConfig {
    public static String gatewayURL;

    public static void Load(){
        gatewayURL = System.getenv("GATEWAY_SERVICE_PORT");
        if (gatewayURL == null){
            log.error("Variable GATEWAY_SERVICE_PORT isn't set.");
        }
    }
}
