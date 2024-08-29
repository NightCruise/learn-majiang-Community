package life.community.controller;

import life.community.dto.QuestionDTO;
import life.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/question/{id}/details")
    public String profile(@PathVariable("id") Long id,
                          Model model){
        QuestionDTO questionDTO = questionService.findById(id);
        questionService.incView(id);
        model.addAttribute("question", questionDTO);
        return "question";
    }

}
