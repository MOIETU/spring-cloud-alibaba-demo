package com.shaylee.api.controller;

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
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping("/whoami")
    public Object whoami() {
        return "shaylee-api";
    }
}
