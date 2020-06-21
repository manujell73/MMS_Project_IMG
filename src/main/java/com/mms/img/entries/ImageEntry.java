package com.mms.img.entries;

import org.springframework.web.multipart.MultipartFile;

public class ImageEntry {
    private final MultipartFile image;
    private final int x;
    private final int y;
    private final int angle;

    public ImageEntry(MultipartFile image, int x, int y, int angle) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public MultipartFile getImage() {
        return image;
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
