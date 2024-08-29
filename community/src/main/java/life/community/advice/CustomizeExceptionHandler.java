package life.community.advice;

import life.community.dto.ResultDTO;
import life.community.exception.CustomizeErrorCode;
import life.community.exception.CustomizeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CustomizeExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomizeExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Object handle(HttpServletRequest request, Throwable ex, Model model) {
        String contentType = request.getContentType();
        if ("application/json".equals(contentType)){
            // 错误页面跳转
            if (ex instanceof CustomizeException){
                logger.error("错误： " + ex.getMessage(), ex);
                return ResultDTO.errorOf((CustomizeException) ex);
            }else {
                logger.error("错误： " + ex.getMessage(), ex);
                return ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
        }else{
            // 错误页面跳转
            if (ex instanceof CustomizeException){
                logger.error("错误： " + ex.getMessage(), ex);
                model.addAttribute("message", ex.getMessage());
            }else {
                logger.error("错误： " + ex.getMessage(), ex);
                model.addAttribute("message", "服务出错了，请稍后再试试吧！");
            }
            return new ModelAndView("error");
        }


    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer code = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        HttpStatus status = HttpStatus.resolve(code);
        return (status != null) ? status : HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
