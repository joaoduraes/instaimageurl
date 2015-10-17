package com.joaoduraes.instaimageurl.web;

import com.joaoduraes.instaimageurl.service.InstagramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Main controller for the service
 */
@RestController
public class InstagramController {

    @Autowired
    private InstagramService instagramService;

    /**
     * This endpoint returns the image URL from the original share URL from Instagram
     * @param originalUrl the original share URL from Instagram
     * @return the direct url to the image
     */
    @RequestMapping("/insta")
    public String getImageUrl(@RequestParam("originalUrl") String originalUrl) {
        return instagramService.getImageUrl(originalUrl);
    }


}
