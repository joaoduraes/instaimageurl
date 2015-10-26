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
    private static final String OG_TYPE = "og:type";
    private static final String OG_IMAGE = "og:image";
    private static final String OG_VIDEO = "og:video";
    private static final String CONTENT = "content";
    private static final String DEFAULT_MESSAGE = "The image URL cannot be retrieved.";
    private static final String PHOTO = "photo";
    private static final String VIDEO = "video";

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
                    if (OG_TYPE.equals(meta.attributes().get(PROPERTY))) {
                        if (meta.attributes().get(CONTENT).contains(PHOTO)) {
                            return retrieveImageUrl(metaList);
                        } else if (meta.attributes().get(CONTENT).contains(VIDEO)) {
                            return retrieveVideoUrl(metaList);
                        }
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

    private String retrieveVideoUrl(Elements metaList) {
        String videoUrl = null;
        for (Element meta : metaList) {
            if (meta.attributes() != null) {
                if (OG_VIDEO.equals(meta.attributes().get(PROPERTY))) {
                    videoUrl = meta.attributes().get(CONTENT);
                    log.debug("Found video " + videoUrl);
                }
            }
        }

        return videoUrl;

    }

    private String retrieveImageUrl(Elements metaList) {
        String imageUrl = null;
        for (Element meta : metaList) {
            if (meta.attributes() != null) {
                if (OG_IMAGE.equals(meta.attributes().get(PROPERTY))) {
                    imageUrl = meta.attributes().get(CONTENT);
                    log.debug("Found image " + imageUrl);
                }
            }
        }

        return imageUrl;
    }

}
