package com.p3springboot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.p3springboot.model.MessageEntity;

@Repository
public interface MessageRepository extends CrudRepository<MessageEntity, Integer>{}
