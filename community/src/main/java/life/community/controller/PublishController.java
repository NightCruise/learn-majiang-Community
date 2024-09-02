package life.community.controller;

import life.community.cache.TagCache;
import life.community.dto.QuestionDTO;
import life.community.model.Question;
import life.community.model.User;
import life.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
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
    public String publish(Model model){
        model.addAttribute("officialTags", TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tags") String tags,
            @RequestParam("id") Long id,
            HttpServletRequest request,
            Model model
    ){

        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tags", tags);
        model.addAttribute("officialTags", TagCache.get());

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

        String invalid = TagCache.filterInvalid(tags);
        if (StringUtils.isNotBlank(invalid)) {
            model.addAttribute("error", "输入非法标签:" + invalid);
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
        question.setTags(tags);
        question.setCreator(user.getId());
        questionService.creatOrUpdate(question);
        return "redirect:/";
    }

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable("id") Long id,
                       Model model){
        QuestionDTO questionDTO = questionService.findById(id);
        model.addAttribute("title", questionDTO.getTitle());
        model.addAttribute("description", questionDTO.getDescription());
        model.addAttribute("tags", questionDTO.getTags());
        model.addAttribute("id", questionDTO.getId());
        model.addAttribute("officialTags", TagCache.get());
        return "publish";
    }

}
