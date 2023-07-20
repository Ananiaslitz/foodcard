package br.com.adg.query;

import br.com.adg.coreapi.FindFoodCardQuery;
import br.com.adg.coreapi.FoodCardCreatedEvent;
import br.com.adg.coreapi.ProductDeselectedEvent;
import br.com.adg.coreapi.ProductSelectedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class FoodCardProjector {
    private final FoodCardViewRepository foodCardViewRepository;

    public FoodCardProjector(FoodCardViewRepository foodCardViewRepository) {
        this.foodCardViewRepository = foodCardViewRepository;
    }

    @EventHandler
    public void on(FoodCardCreatedEvent event) {
        FoodCardView foodCardView = new FoodCardView(event.getFoodCardId(), Collections.emptyMap());
        foodCardViewRepository.save(foodCardView);
    }

    @EventHandler
    public void on(ProductSelectedEvent event) {
        foodCardViewRepository.findById(event.getFoodCardId()).ifPresent(
            foodCardView ->  foodCardView.addProduct(event.getProductId(), event.getQuantity())
        );
    }

    @EventHandler
    public void on(ProductDeselectedEvent event) {
        foodCardViewRepository.findById(event.getFoodCardId()).ifPresent(
            foodCardView -> foodCardView.removeProducts(event.getProductId(), event.getQuantity())
        );
    }

    @QueryHandler
    public FoodCardView handle(FindFoodCardQuery query) {
        return foodCardViewRepository.findById(query.getFoodCardId()).orElse(null);
    }

}
