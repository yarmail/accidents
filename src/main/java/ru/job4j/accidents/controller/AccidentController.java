package ru.job4j.accidents.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.service.AccidentService;
import ru.job4j.accidents.service.AccidentTypeService;
import ru.job4j.accidents.service.RuleService;

import java.util.Optional;

@Controller
@AllArgsConstructor
public class AccidentController {
    private final AccidentService accidentService;
    private final AccidentTypeService accidentTypeService;
    private final RuleService ruleService;

    @GetMapping("/")
    public String accidents(Model model) {
        model.addAttribute("accidents", accidentService.findAll());
        return "accidents";
    }

    @GetMapping("/addAccident")
    public String viewCreateAccident(Model model) {
        model.addAttribute("types", accidentTypeService.findAll());
        model.addAttribute("rules", ruleService.findAll());
        return "createAccident";
    }

    /**
     * @RequestParam - эта аннотация позволяет
     * получить параметр из строки запроса.
     * из request берем список статей rIds - rule id's
     * typeId - id типа происшествия
     */
    @PostMapping("/saveAccident")
    public String save(@ModelAttribute Accident accident,
                       @RequestParam("type.id") int typeId,
                       HttpServletRequest request) {
        String[] rIds = request.getParameterValues("rIds");
        if (!accidentService.save(accident, rIds, typeId)) {
            return "error";
        }
        return "redirect:/";
    }

    @GetMapping("/formUpdateAccident")
    public String viewUpdate(@RequestParam("id") int id, Model model) {
        Optional<Accident> accident = accidentService.findById(id);
        if (accident.isEmpty()) {
            return "error";
        }
        model.addAttribute("accident", accident.get());
        model.addAttribute("types", accidentTypeService.findAll());
        model.addAttribute("rules", ruleService.findAll());
        return "editAccident";
    }

    /**
     * из request берем список статей rIds - rule id's
     * typeId - id типа происшествия
     */
    @PostMapping("/updateAccident")
    public String replace(@ModelAttribute Accident accident,
                         @RequestParam("type.id") int typeId,
                         HttpServletRequest request) {
        String[] rIds = request.getParameterValues("rIds");
        if (!accidentService.replace(accident, rIds, typeId)) {
            return "error";
        }
        return "redirect:/";
    }
}