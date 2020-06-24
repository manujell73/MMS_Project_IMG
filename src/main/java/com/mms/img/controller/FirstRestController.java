package com.mms.img.controller;


import com.mms.img.entries.TextEntry;
import com.mms.img.exceptions.JSONParsingError;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @ResponseBody
    @RequestMapping(value = "/generate", method = RequestMethod.POST, produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<?> generate(@RequestParam MultipartFile file, @RequestParam("textData") String jsonTextData) throws IOException {
        try {
            TextEntry[] textEntries = parseTextEntries(jsonTextData);
            BufferedImage template = ImageIO.read(new ByteArrayInputStream(file.getBytes()));

            // get graphics to add texts, and set font to 'Impact' if found
            Graphics2D g = template.createGraphics();
            Font font = Arrays.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts())
                    .filter(f -> f.getFontName().equals("Impact"))
                    .findAny()
                    .orElse(g.getFont());

            // add the texts into the image with it's own font size - color is always black
            for(TextEntry entry : textEntries) {
                g.setFont(font.deriveFont(entry.getFontSize()));
                g.setColor(Color.BLACK);
                g.setTransform(AffineTransform.getRotateInstance(Math.toRadians(entry.getAngle()), entry.getX(), entry.getY()));
                g.drawString(entry.getText(), entry.getX(), entry.getY());
            }

            // convert graphics to an png image, return its byte values
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ImageIO.write(template, "png", bao);
            return ResponseEntity.status(HttpStatus.OK).body(bao.toByteArray());
        } catch (JSONParsingError e) {
            // handle errors while converting JSON String to TextEntry[]
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(e.getMessage());
        }
    }

    private TextEntry[] parseTextEntries(String jsonTextData) throws JSONParsingError {
        try {
            JSONParser parser = new JSONParser();
            JSONArray jsonT = (JSONArray) parser.parse(jsonTextData);


            TextEntry[] textEntries = new TextEntry[jsonT.size()];
            for(int i=0; i<jsonT.size(); i++){
                JSONObject test = (JSONObject) jsonT.get(i);
                String text = test.getAsString("text");
                Number x = test.getAsNumber("x");
                Number y = test.getAsNumber("y");
                Number angle = test.getAsNumber("angle");
                Number fontSize = test.getAsNumber("fontSize");

                if(x == null || y == null){
                    throw new JSONParsingError("x and/or y values are missing.");
                }

                // create TextEntry object, with possible default values for angle and fontSize
                TextEntry textEntry =
                        new TextEntry(text, x.intValue(), y.intValue(),
                                angle != null ? angle.intValue() : 0,
                                fontSize != null ? fontSize.floatValue() : 40f);
                textEntries[i] = textEntry;
            }
            return textEntries;
        } catch (ParseException e) {
            throw new JSONParsingError("textData is not a valid JSON value");
        }
    }
}
