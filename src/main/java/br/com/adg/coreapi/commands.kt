package br.com.adg.coreapi

import org.axonframework.commandhandling.RoutingKey
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.UUID

class CreateFoodCardCommand(
    @RoutingKey val foodCardId: UUID
)

data class SelectProductCommand(
    @TargetAggregateIdentifier val foodCardId: UUID,
    val productId: UUID,
    val quantity: Int
)

data class DeselectProductCommand(
    @TargetAggregateIdentifier val foodCardId: UUID,
    val productId: UUID,
    val quantity: Int
)

data class ConfirmOrderCommand(
    @TargetAggregateIdentifier val foodCardId: UUID
)