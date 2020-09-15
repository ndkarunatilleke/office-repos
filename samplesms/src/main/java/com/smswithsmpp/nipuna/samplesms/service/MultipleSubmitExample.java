package com.smswithsmpp.nipuna.samplesms.service;

import lombok.extern.slf4j.Slf4j;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.*;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.util.AbsoluteTimeFormatter;
import org.jsmpp.util.TimeFormatter;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
public class MultipleSubmitExample {

    private static final TimeFormatter TIME_FORMATTER = new AbsoluteTimeFormatter();

    private final String smppIp = "222.165.140.145";

    private int port = 5020;

    private final String username = "HELAVIRU";

    private final String password = "helaviru";

    private final String address = "8023";

    private static final String SERVICE_TYPE = "CMT";

    public void broadcastMessage(String message, List numbers) {
        log.info("Broadcasting sms");
        SubmitMultiResult result = null;
        Address[] addresses = prepareAddress(numbers);
        SMPPSession session = initSession();
        if(session != null) {
            try {
//                result = session.submitMultiple(SERVICE_TYPE,
//                        TypeOfNumber.NATIONAL, NumberingPlanIndicator.UNKNOWN, address,
//                        addresses, new ESMClass(), (byte) 0, (byte) 1, TIME_FORMATTER.format(new Date()), null,
//                        new RegisteredDelivery(SMSCDeliveryReceipt.FAILURE), ReplaceIfPresentFlag.REPLACE,
//                        new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, false), (byte) 0,
//                        message.getBytes());

                String messageId = session.submitShortMessage(SERVICE_TYPE,
                        TypeOfNumber.NATIONAL, NumberingPlanIndicator.UNKNOWN, address,
                        TypeOfNumber.NATIONAL, NumberingPlanIndicator.UNKNOWN, numbers.get(0).toString(),
                        new ESMClass(), (byte)0, (byte)1,  TIME_FORMATTER.format(new Date()), null,
                        new RegisteredDelivery(SMSCDeliveryReceipt.FAILURE), (byte)0, new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, false), (byte)0,
                        message.getBytes());

                log.info("Messages submitted, result is {}", messageId);
                Thread.sleep(1000);
            } catch (PDUException e) {
                log.error("Invalid PDU parameter", e);
            } catch (ResponseTimeoutException e) {
                log.error("Response timeout", e);
            } catch (InvalidResponseException e) {
                log.error("Receive invalid response", e);
            } catch (NegativeResponseException e) {
                log.error("Receive negative response", e);
            } catch (IOException e) {
                log.error("I/O error occured", e);
            } catch (Exception e) {
                log.error("Exception occured submitting SMPP request", e);
            }
        }else {
            log.error("Session creation failed with SMPP broker.");
        }
        if(result != null && result.getUnsuccessDeliveries() != null && result.getUnsuccessDeliveries().length > 0) {
            log.error(DeliveryReceiptState.getDescription(result.getUnsuccessDeliveries()[0].getErrorStatusCode()).description() + " - " +result.getMessageId());
        }else {
            log.info("Pushed message to broker successfully");
        }
        if(session != null) {
            session.unbindAndClose();
        }
    }

    private Address[] prepareAddress(List numbers) {
        Address[] addresses = new Address[numbers.size()];
        for(int i = 0; i< numbers.size(); i++){
            addresses[i] = new Address(TypeOfNumber.NATIONAL, NumberingPlanIndicator.UNKNOWN, (String) numbers.get(i));
        }
        return addresses;
    }

    private SMPPSession initSession() {
        SMPPSession session = new SMPPSession();
        try {
            session.setMessageReceiverListener(new MessageReceiverListenerImpl());
            String systemId = session.connectAndBind("222.165.140.145", Integer.valueOf(5020), new BindParameter(BindType.BIND_TX, "HELAVIRU", "helaviru", "session.bind_transceiver", TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, null));
            log.info("Connected with SMPP with system id {}", systemId);
        } catch (IOException e) {
            log.error("I/O error occured", e);
            session = null;
        }
        return session;
    }
}
