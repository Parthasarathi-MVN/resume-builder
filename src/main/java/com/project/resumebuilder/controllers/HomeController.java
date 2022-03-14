package com.project.resumebuilder.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home()
    {
        return "home";
    }

    @GetMapping("/edit")
    public String edit()
    {
        return "edit page";
    }

    @GetMapping("/view/{userName}")
    public String view(@PathVariable String userName, Model model)
    {
        model.addAttribute("userName", userName); // the String i.e., the first parameter will be used as a variable in the frontend and the second attribute is you get as a parameter to the function
        return "resume-templates/3/index";
    }
}
