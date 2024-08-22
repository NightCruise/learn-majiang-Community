package life.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO {
    private List<QuestionDTO> questions;
    private Boolean showPrevious;
    private Boolean showFirstPage;
    private Boolean showNext;
    private Boolean showEndPage;
    private Integer currentPage;
    private List<Integer> pages = new ArrayList<>();
    private Integer totalPageCount;

    public void setPagination( Integer totalPageCount, Integer page) {

        this.currentPage = page;
        this.totalPageCount = totalPageCount;

        pages.add(currentPage);
        for (int i = 1; i <= 3; i++) {
            if (currentPage - i > 0){
                // 添加currentPage左边的页码
                pages.add(0, currentPage - i);
            }
            if (currentPage + i <= totalPageCount ){
                // 添加currentPage右边的页码
                pages.add(currentPage + i);
            }
        }

        // 是否展示上一页按钮
        showPrevious = currentPage != 1;

        // 是否展示下一页按钮
        showNext = !currentPage.equals(totalPageCount);

        // 是否展示第一页按钮
        showFirstPage = !pages.contains(1);

        // 是否展示最后一页按钮
        showEndPage = !pages.contains(totalPageCount);
    }
}
