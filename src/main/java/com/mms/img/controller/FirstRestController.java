package com.mms.img.controller;


import com.mms.img.entries.TextEntry;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
public class FirstRestController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(){
        return "index";
    }

    @ResponseBody
    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] upload(@RequestParam MultipartFile file, @RequestParam String text, Integer x, Integer y, Integer angle) throws Exception {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(file.getBytes()));

        if (x == null || y == null || angle == null) {
            throw new Exception("Homo");
        }

        Graphics2D g = image.createGraphics();
        g.setFont(g.getFont().deriveFont(40f));
        g.setColor(new Color(0, 0, 0));

        FontMetrics fm = g.getFontMetrics();

        int width = fm.stringWidth(text);
        int height = fm.getHeight();

        int xTotal = width / 2 + x;
        int yTotal = height / 2 + y;
        g.setTransform(AffineTransform.getRotateInstance(Math.toRadians(angle), xTotal, yTotal));
        g.drawString(text, x, y);

/*        g.drawString("made with imgflip", 700, 300);
        g.drawString("Made with", 700, 900);
        g.drawString("Image Macro Generator", 700, 950);*/
        g.dispose();

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ImageIO.write(image, "png", bao);
        return bao.toByteArray();
    }
/*
    @ResponseBody
    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] upload(@RequestParam MultipartFile file, @RequestParam TextEntry[] textEntries, @RequestParam ImageEntry[] imageEntries) throws IOException {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(file.getBytes()));

        Graphics g = image.getGraphics();
        g.setFont(g.getFont().deriveFont(40f));
        g.setColor(new Color(0, 0, 0));
        for(TextEntry entry : textEntries) {

            int width = fm.stringWidth(text);
            int height = fm.getHeight();

            int xTotal = width / 2 + x;
            int yTotal = height / 2 + y;
            g.setTransform(AffineTransform.getRotateInstance(Math.toRadians(angle), xTotal, yTotal));
            g.drawString(text, x, y);

            g.drawString(entry.getText(), entry.getX(), entry.getY());
        }
/*        g.drawString("made with imgflip", 700, 300);
        g.drawString("Made with", 700, 900);
        g.drawString("Image Macro Generator", 700, 950);
        g.dispose();

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ImageIO.write(image, "png", bao);
        return bao.toByteArray();
    }*/
}
