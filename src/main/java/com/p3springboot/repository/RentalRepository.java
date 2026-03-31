package com.p3springboot.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.p3springboot.model.RentalEntity;

@Repository
public interface RentalRepository extends CrudRepository<RentalEntity, Integer> {
    List<RentalEntity> findByOwnerId(Integer userId);
}
