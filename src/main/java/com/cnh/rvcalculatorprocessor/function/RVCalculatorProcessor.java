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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static com.cnh.rvcalculatorprocessor.util.Constants.APPLICATION_JSON;
import static com.cnh.rvcalculatorprocessor.util.Constants.CONTENT_TYPE;


@Component
public class RVCalculatorProcessor implements Function<RequestDTO<FinancePlusRequestDTO>, HttpResponseMessage>{


    @Autowired
    ServiceBusConsumer serviceBusConsumer;
    /*@Override
    public Mono<QlikSenseResponseDTO> apply(Mono<FinancePlusRequestDTO> financePlusRequestDTOMono) {
        // creare la logica
        // 1.0 passare per il service bus
        // 2.0 chiamare RV Calculator Processor
        sendMessage(financePlusRequestDTOMono);
        return null;
    }

    @Bean("rvCalculator")
    public Function<HttpRequestMessage<Optional<FinancePlusRequestDTO>>, HttpResponseMessage> sendRequest() {
        // 1.0 triggerare la funzione Azure 'RV Calculator Processor' attraverso il service bus
        QlikSenseResponseDTO responsedto = new QlikSenseResponseDTO();
        responsedto.setCapitalBaseResidual(22.6);
        return response -> response.createResponseBuilder(HttpStatus.ACCEPTED).body(responsedto).build();
    } */

    @Override
    public HttpResponseMessage apply(RequestDTO<FinancePlusRequestDTO> financePlusRequestDTO) {

        // 1.0 send the request on RequestQueue
        System.out.println("Sending message from Producer...");
        FinancePlusRequestDTO request = financePlusRequestDTO.getRequestMessage().getBody().get();
        // decommentare
        try {
            serviceBusConsumer.receiveMessage(request);
        } catch (Exception e) {
            System.out.println(e.getMessage()); // print log...
            return new ResponseBuilder().status(HttpStatusType.custom(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .header(CONTENT_TYPE, APPLICATION_JSON).body(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage())).build();
            //return new ResponseBuilder().status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApplicationException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.name())).build();
        }
        // 2.0 se non riesco a trovare la soluzione request/reply con le librerie Azure allora seguo il passo 3.0

        // 3.0 after sending message I listen on the ResponseQueue
        QlikSenseResponseDTO response = QlikSenseResponseDTO.builder()
                .capitalBaseRV(22.6)
                .dealerGuarantee(66.6)
                .manufactersGuaranty(44.6).build();

        // response di esempio
        return new ResponseBuilder().header(CONTENT_TYPE, APPLICATION_JSON).status(HttpStatus.OK).body(response).build();
    }
}
