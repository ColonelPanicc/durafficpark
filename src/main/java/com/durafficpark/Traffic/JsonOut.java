package com.durafficpark.traffic;

class JsonOut {

    /*

    I know it looks like the fields are never used, but they are.
    This is a wrapper class to be able to serialise two different data objects into one json.
    Please do not touch. :)

     */

    private double[][] values;
    private float time;

    JsonOut(double[][] values, float time) {
        this.values = values;
        this.time = time;
    }
}
