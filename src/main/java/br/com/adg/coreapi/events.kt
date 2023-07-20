package br.com.adg.coreapi

import java.util.UUID

class FoodCardCreatedEvent(
    val foodCardId: UUID
)

data class ProductSelectedEvent(
    val foodCardId: UUID,
    val productId: UUID,
    val quantity: Int
)

data class ProductDeselectedEvent(
    val foodCardId: UUID,
    val productId: UUID,
    val quantity: Int
)

data class OrderConfirmedEvent(
    val foodCardId: UUID
)