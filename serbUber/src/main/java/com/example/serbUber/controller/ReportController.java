package com.example.serbUber.controller;

import com.example.serbUber.dto.ReportDTO;
import com.example.serbUber.dto.ReviewDTO;
import com.example.serbUber.service.ReportService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.example.serbUber.exception.ErrorMessagesConstants.MISSING_ID;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(@Qualifier("reportServiceConfiguration") final ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/all-for-user/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<ReportDTO> getAllForUser(@Valid @NotNull(message = MISSING_ID) @PathVariable Long id) {

        return this.reportService.getAllForUser(id);
    }
}
