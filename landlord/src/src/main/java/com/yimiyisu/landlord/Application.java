package com.yimiyisu.landlord;

import com.zen.ZenApp;

public class Application extends ZenApp {

    public static void main(String[] args) {
        int listenPort = 7037;
        String appName = "landlord";
        ZenApp app = new Application();
        app.multiTenant();
        app.start(args, appName, listenPort);
    }
}
