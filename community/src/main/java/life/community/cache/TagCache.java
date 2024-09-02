package life.community.cache;

import life.community.dto.TagDTO;
import life.community.enums.CategoryEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagCache {
    public static List<TagDTO> get(){
        List<TagDTO> tagDTOS = new ArrayList<>();

        TagDTO frontEnd = new TagDTO();
        frontEnd.setCategory(CategoryEnum.FRONT_END.getName());
        frontEnd.setTags(CategoryEnum.FRONT_END.getTags());
        tagDTOS.add(frontEnd);

        TagDTO backEnd = new TagDTO();
        backEnd.setCategory(CategoryEnum.BACK_END.getName());
        backEnd.setTags(CategoryEnum.BACK_END.getTags());
        tagDTOS.add(backEnd);

        TagDTO mobile = new TagDTO();
        mobile.setCategory(CategoryEnum.MOBILE.getName());
        mobile.setTags(CategoryEnum.MOBILE.getTags());
        tagDTOS.add(mobile);

        TagDTO data = new TagDTO();
        data.setCategory(CategoryEnum.DATA.getName());
        data.setTags(CategoryEnum.DATA.getTags());
        tagDTOS.add(data);

        TagDTO ops = new TagDTO();
        ops.setCategory(CategoryEnum.OPS.getName());
        ops.setTags(CategoryEnum.OPS.getTags());
        tagDTOS.add(ops);

        TagDTO ai = new TagDTO();
        ai.setCategory(CategoryEnum.AI.getName());
        ai.setTags(CategoryEnum.AI.getTags());
        tagDTOS.add(ai);

        TagDTO tool = new TagDTO();
        tool.setCategory(CategoryEnum.TOOL.getName());
        tool.setTags(CategoryEnum.TOOL.getTags());
        tagDTOS.add(tool);

        TagDTO other = new TagDTO();
        other.setCategory(CategoryEnum.OTHER.getName());
        other.setTags(CategoryEnum.OTHER.getTags());
        tagDTOS.add(other);

        return tagDTOS;
    }

    public static String filterInvalid(String tags) {
        String[] split = StringUtils.split(tags, ",");
        List<TagDTO> tagDTOS = get();

        List<String> tagList = tagDTOS.stream().flatMap(tagDTO -> tagDTO.getTags().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(split).filter(t -> StringUtils.isBlank(t) || !tagList.contains(t)).collect(Collectors.joining(","));
        return invalid;
    }

}
