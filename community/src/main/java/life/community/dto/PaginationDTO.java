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
    // 可见页码数量，可以修改为你想要的值
    private final int visiblePages = 4;

    public void setPagination( Integer totalPageCount, Integer page) {

        this.currentPage = page;
        this.totalPageCount = totalPageCount;

        int half = visiblePages / 2;

        int startPage;
        int endPage;

        // 确定起始页码和结束页码
        if (visiblePages % 2 == 0) {
            // 偶数情况，当前页略偏左
            startPage = Math.max(1, currentPage - half + 1);
            endPage = Math.min(totalPageCount, currentPage + half);
        } else {
            // 奇数情况，当前页居中
            startPage = Math.max(1, currentPage - half);
            endPage = Math.min(totalPageCount, currentPage + half);
        }

        // 如果页码数量不足可见页码数量，则进行调整
        if (endPage - startPage + 1 < visiblePages) {
            if (startPage == 1) {
                endPage = Math.min(totalPageCount, startPage + visiblePages - 1);
            } else if (endPage == totalPageCount) {
                startPage = Math.max(1, endPage - visiblePages + 1);
            }
        }

        // 添加页码到列表
        for (int i = startPage; i <= endPage; i++) {
            pages.add(i);
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
