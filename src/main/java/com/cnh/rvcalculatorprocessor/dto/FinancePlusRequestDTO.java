package com.cnh.rvcalculatorprocessor.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FinancePlusRequestDTO {

    private String country;
    private Integer leaseType;
    private String brand;
    private String usage;
    private Integer modelYear;

}
