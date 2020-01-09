package ru.zizitop.example.map;

import java.util.Random;

public class BigWideWorld {

    private static Random random = new Random(System.currentTimeMillis());

    private final Users users  = new Users();

    private final int totalNaumUsers = users.size();

    public String nextUser(){
        User user = users.get(random.nextInt(totalNaumUsers));
        String name = user.getUsername();

        return name;
    }


}
