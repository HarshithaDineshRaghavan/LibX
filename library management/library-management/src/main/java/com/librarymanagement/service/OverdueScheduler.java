package com.librarymanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class OverdueScheduler {

    @Autowired
    private FineService fineService;

    @Autowired
    private BorrowsService borrowsService;

    @Autowired
    private NotificationService notificationService;

    @Scheduled(cron = "0 0 0 * * ?")

    public void dailyJobs() {
        borrowsService.updateOverdueAndFines();
        fineService.generatePendingFines();
        notificationService.sendDueDateReminders();
        notificationService.sendOverdueReminders();
    }

}
