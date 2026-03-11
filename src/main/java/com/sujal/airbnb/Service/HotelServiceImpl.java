package com.sujal.airbnb.Service;

import com.sujal.airbnb.Dto.HotelDTO;
import com.sujal.airbnb.Dto.HotelInfoDTO;
import com.sujal.airbnb.Dto.HotelPriceDTO;
import com.sujal.airbnb.Dto.RoomDTO;
import com.sujal.airbnb.Entities.HotelEntity;
import com.sujal.airbnb.Entities.RoomEntity;
import com.sujal.airbnb.Entities.UserEntity;
import com.sujal.airbnb.Exception.ResourceNotFoundException;
import com.sujal.airbnb.Exception.UnAuthorisedException;
import com.sujal.airbnb.Repository.HotelRepository;
import com.sujal.airbnb.Repository.RoomRepository;
import com.sujal.airbnb.Service.Interfaces.HotelService;
import com.sujal.airbnb.Service.Interfaces.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.sujal.airbnb.Utils.AppUtils.getCurrentUser;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;
    private final RoomRepository roomRepository;

    @Override
    public HotelDTO createNewHotel(HotelDTO hotelDTO) {
        log.info("Creating a new hotel with name: {}", hotelDTO.getName());
        HotelEntity hotel = modelMapper.map(hotelDTO,  HotelEntity.class);
        hotel.setActive(false);

        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        hotel.setOwner(user);

        hotel = hotelRepository.save(hotel);
        log.info("Created a new hotel with ID: {}", hotel.getId());
        return modelMapper.map(hotel, HotelDTO.class);
    }

    @Override
    public HotelDTO getHotelById(Long id) {
        log.info("Getting the hotel with ID: {}", id);
        HotelEntity hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " +id));

        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: "+id);
        }
        return modelMapper.map(hotel, HotelDTO.class);
    }

    @Override
    public HotelDTO updateHotelById(Long id, HotelDTO hotelDTO) {
        log.info("Updating the hotel with ID: {}", id);
        HotelEntity hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: "+id));

        modelMapper.map(hotelDTO, hotel);
        hotel.setId(id);
        hotel = hotelRepository.save(hotel);
        return modelMapper.map(hotel, HotelDTO.class);
    }

    @Override
    @Transactional
    public void deleteHotelById(Long id) {
        HotelEntity hotel = hotelRepository
                .findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found with ID: "+id));

        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.equals(hotel.getRooms())){
            throw new UnAuthorisedException("This user does not own this hotel with id: "+id);
        }
        for(RoomEntity room: hotel.getRooms()){
            inventoryService.deleteAllInventories(room);
            roomRepository.deleteById(room.getId());
        }
        hotelRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void activeHotel(Long hotelId) {
        log.info("Activating the hotel with ID: {}", hotelId);
        HotelEntity hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found with id: " +hotelId));

        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals((hotel.getOwner()))) {
            throw new UnAuthorisedException("This user does not own this hotel with id: " +hotelId);
        }

        hotel.setActive(true);
        //assuming only do it once
        for(RoomEntity room: hotel.getRooms()){
            inventoryService.initializeRoomForAYear(room);
        }
    }

    @Override
    public HotelInfoDTO getHotelInfoById(Long hotelId) {
        HotelEntity hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found with ID: " +hotelId));

        List<RoomDTO> rooms = hotel.getRooms()
                .stream()
                .map((element) -> modelMapper.map(element, RoomDTO.class))
                .toList();
        return new HotelInfoDTO(modelMapper.map(hotel, HotelDTO.class), rooms);
    }

    @Override
    public List<HotelDTO> getALlHotels() {
        UserEntity user = getCurrentUser();
        log.info("Getting all hotels for the admin user with ID: {}", user.getId());
        List<HotelEntity> hotels =  hotelRepository.findByOwner(user);
        return hotels
                .stream()
                .map((elements) -> modelMapper.map(elements, HotelDTO.class))
                .collect(Collectors.toList());
    }
}