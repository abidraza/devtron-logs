package com.logs.search_logs.service;

import org.springframework.stereotype.Service;

import com.logs.search_logs.dto.ResponseDTO;
import com.logs.search_logs.dto.SearchLogsRequestDTO;

import jakarta.servlet.http.HttpServletRequest;

@Service
public interface SearchLogsService {

    ResponseDTO searchLogs(HttpServletRequest request, SearchLogsRequestDTO searchLogsRequestDTO);

}
