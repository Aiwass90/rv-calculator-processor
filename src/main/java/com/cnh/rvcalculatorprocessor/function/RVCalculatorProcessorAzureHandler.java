package com.cnh.rvcalculatorprocessor.function;

import com.cnh.rvcalculatorprocessor.dto.QlikSenseResponseDTO;
import com.cnh.rvcalculatorprocessor.dto.RequestDTO;
import com.cnh.rvcalculatorprocessor.exception.ExceptionFunction;
import com.cnh.rvcalculatorprocessor.servicebus.ServiceBusConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.adapter.azure.FunctionInvoker;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import org.springframework.stereotype.Component;

import static com.cnh.rvcalculatorprocessor.util.Constants.*;
import static com.cnh.rvcalculatorprocessor.util.ConverterUtils.stubQlikResponse;


public class RVCalculatorProcessorAzureHandler extends FunctionInvoker<RequestDTO<String>, OutputBinding<String>> {

    ServiceBusConsumer serviceBusConsumer = new ServiceBusConsumer();

    @FunctionName("rvCalculatorProcessor")
    public void serviceBusProcess(
            @ServiceBusQueueTrigger(name = "message",
                    queueName = REQUEST_QUEUE,
                    connection = "ServiceBusConnection", isSessionsEnabled = true) String message,
            @BindingName("SessionId") String sessionId,
            @QueueOutput(name = "output",
                    queueName = RESPONSE_QUEUE,
                    connection = "ServiceBusConnection") OutputBinding<String> output,
            final ExecutionContext context
    )  {
        context.getLogger().info("START operations in RV Calculator Processor Function...");
        context.getLogger().info("Session ID Processor: " + sessionId);
        // contact QlikSense App and then send the response to the response queue
        try {
            // Sending QlikSense response to response queue...
            serviceBusConsumer.sendMessage(stubQlikResponse(), sessionId);
        } catch (ExceptionFunction e) {
            output.setValue(e.getMessage());
        }

    }


/*@FunctionName("httpToServiceBusQueue")
    @ServiceBusQueueOutput(name = "message", queueName = "myqueue", connection = "AzureServiceBusConnection")
    public String pushToQueue(
            @HttpTrigger(name = "request", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS)
            final FinancePlusRequestDTO req,
            @HttpOutput(name = "response") final OutputBinding<FinancePlusRequestDTO> result ) {
        result.setValue(message + " has been sent.");
        return message;
    }*/
}
