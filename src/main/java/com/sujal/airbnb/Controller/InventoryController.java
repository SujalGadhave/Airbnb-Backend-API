package com.sujal.airbnb.Controller;

import com.sujal.airbnb.Dto.InventoryDTO;
import com.sujal.airbnb.Dto.UpdateInventoryRequestDTO;
import com.sujal.airbnb.Service.Interfaces.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/admin/inventory")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Admin Inventory", description = "Manage hotel room inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/rooms/{roomId}")
    @Operation(summary = "Retrieve inventory of a room",
            description = "Fetch all available inventory items for a given room ID", tags = {"Admin Inventory"})
    public ResponseEntity<List<InventoryDTO>> getAllInventoryByRoom(@PathVariable long roomId) throws AccessDeniedException {
        return ResponseEntity.ok(inventoryService.getAllInventoryByRoom(roomId));
    }

    @PatchMapping("/rooms/{roomId}")
    @Operation(summary = "Update inventory for a room",
            description = "Modify the inventory details for a specific room", tags = {"Admin Inventory"})
    public ResponseEntity<Void> updateInventory(@PathVariable Long roomId,
                                                @RequestBody UpdateInventoryRequestDTO updateInventoryRequestDTO) throws AccessDeniedException {
        inventoryService.updateInventory(roomId, updateInventoryRequestDTO);
        return ResponseEntity.noContent().build();
    }
}

