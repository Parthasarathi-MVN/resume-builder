package com.project.resumebuilder.controllers;

import com.project.resumebuilder.models.Job;
import com.project.resumebuilder.models.UserProfile;
import com.project.resumebuilder.repositories.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


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
    public String edit()
    {
        return "edit page";
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

        String s = userProfile.getEducations().get(0).getCollege();

        System.out.println(s);
        return "resume-templates/"+userProfile.getThemeChoice()+"/index";
    }
}
