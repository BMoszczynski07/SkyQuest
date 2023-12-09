package com.example.lotycrud.Controllers.Views;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping("/views/login") String loginPage() {
        return "views/login";
    }

    @GetMapping("/views") String mainPage() {
        return "views/index";
    }
}
