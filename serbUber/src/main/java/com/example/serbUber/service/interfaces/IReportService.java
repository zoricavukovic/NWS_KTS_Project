package com.example.serbUber.service.interfaces;

import com.example.serbUber.dto.ReportDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.ReportCannotBeCreatedException;
import com.example.serbUber.request.BehaviourReportRequest;
import org.checkerframework.checker.index.qual.SearchIndexBottom;

import java.util.List;

@SearchIndexBottom
public interface IReportService {

    List<ReportDTO> getAllForUser(Long id);

    boolean createReport(
            final Long senderId,
            final Long receiverId,
            final String message
    ) throws EntityNotFoundException, ReportCannotBeCreatedException;
}
