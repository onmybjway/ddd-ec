package core.ec.order.unit

import core.ec.application.IOrderService
import core.ec.port.rest.OrderController
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner::class)
@WebMvcTest(OrderController::class)
class OrderControllerTests {

    @Autowired
    lateinit var mvc: MockMvc


    @MockBean
    lateinit var orderService: IOrderService

    @Test
    fun name() {
        mvc.perform(get("/order/hi")).andExpect(status().isOk)
    }
}