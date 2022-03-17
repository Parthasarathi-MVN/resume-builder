package com.project.resumebuilder.controllers;

import com.project.resumebuilder.models.Job;
import com.project.resumebuilder.models.User;
import com.project.resumebuilder.models.UserProfile;
import com.project.resumebuilder.repositories.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;


@Controller
public class HomeController {

    @Autowired
    UserProfileRepository userProfileRepository;




    @GetMapping("/")
    public String home()
    {
        return "home";
    }

    @GetMapping("/edit")
    public String edit(Principal principal, Model model)
    {
        String userName = principal.getName();
        model.addAttribute("userName", userName);

        UserProfile userProfile = userProfileRepository.findByUserName(userName);
        if(userProfile == null)
        {
            new RuntimeException("Not Found "+userName);
        }
        model.addAttribute("userProfile", userProfile);
        return "edit-page";
    }

    @PostMapping("/edit")
    public String postEdit(Principal principal, Model model, @ModelAttribute UserProfile userProfile)
    {
        String userName = principal.getName();

        UserProfile savedUserProfile = userProfileRepository.findByUserName(userName); //retriving the user from database and finding his ID because the @ModelAttribute received from frontend does not have Id. It just has other fields to edit but not Id. So we retrive the savedUserProfile from database and set the @ModelAttribute's userProfile Id  with with help of savedUserProfile.
        userProfile.setId(savedUserProfile.getId());
        userProfile.setUserName(userName); // or you can do userProfile.setUserName(savedUserProfile.getUserName())

        if(userProfile == null)
        {
            new RuntimeException("Not Found "+userName);
        }

        userProfileRepository.save(userProfile);

        return "redirect:/view/" + userName;
    }


    @GetMapping("/signup")
    public String signUp(Model model)
    {
        model.addAttribute("user", new User());
        return "signup";
    }

    @GetMapping("/view/{userName}")
    public String view(@PathVariable String userName, Model model)
    {

        Job job = new Job();
        UserProfile userProfile = userProfileRepository.findByUserName(userName);
    
        if(userProfile == null)
        {
            new RuntimeException("Not Found "+userName);
        }

        model.addAttribute("userName", userName); // the String i.e., the first parameter will be used as a variable in the frontend and the second attribute is you get as a parameter to the function
        model.addAttribute("userProfile", userProfile);

//        job.getResponsibilities().add("relativity");
//        job.getResponsibilities().add("quantum mechanics");
//        job.getResponsibilities().add("blow");
//
//        userProfile.getJobs().add(job);
//
//        userProfileRepository.save(userProfile);

//        String s = userProfile.getEducations().get(0).getCollege();
//
//        System.out.println(s);
        return "resume-templates/"+userProfile.getThemeChoice()+"/index";
    }
}
