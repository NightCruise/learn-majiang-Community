package life.community.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import life.community.dto.PaginationDTO;
import life.community.dto.QuestionDTO;
import life.community.exception.CustomizeErrorCode;
import life.community.exception.CustomizeException;
import life.community.mapper.*;
import life.community.model.Question;
import life.community.model.QuestionExample;
import life.community.model.User;
import life.community.util.DataUtils;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
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
    @Autowired
    private CacheManager cacheManager;

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
    public PageInfo<QuestionDTO> getQuestions(Integer page, Integer size, String search) {
        if (!StringUtils.isBlank(search)){
            search = RegExUtils.replaceAll(search, " ", "|");
            search = RegExUtils.replaceAll(search, ",", "|");
        }
        PageHelper.startPage(page, size);
        List<QuestionDTO> questions = questionExtMapper.listQuestions(search);
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
        List<String> tagsList = handleTags(question);
        if (Objects.isNull(question.getId())){
            // 创建
            question.setCommentCount(0);
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setScore(0.0);
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
            // 获取创建完成后的问题，主要目的是获取问题ID。questionMapper获得的问题传入handleTag方法
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria()
                    .andTitleEqualTo(question.getTitle())
                    .andTagsEqualTo(question.getTags())
                    .andGmtCreateEqualTo(question.getGmtCreate())
                    .andGmtModifiedEqualTo(question.getGmtModified());
            List<Question> questions = questionMapper.selectByExample(questionExample);
            // 获取缓存，并将文章 ID 存入缓存
            Cache tagCache = cacheManager.getCache("tagCountToUpdate");
            tagCache.put(questions.get(0).getId(), tagsList);
        }else {
            // 更新
            question.setGmtModified(System.currentTimeMillis());
            int update = questionMapper.updateByPrimaryKeySelective(question);
            if (update != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            // 获取缓存，并将文章 ID 存入缓存
            Cache tagCache = cacheManager.getCache("tagCountToUpdate");
            Cache questionRankCache = cacheManager.getCache("questionRankToUpdate");
            tagCache.put(question.getId(), tagsList);
            questionRankCache.put(question.getId(), true);
        }
    }

    private List<String> handleTags(Question question) {
        String tags = question.getTags();
        List<String> newTags = new ArrayList<>();
        if (!tags.isEmpty()){
            String[] tagList = tags.split(",");
            for (String tag : tagList) {
                newTags.add(tag);
            }
        }
        return newTags;
    }

    public void incView(Long id) {
        Question updateQuestion = new Question();
        updateQuestion.setId(id);
        updateQuestion.setViewCount(1);
        questionExtMapper.incView(updateQuestion);
    }

    public List<Question> getQuestion(){
        return questionMapper.selectByExample(new QuestionExample());
    }

    public List<Question> getQuestionByScoreDesc(){
        QuestionExample example = new QuestionExample();
        example.setOrderByClause("score DESC");
        List<Question> questions = questionMapper.selectByExample(example);
        return questions;
    }

    public Question getQuestion(Long id){
        return questionMapper.selectByPrimaryKey(id);
    }

    public void updateQuestionScoreById(Long questionId, double score) {
        Question question = new Question();
        question.setId(questionId);
        question.setScore(score);
        questionMapper.updateByPrimaryKeySelective(question);
    }
}
