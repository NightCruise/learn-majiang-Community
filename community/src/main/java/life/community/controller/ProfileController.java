package life.community.controller;

import com.github.pagehelper.PageInfo;
import life.community.dto.NotificationDTO;
import life.community.dto.QuestionDTO;
import life.community.mapper.UserMapper;
import life.community.model.User;
import life.community.service.NotificationService;
import life.community.service.QuestionService;
import life.community.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Controller
public class ProfileController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable("action") String action,
                          @RequestParam(name = "page", defaultValue = "1") Object page,
                          Model model){
        User user = (User) request.getSession().getAttribute("user");
        if (Objects.isNull(user)){
            return "redirect:/";
        }
        switch(action){
            case "questions" :
                PageInfo<QuestionDTO> questionPageInfo = questionService.listByUser(user.getId(), ObjectUtils.roundObjectToInteger(page), 2);
                model.addAttribute("section", "questions");
                model.addAttribute("sectionName", "我的问题");
                model.addAttribute("pageInfo", questionPageInfo);
                break;
            case "replies" :
                PageInfo<NotificationDTO> notificationPageInfo = notificationService.listByUser(user.getId(), ObjectUtils.roundObjectToInteger(page), 2);
                model.addAttribute("section", "replies");
                model.addAttribute("sectionName", "最新回复");
                model.addAttribute("pageInfo", notificationPageInfo);
                break;
        }

        return "profile";
    }
}
