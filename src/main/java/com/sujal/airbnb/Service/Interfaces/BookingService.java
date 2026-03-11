package com.sujal.airbnb.Service.Interfaces;

import com.stripe.model.Event;
import com.sujal.airbnb.Dto.BookingDTO;
import com.sujal.airbnb.Dto.BookingRequest;
import com.sujal.airbnb.Dto.GuestDTO;
import com.sujal.airbnb.Dto.HotelReportDTO;
import com.sujal.airbnb.Enums.BookingStatus;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    BookingDTO initialiseBooking(BookingRequest bookingRequest);

    BookingDTO addGuests(Long bookingId, List<GuestDTO> guestDTOList);

    String initiatePayments(Long bookingId);

    void capturePayment(Event event);

    void cancelBooking(Long bookingId);

    BookingStatus getBookingStatus(Long bookingId);

    List<BookingDTO> getAllBookingsByHotelId(Long hotelId) throws AccessDeniedException;

    HotelReportDTO getHotelReport(Long hotelId, LocalDate startDate, LocalDate endDate) throws AccessDeniedException;

    List<BookingDTO> getMyBookings();
}

