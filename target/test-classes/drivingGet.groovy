

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'GET'
        url ('/payments/1')
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status 200
        body(
                paymentId: 1,
                charge: "20000",
                paymentStatus: "OKAY"
        )
        bodyMatchers {
            jsonPath('$.paymentId', byRegex(nonEmpty()).asLong())
            jsonPath('$.charge', byRegex(nonEmpty()).asInteger())
            jsonPath('$.paymentStatus', byRegex(nonEmpty()).asString())
        }
        headers {
            contentType(applicationJson())
        }
    }
}