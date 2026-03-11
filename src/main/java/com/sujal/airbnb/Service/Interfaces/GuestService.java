package com.sujal.airbnb.Service.Interfaces;

import com.sujal.airbnb.Dto.GuestDTO;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface GuestService {
    List<GuestDTO> getAllGuests();
    void updateGuest(Long guestId, GuestDTO guestDTO) throws AccessDeniedException;
    void deleteGuest(Long guestId) throws AccessDeniedException;
    GuestDTO addNewGuest (GuestDTO guestDTO);
}