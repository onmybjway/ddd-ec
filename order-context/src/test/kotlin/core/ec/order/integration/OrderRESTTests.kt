package core.ec.order.integration

import core.ec.order.port.dto.Order
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderRESTTests {
    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun name() {
       val body="""
{
"name":"ok1",
"age":100,
"address":{"province":"hebei","city":"baoding","district":"lianchi","address":"hongxing road No.1","zipCode":"071000","contactNumber":"13000000000","receiver":"zhaoqiang"},
"cartItems":[{"product":{"price":100.10,"productId":"p01","productName":"product01"},"quantity":1},{"product":{"price":200.60,"productId":"p02","productName":"product02"},"quantity":2}]
}
"""

        val resp = restTemplate.postForObject("/order", buildRequest(body), String::class.java)
        println(resp)

    }

    @Test
    fun name2() {

        val resp = restTemplate.getForObject("/order/test-order",Order::class.java)
        println(resp)

    }

    private fun buildRequest(body:String):HttpEntity<String>{
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON_UTF8
        return HttpEntity<String>(body, headers)
    }


}