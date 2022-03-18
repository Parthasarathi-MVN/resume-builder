package com.project.resumebuilder.controllers;

import com.project.resumebuilder.models.Education;
import com.project.resumebuilder.models.Job;
import com.project.resumebuilder.models.User;
import com.project.resumebuilder.models.UserProfile;
import com.project.resumebuilder.repositories.UserProfileRepository;
import com.project.resumebuilder.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;



@Controller
public class HomeController {

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    UserRepository userRepository;



    @PostMapping("/register")
    public String signUp(User user)
    {

        user.setActive(true);
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/")
    public String index()
    {
        return "index";
    }

    @GetMapping("/login")
    public String login()
    {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }

        return "redirect:/home";
    }


//    @PostMapping("/login") there is no need of login logic for spring boot
//    public String login(User u)
//    {
//       User savedUser = userRepository.findByUserName(u.getUserName());
//       System.out.println("heyyy");
//       if(savedUser == null)
//       {
//           new RuntimeException("Not Found "+u.getUserName());
//       }
////       System.out.println(u.getPassword());
//       if (savedUser.getPassword().equals(u.getPassword()))
//
//            return "home";
//       return "login";
//    }

    @GetMapping("/home")
    public String home(Principal principal, Model model)
    {
        String userName = principal.getName();
        model.addAttribute("userName", userName); //for userName

        UserProfile userProfile = userProfileRepository.findByUserName(userName);
        if(userProfile == null)
        {
            new RuntimeException("Not Found "+userName);
        }

        model.addAttribute("userProfile", userProfile);


        return "home";
    }

    @GetMapping("/edit")
    public String edit(Principal principal, Model model, @RequestParam(required = false) String add)
    {
        String userName = principal.getName();
        model.addAttribute("userName", userName);

        UserProfile userProfile = userProfileRepository.findByUserName(userName);
        if(userProfile == null)
        {
            new RuntimeException("Not Found "+userName);
        }

        if("job".equals(add))
        {
            userProfile.getJobs().add(new Job());
        }
        else if("education".equals(add))
        {
            userProfile.getEducations().add(new Education());
        }
        else if("skill".equals(add))
        {
            userProfile.getSkills().add("");
        }

        model.addAttribute("userProfile", userProfile);
        return "edit-page";
    }

    @GetMapping("/delete")
    public String edit(Model model, Principal principal, @RequestParam String type, @RequestParam int index)
    {
        String userName = principal.getName();

        UserProfile userProfile = userProfileRepository.findByUserName(userName);
        if(userProfile == null)
        {
            new RuntimeException("Not Found "+userName);
        }

        if("job".equals(type))
        {
            userProfile.getJobs().remove(index);
        }
        else if("education".equals(type))
        {
            userProfile.getEducations().remove(index);
        }
        else if("skill".equals(type))
        {
            userProfile.getSkills().remove(index);
        }

        userProfileRepository.save(userProfile);

        return "redirect:/edit/";

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
    public String view(Principal principal, @PathVariable String userName, Model model)
    {

        if (principal != null && principal.getName() != "")
        {
            boolean currentUserProfile = principal.getName().equals(userName);
            model.addAttribute("currentUserProfile", currentUserProfile);

        }

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
