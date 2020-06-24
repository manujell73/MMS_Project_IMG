package com.mms.img.entries;

public class TextEntry extends Entry {
    private final String text;
    private final float fontSize;

    public TextEntry(String text, int x, int y, int angle, float fontSize) {
        super(x, y, angle);
        this.text = text;
        this.fontSize = fontSize;
    }

    public String getText() {
        return text;
    }

    public float getFontSize() {
        return fontSize;
    }
}
