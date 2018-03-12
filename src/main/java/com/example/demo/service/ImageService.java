package com.example.demo.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService {

    public String searchImage(String q) {
        try {
            String userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36";
            String url = "https://www.google.ru/search?tbm=isch&q=" + q.replace(" ", "+");
            Document doc = Jsoup.connect(url).userAgent(userAgent).referrer("https://www.google.com/").get();
            Elements elements = doc.select("div.rg_meta");

            List<String> resultUrls = new ArrayList<>();
            JSONObject jsonObject;
            for (Element element : elements) {
                if (element.childNodeSize() > 0) {
                    jsonObject = (JSONObject) new JSONParser().parse(element.childNode(0).toString());
                    resultUrls.add((String) jsonObject.get("ou"));
                }
            }

            String imgUrl = "";
            for (String resultUrl : resultUrls) {
                if (resultUrl.toLowerCase().endsWith(".jpg")) {
                    imgUrl = resultUrl;
                    break;
                }
            }
            byte[] bytes = download(imgUrl);

            String imageString = "data:image/jpeg;base64," + DatatypeConverter.printBase64Binary(bytes);

            return imageString;
        } catch (Exception e) {
            return null;
        }
    }

    private byte[] download(String url) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream is = new URL(url).openStream()) {
            byte[] byteChunk = new byte[4096];
            int n;

            while ((n = is.read(byteChunk)) > 0) {
                baos.write(byteChunk, 0, n);
            }

            return baos.toByteArray();
        } catch (IOException e) {
            throw e;
        }
    }
}
