package com.p3springboot.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.p3springboot.model.RentalEntity;
import com.p3springboot.repository.RentalRepository;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public RentalEntity createRental(String name, Integer surface, 
                                     Integer price, String picture,
                                     String description, Integer ownerId) {
        
        
        RentalEntity rental = new RentalEntity();

        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setPicture(picture);
        rental.setDescription(description);
        rental.setOwnerId(ownerId);
        rental.setCreatedAt(LocalDateTime.now());
        rental.setUpdatedAt(LocalDateTime.now());
        
        return rentalRepository.save(rental);
    }

    public List<RentalEntity> getAllRentals() {
        return (List<RentalEntity>) rentalRepository.findAll();
    }

    public Optional<RentalEntity> getRentalById(long id) {
        return rentalRepository.findById((int)id);
    }

    public RentalEntity updateRental(int id, String name, Integer surface, 
                                     Integer price, String picture,
                                     String description) {
        
        Optional<RentalEntity> rentalOpt = rentalRepository.findById(id);
        
        if (rentalOpt.isPresent()) {
            
            RentalEntity rental = rentalOpt.get();
            rental.setName(name);
            rental.setSurface(surface);
            rental.setPrice(price);
            rental.setPicture(picture);
            rental.setDescription(description);
            rental.setUpdatedAt(LocalDateTime.now());

            return rentalRepository.save(rental);
        }

        return null;
    }
}
