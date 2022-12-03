package com.example.serbUber.service.interfaces;

import com.example.serbUber.dto.ReportDTO;
import org.checkerframework.checker.index.qual.SearchIndexBottom;

import java.util.List;

@SearchIndexBottom
public interface IReportService {

    List<ReportDTO> getAllForUser(Long id);
}
