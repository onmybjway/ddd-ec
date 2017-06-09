package core.ec.order.application.exceptions

class MemberNotFoundException(memberId: Long) : RuntimeException("the member [$memberId] not found")

class ProductNotFoundException(productId: String) : RuntimeException("product [$productId] not found")

class ProductNotMatchException(productId: String) : RuntimeException("product [$productId] does not match")

