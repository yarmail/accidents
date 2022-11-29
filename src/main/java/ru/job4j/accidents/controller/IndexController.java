package ru.job4j.accidents.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.accidents.service.AccidentService;

/**
 * Метод index принимает объект Model. Объект модель
 * - это оберта над классом HttpServletRequest.
 * То есть через объект Model можно получить данные
 * из запроса или отправить данные в ответ.
 */
@Controller
public class IndexController {
    private final AccidentService accidentService;

    public IndexController(AccidentService accidentService) {
        this.accidentService = accidentService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("user", "User1");
        model.addAttribute("accidents", accidentService.findAll());
        return "index";
    }
}