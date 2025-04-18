package org.example.bootrest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping
    public String index() {
        return "index"; // 응답을 할 주소, 템플릿...
    }
}
