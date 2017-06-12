package core.ec.order.unit

import core.ec.order.application.OrderCreateCommand
import core.ec.order.application.OrderService
import core.ec.order.domain.model.*
import core.ec.order.exceptions.*
import core.ec.order.port.IMemberService
import core.ec.order.port.IProductService
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
            memberId = 1,
            address = OrderCreateCommand.ShippingAddress("hebei",
                    "baoding", "lianchi", "hongxing road No.1",
                    "071000", "13000000000", "zhaoqiang"),
            cartItems = setOf(
                    OrderCreateCommand.CartItem("product1", 9.99, 1),
                    OrderCreateCommand.CartItem("product2", 9.99, 2)
            ),
            remark = "<h1>this is remark</h1>",
            technical = OrderCreateCommand.Technical("110.110.110.110", "server01")
    )

    @Test
    fun create_should_success() {
        //arrange
        val memberService = mock(IMemberService::class.java)
        `when`(memberService.getByMemberId(1)).thenReturn(Optional.of(Member(1, "ok", MemberStatus.ACTIVE)))

        val productService = mock(IProductService::class.java)
        productServiceSetup(productService) { Product(productId = it, productName = "product", thePrice = 9.99, theCost = 9.00, inStock = 999) }

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
        productServiceSetup(productService) { Product(productId = it, productName = "product", thePrice = 9.99, theCost = 9.00, inStock = 999) }

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
        `when`(memberService.getByMemberId(1)).thenReturn(Optional.of(Member(1, "ok", MemberStatus.ACTIVE)))

        val productService = mock(IProductService::class.java)
        productServiceSetup(productService) { Product(productId = it + "_hide", productName = "product", thePrice = 9.99, theCost = 9.00, inStock = 999) }

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
        `when`(memberService.getByMemberId(1)).thenReturn(Optional.of(Member(1, "ok", MemberStatus.ACTIVE)))

        val productService = mock(IProductService::class.java)
        productServiceSetup(productService) { Product(productId = it, productName = "product", thePrice = 6.66, theCost = 9.00, inStock = 999) }

        val orderRepository = mock(OrderRepository::class.java)
        given(orderRepository.save(any(Order::class.java))).willAnswer({ it.arguments[0] })

        //act
        val orderService = OrderService(orderRepository, memberService, productService)

        //assert
        assertThatThrownBy { orderService.create(createCmd) }.isInstanceOf(ProductNotMatchException::class.java)
    }

    @Test
    fun create_should_fail_when_product_under_stock() {
        //arrange
        val memberService = mock(IMemberService::class.java)
        `when`(memberService.getByMemberId(1)).thenReturn(Optional.of(Member(1, "ok", MemberStatus.ACTIVE)))

        val productService = mock(IProductService::class.java)
        productServiceSetup(productService) { Product(productId = it, productName = "product", thePrice = 9.99, theCost = 9.00, inStock = 0) }

        val orderRepository = mock(OrderRepository::class.java)
        given(orderRepository.save(any(Order::class.java))).willAnswer({ it.arguments[0] })

        //act
        val orderService = OrderService(orderRepository, memberService, productService)

        //assert
        assertThatThrownBy { orderService.create(createCmd) }.isInstanceOf(ProductOutOfStockException::class.java)
    }

    @Test
    fun create_should_fail_when_member_has_blocked() {
        //arrange
        val memberService = mock(IMemberService::class.java)
        `when`(memberService.getByMemberId(1)).thenReturn(Optional.of(Member(1, "ok", MemberStatus.BLOCKED)))

        val productService = mock(IProductService::class.java)
        productServiceSetup(productService) { Product(productId = it, productName = "product", thePrice = 9.99, theCost = 9.00, inStock = 999) }

        val orderRepository = mock(OrderRepository::class.java)
        given(orderRepository.save(any(Order::class.java))).willAnswer({ it.arguments[0] })

        //act
        val orderService = OrderService(orderRepository, memberService, productService)

        //assert
        assertThatThrownBy { orderService.create(createCmd) }.isInstanceOf(MemberUnavailableException::class.java)
    }

    private fun productServiceSetup(productService: IProductService, result: (productId: String) -> Product) {
        given(productService.findByProductIdIn(Matchers.anyCollectionOf(String::class.java)))
                .willAnswer { (it.arguments[0] as Collection<String>).map { result(it) } }
    }
}