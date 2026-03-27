package com.tss.loanEmiSchedular.repository;

import com.tss.loanEmiSchedular.entity.BorrowerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowerRepository extends JpaRepository<BorrowerProfile,Integer> {
}
