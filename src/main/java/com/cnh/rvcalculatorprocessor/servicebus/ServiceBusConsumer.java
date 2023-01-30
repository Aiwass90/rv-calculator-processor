package com.cnh.rvcalculatorprocessor.servicebus;

import com.azure.core.amqp.AmqpRetryMode;
import com.azure.core.amqp.AmqpRetryOptions;
import com.azure.messaging.servicebus.*;
import com.cnh.rvcalculatorprocessor.dto.QlikSenseResponseDTO;
import com.cnh.rvcalculatorprocessor.exception.ExceptionFunction;

import com.cnh.rvcalculatorprocessor.function.RVCalculatorProcessorAzureHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import org.springframework.stereotype.Component;

import static com.cnh.rvcalculatorprocessor.util.Constants.*;
import static com.cnh.rvcalculatorprocessor.util.ConverterUtils.objToJson;

@Component
public class ServiceBusConsumer {

    private static Logger logger = LoggerFactory.getLogger(RVCalculatorProcessorAzureHandler.class);

    public void sendMessage(QlikSenseResponseDTO qlikSenseResponse, String sessionId) throws ExceptionFunction {

        try {
            logger.info("QlikSense Response: \n" + qlikSenseResponse);
            logger.info("Sending QlikSense Response to the response queue: " + RESPONSE_QUEUE);

            Duration delay = Duration.ofMillis(2000);
            AmqpRetryOptions retry = new AmqpRetryOptions();
            retry.setMaxRetries(3);
            retry.setDelay(delay);
            retry.setMode(AmqpRetryMode.EXPONENTIAL);

            ServiceBusClientBuilder builder = new ServiceBusClientBuilder()
                    .connectionString(SERVICE_BUS_CONNECTION_STRING)
                    .retryOptions(retry);
            ServiceBusSenderClient sender = builder
                    .sender()
                    .queueName(RESPONSE_QUEUE)
                    .buildClient();

            String responseStr = objToJson(qlikSenseResponse);

            sender.sendMessage(new ServiceBusMessage(responseStr).setSessionId(sessionId));
            sender.close();


        System.out.println("Message sent to the response queue...");

        } catch (Exception e) {
            logger.error("sendMessage method error:");
            logger.error(e.getMessage());
            sendErrorMessage(sessionId, e.getMessage());
            throw new ExceptionFunction(e.getMessage(), e);
        }
    }

    public void sendErrorMessage(String sessionId, String errorMsg) throws ExceptionFunction {

        try {

            logger.info("Sending error message to response queue...");

            Duration delay = Duration.ofMillis(2000);
            AmqpRetryOptions retry = new AmqpRetryOptions();
            retry.setMaxRetries(2);
            retry.setDelay(delay);
            retry.setMode(AmqpRetryMode.EXPONENTIAL);

            ServiceBusClientBuilder builder = new ServiceBusClientBuilder()
                    .connectionString(SERVICE_BUS_CONNECTION_STRING)
                    .retryOptions(retry);
            ServiceBusSenderClient sender = builder
                    .sender()
                    .queueName(RESPONSE_QUEUE)
                    .buildClient();

            String error = ERROR.concat(" ".concat(errorMsg));

            sender.sendMessage(new ServiceBusMessage(error).setSessionId(sessionId));
            sender.close();


            System.out.println("Error message sent to the response queue...");

        } catch (Exception e) {
            logger.error("sendErrorMessage method error:");
            logger.error(e.getMessage());
        }
    }

    public void sessionReceiverSingleInstantiation() {
        // BEGIN: com.azure.messaging.servicebus.servicebusreceiverclient.instantiation#nextsession
        // The connectionString/sessionQueueName must be set by the application. The 'connectionString' format is shown below.
        // "Endpoint={fully-qualified-namespace};SharedAccessKeyName={policy-name};SharedAccessKey={key}"
        ServiceBusSessionReceiverClient sessionReceiver = new ServiceBusClientBuilder()
                .connectionString(
                        SERVICE_BUS_CONNECTION_STRING)
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
