package com.tss.loanEmiSchedular.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class LoanTypeResponse {
    private final String type;
    private final double rate;
}
