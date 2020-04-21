package com.theword.wordcontent.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.theword.wordcontent.model.Bible;
import com.theword.wordcontent.model.Content; 

public class CustomContentRepositoryImpl implements CustomContentRepository {

private final MongoTemplate mongoTemplate;
	
	@Autowired
	public  CustomContentRepositoryImpl(MongoTemplate mongoTemp) {
		this.mongoTemplate = mongoTemp;
	}
	
	@Override
	public Content getChapterVerses(String bibleId, String bookCode,String chapter) {
		
		String query = ""
				+ ""
				+ ""
				+ ""
				+ "	{ $lookup: { " +
				  			"	from: '"+bibleId+"',   " +
				  			"	 let:{ code: '$code'}, " +
				  		"	pipeline:[  " +
				  		"	          {$match :{'$expr':{$eq:['$book','$$code']}}},  " +
				  		"	          {$match :{'$expr':{$eq:['$chapter','"+ chapter +"']}}},  " +
				        "			  {$project:{verse:1,text:1,_id:0}} " +
				  		"	          ], " + 
				  		"	as: 'verses' " +
						" }} ";
		
		
		return mongoTemplate.aggregate(Aggregation.newAggregation(
		        match(Criteria.where("language").is(bibleId)),
		        match(Criteria.where("code").is(bookCode)),
		       new CustomProjectAggregationOperation(query) 
		    ), "B_BOOK", Content.class).getUniqueMappedResult();
	}

	@Override
	public Content getChapterVerse(String bibleId, String bookCode,String chapter,String verse) {
		
		String query = ""
				+ ""
				+ ""
				+ ""
				+ "	{ $lookup: { " +
				  			"	from: '"+bibleId+"',   " +
				  			"	 let:{ code: '$code'}, " +
				  		"	pipeline:[  " +
				  		"	          {$match :{'$expr':{$eq:['$book','$$code']}}},  " +
				  		"	          {$match :{'$expr':{$eq:['$chapter','"+ chapter +"']}}},  " +
				  		"	          {$match :{'$expr':{$eq:['$verse','"+ verse +"']}}},  " +
				        "			  {$project:{verse:1,text:1,_id:0}} " +
				  		"	          ], " + 
				  		"	as: 'verses' " +
						" }} ";
		
		
		return mongoTemplate.aggregate(Aggregation.newAggregation(
		        match(Criteria.where("language").is(bibleId)),
		        match(Criteria.where("code").is(bookCode)),
		       new CustomProjectAggregationOperation(query) 
		    ), "B_BOOK", Content.class).getUniqueMappedResult();
	}

	
	@Override
	public Bible getBible(String bibleId) {
		return mongoTemplate.aggregate(Aggregation.newAggregation(
		        match(Criteria.where("code").is(bibleId))
		    ), "BLANG", Bible.class).getUniqueMappedResult();
	}


}
