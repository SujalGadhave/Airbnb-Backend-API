package com.sujal.airbnb.Service.Interfaces;

import com.sujal.airbnb.Dto.HotelPriceDTO;
import com.sujal.airbnb.Dto.HotelSearchRequest;
import com.sujal.airbnb.Dto.InventoryDTO;
import com.sujal.airbnb.Dto.UpdateInventoryRequestDTO;
import com.sujal.airbnb.Entities.RoomEntity;
import org.springframework.data.domain.Page;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface InventoryService {
    void initializeRoomForAYear(RoomEntity room);
    void deleteAllInventories(RoomEntity room);
    Page<HotelPriceDTO> searchHotels(HotelSearchRequest hotelSearchRequest);
    List<InventoryDTO> getAllInventoryByRoom(Long roomId) throws AccessDeniedException;
    void updateInventory(Long roomId, UpdateInventoryRequestDTO updateInventoryRequestDTO) throws AccessDeniedException;
}
