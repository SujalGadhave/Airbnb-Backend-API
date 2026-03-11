package com.sujal.airbnb.Controller;

import com.sujal.airbnb.Dto.RoomDTO;
import com.sujal.airbnb.Service.Interfaces.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hotels/{hotelId}/rooms")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Room Admin Management", description = "Admin manage rooms/hotels/inventories in a hotel")
public class RoomAdminController {

    private final RoomService roomService;

    @PostMapping
    @Operation(summary = "Create a new room",
            description = "Adds a new room to a specific hotel")
    public ResponseEntity<RoomDTO> createNewRoom(@PathVariable Long hotelId,
                                                 @RequestBody RoomDTO roomDTO) {
        RoomDTO room = roomService.createNewRoom(hotelId, roomDTO);
        return new ResponseEntity<>(room, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Retrieve all rooms in a hotel",
            description = "Fetches all rooms belonging to the specified hotel")
    public ResponseEntity<List<RoomDTO>> getAllRoomsInHotels(@PathVariable Long hotelId) {
        return ResponseEntity.ok(roomService.getAllRoomsInHotel(hotelId));
    }

    @DeleteMapping("/{roomId}")
    @Operation(summary = "Delete a room",
            description = "Deletes a room from the hotel by ID")
    public ResponseEntity<Void> deleteRoomById(@PathVariable Long hotelId, @PathVariable Long roomId) {
        roomService.deleteRoomById(roomId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{roomId}")
    @Operation(summary = "Update a room",
            description = "")
    public ResponseEntity<RoomDTO> updateRoomById(@PathVariable Long hotelId,
                                                  @PathVariable Long roomId,
                                                  @RequestBody RoomDTO roomDTO) {
        return ResponseEntity.ok(roomService.updateRoomById(roomId, hotelId, roomDTO));
    }
}