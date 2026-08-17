package com.careertrack.scheduler;

import com.careertrack.enums.InterviewStatus;
import com.careertrack.model.Interview;
import com.careertrack.model.User;
import com.careertrack.repository.InterviewRepository;
import com.careertrack.service.EmailService;
import com.careertrack.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class InterviewReminderScheduler {

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmailService emailService;

    // Runs daily based on app.notification.reminder-cron
    @Scheduled(cron =
            "${app.notification.reminder-cron:0 0 8 * * *}")
    @Transactional
    public void sendInterviewReminders() {

        LocalDate tomorrow =
                LocalDate.now().plusDays(1);

        List<Interview> interviews = interviewRepository
                .findByInterviewDateAndStatus(
                        tomorrow, InterviewStatus.SCHEDULED);

        for (Interview interview : interviews) {

            User user = interview.getJobApplication()
                    .getUser();

            String company = interview.getJobApplication()
                    .getCompanyName();

            String role = interview.getJobApplication()
                    .getJobRole();

            String time = interview.getInterviewTime() != null
                    ? interview.getInterviewTime().format(
                    DateTimeFormatter.ofPattern("hh:mm a"))
                    : "Time not set";

            // In-app notification
            notificationService.createNotification(
                    user,
                    "Interview Reminder",
                    "You have an interview with " + company
                            + " (" + role + ") tomorrow at "
                            + time + ".",
                    "INTERVIEW_REMINDER");

            // Email notification
            if (user.getEmail() != null) {

                String subject =
                        "Reminder: Interview with " + company
                                + " Tomorrow";

                String body = "Hi " + user.getFullName() + ",\n\n"
                        + "This is a reminder that you have an "
                        + "interview scheduled for tomorrow.\n\n"
                        + "Company: " + company + "\n"
                        + "Role: " + role + "\n"
                        + "Date: " + interview.getInterviewDate() + "\n"
                        + "Time: " + time + "\n"
                        + "Mode: " + (interview.getMode() != null
                        ? interview.getMode() : "Not specified") + "\n"
                        + (interview.getMeetingLink() != null
                        ? "Meeting Link: " + interview.getMeetingLink() + "\n" : "")
                        + (interview.getInterviewLocation() != null
                        ? "Location: " + interview.getInterviewLocation() + "\n" : "")
                        + "\nGood luck!\n\n"
                        + "- CareerTrack";

                emailService.sendEmail(
                        user.getEmail(), subject, body);
            }
        }
    }
}