package com.cnh.rvcalculatorprocessor.util;

import com.cnh.rvcalculatorprocessor.dto.FinancePlusRequestDTO;

import com.cnh.rvcalculatorprocessor.dto.QlikSenseResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConverterUtils {

    public static String objToJson(Object obj) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(obj);

        return json;
    }

    public static QlikSenseResponseDTO stubQlikResponse(){

        QlikSenseResponseDTO response = QlikSenseResponseDTO.builder()
                .capitalBaseRV(22.6)
                .dealerGuarantee(66.6)
                .manufactersGuaranty(44.6)
                .causticUsageDeduct(11.3)
                .finalLeaseRV(44.2)
                .finalMainUnitRV(55.79)
                .totalAttachment(7.0)
                .capitalBaseRV(77.7)
                .additionalDealerGuarantee(56.4)
                .build();

        return response;
    }

}
