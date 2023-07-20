package br.com.adg.gui;

import br.com.adg.coreapi.CreateFoodCardCommand;
import br.com.adg.coreapi.DeselectProductCommand;
import br.com.adg.coreapi.FindFoodCardQuery;
import br.com.adg.coreapi.SelectProductCommand;
import br.com.adg.query.FoodCardView;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequestMapping("/foodCard")
@RestController
public class FoodOrderingController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public FoodOrderingController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping("/create")
    public CompletableFuture<UUID> createFoodCard() {
        return commandGateway.send(new CreateFoodCardCommand(UUID.randomUUID()));
    }

    @PostMapping("/{foodCardId}/select/{productId}/quantity/{quantity}")
    public void selectProduct(@PathVariable("foodCardId") String foodCardId,
                              @PathVariable("productId") String productId,
                              @PathVariable("quantity") Integer quantity) {
        commandGateway.send(new SelectProductCommand(
                UUID.fromString(foodCardId),
                UUID.fromString(productId),
                quantity
            )
        );
    }

    @PostMapping("/{foodCardId}/deselect/{productId}/quantity/{quantity}")
    public void deselectProduct(@PathVariable("foodCardId") String foodCardId,
                                @PathVariable("productId") String productId,
                                @PathVariable("quantity") Integer quantity) {
        commandGateway.send(new DeselectProductCommand(
            UUID.fromString(foodCardId),
            UUID.fromString(productId),
            quantity)
        );
    }

    @GetMapping("/{foodCardId}")
    public CompletableFuture<FoodCardView> findFoodCart(@PathVariable("foodCardId") String foodCardId) {
        return queryGateway.query(
            new FindFoodCardQuery(UUID.fromString(foodCardId)),
            ResponseTypes.instanceOf(FoodCardView.class)
        );
    }
}
