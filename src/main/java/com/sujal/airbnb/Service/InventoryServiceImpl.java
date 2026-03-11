package com.sujal.airbnb.Service;

import com.sujal.airbnb.Dto.HotelPriceDTO;
import com.sujal.airbnb.Dto.HotelSearchRequest;
import com.sujal.airbnb.Dto.InventoryDTO;
import com.sujal.airbnb.Dto.UpdateInventoryRequestDTO;
import com.sujal.airbnb.Entities.InventoryEntity;
import com.sujal.airbnb.Entities.RoomEntity;
import com.sujal.airbnb.Entities.UserEntity;
import com.sujal.airbnb.Exception.ResourceNotFoundException;
import com.sujal.airbnb.Repository.HotelMiniPriceRepository;
import com.sujal.airbnb.Repository.InventoryRepository;
import com.sujal.airbnb.Repository.RoomRepository;
import com.sujal.airbnb.Service.Interfaces.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.sujal.airbnb.Utils.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;
    private final HotelMiniPriceRepository hotelMiniPriceRepository;
    private final RoomRepository roomRepository;

    @Override
    public void initializeRoomForAYear(RoomEntity room) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusYears(1);
        for (; !today.isAfter(endDate); today = today.plusDays(1)) {
            InventoryEntity inventory = InventoryEntity.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .bookCount(0)
                    .reservedCount(0)
                    .city(room.getHotel().getCity())
                    .date(today)
                    .price(room.getBasePrice())
                    .surgeFactor(BigDecimal.ONE)
                    .totalCount(room.getTotalCount())
                    .closed(false)
                    .build();
            inventoryRepository.save(inventory);
        }
    }

    @Override
    public void deleteAllInventories(RoomEntity room) {
        log.info("Deleting the inventories of room with id: {}", room.getId());
        inventoryRepository.deleteByRoom(room);
    }

    @Override
    public Page<HotelPriceDTO> searchHotels(HotelSearchRequest hotelSearchRequest) {
        log.info("Searching hotels for {} city, from {} to {}", hotelSearchRequest.getCity(), hotelSearchRequest.getStartDate(), hotelSearchRequest.getEndDate());
        Pageable pageable = PageRequest.of(hotelSearchRequest.getPage(), hotelSearchRequest.getSize());
        Long dateCount =
                ChronoUnit.DAYS.between(hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate()) + 1;

        Page<HotelPriceDTO> hotelPriceDTOPage = hotelMiniPriceRepository.findHotelsWithAvailableInventory(hotelSearchRequest.getCity(),
                hotelSearchRequest.getStartDate(), hotelSearchRequest.getEndDate(), hotelSearchRequest.getRoomsCount(),
                dateCount, pageable);
        return hotelPriceDTOPage.map((element)-> modelMapper.map(element, HotelPriceDTO.class));
    }

    @Override
    public List<InventoryDTO> getAllInventoryByRoom(Long roomId) throws AccessDeniedException {
        log.info("Getting All inventory by room for room with id: {}", roomId);
        RoomEntity room = (RoomEntity) roomRepository.findById(roomId)
                .orElseThrow(()-> new ResourceNotFoundException("Room not found with id: "+roomId));

        UserEntity user = getCurrentUser();
        if(!user.equals(room.getHotel().getOwner())) throw new AccessDeniedException("You are not the owner of the room with id: "+roomId);

        return inventoryRepository.findByRoomOrderByDate(room).stream()
                .map((element)-> modelMapper.map(element,
                        InventoryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateInventory(Long roomId, UpdateInventoryRequestDTO updateInventoryRequestDTO) throws AccessDeniedException {
        log.info("Updating All inventory by room for room with id: {} between date range: {}-{}", roomId,
                updateInventoryRequestDTO.getStartDate(), updateInventoryRequestDTO.getEndDate());

        RoomEntity room = (RoomEntity) roomRepository.findById(roomId)
                .orElseThrow(()-> new ResourceNotFoundException("Room not found with id: "+roomId));

        UserEntity user = getCurrentUser();
        if(!user.equals(room.getHotel().getOwner())) throw new AccessDeniedException("You are not the owner of rrom with id: "+roomId);

        inventoryRepository.getInventoryAndLockBeforeUpdate(roomId, updateInventoryRequestDTO.getStartDate(),
                updateInventoryRequestDTO.getEndDate());

        inventoryRepository.updateInventory(roomId, updateInventoryRequestDTO.getStartDate(),
                updateInventoryRequestDTO.getEndDate(), updateInventoryRequestDTO.getClosed(),
                updateInventoryRequestDTO.getSurgeFactor());
    }
}
