package tada;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import tada.ContractVerifierBase;

import static com.toomuchcoding.jsonassert.JsonAssertion.assertThatJson;
import static io.restassured.RestAssured.*;
import static org.springframework.cloud.contract.verifier.assertion.SpringCloudContractAssertions.assertThat;
import static org.springframework.cloud.contract.verifier.util.ContractVerifierUtil.*;

public class TadaTest extends ContractVerifierBase {

	@Test
	public void validate_drivingGet() throws Exception {
		// given:
			RequestSpecification request = given()
					.header("Content-Type", "application/json");

		// when:
			Response response = given().spec(request)
					.get("/payments/1");

		// then:
			assertThat(response.statusCode()).isEqualTo(200);
			assertThat(response.header("Content-Type")).matches("application/json.*");
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
		// and:
			assertThat(parsedJson.read("$.paymentId", String.class)).matches("[\\S\\s]+");
			assertThat(parsedJson.read("$.charge", String.class)).matches("[\\S\\s]+");
			assertThat(parsedJson.read("$.paymentStatus", String.class)).matches("[\\S\\s]+");
	}

}
