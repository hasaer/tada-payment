package tada;

import tada.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{

    @Autowired
    PaymentRepository paymentRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void onEvent(@Payload String message) { }
    
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverDrivingCreated_RequestPayment(@Payload DrivingCreated drivingCreated){

        if(drivingCreated.isMe()){
            //System.out.println("##### listener RequestPayment : " + drivingCreated.toJson());
            Payment payment = new Payment();
            payment.setCallId(drivingCreated.getCallId());
            payment.setCharge(drivingCreated.getCharge());
            payment.setPaymentStatus("Payed");
            paymentRepository.save(payment);
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverDrivingCanceled_RequestCancel(@Payload DrivingCanceled drivingCanceled){

        if(drivingCanceled.isMe()){
            //System.out.println("##### listener RequestCancel : " + drivingCanceled.toJson());
            Payment payment = paymentRepository.findByCallId(drivingCanceled.getCallId());
            payment.setCharge(drivingCanceled.getCharge());
            payment.setPaymentStatus("Canceled");
            paymentRepository.save(payment);
        }
    }

}
