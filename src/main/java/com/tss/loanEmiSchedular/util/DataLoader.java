package com.tss.loanEmiSchedular.util;

import com.tss.loanEmiSchedular.entity.FinancialProfile;
import com.tss.loanEmiSchedular.repository.FinancialProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final FinancialProfileRepository repo;

    @Override
    public void run(String... args) {

        saveIfNotExists("ABCDE1234F", "Amit Sharma", LocalDate.of(1990, 5, 12), BigDecimal.valueOf(50000), BigDecimal.valueOf(10000), 750);

        saveIfNotExists("PQRSX5678L", "Priya Verma", LocalDate.of(1988, 8, 24), BigDecimal.valueOf(60000), BigDecimal.valueOf(15000), 720);

        saveIfNotExists("LMNOP4321Z", "Rahul Mehta", LocalDate.of(1995, 3, 18), BigDecimal.valueOf(40000), BigDecimal.valueOf(5000), 680);

        saveIfNotExists("WXYZT9876K", "Sneha Iyer", LocalDate.of(1992, 11, 2), BigDecimal.valueOf(80000), BigDecimal.valueOf(20000), 790);

        saveIfNotExists("GHJKL2468M", "Karan Gupta", LocalDate.of(1987, 7, 9), BigDecimal.valueOf(55000), BigDecimal.valueOf(12000), 710);

        saveIfNotExists("QWERT1122Y", "Neha Kapoor", LocalDate.of(1993, 1, 27), BigDecimal.valueOf(70000), BigDecimal.valueOf(25000), 760);

        saveIfNotExists("ZXCVB3344N", "Vikas Singh", LocalDate.of(1991, 6, 14), BigDecimal.valueOf(45000), BigDecimal.valueOf(8000), 690);

        saveIfNotExists("ASDFG5566H", "Anjali Desai", LocalDate.of(1989, 9, 30), BigDecimal.valueOf(90000), BigDecimal.valueOf(30000), 810);

        saveIfNotExists("POIUY7788J", "Rohit Agarwal", LocalDate.of(1996, 4, 5), BigDecimal.valueOf(30000), BigDecimal.valueOf(5000), 650);

        saveIfNotExists("LKJHG9900P", "Pooja Nair", LocalDate.of(1994, 12, 21), BigDecimal.valueOf(65000), BigDecimal.valueOf(18000), 730);
    }

    private void saveIfNotExists(String pan, String name, LocalDate date, BigDecimal income, BigDecimal debt, int score) {
        String hashedPan = PanHashUtil.hashPan(pan);


        FinancialProfile fp = new FinancialProfile();
        fp.setName(name);
        fp.setDob(date);
        fp.setPan(hashedPan);
        fp.setMonthlyIncome(income);
        fp.setExistingDebt(debt);
        fp.setCreditScore(score);
        repo.save(fp);

    }
}
