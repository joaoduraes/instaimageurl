package com.joaoduraes.instaimageurl.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class InstagramService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String META = "meta";
    private static final String PROPERTY = "property";
    private static final String OG_IMAGE = "og:image";
    private static final String CONTENT = "content";
    private static final String DEFAULT_MESSAGE = "An error occurred. The URL might be malformed.";

    public String getImageUrl(String originalUrl) {
        String toReturn = DEFAULT_MESSAGE;
        try {
            log.debug("Requesting url " + originalUrl);

            Document doc = Jsoup.connect(originalUrl).get();
            Elements metaList = doc.select(META);

            for (Element meta : metaList) {
                if (meta.attributes() != null) {
                    if (OG_IMAGE.equals(meta.attributes().get(PROPERTY))) {
                        String imageUrl = meta.attributes().get(CONTENT);
                        log.debug("Found image " + imageUrl);

                        toReturn = imageUrl;
                    }
                }
            }

        } catch (IOException e) {
            log.error("Error searching for image url. " + e);
        }
        return toReturn;
    }

}
