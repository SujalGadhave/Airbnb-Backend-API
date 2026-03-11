package com.sujal.airbnb.Service;

import com.sujal.airbnb.Dto.ProfileUpdateRequestDTO;
import com.sujal.airbnb.Dto.UserDTO;
import com.sujal.airbnb.Entities.UserEntity;
import com.sujal.airbnb.Exception.ResourceNotFoundException;
import com.sujal.airbnb.Repository.UserRepository;
import com.sujal.airbnb.Service.Interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.sujal.airbnb.Utils.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

   private final UserRepository userRepository;
   private final ModelMapper modelMapper;

    @Override
    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found with id; "+id));
    }

    @Override
    public void updateProfile(ProfileUpdateRequestDTO profileUpdateRequestDTO) {
        UserEntity user = getCurrentUser();

        if(profileUpdateRequestDTO.getDateOfBirth() !=null) user.setDateOfBirth(profileUpdateRequestDTO.getDateOfBirth());
        if (profileUpdateRequestDTO.getGender() !=null) user.setGender(profileUpdateRequestDTO.getGender());
        if (profileUpdateRequestDTO.getName() !=null) user.setName(profileUpdateRequestDTO.getName());

        userRepository.save(user);
    }

    @Override
    public UserDTO getMyProfile() {
        UserEntity user = getCurrentUser();
        log.info("Getting the profile for user for user with ID: {}", user.getId());
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) userRepository.findByEmail(username).orElse(null);
    }
}
