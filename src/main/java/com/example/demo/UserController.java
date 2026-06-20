
package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/register")
    public String getRegisterPage() {
        return "Register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        userRepository.save(user);
        return "redirect:/login";  // ← was: return "Login"
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "Login";
    }

	
    
	/*
	 * @PostMapping("/login") public String login(@RequestParam String email,
	 * 
	 * @RequestParam String password, HttpSession session) {
	 * 
	 * System.out.println("Login attempt: " + email);
	 * 
	 * User user = userRepository.findByEmail(email);
	 * 
	 * if (user == null) { System.out.println("User NOT found"); return
	 * "redirect:/login?error"; }
	 * 
	 * if (!user.getPassword().equals(password)) {
	 * System.out.println("Wrong password"); return "redirect:/login?error"; }
	 * 
	 * session.setAttribute("user", user);
	 * 
	 * if ("ADMIN".equals(user.getRole())) { return "/adminhome"; } else { return
	 * "customerhome"; } }
	 */
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate(); // clears login session

        return "redirect:/login";
    }
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            return "redirect:/login?error";
        }

        if (!user.getPassword().equals(password)) {
            return "redirect:/login?error";
        }

        session.setAttribute("user", user);

        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/admin/home";   // ← was: redirect:/adminhome
        } else {
            return "redirect:/customer/home";
        }
    }
}