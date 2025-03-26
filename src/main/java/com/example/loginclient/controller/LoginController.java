package com.example.loginclient.controller;

import com.example.loginclient.service.CustomerService;
import com.example.loginclient.service.ExternalLoginService;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private String token = null;
    @Autowired
    private ExternalLoginService loginService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // render login.html
    }

    @PostMapping("/login")
    public String handleLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {
        boolean success = loginService.authenticate(username, password, session);
        token = loginService.loginAndGetToken(username, password);

        if (success) {
            session.setAttribute("token", token);
            return "redirect:/customers"; // chuyển đến trang home (hoặc tùy chỉnh)
        } else {
            model.addAttribute("error", "Invalid username or password.");
            return "login";
        }
    }
}
