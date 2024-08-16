package com.openpayd.foreign.exchange.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "A paginated response containing metadata and a list of items.")
public record PageResponse<T>(
    @Schema(description = "The total number of elements in the entire dataset.", example = "100")
        long totalElements,
    @Schema(description = "The total number of pages available.", example = "10") int totalPages,
    @Schema(description = "The current page number.", example = "1") int currentPage,
    @Schema(description = "The size of each page.", example = "10") int pageSize,
    @Schema(description = "Indicates if this is the first page of the dataset.", example = "true")
        boolean isFirst,
    @Schema(description = "Indicates if this is the last page of the dataset.", example = "false")
        boolean isLast,
    @Schema(description = "The list of items on the current page.") List<T> data) {}
