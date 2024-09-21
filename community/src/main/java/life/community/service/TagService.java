package life.community.service;

import life.community.bo.TagBO;
import life.community.mapper.QuestionMapper;
import life.community.mapper.TagMapper;
import life.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TagService {

    private Map<String, TagBO> tagNameToIdMap = new HashMap<>();

    public Map<String, TagBO> getTagNameToIdMap() {
        return tagNameToIdMap;
    }

    public void setTagNameToIdMap(Map<String, TagBO> tagNameToIdMap) {
        this.tagNameToIdMap = tagNameToIdMap;
    }
}