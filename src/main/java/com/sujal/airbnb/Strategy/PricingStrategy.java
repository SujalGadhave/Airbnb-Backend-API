package com.sujal.airbnb.Strategy;

import com.sujal.airbnb.Entities.InventoryEntity;

import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal calculatePrice(InventoryEntity inventory);
}
