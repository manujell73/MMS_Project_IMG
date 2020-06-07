package com.mms.img.controller;


import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
public class FirstRestController {

    @GetMapping("/")
    public String index(Model model){
        return "index";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] upload(Model model, @RequestParam MultipartFile file) throws IOException {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(file.getBytes()));

        Graphics g = image.getGraphics();
        g.setFont(g.getFont().deriveFont(40f));
        g.setColor(new Color(0, 0, 0));
        g.drawString("made with imgflip", 700, 300);
        g.drawString("Made with", 700, 900);
        g.drawString("Image Macro Generator", 700, 950);
        g.dispose();

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ImageIO.write(image, "png", bao);
        return bao.toByteArray();
    }
}
