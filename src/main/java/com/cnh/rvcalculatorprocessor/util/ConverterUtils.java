package com.cnh.rvcalculatorprocessor.util;

import com.cnh.rvcalculatorprocessor.dto.FinancePlusRequestDTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConverterUtils {

    public static String objToJson(FinancePlusRequestDTO obj) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(obj);

        return json;
    }

}
