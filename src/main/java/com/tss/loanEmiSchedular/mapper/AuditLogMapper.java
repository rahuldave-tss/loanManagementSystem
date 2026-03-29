package com.tss.loanEmiSchedular.mapper;

import com.tss.loanEmiSchedular.dto.response.AuditLogResponseDto;
import com.tss.loanEmiSchedular.entity.AuditLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {

    @Mapping(source = "loan.id", target = "loanId")

    @Mapping(
            target = "performedBy",
            expression = "java(auditLog.getPerformedBy() != null ? auditLog.getPerformedBy().getEmail() : \"SYSTEM\")"
    )

    @Mapping(source = "emi.id", target = "emiId")
    @Mapping(source = "createdAt", target = "timestamp")

    AuditLogResponseDto toResponseDto(AuditLog auditLog);
}