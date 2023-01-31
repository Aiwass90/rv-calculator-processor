package com.cnh.rvcalculatorprocessor.function;


import com.cnh.rvcalculatorprocessor.conf.SecretConfig;
import com.cnh.rvcalculatorprocessor.dto.*;
import com.cnh.rvcalculatorprocessor.exception.ExceptionFunction;
import com.cnh.rvcalculatorprocessor.exception.ExceptionResponse;
import com.cnh.rvcalculatorprocessor.servicebus.ServiceBusConsumer;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.HttpStatusType;
import com.microsoft.azure.functions.OutputBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.adapter.azure.FunctionInvoker;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static com.cnh.rvcalculatorprocessor.util.Constants.APPLICATION_JSON;
import static com.cnh.rvcalculatorprocessor.util.Constants.CONTENT_TYPE;
import static com.cnh.rvcalculatorprocessor.util.ConverterUtils.stubQlikResponse;


@Component
public class RVCalculatorProcessor implements Function<RequestQueueDTO, OutputBinding<String>> {


    private static Logger logger = LoggerFactory.getLogger(RVCalculatorProcessor.class);
    @Autowired
    ServiceBusConsumer serviceBusConsumer;

    @Autowired
    SecretConfig secretConfig;

    @Override
    public OutputBinding<String> apply(RequestQueueDTO output) {

        // Get service bus connciton from secret
        //secretConfig.getStoredValue("Test");
        // 1.0 contact QlikSense App and then send the response to the response queue

        // 2.0 Sending QlikSense response to response queue...
        try {
            serviceBusConsumer.sendMessage(stubQlikResponse(), output.getSessionId());
        } catch (ExceptionFunction e) {
            logger.error(e.getMessage());
        }

        // 3.0 after sending message I listen on the ResponseQueue
        QlikSenseResponseDTO response = QlikSenseResponseDTO.builder()
                .capitalBaseRV(22.6)
                .dealerGuarantee(66.6)
                .manufactersGuaranty(44.6).build();


        // response di esempio
        return null;
    }
}
