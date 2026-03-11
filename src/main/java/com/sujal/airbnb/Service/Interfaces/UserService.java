package com.sujal.airbnb.Service.Interfaces;

import com.sujal.airbnb.Dto.ProfileUpdateRequestDTO;
import com.sujal.airbnb.Dto.UserDTO;
import com.sujal.airbnb.Entities.UserEntity;

public interface UserService {
    UserEntity getUserById(Long id);
    void updateProfile(ProfileUpdateRequestDTO profileUpdateRequestDTO);
    UserDTO getMyProfile();
}
