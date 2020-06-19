package tada;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

 @RestController
 public class PaymentController {
  @Autowired
  PaymentRepository paymentRepository;

  @GetMapping("/payments/{callId}")
  public Payment returnPayment(@PathVariable("callId") Long callId) {
   return paymentRepository.findByCallId(callId);
  }
 }

