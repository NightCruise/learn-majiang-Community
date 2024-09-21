package life.community.schedule;

import life.community.bo.TagBO;
import life.community.exception.CustomizeErrorCode;
import life.community.exception.CustomizeException;
import life.community.mapper.QuestionTagMapper;
import life.community.mapper.TagExtMapper;
import life.community.mapper.TagMapper;
import life.community.model.*;
import life.community.service.TagService;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

@Component
public class TagCountUpdateSchedule {

    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private QuestionTagMapper questionTagMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private TagExtMapper tagExtMapper;
    @Autowired
    private TagService tagService;

    private static final Logger log = LoggerFactory.getLogger(HotTagSchedule.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 60000 * 10)
    @Transactional
    public void updateTagsCount() {
        log.info("The time is start {}", dateFormat.format(new Date()));
        // 项目开启时 tagService的map为空，需要有一个初始量
        if (tagService.getTagNameToIdMap().isEmpty()) {
            List<Tag> initTagList = tagMapper.selectByExample(new TagExample());
            HashMap<String, TagBO> initTagNameToIdMap = new HashMap<>();
            for (Tag tag : initTagList) {
                initTagNameToIdMap.put(tag.getName(), new TagBO(tag.getId(), tag.getCount()));
            }
            tagService.setTagNameToIdMap(initTagNameToIdMap);
        }
        // 获取缓存的原生 API
        com.github.benmanes.caffeine.cache.Cache<Object, Object> nativeCache =
                (com.github.benmanes.caffeine.cache.Cache<Object, Object>) cacheManager.getCache("tagCountToUpdate").getNativeCache();
        // 获取所有待更新的文章 ID
        ConcurrentMap<@NonNull Object, @NonNull Object> concurrentMap = nativeCache.asMap();
        // 待更新的文章IDS
        Set<@NonNull Object> questionIds  = nativeCache.asMap().keySet();
        if (concurrentMap.isEmpty()) {
            log.info("The time is stop {}", dateFormat.format(new Date()));
            return;
        } else {
            // 将 caffeine 的 concurrentMap 转换成 HashMap<Long, List<String>>
            HashMap<Long, List<String>> questionTagNameMap = new HashMap<>();
            for (Map.Entry<@NonNull Object, @NonNull Object> entry : concurrentMap.entrySet()) {
                questionTagNameMap.put((Long) entry.getKey(), (List<String>) entry.getValue());
            }
            // 根据 QuestionId 获取 对应tagId 的 QuestionTag对象
            QuestionTagExample selectTagIdExample = new QuestionTagExample();
            selectTagIdExample.createCriteria().andQuestionIdIn(new ArrayList<>(questionTagNameMap.keySet()));
            List<QuestionTag> questionTagsByQuestionId = questionTagMapper.selectByExample(selectTagIdExample);
            // 将查询结果转换为 Map<Long, List<Long>>
            Map<Long, List<Long>> oldQuestionIdToTagIdsMap = new HashMap<>();
            for (QuestionTag questionTag : questionTagsByQuestionId) {
                oldQuestionIdToTagIdsMap
                        .computeIfAbsent(questionTag.getQuestionId(), k -> new ArrayList<>())
                        .add(questionTag.getTagId());
            }
            // 将questionTagNameList 缓存 转换为 Map<Long, List<Long>>
            Map<Long, List<Long>> newQuestionIdToTagIdsMap = new HashMap<>();
            for (Map.Entry<Long, List<String>> entry : questionTagNameMap.entrySet()) {
                List<Long> tagIds = new ArrayList<>();
                for (String tagName : entry.getValue()) {
                    if (tagService.getTagNameToIdMap().containsKey(tagName)) {
                        tagIds.add(tagService.getTagNameToIdMap().get(tagName).getId());
                    } else {
                        throw new CustomizeException(CustomizeErrorCode.TAG_NOT_FOUND);
                    }
                }
                newQuestionIdToTagIdsMap.put(entry.getKey(), tagIds);
            }
            // 去重 并插入
            HashMap<Long, Integer> deleteTagCount = new HashMap<>();
            HashMap<Long, Integer> addTagCount = new HashMap<>();
            for (Map.Entry<Long, List<Long>> entry : newQuestionIdToTagIdsMap.entrySet()) {
                List<Long> newTags = entry.getValue();
                List<Long> oldTags = oldQuestionIdToTagIdsMap.getOrDefault(entry.getKey(), new ArrayList<>());
                List<Long> deleteTagIds = new ArrayList<>();
                List<Long> addTagIds = new ArrayList<>();
                deleteTagIds.addAll(oldTags);
                deleteTagIds.removeAll(newTags);
                addTagIds.addAll(newTags);
                addTagIds.removeAll(oldTags);
                QuestionTagExample deleteQuestionToTagExample = new QuestionTagExample();
                // 去除 tags
                if (!deleteTagIds.isEmpty()){
                    deleteQuestionToTagExample.createCriteria()
                            .andGmtCreateEqualTo(entry.getKey())
                            .andTagIdIn(deleteTagIds);
                    questionTagMapper.deleteByExample(deleteQuestionToTagExample);
                }
                // 插入tags
                if(!addTagIds.isEmpty()){
                    for (Long addTag : addTagIds) {
                        QuestionTag questionTag = new QuestionTag();
                        questionTag.setQuestionId(entry.getKey());
                        questionTag.setTagId(addTag);
                        questionTag.setGmtCreate(System.currentTimeMillis());
                        questionTag.setGmtModified(questionTag.getGmtCreate());
                        questionTagMapper.insert(questionTag);
                    }
                }
                // 遍历集合中的每个 TagId, 对删除的数量变成复数，添加的数量变成正数
                for (Long tagId : deleteTagIds) {
                    // 如果该 TagId 已经存在于 tagIdCountMap 中，则计数 -1
                    deleteTagCount.put(tagId, deleteTagCount.getOrDefault(tagId, 0) - 1);
                }
                for (Long tagId : addTagIds) {
                    // 如果该 TagId 已经存在于 tagIdCountMap 中，则计数 -1
                    addTagCount.put(tagId, addTagCount.getOrDefault(tagId, 0) + 1);
                }
            }
            // 新的 Map 来存储合并结果
            HashMap<Long, Integer> mergedTagCount = new HashMap<>(deleteTagCount);
            // 遍历 addTagCount，合并到 mergedTagCount
            for (Map.Entry<Long, Integer> entry : addTagCount.entrySet()) {
                Long key = entry.getKey();
                Integer value = entry.getValue();
                // 如果 key 已经存在于 mergedTagCount 中，则累加 value
                mergedTagCount.put(key, mergedTagCount.getOrDefault(key, 0) + value);
            }
            // 更新TagMapper的count值
            for (Map.Entry<Long, Integer> entry : mergedTagCount.entrySet()) {
                if (entry.getValue() == 0L) {
                    continue;
                }
                Tag updateTag = new Tag();
                updateTag.setId(entry.getKey());
                updateTag.setCount(entry.getValue());
                tagExtMapper.incTagCount(updateTag);
                TagExample example = new TagExample();
                example.createCriteria().andIdEqualTo(entry.getKey());
                List<Tag> tags = tagMapper.selectByExample(example);
                // 将tagNameToIdMap 存储到 TagService。
                tagService.getTagNameToIdMap().put(tags.get(0).getName(),
                        new TagBO(tags.get(0).getId(), tags.get(0).getCount()));
            }
        }
        // 6. 清空已处理的文章 ID
        nativeCache.invalidateAll(questionIds);
        log.info("The time is stop {}", dateFormat.format(new Date()));
    }

}
