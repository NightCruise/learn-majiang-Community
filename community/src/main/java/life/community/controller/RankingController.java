package life.community.controller;

import life.community.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class RankingController {

    @Autowired
    private RankingService rankingService;

    @ResponseBody
    @PostMapping("/getRanking/question")
    public List<String> getRanking(){
        return rankingService.getRanking();
    }
}
