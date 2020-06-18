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
    
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverDrivingCreated_RequestPayment(@Payload DrivingCreated drivingCreated){

        if(drivingCreated.isMe()){
            System.out.println("##### listener RequestPayment : " + drivingCreated.toJson());
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverDrivingCanceled_RequestCancel(@Payload DrivingCanceled drivingCanceled){

        if(drivingCanceled.isMe()){
            System.out.println("##### listener RequestCancel : " + drivingCanceled.toJson());
        }
    }

}
