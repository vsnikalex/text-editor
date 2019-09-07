package com.epam.texteditor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        String pageTitle = "EPAM Text Editor";
        String appName = "Text Editor Home Page";

        model.addAttribute("appName", appName);
        model.addAttribute("pageTitle", pageTitle);

        return "index";
    }

}
