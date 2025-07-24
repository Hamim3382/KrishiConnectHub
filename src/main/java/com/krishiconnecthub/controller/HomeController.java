package com.krishiconnecthub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller to handle requests for the home page.
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String showHomePage() {
        return "home"; // This resolves to src/main/resources/templates/home.html
    }
}