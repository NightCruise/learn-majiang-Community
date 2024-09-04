package life.community.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import life.community.dto.PaginationDTO;
import life.community.dto.QuestionDTO;
import life.community.exception.CustomizeErrorCode;
import life.community.exception.CustomizeException;
import life.community.mapper.QuestionExtMapper;
import life.community.mapper.QuestionMapper;
import life.community.mapper.UserMapper;
import life.community.model.Question;
import life.community.model.QuestionExample;
import life.community.model.User;
import life.community.util.DataUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;

    // 使用 PaginationDTO 的 setPagination 方法进行分页
    public PaginationDTO list(Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = Math.toIntExact(questionMapper.countByExample(new QuestionExample()));
        // 数据库查出来的分组
        int totalPageCount = totalCount % size == 0 ? totalCount / size : totalCount / size + 1;
        // 确保传进去的page在1-totalCount之间,避免前端传入错误的值。
        page = Math.max(1, Math.min(page, totalPageCount));
        paginationDTO.setPagination(totalPageCount, page);
        // 拿到数据库查询起始值
        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, size));
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setGmtCreate(String.valueOf(question.getGmtCreate()));
            questionDTO.setGmtCreate(formatTimestamp(question.getGmtCreate()));
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }

        paginationDTO.setQuestions(questionDTOS);
        return paginationDTO;
    }

    public PageInfo<QuestionDTO> listByUser(Long userId, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<QuestionDTO> userQuestions = questionExtMapper.listQuestionsByUser(userId);
        for (QuestionDTO questionDTO : userQuestions) {
            questionDTO.setGmtCreate(formatTimestamp(Long.valueOf(questionDTO.getGmtCreate())));
        }
        return new PageInfo<>(userQuestions, 5);
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

    // 使用 PageHelper进行分页
    public PageInfo<QuestionDTO> getQuestions(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<QuestionDTO> questions = questionExtMapper.listQuestions();
        for (QuestionDTO questionDTO : questions) {
            questionDTO.setGmtCreate(formatTimestamp(Long.valueOf(questionDTO.getGmtCreate())));
        }
        return new PageInfo<>(questions, 5);
    }


    public QuestionDTO findById(Long id) {
        QuestionDTO questionDTO = new QuestionDTO();
        Question question = questionMapper.selectByPrimaryKey(id);
        if (Objects.isNull(question)){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        String timestamp = formatTimestamp(question.getGmtCreate());
        BeanUtils.copyProperties(question, questionDTO);
        questionDTO.setGmtCreate(timestamp);
        User author = userMapper.selectByPrimaryKey(questionDTO.getCreator());
        questionDTO.setUser(author);
        return questionDTO;
    }

    public void creatOrUpdate(Question question) {
        handleTags(question);
        if (Objects.isNull(question.getId())){
            // 创建
            question.setCommentCount(0);
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        }else {
            // 更新
            question.setGmtModified(System.currentTimeMillis());
            int update = questionMapper.updateByPrimaryKeySelective(question);
            if (update != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    private void handleTags(Question question) {
        String tags = question.getTags();
        ArrayList<String> newTags = new ArrayList<>();
        if (!tags.isEmpty()){
            String[] tagList = tags.split(",");
            for (String tag : tagList) {
                tag = tag.substring(0, tag.length() - 1);
                newTags.add(tag);
            }
            tags = String.join(",", newTags);
            question.setTags(tags);
        }
    }

    public void incView(Long id) {
        Question updateQuestion = new Question();
        updateQuestion.setId(id);
        updateQuestion.setViewCount(1);
        questionExtMapper.incView(updateQuestion);
    }
}
