package com.cnh.rvcalculatorprocessor.servicebus;

import com.azure.core.amqp.models.AmqpMessageBody;
import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusReceiverAsyncClient;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
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

import static com.cnh.rvcalculatorprocessor.util.ConverterUtils.objToJson;


public class ServiceBusSenderResponse {

    static String connectionString = "<NAMESPACE CONNECTION STRING>";
    static String queueName = "<QUEUE NAME>";

    // IMPORTANTE!!!! impostare il metodo di ricezione e poi di reinvio a RVCalculatorRestAPI
    public static void receiveMessage(FinancePlusRequestDTO request) throws ApplicationException {
        // create a Service Bus Sender client for the queue
        try {

            request.getBrand().toString();

        String sessionId = String.valueOf(UUID.randomUUID());

        ServiceBusSenderClient senderClient = new ServiceBusClientBuilder()
                .connectionString(connectionString)
                .sender()
                .queueName(queueName)
                .buildClient();

        // send one message to the queue
        senderClient.sendMessage(new ServiceBusMessage(objToJson(request)).setSessionId(sessionId));
        System.out.println("Sent a single message to the queue: " + queueName);

        } catch (Exception e) {
            throw new ApplicationException(e.getMessage(), e);
        }
    }

    static List<ServiceBusMessage> createMessages()
    {
        // create a list of messages and return it to the caller
        ServiceBusMessage[] messages = {
                new ServiceBusMessage("First message"),
                new ServiceBusMessage("Second message"),
                new ServiceBusMessage("Third message")
        };
        return Arrays.asList(messages);
    }

    // testare per vedere se request/reply pattern può andare bene
    public static void sendReceiveAmqpMessageBodySample() throws InterruptedException {

        AtomicBoolean sampleSuccessful = new AtomicBoolean(true);
        CountDownLatch countdownLatch = new CountDownLatch(1);

        // The connection string value can be obtained by:
        // 1. Going to your Service Bus namespace in Azure Portal.
        // 2. Go to "Shared access policies"
        // 3. Copy the connection string for the "RootManageSharedAccessKey" policy.

        // The 'connectionString' format is shown below.
        // 1. "Endpoint={fully-qualified-namespace};SharedAccessKeyName={policy-name};SharedAccessKey={key}"
        // 2. "<<fully-qualified-namespace>>" will look similar to "{your-namespace}.servicebus.windows.net"
        // 3. "queueName" will be the name of the Service Bus queue instance you created
        //    inside the Service Bus namespace.

        // At most, the receiver will automatically renew the message lock until 120 seconds have elapsed.
        ServiceBusClientBuilder builder = new ServiceBusClientBuilder()
                .connectionString(connectionString);

        ServiceBusReceiverAsyncClient receiver = builder
                .receiver()
                .queueName(queueName)
                .maxAutoLockRenewDuration(Duration.ofMinutes(2))
                .buildAsyncClient();

        ServiceBusSenderClient sender = builder
                .sender()
                .queueName(queueName)
                .buildClient();

        // Prepare the data to send as AMQP Sequence
        List<Object> list = new ArrayList<>();
        list.add(10L);
        list.add("The cost of this product is $10.");

        sender.sendMessage(new ServiceBusMessage(AmqpMessageBody.fromSequence(list)));

        // Now receive the data.
        Disposable subscription = receiver.receiveMessages()
                .subscribe(message -> {
                            AmqpMessageBody amqpMessageBody = message.getRawAmqpMessage().getBody();
                            // You should check the body type of the received message and call appropriate getter on AMQPMessageBody
                            switch (amqpMessageBody.getBodyType()) {
                                case SEQUENCE:
                                    amqpMessageBody.getSequence().forEach(payload -> {
                                        System.out.printf("Sequence #: %s, Body Type: %s, Contents: %s%n", message.getSequenceNumber(),
                                                amqpMessageBody.getBodyType(), payload);
                                    });
                                    break;
                                case VALUE:
                                    System.out.printf("Sequence #: %s, Body Type: %s, Contents: %s%n", message.getSequenceNumber(),
                                            amqpMessageBody.getBodyType(), amqpMessageBody.getValue());
                                    break;
                                case DATA:
                                    System.out.printf("Sequence #: %s. Contents: %s%n", message.getSequenceNumber(),
                                            message.getBody().toString());
                                    break;
                                default:
                                    System.out.println("Invalid message body type: " + amqpMessageBody.getBodyType());
                            }
                        },
                        error -> {
                            System.err.println("Error occurred while receiving message: " + error);
                            sampleSuccessful.set(false);
                        });

        // Subscribe is not a blocking call so we wait here so the program does not end.
        countdownLatch.await(30, TimeUnit.SECONDS);

        // Disposing of the subscription will cancel the receive() operation.
        subscription.dispose();

        // Close the receiver.
        receiver.close();
    }


}
