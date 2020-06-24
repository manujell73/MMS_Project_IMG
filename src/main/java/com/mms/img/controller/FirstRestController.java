package com.mms.img.controller;


import com.mms.img.entries.TextEntry;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
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
import java.util.Arrays;

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

        g.dispose();

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ImageIO.write(image, "png", bao);
        return bao.toByteArray();
    }

    @ResponseBody
    @RequestMapping(value = "/upload2", method = RequestMethod.POST, produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] upload(@RequestParam MultipartFile file, @RequestParam String tE) throws IOException, ParseException {
        BufferedImage template = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
        JSONParser parser = new JSONParser();
        JSONArray jsonT = (JSONArray) parser.parse(tE);
        TextEntry[] textEntries = new TextEntry[jsonT.size()];
        for(int i=0; i<jsonT.size(); i++){
            JSONObject test = (JSONObject) jsonT.get(i);
            String text = test.getAsString("text");
            Number x = test.getAsNumber("x");
            Number y = test.getAsNumber("y");
            Number angle = test.getAsNumber("angle");
            Number fontSize = test.getAsNumber("fontSize");
            TextEntry textEntry = new TextEntry(text,
                    x != null ? x.intValue() : 0,
                    y != null ? y.intValue() : 0,
                    angle != null ? angle.intValue() : 0,
                    fontSize != null ? fontSize.floatValue() : 40f);
            textEntries[i] = textEntry;
        }

        Graphics2D g = template.createGraphics();
        Font font = Arrays.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts())
                .filter(f -> f.getFontName().equals("Impact"))
                .findAny()
                .orElse(g.getFont());


        for(TextEntry entry : textEntries) {
            g.setFont(font.deriveFont(entry.getFontSize()));
            FontMetrics fm = g.getFontMetrics();
            int width = fm.stringWidth(entry.getText());
            int height = fm.getHeight();

            int xTotal = width / 2 + entry.getX();
            int yTotal = height / 2 + entry.getY();

            g.setColor(new Color(0, 0, 0));
            g.setTransform(AffineTransform.getRotateInstance(Math.toRadians(entry.getAngle()), xTotal, yTotal));
            g.drawString(entry.getText(), entry.getX(), entry.getY());
        }
        /*for(ImageEntry entry : imageEntries) {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(entry.getImage().getBytes()));
            int width = image.getWidth();
            int height = image.getHeight();

            int xTotal = width / 2 + entry.getX();
            int yTotal = height / 2 + entry.getY();
            g.setTransform(AffineTransform.getRotateInstance(Math.toRadians(entry.getAngle()), xTotal, yTotal));
            g.drawImage(image, entry.getX(), entry.getY(), null);
        }*/

        g.dispose();

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ImageIO.write(template, "png", bao);
        return bao.toByteArray();
    }
}
