package com.pucpr.realtimefirebase.model;

public class Volume {
    private int volume;
    private String status;

    public Volume (){}

    public Volume(int volume, String status) {
        this.volume = volume;
        this.status = status;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
