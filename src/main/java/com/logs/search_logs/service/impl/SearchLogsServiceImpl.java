package com.logs.search_logs.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.logs.search_logs.dto.ResponseDTO;
import com.logs.search_logs.dto.SearchLogsRequestDTO;
import com.logs.search_logs.dto.SearchLogsResponseDTO;
import com.logs.search_logs.service.SearchLogsService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SearchLogsServiceImpl implements SearchLogsService {

    @Override
    public ResponseDTO searchLogs(HttpServletRequest request, SearchLogsRequestDTO searchLogsRequestDTO) {

        ResponseDTO validateRequestDTO = validateRequest(searchLogsRequestDTO);
        if (!"SUCCESS".equals(validateRequestDTO.getStatus()))
            return validateRequestDTO;
        List<String> searchedLogs = new ArrayList<>();
        log.info("Inside SearchLogsServiceImpl with request: {}", searchLogsRequestDTO);
        try {
            Storage storage = StorageOptions.newBuilder().setProjectId("dummy-d5e8a").build().getService();
            String bucketName = "dummy-d5e8a.appspot.com";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
            LocalDateTime start = LocalDateTime.parse(searchLogsRequestDTO.getLogsFrom().toString(), formatter).minusHours(5).minusMinutes(30);
            LocalDateTime end = LocalDateTime.parse(searchLogsRequestDTO.getLogsTo().toString(), formatter).minusHours(5).minusMinutes(30);
            while (start.isBefore(end)) {
                String date = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String hour = String.valueOf(start.getHour());
                hour=hour.length()==1?"0"+hour:hour;
                String filePath = date+"/"+hour+".txt";
                log.info("fileName: {}",filePath);
                start = start.plusHours(1);
                Blob blob = storage.get(bucketName, filePath);
                if(blob==null)
                    continue;
                InputStream contentStream = new ByteArrayInputStream(blob.getContent());
                log.info("contentStream: {}", contentStream);
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(contentStream))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.contains(searchLogsRequestDTO.getSearchKeyword())) {
                            log.info("line with :{} is {}", searchLogsRequestDTO.getSearchKeyword(), line);
                            searchedLogs.add(line);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.info(
                    "Inside SearchLogsServiceImpl with request: {}, Exception occurred while fetching data from firebase. : {}",
                    searchLogsRequestDTO, e.getMessage());
            return ResponseDTO.builder().status("FAILURE").statusCode(5000).message("exception occurred!")
                    .build();
        }
        SearchLogsResponseDTO searchLogsResponseDTO = SearchLogsResponseDTO.builder().searchedLogs(searchedLogs)
                .build();
        return ResponseDTO.builder().status("SUCCESS").statusCode(20000).message("logs fetched!")
                .data(searchLogsResponseDTO).build();
    }

    private ResponseDTO validateRequest(SearchLogsRequestDTO searchLogsRequestDTO) {
        log.info("validating request: {}", searchLogsRequestDTO);
        Timestamp startDateTime = searchLogsRequestDTO.getLogsFrom();
        Timestamp endDateTime = searchLogsRequestDTO.getLogsTo();
        if (endDateTime.before(startDateTime)) {
            log.info("Request validation failed. End date: {} cannot be before start date: {}", startDateTime,
                    endDateTime);
            return ResponseDTO.builder().status("FAILURE").statusCode(10000)
                    .message("End date cannot be before start date").build();

        }
        return ResponseDTO.builder().status("SUCCESS").statusCode(20000).message("validation successful").build();
    }

}
