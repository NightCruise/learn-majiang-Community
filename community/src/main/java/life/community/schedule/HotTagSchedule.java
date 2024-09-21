package life.community.schedule;

import life.community.model.Question;
import life.community.service.QuestionService;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class HotTagSchedule {

    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private RedisTemplate redisTemplate;

    private static final Logger log = LoggerFactory.getLogger(HotTagSchedule.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


    @Scheduled(fixedRate =  10 * 60000)
    public void hotTagSchedule() {
        log.info("The time is start {}", dateFormat.format(new Date()));
        com.github.benmanes.caffeine.cache.Cache<Object, Object> nativeCache =
                (com.github.benmanes.caffeine.cache.Cache<Object, Object>) cacheManager.getCache("questionRankToUpdate").getNativeCache();
        // 获取所有待更新的文章 ID
        Set<@NonNull Object> updateQuestionIds = nativeCache.asMap().keySet();
        for (Object updateQuestionId : updateQuestionIds) {
            this.refresh((Long) updateQuestionId);
        }
        log.info("The time is stop {}", dateFormat.format(new Date()));
    }

    private void refresh(Long questionId){
        Question questionById = questionService.getQuestion(questionId);
        if (questionById == null) {
            log.error("该帖子不存在: id = " + questionId);
            return;
        }
        // 评论数量
        int commentCount = questionById.getCommentCount();
        // 分数 = (评论数 * 0.7) * 1000 / (当前时间 - 创建时间 + 1)
        // 计算时间差（以天为单位）
        long daysSinceCreated = (System.currentTimeMillis() - questionById.getGmtCreate()) / (1000 * 60 * 60 * 24);
        // 防止出现0天的情况，避免除以0
        if (daysSinceCreated == 0) {
            daysSinceCreated = 1;
        }
        // 计算score（确保参与计算的数值是double）
        double score = (commentCount * 700.0) / daysSinceCreated;
        questionService.updateQuestionScoreById(questionId, score);
        List<Question> questionByScoreDesc = questionService.getQuestionByScoreDesc();
        redisTemplate.delete("question:rank");
        questionByScoreDesc.forEach((question) -> {
            redisTemplate.opsForZSet().add("question:rank", question.getId(), question.getScore());
        });
        redisTemplate.expire("question:rank", 10, TimeUnit.MINUTES); // 10分钟过期
    }

}

