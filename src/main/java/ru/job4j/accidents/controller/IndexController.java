package ru.job4j.accidents.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Метод index принимает объект Model. Объект модель
 * - это оберта над классом HttpServletRequest.
 * То есть через объект Model можно получить данные
 * из запроса или отправить данные в ответ.
 */
@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }
}