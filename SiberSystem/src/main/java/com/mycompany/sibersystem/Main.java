/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sibersystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author mrvladislav
 */
public class Main {
    public static void main(String[] args) {
        StringBuilder htmlTable;
        List<int[]> list;
        try {                       
            list = readFile(args[0]);            
            htmlTable = createTableHtml(list);
            writeFile("test.html", htmlTable.toString());            
        } catch (IOException e) {
            System.out.println(e.toString()); 
        }
    }
    
    public static String createOpenRowCell() {
        String row = String.format("<tr>\n");
        return row;
    }
    
    public static String createExitRowCell() {
        String row = String.format("</tr>");
        return row;
    }
    
    public static String createCell(String color, String border, String addBorder) {
        String cell = String.format("<td style=\"background:%s; "
                + "border-%s:1px solid white; border-%s:1px solid white;\"> </td>\n", color, border, addBorder);
        return cell;
    }
    
    public static void getSizeTable(List<int[]> coordCeil, int[] sizeTable) {
        for (int[] coord : coordCeil) {
            if (sizeTable[0] < coord[2]) {
                sizeTable[0] = coord[2];
            }
            if(sizeTable[1] < coord[3]) {
                sizeTable[1] = coord[3];
            }
        }
    }
    
    public static StringBuilder createTableHtml(List<int[]> coordCeil) {
        StringBuilder htmlTable = new StringBuilder();
        StringBuilder htmlCeils = new StringBuilder();
        int[] sizeTable = {0,0};
        getSizeTable(coordCeil, sizeTable);
        
        htmlTable.append(String.format("<table width=\"%dpx\" height=\"%dpx\" "
                + "cellspacing=\"0\" cellpadding=\"1\">",
                    sizeTable[0], sizeTable[1]));
        
        int[][] tableCeils = new int[sizeTable[1]][sizeTable[0]];
        for (int[] coord : coordCeil) {
            for(int y = coord[1]; y<coord[3]; y++) {
                for(int x = coord[0]; x<coord[2]; x++) {
                    if (x==coord[0] && y==coord[1]) {
                        tableCeils[y][x] = 4; //top and left
                    } else if(x==coord[0]) {
                        tableCeils[y][x] = 3; //left
                    } else if (y==coord[1]) {
                        tableCeils[y][x] = 2; //top
                    } else {
                        tableCeils[y][x] = 1;
                    }
                }
            }
        }
        
        int heightTable = -1;
        while(++heightTable < sizeTable[1]) {
            htmlCeils.append(createOpenRowCell());
            int widthTable = -1;
            while(++widthTable < sizeTable[0]) {
                String color;
                String border;
                String addBorder = "none";
                switch (tableCeils[heightTable][widthTable]) {   
                    case 4:
                        color = "black";
                        border = "left";
                        addBorder = "top";
                        break;
                    case 3:
                        color = "black";
                        border = "left";
                        break;
                    case 2:
                        color = "black";
                        border = "top";
                        break;
                    case 1:
                        color = "black";
                        border = "none";
                        break;
                    default:
                        color = "white";
                        border = "none";
                        break;
                }            
                htmlCeils.append(createCell(color, border, addBorder));                
            }
            htmlCeils.append(createExitRowCell());
        }
        htmlTable.append(htmlCeils.toString());
        htmlTable.append("</table>"); 
        return htmlTable;
    }
    
    public static void writeFile(String fileName, String text) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(text);
            writer.flush();            
        }
    }
    
    public static List readFile(String fileName) throws IOException {
        try (FileReader reader = new FileReader(fileName)) {
            BufferedReader bufferedReader = new BufferedReader(reader);            
            
            String c;
            List<int[]> list = new LinkedList<int[]>();
            while( (c = bufferedReader.readLine()) !=null) {                
                String[] coordString = c.split(" ");
                int[] coordInt = new int[coordString.length];
                for (int i=0; i < coordString.length; i++) {
                    coordInt[i] = Integer.parseInt(coordString[i]);
                }
                list.add(coordInt);
            }
            return list;
        }        
    }
}
