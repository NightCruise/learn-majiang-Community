package life.community.controller;

import life.community.dto.CommentDTO;
import life.community.dto.QuestionDTO;
import life.community.enums.CommentTypeEnum;
import life.community.service.CommentService;
import life.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}/details")
    public String profile(@PathVariable("id") Long id,
                          Model model){
        QuestionDTO questionDTO = questionService.findById(id);
        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.Question);
        List<QuestionDTO> relatedQuestionDTOs = commentService.selectRelated(questionDTO);
        questionService.incView(id);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedQuestionDTOs);
        return "question";
    }

}
