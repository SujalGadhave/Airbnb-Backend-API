package com.sujal.airbnb.Dto;

import com.sujal.airbnb.Enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDTO {
    private Long id;
    private String email;
    private  String name;
    private Gender gender;
    private LocalDate dateOfBirth;
}