package com.example.serbUber.service;

import com.example.serbUber.dto.ReportDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.ReportCannotBeCreatedException;
import com.example.serbUber.model.Driving;
import com.example.serbUber.model.Report;
import com.example.serbUber.model.user.Admin;
import com.example.serbUber.model.user.User;
import com.example.serbUber.repository.ReportRepository;
import com.example.serbUber.service.interfaces.IReportService;
import com.example.serbUber.service.user.AdminService;
import com.example.serbUber.service.user.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.example.serbUber.dto.ReportDTO.fromReports;
import static com.example.serbUber.util.Constants.getReportMessage;

@Component
@Qualifier("reportServiceConfiguration")
public class ReportService implements IReportService {

    private final ReportRepository reportRepository;

    private final DrivingService drivingService;

    private final UserService userService;

    private final WebSocketService webSocketService;

    public ReportService(
            final ReportRepository reportRepository,
            final DrivingService drivingService,
            final UserService userService,
            final WebSocketService webSocketService
    ) {
        this.reportRepository = reportRepository;
        this.drivingService = drivingService;
        this.userService = userService;
        this.webSocketService = webSocketService;
    }

    public List<ReportDTO> getAllForUser(Long id) {
        List<Report> reports = reportRepository.getAllForUser(id);

        return fromReports(reports);
    }

    public boolean createReportDriver(
            final Long senderId,
            final Long drivingId,
            final String message
    ) throws EntityNotFoundException {
        User sender = this.userService.getUserById(senderId);
        Driving driving = this.drivingService.getDriving(drivingId);

        for (User receiver : driving.getUsers()) {
            this.saveAndSendMessage(sender, receiver, message);
        }

        return true;
    }

    public boolean createReport(
            final Long senderId,
            final Long receiverId,
            final String message
    ) throws EntityNotFoundException, ReportCannotBeCreatedException {
        User sender = this.userService.getUserById(senderId);
        User receiver = this.userService.getUserById(receiverId);
        checkIfReportCreationValid(sender, receiver);
        this.saveAndSendMessage(sender, receiver, message);
        this.sendMessageToAdmin(sender, receiver);

        return true;
    }

    private User findAdminForReportHandling() {

       return this.userService.findAdminForReportHandling();
    }

    private void saveAndSendMessage(User sender, User receiver, String message) {
        this.reportRepository.save(new Report(
                message,
                sender,
                receiver,
                null,
                false
        ));

        this.sendMessageToAdmin(sender, receiver);
    }

    private void sendMessageToAdmin(User sender, User receiver) {
        User admin = findAdminForReportHandling();
        if (admin != null) {

            this.webSocketService.sendReportNotification(admin.getId(), admin.getEmail(), getReportMessage(sender, receiver), receiver.getId());
        }
    }

    private void checkIfReportCreationValid(final User sender, final User receiver) throws ReportCannotBeCreatedException {
        Long driverId = sender.getRole().isDriver() ? sender.getId() : receiver.getId();
        Long userId = sender.getRole().isRegularUser() ? sender.getId() : receiver.getId();
        if (!this.drivingService.checkSenderAndReceiverInActiveDriving(driverId, userId)) {
            throw new ReportCannotBeCreatedException("Cannot create report if driving is not active.");
        }
    }
}
