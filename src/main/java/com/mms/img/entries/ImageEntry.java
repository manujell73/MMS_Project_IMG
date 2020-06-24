package com.mms.img.entries;

import org.springframework.web.multipart.MultipartFile;

// for future use
public class ImageEntry extends Entry {
    private final MultipartFile image;

    public ImageEntry(MultipartFile image, int x, int y, int angle) {
        super(x, y, angle);
        this.image = image;
    }

    public MultipartFile getImage() {
        return image;
    }
}
