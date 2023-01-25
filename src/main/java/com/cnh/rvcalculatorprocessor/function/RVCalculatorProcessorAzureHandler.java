package com.cnh.rvcalculatorprocessor.function;


import ch.qos.logback.core.net.SyslogOutputStream;
import com.cnh.rvcalculatorprocessor.dto.FinancePlusRequestDTO;
import com.cnh.rvcalculatorprocessor.dto.QlikSenseResponseDTO;
import com.cnh.rvcalculatorprocessor.dto.RequestDTO;
import org.springframework.cloud.function.adapter.azure.FunctionInvoker;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import java.util.Optional;


public class RVCalculatorProcessorAzureHandler extends FunctionInvoker<RequestDTO<FinancePlusRequestDTO>, HttpResponseMessage> {


    @FunctionName("rvCalculatorProcessor")
    public void serviceBusProcess(
            @ServiceBusQueueTrigger(name = "message",
                    queueName = "rvcalculationrequestqueue",
                    connection = "AccountingServiceBusConnection") String message,
            @QueueOutput(name = "output",
                    queueName = "rvcalculationresponsequeue",
                    connection = "AccountingServiceBusConnection") OutputBinding<String> output,
            final ExecutionContext context
    ) {
        context.getLogger().info("Calling Azure Function Receiver RV Calculator Processor...");

        context.getLogger().info(message);

        output.setValue(message);
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
