package com.sujal.airbnb.Repository;

import com.sujal.airbnb.Dto.GuestDTO;
import com.sujal.airbnb.Entities.GuestEntity;
import com.sujal.airbnb.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuestRepository extends JpaRepository<GuestEntity, Long> {
    List<GuestEntity> findByUser(UserEntity user);
}