package br.com.adg.command;

import br.com.adg.coreapi.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Aggregate
public class FoodCard {
    private static final Logger logger = LoggerFactory.getLogger(FoodCard.class);

    @AggregateIdentifier
    private UUID foodCardId;
    private Map<UUID, Integer> selectedProducts;
    private boolean confirmed;

    @CommandHandler
    public FoodCard(CreateFoodCardCommand command) {
        AggregateLifecycle.apply(new FoodCardCreatedEvent(command.getFoodCardId()));
    }

    @CommandHandler
    public void handle(SelectProductCommand command) {
        AggregateLifecycle.apply(
            new ProductSelectedEvent(
                foodCardId,
                command.getProductId(),
                command.getQuantity()
            )
        );
    }

    @CommandHandler
    public void handle(DeselectProductCommand command) {
        AggregateLifecycle.apply(new ProductDeselectedEvent(foodCardId, command.getProductId(), command.getQuantity()));
    }

    @CommandHandler
    public void handle(ConfirmOrderCommand command) {
        AggregateLifecycle.apply(new OrderConfirmedEvent(foodCardId));
    }

    @EventSourcingHandler
    public void on(FoodCardCreatedEvent event) {
        foodCardId = event.getFoodCardId();
        selectedProducts = new HashMap<>();
        confirmed = false;
    }

    @EventSourcingHandler
    public void on(ProductSelectedEvent event) {
        selectedProducts.merge(event.getProductId(), event.getQuantity(), Integer::sum);
    }

    @EventSourcingHandler
    public void on(ProductDeselectedEvent event) {
        selectedProducts.computeIfPresent(
                event.getProductId(),
                (productId, quantity) -> quantity -= event.getQuantity()
        );
    }

    @EventSourcingHandler
    public void on(OrderConfirmedEvent event) {
        confirmed = true;
    }

    public FoodCard() {}
}
