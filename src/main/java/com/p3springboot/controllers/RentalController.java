package com.p3springboot.controllers;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.p3springboot.model.RentalEntity;
import com.p3springboot.model.UserEntity;
import com.p3springboot.services.FileStorageService;
import com.p3springboot.services.RentalService;
import com.p3springboot.services.UserService;

@RestController
@RequestMapping("/api")
public class RentalController {

    private final RentalService rentalService;
    private final UserService userService;
    private final FileStorageService fileStorageService;

    public RentalController(RentalService rentalService, UserService userService, FileStorageService fileStorageService) {
        this.rentalService = rentalService;
        this.userService = userService;
        this.fileStorageService = fileStorageService;
    }

    //DTOs

    public static record MessageResponse(String message) {}
    
    public static record RentalResponse(
        int id,
        String name, 
        int surface,
        int price, 
        String picture, 
        String description,
        int owner_id,
        String created_at,
        String updated_at) {}
    
    public static record RentalListResponse(List<RentalResponse> rentals) {}

    private RentalResponse toResponse(RentalEntity rental) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return new RentalResponse(
            rental.getId(),
            rental.getName(),
            rental.getSurface(),
            rental.getPrice(),
            rental.getPicture(),
            rental.getDescription(),
            rental.getOwnerId(),
            rental.getCreatedAt().format(formatter),
            rental.getUpdatedAt().format(formatter)
        );
    }

    @PostMapping(value = "/rentals", consumes = "multipart/form-data")
    public ResponseEntity<MessageResponse> createRental(
        @RequestParam("name") String name,
        @RequestParam("surface") Integer surface,
        @RequestParam("price") Integer price,
        @RequestParam("picture") MultipartFile picture,
        @RequestParam("description") String description,
        Authentication authentication) {
        
        String email = authentication.getName();
        UserEntity user = userService.findByEmail(email);

        if(user == null) {
            return ResponseEntity.status(401).build();
        }

        String pictureUrl = fileStorageService.saveFile(picture);

        rentalService.createRental(
            name,
            surface,
            price,
            pictureUrl,
            description,
            user.getId()
        );

        return ResponseEntity.ok(new MessageResponse("Rental created successfully"));
    }
    
    @GetMapping("/rentals")
    public ResponseEntity<RentalListResponse> getAllRentals() {
        List<RentalEntity> rentals = rentalService.getAllRentals();
        List<RentalResponse> rentalResponses = rentals.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(new RentalListResponse(rentalResponses)); 
    }

    @GetMapping("/rentals/{id}")
    public ResponseEntity<RentalResponse> getRentalById(@PathVariable int id) {
        
        Optional<RentalEntity> rentalOpt = rentalService.getRentalById(id);

        if (rentalOpt.isEmpty()) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(toResponse(rentalOpt.get()));
    }

    @PutMapping("/rentals/{id}")
    public ResponseEntity<MessageResponse> updateRental(
        @PathVariable long id,
        @RequestParam("name") String name,
        @RequestParam("surface") Integer surface,
        @RequestParam("price") Integer price,
        @RequestParam("description") String description,
        Authentication authentication) {    
            
            Optional<RentalEntity> rentalOpt = rentalService.getRentalById(id);

            if (rentalOpt.isEmpty()) {
                return ResponseEntity.status(404).build();
            }

            String email = authentication.getName();
            UserEntity user = userService.findByEmail(email);

            if (user == null || !rentalOpt.get().getOwnerId().equals(user.getId())) {
                return ResponseEntity.status(401).build();
            }

            /*
            On récupère l'URL existance pour la réutiliser. 
            Nécessaire pour update le rental même si l'utilisateur ne peux pas passer une nouvelle image dans le formulaire.
            */
            String existingPictureUrl = rentalOpt.get().getPicture();

            RentalEntity updatedRental = rentalService.updateRental(
                (int)id,
                name,
                surface,
                price,
                existingPictureUrl,
                description
            );

            if (updatedRental == null) {
                return ResponseEntity.status(404).build();
            }

            return ResponseEntity.ok(new MessageResponse("Rental updated successfully"));
        }
}
