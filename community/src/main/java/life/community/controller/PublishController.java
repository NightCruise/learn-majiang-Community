package life.community.controller;

import life.community.dto.QuestionDTO;
import life.community.model.Question;
import life.community.model.User;
import life.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    // 跳转publish页面
    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tags") String tags,
            @RequestParam("id") Integer id,
            HttpServletRequest request,
            Model model
    ){

        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tags", tags);

        if (Objects.isNull(title) || "".equals(title)){
            model.addAttribute("error" , "标题不能为空");
            return "publish";
        }
        if (Objects.isNull(description) || "".equals(description)){
            model.addAttribute("error" , "问题描述不能为空");
            return "publish";
        }
        if (Objects.isNull(tags) || "".equals(tags)){
            model.addAttribute("error" , "标签不能为空");
            return "publish";
        }

        User user = (User) request.getSession().getAttribute("user");

        if(Objects.isNull(user)){
            model.addAttribute("error", "用户未登录");
            return "publish";
        }

        Question question = new Question();
        question.setId(id);
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tags);
        question.setCreator(user.getId());
        questionService.creatOrUpdate(question);
        return "redirect:/";
    }

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable("id") String id,
                       Model model){
        QuestionDTO questionDTO = questionService.findById(id);
        model.addAttribute("title", questionDTO.getTitle());
        model.addAttribute("description", questionDTO.getDescription());
        model.addAttribute("tags", questionDTO.getTag());
        model.addAttribute("id", questionDTO.getId());
        return "publish";
    }

}
