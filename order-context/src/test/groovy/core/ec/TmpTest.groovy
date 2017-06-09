package core.ec


import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TmpTest{

    @Autowired
    private TestRestTemplate restTemplate

    @Test
    public void name() throws Exception {
        def entity = new HttpEntity<String>("""
{
"name":"ok1",
"age":100,
"address":{"province":"hebei","city":"baoding","district":"lianchi","address":"hongxing road No.1","zipCode":"071000","contactNumber":"13000000000","receiver":"zhaoqiang"},
"cartItems":[{"product":{"price":100.10,"productId":"p01","productName":"product01"},"quantity":1},{"product":{"price":200.60,"productId":"p02","productName":"product02"},"quantity":2}]
}

""", new HttpHeaders(contentType: MediaType.APPLICATION_JSON_UTF8))
        def response = restTemplate.postForObject("/order", entity, String)
        println(response)
    }


}