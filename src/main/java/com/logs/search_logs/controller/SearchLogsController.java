package com.logs.search_logs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.logs.search_logs.dto.ResponseDTO;
import com.logs.search_logs.dto.SearchLogsRequestDTO;
import com.logs.search_logs.service.SearchLogsService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/v1")
public class SearchLogsController {

    @Autowired
    SearchLogsService searchLogsService;

    @GetMapping(value = { "/search-logs" })
	public ResponseEntity<Object> searchLogs(HttpServletRequest request,@RequestBody SearchLogsRequestDTO searchLogsRequestDTO) {
		log.info("Inside searchLogs with request: {}",searchLogsRequestDTO);
        ResponseDTO searchResponse= searchLogsService.searchLogs(request,searchLogsRequestDTO);
        log.info("searchLogs completed with response: {}",searchResponse);
        return new ResponseEntity<>(searchResponse, HttpStatus.OK);
	}

}
