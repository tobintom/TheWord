package com.theword.wordcontent.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.theword.wordcontent.model.Content;

@Repository
public interface ContentRepository extends MongoRepository<Content, String> , CustomContentRepository{

}
