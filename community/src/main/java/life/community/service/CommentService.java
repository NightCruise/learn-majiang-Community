package life.community.service;

import life.community.dto.CommentDTO;
import life.community.dto.QuestionDTO;
import life.community.enums.CommentTypeEnum;
import life.community.exception.CustomizeErrorCode;
import life.community.exception.CustomizeException;
import life.community.mapper.*;
import life.community.model.*;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public void insert(Comment comment) {
        if (Objects.isNull(comment.getParentId()) || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (Objects.isNull(comment.getType()) || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if (CommentTypeEnum.Comment.getType().equals(comment.getType())) {
            // 回复评论->reply
            Comment parent = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (Objects.isNull(parent)) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
            // 增加回复的评论数
            Comment parentComment = new Comment();
            Long parentId = comment.getParentId();
            parentComment.setId(parentId);
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);
        } else {
            // 评论问题->comment
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (Objects.isNull(question)) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            // 增加评论的回复数
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
        }
    }

    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        // 根据问题的ID获取第一层评论
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create DESC");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size() == 0) {
            return new ArrayList<>();
        }
        // 拿到去重的问题的评论用户ID
        Set<Long> commentators = comments.stream().map(Comment::getCommentator).collect(Collectors.toSet());
        // 拿到去重的评论用户列表
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(new ArrayList<>(commentators));
        List<User> users = userMapper.selectByExample(userExample);
        // 将评论用户列表转换成map，对应关系是 用户ID:用户对象
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));
        // 根据comments的评论者ID找到map对应键的用户对象的值传入到CommentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if (StringUtils.isBlank(queryDTO.getTags())){
            return new ArrayList<>();
        }
        String relatedTags = RegExUtils.replaceAll(queryDTO.getTags(), ",", "|");
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTags(relatedTags);
        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}
