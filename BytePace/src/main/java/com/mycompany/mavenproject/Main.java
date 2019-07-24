/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import org.jsoup.select.Elements;

/**
 *
 * @author mrvladislav
 */
public class Main {
    public static void main(String[] args) {
        Document doc;
        List<Map> attrList = new ArrayList<Map>();
        try {
            doc = Jsoup.connect(args[0]).get();
            Elements elementsImage = doc.getElementsByTag("img");
            
            System.out.println(elementsImage.size());
            for (Element element : elementsImage) {
                attrList.add(getAttrImage(element));
            }
            
            String fileName = "listImage.txt";
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            try (FileWriter writer = new FileWriter(fileName)) {
                for (Map element : attrList) {
                    String url = element.get("url").toString();
                    String resolution = element.get("resolution").toString();
                    String size = element.get("size").toString();
                    writer.write(String.format("%s %s %s \n", url, resolution, size));
                    writer.flush();
                }
            } 
        } catch (IOException e) {
            System.out.println(e.toString()); 
        }
        
    }
    
    public static Map getAttrImage(Element image) throws IOException {
        String urlImage;
        if (image.absUrl("src").equals("")){
            urlImage = image.absUrl("data-src");
        } else { 
            urlImage = image.absUrl("src");
        }
        Pattern pattern = Pattern.compile("http:");
        Matcher matcher = pattern.matcher(urlImage);
        urlImage = matcher.replaceFirst("https:");
        
        String[] mas = urlImage.split("\\.");
        String typeImage = mas[mas.length-1];
        if (typeImage.length() > 3) {
            typeImage = "png";
        }
        
        BufferedImage img = ImageIO.read(new URL(urlImage));

        String fileName = "image."+typeImage;
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        ImageIO.write(img, typeImage, file);
        
        Map<String,String> attrs = new HashMap<String,String>();
        attrs.put("url", urlImage);
        attrs.put("resolution", String.format("%dX%d", img.getHeight(), img.getWidth()));
        attrs.put("size", String.format("%d KB", file.length()/1024));
        file.delete();
        return attrs;
    }
}
