package com.instaURLSgetter.app;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {

    public static void main (String[] args) throws IOException {



        //Testing purposes
        args = new String[]{"https://www.instagram.com/ale_hh17/"};

        //regex to later find in html code
        String pattern = "(?<=shortcode\":\").*?(?=\")";
        //Preparations for final URL
        String prefix = "https://www.instagram.com/p/";
        String suffix = "/media/?size=l";

        //Multiple URLS support
        for (String arg : args) {
            //Reading from the public URL
            String content = null;
            try {
                URLConnection connection = new URL(arg).openConnection();
                Scanner scanner = new Scanner(connection.getInputStream());
                scanner.useDelimiter("\\Z");
                content = scanner.next();
                scanner.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            //Creating a list of all image codes
            List<String> allMatches = new ArrayList<>();

            //Populating list with regex matches
            if (content == null) throw new AssertionError("No content in the website!");
            Matcher m = Pattern.compile(pattern)
                    .matcher(content);
            while (m.find()) {
                //Adding to the list with the proper URL
                allMatches.add(prefix + m.group() + suffix);
            }
            //Print list result
            for (String x : allMatches) {
                saveImage(x, Utils.randomAlphabetic(6));
                System.out.println(x);
            }
        }
    }

    public static void saveImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationFile+".jpg");

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
    }

}
