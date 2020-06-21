package com.mms.img.entries;

public class TextEntry {
    private final String text;
    private final int x;
    private final int y;
    private final int angle;

    public TextEntry(String text, int x, int y, int angle) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public String getText() {
        return text;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getAngle() {
        return angle;
    }
}
