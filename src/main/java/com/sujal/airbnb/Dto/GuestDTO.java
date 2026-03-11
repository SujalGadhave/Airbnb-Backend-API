package com.sujal.airbnb.Dto;

import com.sujal.airbnb.Enums.Gender;
import lombok.Data;

@Data
public class GuestDTO {
    private Long id;
    private String name;
    private Gender gender;
    private Integer age;
}