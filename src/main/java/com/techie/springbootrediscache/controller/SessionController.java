package com.techie.springbootrediscache.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    @PostMapping("/login")
    public String login(HttpSession session, @RequestParam String username) {
        session.setAttribute("user", username);
        return "User " + username + " logged in. Session ID: " + session.getId();
    }

    @GetMapping("/me")
    public String currentUser(HttpSession session) {
        String user = (String) session.getAttribute("user");
        return user != null ? "Logged in as: " + user : "No session found";
    }
}
