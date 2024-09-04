package life.community.mapper;

import life.community.dto.NotificationDTO;
import life.community.dto.QuestionDTO;

import java.util.List;

public interface NotificationExtMapper {
    /**
     *
     * @param userId
     * @return
     */
    List<NotificationDTO> listNotificationsByUser(Long userId);
}
