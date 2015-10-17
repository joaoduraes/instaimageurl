package com.joaoduraes.instaimageurl.service;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Service responsible for communicating with Instagram
 */
@Component
public class InstagramService {

    private static final String META = "meta";
    private static final String PROPERTY = "property";
    private static final String OG_IMAGE = "og:image";
    private static final String CONTENT = "content";
    private static final String DEFAULT_MESSAGE = "The image URL cannot be retrieved.";
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * This retrieves the direct image url from the original share URL
     *
     * @param originalUrl the original share URL from Instagram
     * @return the direct url to the image
     */
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

        } catch (HttpStatusException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
                log.error("Image not found. URL is probably malformed or the image is private. " + e);
            } else {
                log.error("Error retrieving image url. " + e);
            }
        } catch (Exception e) {
            log.error("Error retrieving image url. " + e);
        }
        return toReturn;
    }

}
