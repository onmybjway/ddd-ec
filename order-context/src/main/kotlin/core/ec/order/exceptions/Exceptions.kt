package core.ec.order.exceptions

class MemberNotFoundException(memberId: Long) : RuntimeException("the member [$memberId] not found")

class ProductNotFoundException(productId: String) : RuntimeException("product [$productId] not found")

class ProductNotMatchException(productId: String) : RuntimeException("product [$productId] does not match")

class ProductOutOfStockException(productId: String):RuntimeException("product [$productId] is out of stock")

class MemberUnavailableException(memberId: Long) : RuntimeException("the member [$memberId] is unavailable")