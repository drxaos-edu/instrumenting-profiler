package com.example.demo.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

    public String searchImage(String q) {
        try {
            String url = "https://www.google.ru/search?tbm=isch&q=" + q.replace(" ", "+");
            Document doc = Jsoup.connect(url).get();
            Elements images = doc.select("#main #ires #rg img");
            String image = images.attr("src");
            return image;
        } catch (Exception e) {
            return null;
        }
    }
}
