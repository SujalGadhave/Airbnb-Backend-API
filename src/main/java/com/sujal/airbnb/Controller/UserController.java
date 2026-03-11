package com.sujal.airbnb.Controller;

import com.sujal.airbnb.Dto.BookingDTO;
import com.sujal.airbnb.Dto.GuestDTO;
import com.sujal.airbnb.Dto.ProfileUpdateRequestDTO;
import com.sujal.airbnb.Dto.UserDTO;
import com.sujal.airbnb.Service.Interfaces.BookingService;
import com.sujal.airbnb.Service.Interfaces.GuestService;
import com.sujal.airbnb.Service.Interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "User Profile", description = "Manage user profile and bookings")
public class UserController {
    private final UserService userService;
    private final BookingService bookingService;
    private final GuestService guestService;

    @PatchMapping("/profile")
    @Operation(summary = "Update my profile", description = "Allows a user to update their profile details.", tags = {"User Profile"})
    public ResponseEntity<Void> updateProfile(@RequestBody ProfileUpdateRequestDTO profileUpdateRequestDTO) {
        userService.updateProfile(profileUpdateRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/myBookings")
    @Operation(summary = "Get my bookings", description = "Fetches a list of all past bookings for the user.", tags = {"User Profile"})
    public ResponseEntity<List<BookingDTO>> getMyBookings() {
        return ResponseEntity.ok(bookingService.getMyBookings());
    }

    @GetMapping("/profile")
    @Operation(summary = "Get my profile", description = "Retrieves the current user's profile details.", tags = {"User Profile"})
    public ResponseEntity<UserDTO> getMyProfile() {
        return ResponseEntity.ok(userService.getMyProfile());
    }

    @GetMapping("/guests")
    @Operation(summary = "Get my guests", description = "Retrieves a list of guests associated with the user's bookings.", tags = {"User Profile"})
    public ResponseEntity<List<GuestDTO>> getAllGuests() {
        return ResponseEntity.ok(guestService.getAllGuests());
    }

    @PostMapping("/guests")
    @Operation(summary = "Add a guest", description = "Adds a new guest to the user's guest list.", tags = {"Booking Guests"})
    public ResponseEntity<GuestDTO> addNewGuest(@RequestBody GuestDTO guestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(guestService.addNewGuest(guestDTO));
    }

    @PutMapping("/guests/{guestId}")
    @Operation(summary = "Update a guest", description = "Updates details of a guest in the user's guest list.", tags = {"Booking Guests"})
    public ResponseEntity<Void> updateGuest(@PathVariable Long guestId, @RequestBody GuestDTO guestDTO) throws AccessDeniedException {
        guestService.updateGuest(guestId, guestDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/guests/{guestId}")
    @Operation(summary = "Remove a guest", description = "Removes a guest from the user's guest list.", tags = {"Booking Guests"})
    public ResponseEntity<Void> deleteGuest(@PathVariable Long guestId) throws AccessDeniedException {
        guestService.deleteGuest(guestId);
        return ResponseEntity.noContent().build();
    }
}