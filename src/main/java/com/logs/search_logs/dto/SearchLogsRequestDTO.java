package com.logs.search_logs.dto;

import java.sql.Timestamp;

import com.google.firebase.database.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SearchLogsRequestDTO {

    @NotNull
    private String searchKeyword;
    @NotNull
    private Timestamp logsFrom;
    @NotNull
    private Timestamp logsTo;


}
