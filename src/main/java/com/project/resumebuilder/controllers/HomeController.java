//Endpoints of the API's

package com.project.resumebuilder.controllers;

import com.project.resumebuilder.models.Education;
import com.project.resumebuilder.models.Job;
import com.project.resumebuilder.models.User;
import com.project.resumebuilder.models.UserProfile;
import com.project.resumebuilder.repositories.UserProfileRepository;
import com.project.resumebuilder.repositories.UserRepository;
import com.project.resumebuilder.services.EmailSenderService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;


@Controller
public class HomeController {

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    UserRepository userRepository;


    @PostMapping("/register")
    public String signUp(User user, HttpServletRequest request)
    {

//        user.setActive(true);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        String randomCode = RandomString.make(64);
        user.setVerification_code(randomCode);
        user.setEnabled(false);

        userRepository.save(user);
        String siteURL = request.getRequestURL().toString();

        emailSenderService.sendVerificationEmail(user, siteURL.replace(request.getServletPath(), ""));
        System.out.println(siteURL.replace(request.getServletPath(), ""));
        return "redirect:/login";
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code)
    {
        User user = userRepository.findByverification_code(code);

        if(code == null)  //There must be some code after /verify. Checking if this route does not actually exist and returning "Oops" page.
        {
            return "oops";
        }

        if(user == null || user.isEnabled())
        {
            return "verify-fail";
        }
        else
        {
            user.setVerification_code(null);
            user.setEnabled(true);
            userRepository.save(user);
            return "verify-success";
        }
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

        //Preventing the user to access login page after logging in.
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        return "redirect:/home";
    }


//    @PostMapping("/login") there is no need of login logic for spring boot

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
//        UserProfile userP = new UserProfile();

        String userName = principal.getName();

//        userP.setUserName(userName);
//        userProfileRepository.save(userP); // Initially adding the user_name to UserProfile table. Becuase once a User signs up, if he clicks on "make your resume", then there would be Null error as there was no user_name in UserProfile table. Simply, this enables user to make resume as soon as they Sign Up and Login for the first time.

        model.addAttribute("userName", userName);

        UserProfile userProfile = userProfileRepository.findByUserName(userName);
//        if(userProfile == null)
//        {
//            new RuntimeException("Not Found "+userName);
//        }

        if(userProfile == null)
        {
            userProfile.setUserName(userName);
            userProfileRepository.save(userProfile);
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

        return "resume-templates/"+userProfile.getThemeChoice()+"/index";
    }


    @GetMapping("/forgot-password")
    public String forgotPassword()
    {
        return "forgot-password";
    }


    @Autowired
    EmailSenderService emailSenderService;
    @PostMapping("/send-mail")
    public String triggerMail(@RequestParam String userEmail, HttpSession session)
    {
        System.out.println(userEmail);
        User user = userRepository.findByEmail(userEmail);
        String email = user.getEmail();
        int otpSize = 6;
        char[] o = new char[otpSize];
        o = emailSenderService.generateOTP(otpSize);
        String otp = String.valueOf(o);
        user.setOtp(otp);
        userRepository.save(user);
        String subject = "Forgot Password - OTP";
        String body = "This is your OTP " + otp + "\n Enter this OTP to change your password.";
        emailSenderService.sendEmail(email, subject, body);
        session.setAttribute("email", userEmail);//its like key:value. first paramenter is key, second is value
                                                        // Using HTTP Session to save Email in the session. Because, while verifying the OTP we have to get the User object. For getting the User object we need Email and after getting the User object, we can verify the OTP from database and the OTP entered in the form.
        return  "enter-otp";
    }

    @PostMapping("verify-otp")
    public String verifyOTP(Model model, HttpSession session, @RequestParam String mailOTP)
    {
        String userEmail = (String)session.getAttribute("email"); // This is the key of HTTP Session Attribute
                                                                        // Session returns an object, so we need to type cast
        System.out.println(userEmail);
        User user = userRepository.findByEmail(userEmail);
        String databaseOTP = user.getOtp();
        boolean isOTPMatch = false;
        model.addAttribute("isOTPMatch", isOTPMatch);
        if (databaseOTP.equals(mailOTP))
        {
            isOTPMatch = true;
            model.addAttribute("isOTPMatch", isOTPMatch);

            System.out.println("OTP Matched");
            return "new-password";
        }

        else
            return "enter-otp";
    }

    @PostMapping("/change-password")
    public String changePassword(Model model, HttpSession session, @RequestParam String password1, @RequestParam String password2)
    {
        String userEmail = (String)session.getAttribute("email");
        User user = userRepository.findByEmail(userEmail);
        System.out.println(password1);
        System.out.println(password2);
        boolean isPasswordMatch = false;
        model.addAttribute("isPasswordMatch", isPasswordMatch);

        if(password1.equals(password2))
        {
            System.out.println("Both pass words match.");
            isPasswordMatch = true;
            model.addAttribute("isPasswordMatch", isPasswordMatch);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodedPassword = encoder.encode(password1);
            user.setPassword(encodedPassword);
            userRepository.save(user);
            return "login";
        }
        else
        {
            return "new-password";
        }


    }

}
