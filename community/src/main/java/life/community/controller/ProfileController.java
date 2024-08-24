package life.community.controller;

import com.github.pagehelper.PageInfo;
import life.community.dto.QuestionDTO;
import life.community.mapper.UserMapper;
import life.community.model.User;
import life.community.service.QuestionService;
import life.community.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Controller
public class ProfileController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable("action") String action,
                          @RequestParam(name = "page", defaultValue = "1") Object page,
                          Model model){
        User user = null;
        Cookie[] cookies = request.getCookies();
        if (Objects.nonNull(cookies)){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    user = userMapper.findByToken(token);
                    if (user != null){
                        request.getSession().setAttribute("user", user);
                    }else{
                        return "redirect:/";
                    }
                }
            }
        }else {
            return "redirect:/";
        }

        switch(action){
            case "questions" :
                //语句
                model.addAttribute("section", "questions");
                model.addAttribute("sectionName", "我的问题");
                PageInfo<QuestionDTO> pageInfo = questionService.listByUser(user.getId(), ObjectUtils.roundObjectToInteger(page), 2);
                model.addAttribute("pageInfo", pageInfo);
                break;
            case "replies" :
                //语句
                model.addAttribute("section", "replies");
                model.addAttribute("sectionName", "最新回复");
                break;
        }

        return "profile";
    }
}
