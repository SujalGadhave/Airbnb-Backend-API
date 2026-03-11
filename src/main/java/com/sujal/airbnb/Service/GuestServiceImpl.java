package com.sujal.airbnb.Service;

import com.sujal.airbnb.Dto.GuestDTO;
import com.sujal.airbnb.Entities.GuestEntity;
import com.sujal.airbnb.Entities.UserEntity;
import com.sujal.airbnb.Repository.GuestRepository;
import com.sujal.airbnb.Service.Interfaces.GuestService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

import static com.sujal.airbnb.Utils.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<GuestDTO> getAllGuests() {
        UserEntity user = getCurrentUser();
        log.info("fetching all guests of user with id: {}", user.getId());
        List<GuestEntity> guests = guestRepository.findByUser(user);
        return guests.stream()
                .map(guest -> modelMapper.map(guest, GuestDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void updateGuest(Long guestId, GuestDTO guestDTO) throws AccessDeniedException {
        log.info("Updating guest with ID: {}", guestId);
        GuestEntity guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new EntityNotFoundException("Guest not found"));

        UserEntity user = getCurrentUser();
        if (!user.equals(guest.getUser())) throw new AccessDeniedException("You are not the owner of this guest");

        modelMapper.map(guestDTO, guest);
        guest.setUser(user);
        guest.setId(guestId);

        guestRepository.save(guest);
        log.info("Guest with ID: {} updated successfully", guestId);
    }

    @Override
    public void deleteGuest(Long guestId) throws AccessDeniedException {
        log.info("Deleting guest with ID: {}", guestId);
        GuestEntity guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new EntityNotFoundException("Guest not found"));

        UserEntity user = getCurrentUser();
        if(!user.equals(guest.getUser())) throw new AccessDeniedException("You are not the owner of this guest");

        guestRepository.deleteById(guestId);
        log.info("Guest with ID: {} deleted successfully", guestId);
    }

    @Override
    public GuestDTO addNewGuest(GuestDTO guestDTO) {
        log.info("Adding new guest: {}", guestDTO);
        UserEntity user =  getCurrentUser();
        GuestEntity guest = modelMapper.map(guestDTO, GuestEntity.class);
        guest.setUser(user);
        GuestEntity savedGuest = guestRepository.save(guest);
        log.info("Guest added with ID: {}", savedGuest.getId());
        return modelMapper.map(savedGuest, GuestDTO.class);
    }
}
