package life.community.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import life.community.dto.NotificationDTO;
import life.community.enums.NotificationStatusEnum;
import life.community.enums.NotificationTypeEnum;
import life.community.exception.CustomizeErrorCode;
import life.community.exception.CustomizeException;
import life.community.mapper.NotificationExtMapper;
import life.community.mapper.NotificationMapper;
import life.community.model.Notification;
import life.community.model.NotificationExample;
import life.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {

    @Autowired
    private NotificationExtMapper notificationExtMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    public PageInfo<NotificationDTO> listByUser(Long userId, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<NotificationDTO> userQuestions = notificationExtMapper.listNotificationsByUser(userId);
        for (NotificationDTO userQuestion : userQuestions) {
            userQuestion.setTypeName(NotificationTypeEnum.nameOfType(userQuestion.getType()));
        }
        return new PageInfo<>(userQuestions, 5);
    }

    public Long unreadCount(Long userId) {
        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(userId)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(example);
    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if (Objects.isNull(notification)){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        if (!Objects.equals(notification.getReceiver(), user.getId())){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
