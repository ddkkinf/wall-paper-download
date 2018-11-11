package dev.argent;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Saki on 2017-07-20.
 */
public class Falcom {
    public static void main(String[] args) throws URISyntaxException, IOException {
        int count = 53;
        String folderName = "ED8-4";
        String fileName = "sen4_%02dh.jpg";
        String template = "https://www.falcom.co.jp/sen4/assets/special/wallpaper/" + fileName;
        new File("C:\\Users\\Saki\\Documents\\Wall Paper\\" + folderName).mkdir();
        for (int i = 1; i <= count; i++) {
            URL url = new URL(String.format(template, i));
            Path target = Paths.get(String.format("C:\\Users\\Saki\\Documents\\Wall Paper\\" + folderName + File.separator + fileName, i));
            Files.copy(url.openConnection().getInputStream(), target);
            System.out.println(url + " downloaded to " + target);
        }
    }
}
