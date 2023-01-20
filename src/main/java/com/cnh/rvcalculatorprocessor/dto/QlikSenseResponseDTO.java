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

    private Double capitalBaseResidual;
    private Double mfgGuarantee;
    private Double dealerGuarantee;
    private Double additionalDealerGuarantee;
}
