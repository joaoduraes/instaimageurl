package com.joaoduraes.instaimageurl.web;

import com.joaoduraes.instaimageurl.service.InstagramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InstagramController {

    @Autowired
    private InstagramService instagramService;

    @RequestMapping("/insta")
    public String getImageUrl(@RequestParam("originalUrl") String originalUrl) {
        return instagramService.getImageUrl(originalUrl);
    }


}
