package life.community.service;

import life.community.dto.PaginationDTO;
import life.community.dto.QuestionDTO;
import life.community.mapper.QuestionMapper;
import life.community.mapper.UserMapper;
import life.community.model.Question;
import life.community.model.User;
import life.community.util.DataUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.count();
        // 数据库查出来的分组
        int totalPageCount = totalCount % size == 0 ? totalCount / size : totalCount / size + 1;
        // 确保传进去的page在1-totalCount之间,避免前端传入错误的值。
        page = Math.max(1, Math.min(page, totalPageCount));
        paginationDTO.setPagination(totalPageCount, page);
        // 拿到数据库查询起始值
        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.list(offset, size);
        List<QuestionDTO> questionDTOS = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setGmtCreate(formatTimestamp(question.getGmtCreate()));
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOS);
        return paginationDTO;
    }

    private String formatTimestamp(Long timestamp){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if(DataUtils.isToday(timestamp)){
            String hours = String.valueOf((System.currentTimeMillis() - timestamp) / (1000 * 60 * 60));
            return hours + " 小时前";
        }else if(DataUtils.isYesterday(timestamp)){
            return "昨天";
        }else{
            return dateFormat.format(timestamp);
        }
    }
}
