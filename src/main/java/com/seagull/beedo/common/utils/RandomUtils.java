package com.seagull.beedo.common.utils;

import java.util.Random;
import java.util.UUID;

/**
 * Created by hgs on 2017/7/21.
 */
public class RandomUtils {
    /**
     * 得到一个UUID
     * @return
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }

    public static int getRandomInt(int bound){
        Random random = new Random();
        return  random.nextInt(bound);
    }
}
