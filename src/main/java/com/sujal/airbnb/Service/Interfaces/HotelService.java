package com.sujal.airbnb.Service.Interfaces;

import com.sujal.airbnb.Dto.HotelDTO;
import com.sujal.airbnb.Dto.HotelInfoDTO;

import java.util.List;

public interface HotelService {
    HotelDTO createNewHotel (HotelDTO hotelDTO);
    HotelDTO getHotelById(Long id);
    HotelDTO updateHotelById(Long id, HotelDTO hotelDTO);
    void deleteHotelById(Long id);
    void activeHotel(Long hotelId);
    HotelInfoDTO getHotelInfoById(Long hotelId);
    List<HotelDTO> getALlHotels();
}
