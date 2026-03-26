package com.tss.loanEmiSchedular.util;

import com.tss.loanEmiSchedular.entity.FinancialProfile;
import com.tss.loanEmiSchedular.repository.FinancialProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final FinancialProfileRepository repo;

    @Override
    public void run(String... args) {

        saveIfNotExists("ABCDE1234F", 50000, 10000, 750);
        saveIfNotExists("PQRSX5678L", 60000, 15000, 720);
        saveIfNotExists("LMNOP4321Z", 40000, 5000, 680);
        saveIfNotExists("WXYZT9876K", 80000, 20000, 790);
        saveIfNotExists("GHJKL2468M", 55000, 12000, 710);
        saveIfNotExists("QWERT1122Y", 70000, 25000, 760);
        saveIfNotExists("ZXCVB3344N", 45000, 8000, 690);
        saveIfNotExists("ASDFG5566H", 90000, 30000, 810);
        saveIfNotExists("POIUY7788J", 30000, 5000, 650);
        saveIfNotExists("LKJHG9900P", 65000, 18000, 730);
    }

    private void saveIfNotExists(String pan, double income, double debt, int score) {
        String hashedPan = PanHashUtil.hashPan(pan);

        if (!repo.existsById(hashedPan)) {
            FinancialProfile fp = new FinancialProfile();
            fp.setPan(hashedPan);
            fp.setMonthlyIncome(BigDecimal.valueOf(income));
            fp.setExistingDebt(BigDecimal.valueOf(debt));
            fp.setCreditScore(score);
            repo.save(fp);
        }
    }
}
