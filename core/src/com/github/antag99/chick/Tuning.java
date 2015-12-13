package com.github.antag99.chick;

import com.badlogic.gdx.utils.ObjectMap;
import com.github.antag99.retinazer.Component;

public final class Tuning implements Component {

    public static final ObjectMap<String, float[]> JUMP_SPEEDS = new ObjectMap<String, float[]>();

    static {
        JUMP_SPEEDS.put("lj", new float[] { 0f, 8f });
        JUMP_SPEEDS.put("rj", new float[] { 8f, 0f });
        JUMP_SPEEDS.put("ltj", new float[] { 0f, 12f });
        JUMP_SPEEDS.put("rtj", new float[] { 12, 0f });
    }

    public static final float NAUSEA_THRESHOLD = 1f;
    public static final float NAUSEA_RECOVERY_SPEED = 0.1f;
    public static final float NAUSEA_ROTATION_SPEED = 1f;

    public static final float WINE_AMOUNT = 1f;
    public static final float WINE_CONSUME_PER_SIP = 0.1f;

    public static final float CHICKEN_AMOUNT = 2f;
    public static final float CHICKEN_CONSUME_PER_BITE = 0.05f;

    public static final float PLAYER_WIDTH = 0.8f;
    public static final float PLAYER_HEIGHT = 0.8f;
    public static final float PLAYER_MOVE_SPEED = 5f;
    public static final float PLAYER_HEAR_RANGE = 30f;
    public static final float PLAYER_BITE_DELAY = 0.2f;
    public static final float PLAYER_FOOD_VICTORY = 5f;

    public static final float GRAVITY_X = 0f;
    public static final float GRAVITY_Y = -30f;
    public static final float MAX_GRAVITY_X = 0f;
    public static final float MAX_GRAVITY_Y = -10f;

    public static final float CHICKEN_MIN_DURATION = 2f;
    public static final float CHICKEN_MAX_DURATION = 3f;
    public static final float LID_MIN_DURATION = 3f;
    public static final float LID_MAX_DURATION = 5f;
}
