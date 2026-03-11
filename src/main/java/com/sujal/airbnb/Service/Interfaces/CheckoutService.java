package com.sujal.airbnb.Service.Interfaces;

import com.sujal.airbnb.Entities.BookingEntity;

public interface CheckoutService {
    String getCheckoutSession(BookingEntity booking, String successUrl, String failureUrl);
}
