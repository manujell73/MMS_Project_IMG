package com.mms.img.entries;

public class TextEntry extends Entry {
    private final String text;
    private final float fontSize;

    public TextEntry(String text, int x, int y, int angle, float fontSize) {
        // y coordinate start on the bottom of the text, we want it to start at the top
        super(x, y + (int) fontSize, angle);
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
