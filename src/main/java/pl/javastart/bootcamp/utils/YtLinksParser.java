package pl.javastart.bootcamp.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class YtLinksParser {

    public static List<String> parseVideoLinks(String[] videoLinks) {
        Pattern regularUrl = Pattern.compile("youtube\\.com");
        Pattern sharedUrl = Pattern.compile("youtu\\.be");
        List<String> newVideoLinks = new ArrayList<>();
        for (String videoLink : videoLinks) {
            String newVideoLink;
            if (regularUrl.matcher(videoLink).find()) {
                newVideoLink = videoLink.split("=")[1];
            } else if (sharedUrl.matcher(videoLink).find()) {
                newVideoLink = videoLink.substring(videoLink.lastIndexOf("/") + 1);
            } else {
                newVideoLink = videoLink;
            }
            newVideoLinks.add(newVideoLink);
        }
        return newVideoLinks;
    }
}
