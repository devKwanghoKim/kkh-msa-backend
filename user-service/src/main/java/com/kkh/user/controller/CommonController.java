package com.kkh.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class CommonController {

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome";
    }
}
