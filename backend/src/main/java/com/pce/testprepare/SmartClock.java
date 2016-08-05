package com.pce.testprepare;

public class SmartClock extends Clock{

    public long getTimeInSeconds() {
        return time / 1000;
    }
}