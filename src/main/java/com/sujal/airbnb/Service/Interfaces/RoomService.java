package com.sujal.airbnb.Service.Interfaces;

import com.sujal.airbnb.Dto.RoomDTO;

import java.util.List;

public interface RoomService {
    RoomDTO createNewRoom(Long hotelId, RoomDTO roomDTO);
    List<RoomDTO> getAllRoomsInHotel(Long hotelId);
    RoomDTO getRoomById(Long roomId);
    void deleteRoomById(Long roomId);
    RoomDTO updateRoomById(Long hotelId, Long roomId, RoomDTO roomDTO);
}