package pl.javastart.bootcamp.utils;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class YtLinksParser {

    public List<String> parseVideoLinks(String[] videoLinks) {
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
