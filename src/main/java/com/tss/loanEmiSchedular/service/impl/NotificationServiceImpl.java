package com.tss.loanEmiSchedular.service.impl;

import com.tss.loanEmiSchedular.entity.Emi;
import com.tss.loanEmiSchedular.entity.Loan;
import com.tss.loanEmiSchedular.service.EmailService;
import com.tss.loanEmiSchedular.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final EmailService emailService;
    private final TemplateEngine templateEngine;

    @Override
    public void sendLoanApplicationEmail(String email, Loan loan) {
        Context context=new Context();

        context.setVariable("name",loan.getBorrower().getUser().getEmail());
        context.setVariable("amount",loan.getLoanAmount());
        context.setVariable("tenure",loan.getTenure());

        String body= templateEngine.process("loan-application",context);

        emailService.sendEmail(email,"Loan Application Submitted",body);


    }

    @Override
    public void sendLoanDecisionEmail(String email, Loan loan) {

        Context context = new Context();

        context.setVariable("name", loan.getBorrower().getUser().getEmail());
        context.setVariable("amount", loan.getLoanAmount());
        context.setVariable("status", loan.getStatus());

        String body = templateEngine.process("loan-decision", context);

        emailService.sendEmail(
                email,
                "Loan Application Status Update",
                body
        );
    }

    @Override
    public void sendPaymentReminder(String email, Emi emi) {

        Context context = new Context();

        context.setVariable("name", emi.getLoan().getBorrower().getUser().getEmail());
        context.setVariable("amount", emi.getTotalDueAmount());
        context.setVariable("dueDate", emi.getDueDate());

        String body = templateEngine.process("payment-reminder", context);

        emailService.sendEmail(
                email,
                "Payment Reminder for Your EMI",
                body
        );
    }

    @Override
    public void sendOverdueAlert(String email, Emi emi) {

        Context context = new Context();

        context.setVariable("name", emi.getLoan().getBorrower().getUser().getEmail());
        context.setVariable("amount", emi.getTotalDueAmount());
        context.setVariable("dueDate", emi.getDueDate());

        String body = templateEngine.process("overdue-alert", context);

        emailService.sendEmail(
                email,
                "EMI Overdue Alert",
                body
        );
    }
}
