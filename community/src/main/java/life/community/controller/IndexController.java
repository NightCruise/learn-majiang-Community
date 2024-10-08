package life.community.controller;

import com.github.pagehelper.PageInfo;
import life.community.dto.QuestionDTO;
import life.community.mapper.UserMapper;
import life.community.service.QuestionService;
import life.community.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "1") Object page,
                        @RequestParam(name = "size", defaultValue = "2") Object size,
                        @RequestParam(name = "search", required = false) String search) {
        // 使用 PageHelper 展示问题界面
        PageInfo<QuestionDTO> pageInfo = questionService.getQuestions(ObjectUtils.roundObjectToInteger(page), ObjectUtils.roundObjectToInteger(size), search);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("search", search);
        return "index";
    }

}
