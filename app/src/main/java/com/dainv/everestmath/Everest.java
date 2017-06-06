package com.dainv.everestmath;

import java.util.Random;

/**
 * Created by dainv on 1/6/2016.
 */
public class Everest {
    public final static String CUSTOM_FONT_NORMAL = "fonts/Comfortaa_Regular.ttf";
    public final static String CUSTOM_FONT_BOLD = "fonts/Comfortaa_Bold.ttf";

    //public final static int BASE_CAMP = 5360;
    public final static int BASE_CAMP = 8848;
    public final static int SUMMIT = 8848;

    private final static int TOP_NUMBER = 10;
    public final static int FLAG_TIMEOUT = 1;
    public final static int FLAG_GAMEOVER = 2;

    private Random random = null;

    public static int score = 0;
    public static int curHeight = 1;
    public int nextHeight = 0;
    public int ans1 = 0;
    public int ans2 = 0;

    public Everest() {
        random = new Random();
        score = 0;
    }

    /**
     * generate next values for challenge
     */
    public void generate() {
        if (atSummit())
            return;

        /* in case current height is quire far from BASE CAMP height */
        if ((BASE_CAMP - curHeight) >= TOP_NUMBER) {
            do {
                ans1 = random.nextInt(TOP_NUMBER);
            } while (ans1 == 0);

            do {
                ans2 = random.nextInt(TOP_NUMBER);
            } while (ans2 == ans1 || ans2 == 0);

            /* randomize answer */
            if (random.nextBoolean())
                nextHeight = ans1 + curHeight;
            else
                nextHeight = ans2 + curHeight;

        /* in case current height is close to BASE CAMP height */
        } else {
            nextHeight = BASE_CAMP;
            /* randomize answer */
            if (random.nextBoolean()) {
                ans1 = BASE_CAMP - curHeight;
                do {
                    ans2 = random.nextInt(TOP_NUMBER);
                } while (ans2 == ans1 || ans2 == 0);
            } else {
                ans2 = BASE_CAMP - curHeight;
                do {
                    ans1 = random.nextInt(TOP_NUMBER);
                } while (ans1 == ans2 || ans1 == 0);
            }
        }
    }

    public boolean atSummit() {
        boolean ret = false;
        if (curHeight == BASE_CAMP) {
            ret = true;
        }
        return ret;
    }

    public void reset() {
        curHeight = 1;
        nextHeight = 0;
        score = 0;
        ans1 = 0;
        ans2 = 0;
    }
}
