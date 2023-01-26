package com.cnh.rvcalculatorprocessor.function;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusReceiverClient;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.cnh.rvcalculatorprocessor.dto.RequestDTO;
import com.microsoft.azure.servicebus.Message;
import org.springframework.cloud.function.adapter.azure.FunctionInvoker;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;

import java.util.UUID;


public class RVCalculatorProcessorAzureHandler extends FunctionInvoker<RequestDTO<String>, OutputBinding<String>> {


    @FunctionName("rvCalculatorProcessor")
    public OutputBinding<String> serviceBusProcess(
            @ServiceBusQueueTrigger(name = "message",
                    queueName = "rvcalculationrequestqueue",
                    connection = "AccountingServiceBusConnection", isSessionsEnabled = true) String message,
            @BindingName("SessionId") String sessionId,
            @QueueOutput(name = "output",
                    queueName = "rvcalculationresponsequeue",
                    connection = "AccountingServiceBusConnection") OutputBinding<String> output,
            final ExecutionContext context
    ) {

        context.getLogger().info("Session ID Processor: " + sessionId);
        context.getLogger().info("Calling Azure Function Receiver RV Calculator Processor...");
        ServiceBusClientBuilder builder = new ServiceBusClientBuilder()
                .connectionString("Endpoint=sb://eufstsvcbusd01.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=TbsW8lrsJyUf0fb0JIqOen8UPddp4u4yxiM752hAD6M=");
        ServiceBusSenderClient sender = builder
                .sender()
                .queueName("rvcalculationresponsequeue")
                .buildClient();

        context.getLogger().info("Calling Azure Function Receiver RV Calculator Processor...");
        sender.sendMessage(new ServiceBusMessage("The new Response by Processor is: " + message).setSessionId(sessionId));
        sender.close();

        /*ServiceBusReceiverClient receiver = new ServiceBusClientBuilder()
                .connectionString("Endpoint=sb://eufstsvcbusd01.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=TbsW8lrsJyUf0fb0JIqOen8UPddp4u4yxiM752hAD6M=")
                .receiver()
                .queueName("rvcalculationrequestqueue")
                .buildClient();*/


        /*System.out.println("ServiceBusMessage Session ID: " + messaggio.getSessionId());
        System.out.println("ServiceBusMessage Body: " + messaggio.getMessageBody());
        output.setValue("ServiceBusMessage Body: " + messaggio.getMessageBody());*/

        return output;
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
