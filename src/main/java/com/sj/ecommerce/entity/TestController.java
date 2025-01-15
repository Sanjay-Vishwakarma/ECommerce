package com.sj.ecommerce.entity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@RequestMapping("/system")
public class TestController {

    @GetMapping("/systemCheck")
    public String systemCheck() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("System is working = " + now);
        return now.toString();
    }
}
