package ru.job4j.accidents.controller;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.accidents.model.User;
import ru.job4j.accidents.repository.AuthorityRepository;
import ru.job4j.accidents.repository.UserRepository;

@AllArgsConstructor
@Controller
public class RegController {
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private static final Logger LOG = LogManager.getLogger(RegController.class);

    @PostMapping("/reg")
    public String regSave(@ModelAttribute User user) {
        user.setEnabled(true);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setAuthority(authorityRepository.findByAuthority("ROLE_USER"));
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/reg")
    public String regPage(@RequestParam(value = "error", required = false)
                          String error, Model model) {
        String errorMessage = null;
        if (error != null) {
            errorMessage = "Username already exists";
        }
        model.addAttribute("errorMessage", errorMessage);
        return "reg";
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public String exceptionHandler(Exception e) {
        LOG.error(e.getLocalizedMessage(), e);
        return "redirect:/reg?error=true";
    }
}