package com.theword.wordmeta.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.theword.wordmeta.model.Bible;

@Repository
public interface BibleRepository extends MongoRepository<Bible, String>,BibleCustomRepository {

}
