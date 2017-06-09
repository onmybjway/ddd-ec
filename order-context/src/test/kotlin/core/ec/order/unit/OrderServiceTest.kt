package core.ec.order.unit

import core.ec.application.*
import core.ec.application.exceptions.*
import core.ec.domain.model.Member
import core.ec.domain.model.Order
import core.ec.domain.model.OrderRepository
import core.ec.domain.model.Product
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Matchers
import org.mockito.Mockito.*
import org.springframework.test.context.junit4.SpringRunner
import java.util.*

@RunWith(SpringRunner::class)
class OrderServiceTest {


    private val createCmd = OrderCreateCommand(
            1,
            OrderCreateCommand.ShippingAddress("hebei",
                    "baoding", "lianchi", "hongxing road No.1",
                    "071000", "13000000000", "zhaoqiang"),
            setOf(
                    OrderCreateCommand.CartItem("product1", 9.99, 1),
                    OrderCreateCommand.CartItem("product2", 9.99, 2)
            ),
            "<h1>this is remark</h1>"
    )

    @Test
    fun create_should_success() {
        //arrange
        val memberService = mock(IMemberService::class.java)
        `when`(memberService.getByMemberId(1)).thenReturn(Optional.of(Member(1, "ok")))

        val productService = mock(IProductService::class.java)
        given(productService.getByProductId(Matchers.anyString()))
                .willAnswer({ Optional.of(Product(it.arguments[0] as String, "product", 9.99)) })

        val orderRepository = mock(OrderRepository::class.java)
        given(orderRepository.save(any(Order::class.java))).willAnswer({ it.arguments[0] })

        //act
        val orderService = OrderService(orderRepository, memberService, productService)
        val result = orderService.create(createCmd)

        //assert
        assertThat(result).isNotBlank()
    }

    @Test
    fun create_should_fail_when_member_not_found() {
        //arrange
        val memberService = mock(IMemberService::class.java)
        `when`(memberService.getByMemberId(1)).thenReturn(Optional.ofNullable(null))

        val productService = mock(IProductService::class.java)
        given(productService.getByProductId(Matchers.anyString()))
                .willAnswer({ Optional.of(Product(it.arguments[0] as String, "product", 9.99)) })

        val orderRepository = mock(OrderRepository::class.java)
        given(orderRepository.save(any(Order::class.java))).willAnswer({ it.arguments[0] })

        //act
        val orderService = OrderService(orderRepository, memberService, productService)

        //assert
        assertThatThrownBy { orderService.create(createCmd) }.isInstanceOf(MemberNotFoundException::class.java)
    }

    @Test
    fun create_should_fail_when_product_not_found() {
        //arrange
        val memberService = mock(IMemberService::class.java)
        `when`(memberService.getByMemberId(1)).thenReturn(Optional.of(Member(1, "ok")))

        val productService = mock(IProductService::class.java)
        given(productService.getByProductId(Matchers.anyString())).willReturn(Optional.ofNullable(null))

        val orderRepository = mock(OrderRepository::class.java)
        given(orderRepository.save(any(Order::class.java))).willAnswer({ it.arguments[0] })

        //act
        val orderService = OrderService(orderRepository, memberService, productService)

        //assert
       assertThatThrownBy { orderService.create(createCmd) }.isInstanceOf(ProductNotFoundException::class.java)
    }

    @Test
    fun create_should_fail_when_product_not_match() {
        //arrange
        val memberService = mock(IMemberService::class.java)
        `when`(memberService.getByMemberId(1)).thenReturn(Optional.of(Member(1, "ok")))

        val productService = mock(IProductService::class.java)
        given(productService.getByProductId(Matchers.anyString()))
                .willAnswer({ Optional.of(Product(it.arguments[0] as String, "product", 6.66)) })

        val orderRepository = mock(OrderRepository::class.java)
        given(orderRepository.save(any(Order::class.java))).willAnswer({ it.arguments[0] })

        //act
        val orderService = OrderService(orderRepository, memberService, productService)

        //assert
        assertThatThrownBy { orderService.create(createCmd) }.isInstanceOf(ProductNotMatchException::class.java)
    }
}