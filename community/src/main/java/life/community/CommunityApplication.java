package life.community;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import life.community.model.Question;
import life.community.schedule.HotTagSchedule;
import life.community.service.QuestionService;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootApplication
@EnableScheduling
@EnableCaching
@MapperScan("life.community.mapper")
public class CommunityApplication {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private RedisTemplate redisTemplate;

    private static final Logger log = LoggerFactory.getLogger(HotTagSchedule.class);

    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
    }

    // 缓存预热方法，应用启动时执行
    @PostConstruct
    public void initCache() {
        // 调用预热缓存的业务逻辑，更新 Redis 中的排行榜数据
        updateRankingData();
    }

    // 示例：预热缓存的逻辑
    private void updateRankingData() {
        // 从数据库查询文章、评论、标签等基础数据
        // 计算文章的分数（score）
        // 将数据写入到 Redis 的 Zset 中
        List<Question> questionList = questionService.getQuestion();
        for (Question question : questionList) {
            redisTemplate.opsForZSet().add("question:rank", String.valueOf(question.getId()), question.getScore());
        }
        log.info("预热排行榜缓存...");
    }

    /**
     * 设置 redisTemplate 的序列化设置
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        //1.创建redisTemplate模板
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        //2.关联redisConnectionFactory
        template.setConnectionFactory(redisConnectionFactory);
        //3.创建序列化类
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        //4.设置可见度
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //5.启动默认的类型
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        //6.序列化类，对象映射设置
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        //7.设置value的转化格式和key的转换格式
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

}
