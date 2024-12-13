package org.mastersdbis.mtsd.Controllers;

import jakarta.validation.Valid;
import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.DTO.RegistrationDTO;
import org.mastersdbis.mtsd.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebController {

    private final UserService userService;

    @Autowired
    public WebController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/admin/home")
    public String adminHome() {
        return "admin/home";
    }

    @GetMapping("/provider/home")
    public String providerHome() {
        return "provider/home";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationDTO", new RegistrationDTO());
        return "register";
    }

    /*
        @PostMapping("/register")
        public String register(@ModelAttribute("registrationDTO") @Valid RegistrationDTO registrationDTO,
                               BindingResult bindingResult, Model model) {
            System.out.println("Received registration data:");
            System.out.println("Username: " + registrationDTO.getUsername());
            System.out.println("Email: " + registrationDTO.getEmail());
            System.out.println("Phone Number: " + registrationDTO.getPhoneNumber());
            System.out.println("Address: " + registrationDTO.getAddress());
            System.out.println("Password: " + registrationDTO.getPassword());
            System.out.println("DTO content: " + registrationDTO);
            System.out.println("Validation Errors: " + bindingResult.getAllErrors());


            if (bindingResult.hasErrors()) {
                System.out.println("Validation errors detected");
                return "register";
            }

            try {
                userService.registerUser(
                        registrationDTO.getUsername(),
                        registrationDTO.getEmail(),
                        registrationDTO.getPhoneNumber(),
                        registrationDTO.getAddress(),
                        registrationDTO.getPassword()
                );
                System.out.println("User registered successfully");
            } catch (IllegalArgumentException e) {
                System.out.println("Error during registration: " + e.getMessage());
                model.addAttribute("errorMessage", e.getMessage());
                return "register";
            }
            return "redirect:/login";
        }
        */
    @PostMapping("/register")
    public String register(@ModelAttribute("registrationDTO") RegistrationDTO registrationDTO, Model model) {
        try {
            userService.registerUser(
                    registrationDTO.getUsername(),
                    registrationDTO.getEmail(),
                    registrationDTO.getPhoneNumber(),
                    registrationDTO.getAddress(),
                    registrationDTO.getPassword()
            );
            return "redirect:/success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }
}
