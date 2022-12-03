package com.example.serbUber.service;

import com.example.serbUber.dto.ReportDTO;
import com.example.serbUber.model.Report;
import com.example.serbUber.repository.ReportRepository;
import com.example.serbUber.service.interfaces.IReportService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.example.serbUber.dto.ReportDTO.fromReports;

@Component
@Qualifier("reportServiceConfiguration")
public class ReportService implements IReportService {

    private final ReportRepository reportRepository;

    public ReportService(final ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public List<ReportDTO> getAllForUser(Long id) {
        List<Report> reports = reportRepository.getAllForUser(id);

        return fromReports(reports);
    }
}
