package ru.job4j.accidents.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.service.AccidentService;
import ru.job4j.accidents.service.AccidentTypeService;
import ru.job4j.accidents.service.RuleService;

@Controller
public class AccidentController {
    private final AccidentService accidentService;
    private final AccidentTypeService accidentTypeService;
    private final RuleService ruleService;

    public AccidentController(AccidentService accidentService,
                              AccidentTypeService accidentTypeService,
                              RuleService ruleService) {
        this.accidentService = accidentService;
        this.accidentTypeService = accidentTypeService;
        this.ruleService = ruleService;
    }

    @GetMapping("/accidents")
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
     * @RequestParam - эта аннотация позволяет получить параметр из строки запроса.
     */
    @PostMapping("/saveAccident")
    public String save(@ModelAttribute Accident accident,
                       @RequestParam("type.id") int id,
                       HttpServletRequest request) {
        String[] ids = request.getParameterValues("rIds");
        Set<Rule> rules = new HashSet<>();
        for (String statId : ids) {
            rules.add(ruleService.findById(Integer.parseInt(statId)));
        }
        accident.setRules(rules);
        accident.setType(accidentTypeService.findById(id));
        accidentService.add(accident);
        return "redirect:/accidents";
    }

    @GetMapping("/formUpdateAccident")
    public String viewUpdate(@RequestParam("id") int id, Model model) {
        model.addAttribute("accident", accidentService.findById(id));
        model.addAttribute("types", accidentTypeService.findAll());
        model.addAttribute("rules", ruleService.findAll());
        return "editAccident";
    }

    @PostMapping("/updateAccident")
    public String update(@ModelAttribute Accident accident,
                         @RequestParam("type.id") int id,
                         HttpServletRequest request) {
        String[] ids = request.getParameterValues("rIds");
        Set<Rule> rules = new HashSet<>();
        for (String statId : ids) {
            rules.add(ruleService.findById(Integer.parseInt(statId)));
        }
        accident.setRules(rules);
        accident.setType(accidentTypeService.findById(id));
        accidentService.replace(accident);
        return "redirect:/accidents";
    }
}