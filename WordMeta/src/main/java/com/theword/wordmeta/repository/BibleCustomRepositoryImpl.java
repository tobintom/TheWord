package com.theword.wordmeta.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.theword.wordmeta.model.Bible;
import com.theword.wordmeta.model.Book;

public class BibleCustomRepositoryImpl  implements BibleCustomRepository{

	private final MongoTemplate mongoTemplate;
	
	@Autowired
	public  BibleCustomRepositoryImpl(MongoTemplate mongoTemp) {
		this.mongoTemplate = mongoTemp;
	}
	
	@Override
	public Bible getBibleBooks(String id) {
		
		return mongoTemplate.aggregate(Aggregation.newAggregation(
		        match(Criteria.where("code").is(id)),
		        LookupOperation.newLookup()
		        .from("B_BOOK")
		        .localField("code")
		        .foreignField("language")
		        .as("books")
		    ), "BLANG", Bible.class).getUniqueMappedResult();
	}

	@Override
	public Book getBookChapters(String bibleId, String bookCode) {
		
		String query = "{ $lookup: { " +
				  			"	from: '"+bibleId+"',   " +
				  			"	 let:{ code: '$code'}, " +
				  		"	pipeline:[  " +
				  		"	          {$match :{'$expr':{$eq:['$book','$$code']}}},  " +
				        "			  {$project:{chapter:1,_id:0}} " +
				  		"	          ], " + 
				  		"	as: 'chapters' " +
						" }} ";
		
		
		return mongoTemplate.aggregate(Aggregation.newAggregation(
		        match(Criteria.where("language").is(bibleId)),
		        match(Criteria.where("code").is(bookCode)),
		       new CustomProjectAggregationOperation(query) 
		    ), "B_BOOK", Book.class).getUniqueMappedResult();
	}
    
	@Override
	public Book getChapterVerses(String bibleId, String bookCode,String chapter) {
		
		String query = "{ $lookup: { " +
				  			"	from: '"+bibleId+"',   " +
				  			"	 let:{ code: '$code'}, " +
				  		"	pipeline:[  " +
				  		"	          {$match :{'$expr':{$eq:['$book','$$code']}}},  " +
				  		"	          {$match :{'$expr':{$eq:['$chapter','"+ chapter +"']}}},  " +
				        "			  {$project:{verse:1,_id:0}} " +
				  		"	          ], " + 
				  		"	as: 'verses' " +
						" }} ";
		
		
		return mongoTemplate.aggregate(Aggregation.newAggregation(
		        match(Criteria.where("language").is(bibleId)),
		        match(Criteria.where("code").is(bookCode)),
		       new CustomProjectAggregationOperation(query) 
		    ), "B_BOOK", Book.class).getUniqueMappedResult();
	}

}
