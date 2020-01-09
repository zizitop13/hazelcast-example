package ru.zizitop.example.map;

import java.util.concurrent.TimeUnit;

public class MainMapExample {

    public static void main(String[] args) throws InterruptedException {

        BigWideWorld theWorld = new BigWideWorld();

        MapApplication application = new MapApplication();

        while (true) {

            String username = theWorld.nextUser();

            if (application.isLoggedOn(username)) {
                application.logout(username);
            } else {
                application.logon(username);
            }

            application.displayUsers();
            TimeUnit.SECONDS.sleep(2);
        }

    }
}
