package com.cnh.rvcalculatorprocessor.servicebus;

import com.azure.core.amqp.models.AmqpMessageBody;
import com.azure.messaging.servicebus.*;
import com.cnh.rvcalculatorprocessor.dto.FinancePlusRequestDTO;
import com.cnh.rvcalculatorprocessor.exception.ApplicationException;

import reactor.core.Disposable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Component;

import static com.cnh.rvcalculatorprocessor.util.ConverterUtils.objToJson;

@Component
public class ServiceBusConsumer {

    String connectionString = System.getenv("");
    String queueName = System.getenv("rvcalculationrequestqueue");
    String sessionQueueName = System.getenv("AZURE_SERVICEBUS_SAMPLE_SESSION_QUEUE_NAME");

    // IMPORTANTE!!!! impostare il metodo di ricezione e poi di reinvio a RVCalculatorRestAPI
    public void receiveMessage(FinancePlusRequestDTO request) throws ApplicationException {
        // create a Service Bus Sender client for the queue
        try {

            ServiceBusReceiverClient receiver = new ServiceBusClientBuilder()
                    .connectionString(connectionString)
                    .receiver()
                    .queueName(queueName)
                    .buildClient();



        System.out.println("Sent a single message to the queue: " + queueName);

        } catch (Exception e) {
            throw new ApplicationException(e.getMessage(), e);
        }
    }

    public void sessionReceiverSingleInstantiation() {
        // BEGIN: com.azure.messaging.servicebus.servicebusreceiverclient.instantiation#nextsession
        // The connectionString/sessionQueueName must be set by the application. The 'connectionString' format is shown below.
        // "Endpoint={fully-qualified-namespace};SharedAccessKeyName={policy-name};SharedAccessKey={key}"
        ServiceBusSessionReceiverClient sessionReceiver = new ServiceBusClientBuilder()
                .connectionString(
                        "Endpoint={fully-qualified-namespace};SharedAccessKeyName={policy-name};SharedAccessKey={key}")
                .sessionReceiver()
                .queueName("<< QUEUE NAME >>")
                .buildClient();
        ServiceBusReceiverClient receiver = sessionReceiver.acceptNextSession();

        // Use the receiver and finally close it along with the sessionReceiver.
        receiver.close();
        sessionReceiver.close();
        // END: com.azure.messaging.servicebus.servicebusreceiverclient.instantiation#nextsession
    }


}
