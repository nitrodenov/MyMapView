package com.mymapview.presentation.model;


import static com.mymapview.presentation.model.Constants.FLING_DELAY;
import static com.mymapview.presentation.model.Constants.FLING_DURATION;
import static com.mymapview.presentation.model.Constants.FLING_VELOCITY;

public class FlingData {

    private float v0n_1 = 0;
    private float a = 0;
    private float sn_1 = 0;
    private float initVelocity;

    public float countInitDist(float velocity) {
        initVelocity = velocity;
        float v;

        if (Math.abs(velocity) >= FLING_VELOCITY) {
            v = FLING_VELOCITY;
        } else {
            v = Math.abs(velocity);
        }

        v0n_1 = v / 1000;
        a = getA(v0n_1);
        sn_1 = getS(v0n_1, a, FLING_DELAY);

        return initVelocity < 0 ? sn_1 : -sn_1;
    }

    public float countDist() {
        v0n_1 = getV0(v0n_1, a, sn_1);
        float s = getS(v0n_1, a, FLING_DELAY);

        return initVelocity < 0 ? s : -s;
    }

    private float getS(float velocity, float a, float t) {
        return velocity * t + a * t * t / 2;
    }

    private float getV0(float velocity, float a, float x) {
        return (float) Math.sqrt(velocity * velocity + 2 * a * x);
    }

    private float getA(float velocity) {
        return -velocity / FLING_DURATION;
    }
}
