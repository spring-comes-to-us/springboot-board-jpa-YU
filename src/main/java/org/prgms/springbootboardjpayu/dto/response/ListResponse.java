package org.prgms.springbootboardjpayu.dto.response;

import lombok.Builder;

import java.util.List;

public record ListResponse(
        List content,
        int pageNumber,
        int pageSize,
        int totalPages,
        long totalElements
) {

    @Builder
    public ListResponse {
    }
}
