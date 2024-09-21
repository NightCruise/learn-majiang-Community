package life.community.service;

import life.community.exception.CustomizeErrorCode;
import life.community.exception.CustomizeException;
import life.community.model.Question;
import life.community.schedule.HotTagSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class RankingService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private QuestionService questionService;

    private static final Logger log = LoggerFactory.getLogger(RankingService.class);

    public List<String> getRanking() {
        Set<DefaultTypedTuple<Number>> questionRankSet = redisTemplate.opsForZSet().reverseRangeWithScores("question:rank", 0, 10);
        List<String> questionTitles =  new ArrayList<>();
        if (Objects.requireNonNull(questionRankSet).isEmpty()){
            synchronized (this){
                // 缓存空，从数据库查询
                List<Question> questions = questionService.getQuestionByScoreDesc();
                for (int i = 0; i < 10; i++) {
                    questionTitles.add(questions.get(i).getTitle());
                }
                CompletableFuture.runAsync(() -> {
                    for (Question question : questions) {
                        redisTemplate.delete("question:rank");
                        redisTemplate.opsForZSet().add("question:rank", String.valueOf(question.getId()), question.getScore());
                    }
                    redisTemplate.expire("question:rank", 10, TimeUnit.MINUTES);
                }).thenRun(() -> {
                    log.info("缓存写入完成");
                }).exceptionally(ex -> {
                    // 异常处理
                    ex.printStackTrace();
                    throw new CustomizeException(CustomizeErrorCode.REDIS_WRITE_ERROR);
                });
            }
        }else {
            // 从缓存查询
            for (DefaultTypedTuple<Number> doubleDefaultTypedTuple : questionRankSet) {
                if (doubleDefaultTypedTuple.getValue() == null){
                    throw new CustomizeException(CustomizeErrorCode.REDIS_READ_ERROR);
                }
                Question question = questionService.getQuestion(doubleDefaultTypedTuple.getValue().longValue());
                questionTitles.add(question.getTitle());
            }
        }
        return questionTitles;
    }
}
