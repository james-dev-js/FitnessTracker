package com.example.fitnesstracker;

public class Exercise {
    private int position;
    private boolean isChecked;
    public int getPosition(){
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
