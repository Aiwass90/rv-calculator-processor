package com.cnh.rvcalculatorprocessor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QlikSenseResponseDTO {

    private Double capitalBaseRV;
    private Double manufactersGuaranty; // Integer or Double?
    private Double dealerGuarantee;
    private Double additionalDealerGuarantee;
    private Double causticUsageDeduct;
    private Double dealerStatusDeduct;
    private Double ppp;
    private Double cNHMaintenanceContract;
    private Double finalMainUnitRV;
    private Double totalAttachment;
    private Double finalLeaseRV;

}
