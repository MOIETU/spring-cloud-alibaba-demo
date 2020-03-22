package com.shaylee.apioss.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Title: demo控制器
 * Project: shaylee-cloud
 *
 * @author Adrian
 * @date 2020-03-20
 */
@RestController
@RequestMapping("/oss")
public class OssController {

    @Value("${server.port}")
    private Integer port;

    @RequestMapping("/whoami")
    public Object whoami() {
        return "shaylee-api-oss:" + port;
    }
}
