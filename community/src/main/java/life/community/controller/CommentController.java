package life.community.controller;

import life.community.dto.CommentCreateDTO;
import life.community.dto.CommentDTO;
import life.community.dto.ResultDTO;
import life.community.enums.CommentTypeEnum;
import life.community.exception.CustomizeErrorCode;
import life.community.model.Comment;
import life.community.model.User;
import life.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (Objects.isNull(user)){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if (Objects.isNull(commentCreateDTO) || StringUtils.isEmpty(commentCreateDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setId(commentCreateDTO.getId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setCommentator(1L);
        comment.setLikeCount(0L);
        commentService.insert(comment);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{commentId}", method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> secondaryComments(@PathVariable(name = "commentId") Long id){
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.Comment);
        return ResultDTO.okOf(commentDTOS);
    }
}
