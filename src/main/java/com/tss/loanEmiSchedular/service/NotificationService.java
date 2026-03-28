package com.tss.loanEmiSchedular.service;

import com.tss.loanEmiSchedular.entity.Emi;
import com.tss.loanEmiSchedular.entity.Loan;

public interface NotificationService {
    void sendLoanApplicationEmail(String email, Loan loan);

    void sendLoanDecisionEmail(String email, Loan loan);

    void sendPaymentReminder(String email, Emi emi);

    void sendOverdueAlert(String email, Emi emi);
}
