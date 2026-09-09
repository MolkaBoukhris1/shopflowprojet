package com.shopflow.shpflow.dto.response;
import lombok.*;
import org.springframework.data.domain.Page;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PageResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean dernierePage;
    private boolean premierePage;

    public static <T> PageResponse<T> of(Page<T> page) {
        return PageResponse.<T>builder()
            .content(page.getContent())
            .page(page.getNumber())
            .size(page.getSize())
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .dernierePage(page.isLast())
            .premierePage(page.isFirst()).build();
    }
}