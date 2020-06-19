package tada;


import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

//import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import tada.config.kafka.KafkaProcessor;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

//import org.springframework.kafka.test.context.EmbeddedKafka;

@RunWith(SpringRunner.class)    // junit4 와 springboot 를 연결해준다.
// 통합테스트
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@EmbeddedKafka  // 테스트시 kafka 를 테스트용으로 돌리기 위하여 필요
// 테스트를 여러번 수행하면 applicationContext 가 변경될수 있는데,
// 기존것을 폐기하고 새로운 applicationContext를 생성하여 컨텍스트를 공유하지 않도록 설정함
// DB test 나 kafka 테스트시 설정을 해줘야함
@DirtiesContext
// test 를 할때 test resource 에 application.yaml 을 만드는 방법도 있지만, 하나의 파일에서 관리하고 싶거나,
// 특정 test 시 특정 Profiles 을 load 하고 싶지 않을때 설정하여 사용한다.
@ActiveProfiles("test")
@AutoConfigureMessageVerifier
public class ContractVerifierBase {

    @MockBean
    PaymentRepository paymentRepository;

    @Autowired
    WebApplicationContext webApplicationContext;

    @LocalServerPort
    int port;


    @Before
    public void setup() {
        //remove::start[]
        RestAssuredMockMvc.webAppContextSetup(this.webApplicationContext);

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = this.port;

        Payment payment = new Payment();
        payment.setPaymentId(1L);
        payment.setCharge(3000);
        payment.setPaymentStatus("TEST");


        given(this.paymentRepository.findByCallId(any())).willReturn(payment);
        Mockito.when(this.paymentRepository.findByCallId(any())).thenReturn(payment);
    }
}