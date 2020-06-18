package tada;

import javax.persistence.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;
import tada.config.kafka.KafkaProcessor;

import java.util.List;

@Entity
@Table(name="Payment_table")
public class Payment {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long paymentId;
    private Integer charge;
    private String paymentStatus;
    private Long callId;

    @PostPersist
    public void onPostPersist(){
        PaymentDone paymentDone = new PaymentDone();
        paymentDone.setPaymentId(this.getPaymentId());
        paymentDone.setCharge(this.getCharge());
        paymentDone.setPaymentStatus(this.getPaymentStatus());
        paymentDone.setCallId(this.getCallId());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        try {
            json = objectMapper.writeValueAsString(paymentDone);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }

        KafkaProcessor processor = Application.applicationContext.getBean(KafkaProcessor.class);
        MessageChannel outputChannel = processor.outboundTopic();

        outputChannel.send(MessageBuilder
                .withPayload(json)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }

    @PostUpdate
    public void onPostUpdate() {
        PaymentCanceled paymentCanceled = new PaymentCanceled();
        paymentCanceled.setPaymentId(this.getPaymentId());
        paymentCanceled.setCharge(this.getCharge());
        paymentCanceled.setCallId(this.getCallId());
        paymentCanceled.setPaymentStatus("Canceled");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        try {
            json = objectMapper.writeValueAsString(paymentCanceled);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }

        KafkaProcessor processor = Application.applicationContext.getBean(KafkaProcessor.class);
        MessageChannel outputChannel = processor.outboundTopic();

        outputChannel.send(MessageBuilder
                .withPayload(json)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }


    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }
    public Integer getCharge() {
        return charge;
    }

    public void setCharge(Integer charge) {
        this.charge = charge;
    }
    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    public Long getCallId() {
        return callId;
    }

    public void setCallId(Long callId) {
        this.callId = callId;
    }




}
