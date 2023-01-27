package com.cnh.rvcalculatorprocessor.function;


import com.cnh.rvcalculatorprocessor.dto.FinancePlusRequestDTO;
import com.cnh.rvcalculatorprocessor.dto.QlikSenseResponseDTO;
import com.cnh.rvcalculatorprocessor.dto.RequestDTO;
import com.cnh.rvcalculatorprocessor.dto.ResponseBuilder;
import com.cnh.rvcalculatorprocessor.exception.ExceptionResponse;
import com.cnh.rvcalculatorprocessor.servicebus.ServiceBusConsumer;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.HttpStatusType;
import com.microsoft.azure.functions.OutputBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.adapter.azure.FunctionInvoker;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static com.cnh.rvcalculatorprocessor.util.Constants.APPLICATION_JSON;
import static com.cnh.rvcalculatorprocessor.util.Constants.CONTENT_TYPE;


@Component
public class RVCalculatorProcessor implements Function<RequestDTO<String>, OutputBinding<String>> {


    @Autowired
    ServiceBusConsumer serviceBusConsumer;


    /*@Bean("rvCalculatorProcessor")
    public Function<RequestDTO<String>, OutputBinding<String>> sendRequest() {
        // 1.0 triggerare la funzione Azure 'RV Calculator Processor' attraverso il service bus
        QlikSenseResponseDTO responsedto = new QlikSenseResponseDTO();

        return response -> null;
    }*/

    @Override
    public OutputBinding<String> apply(RequestDTO<String> output) {

        // 1.0 send the request on RequestQueue
        System.out.println("Sending message from Producer...");
        System.out.println(output.toString());
        // decommentare
        try {
            //serviceBusConsumer.receiveMessage(output);
        } catch (Exception e) {
            System.out.println(e.getMessage()); // print log...
            //return new ResponseBuilder().status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApplicationException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.name())).build();
        }
        // 2.0 se non riesco a trovare la soluzione request/reply con le librerie Azure allora seguo il passo 3.0

        // 3.0 after sending message I listen on the ResponseQueue
        QlikSenseResponseDTO response = QlikSenseResponseDTO.builder()
                .capitalBaseRV(22.6)
                .dealerGuarantee(66.6)
                .manufactersGuaranty(44.6).build();


        // response di esempio
        return null;
    }
}
