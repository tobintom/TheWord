package com.theword.wordcontent.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.theword.wordcontent.model.Bible;
import com.theword.wordcontent.model.Book;
import com.theword.wordcontent.model.Content;
import com.theword.wordcontent.model.VerseText;

import static com.mongodb.client.model.Filters.*;

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
	@Cacheable("bibles")
	public Bible getBible(String bibleId) { 
		return mongoTemplate.aggregate(Aggregation.newAggregation(
		        match(Criteria.where("code").is(bibleId))
		    ), "BLANG", Bible.class).getUniqueMappedResult();
	}

	@Override
	@Cacheable("bibleBooks")
	public List<Book> getBibleBooks(String language) {
		return mongoTemplate.find(Query.query(Criteria.where("language").is(language)), Book.class);
	}

	@Override
	public List<VerseText> getPassages(List<String> passages,String bibleID) {
		List<Bson> filters = new ArrayList<>();
		List<String> verses = null;
		List<VerseText> returnObject = new ArrayList<VerseText>();
		if(passages!=null && passages.size()>0) {
			
			for(String verse:passages) {				
				String book = verse.split("\\s+")[0];
				String chapter = verse.split("\\s+")[1].split(":")[0];
				String verseNumber = verse.split("\\s+")[1].split(":")[1];
				
				verses = new ArrayList<String>();
				
				String[] nparts = verseNumber.split(",");
				for(String part:nparts) {
					String[] vparts = part.split("-");
					if(vparts.length==1) {
						verses.add(vparts[0]);
					}else {
						int begin = Integer.valueOf(vparts[0]);
						int end = Integer.valueOf(vparts[1]);
						for(int i = begin;i<=end;i++) {
							verses.add(String.valueOf(i));
						}				
					}				
				}		
				
				//add to filter
				filters.add(and(eq("book", book),eq("chapter", chapter),in("verse", verses)));
			}					
			MongoCursor<Document> documents = mongoTemplate.getCollection(bibleID.toUpperCase()).find(Filters.or(filters)).iterator();		 
			while(documents!=null && documents.hasNext()) {
				 Document document = documents.next();
				 VerseText v = new VerseText();
				 v.setBook(document.getString("book"));
				 v.setChapter(document.getString("chapter"));
				 v.setVerse(document.getString("verse"));
				 v.setText(document.getString("text"));
				 returnObject.add(v);
			}
		}
		 return returnObject;
	}

	@Override
	public List<VerseText> getSearchPassages(String key, String strict, String bibleID, String limit) {
		int ilimitDefault = 20;
		int ilimit = 20;
		try {
			limit = (limit == null || limit.trim().equalsIgnoreCase(""))?"0":limit.trim();
			ilimit = Integer.valueOf(limit);
		}catch(NumberFormatException e) {
			//Ignore
		}
		
		int limitToUse = (ilimit > 0 && ilimit <50)?ilimit:ilimitDefault;
		List<Bson> filters = new ArrayList<>();
		List<VerseText> returnObject = new ArrayList<VerseText>();
		if(key!=null && key.trim().length()>0) {
			if(strict!=null && strict.trim().equalsIgnoreCase("false")) {
				String[] parts = key.split("\\s+");
				for(String part:parts) {
					filters.add(regex("text", part.trim(),"i"));
				}								 
			}else {
				filters.add(regex("text", key.trim(),"i"));			
			}
			
			MongoCursor<Document> documents = mongoTemplate.getCollection(bibleID.toUpperCase()).find(Filters.or(filters)).limit(limitToUse).iterator();
			 System.out.println(documents);
			 while(documents!=null && documents.hasNext()) {				 
				 Document document = documents.next();
				 VerseText v = new VerseText();
				 v.setBook(document.getString("book"));
				 v.setChapter(document.getString("chapter"));
				 v.setVerse(document.getString("verse"));
				 v.setText(document.getString("text"));
				 returnObject.add(v);				  
			 }
		}
		return returnObject;
	}

	@Override
	public List<String> getDailyVerse(String month, String day) {
		List<String> verses = null;
		List<Bson> filters = new ArrayList<>();
		filters.add(and(eq("month", month),eq("date", day)));
		MongoCursor<Document> documents = mongoTemplate.getCollection("DAILY_VERSE").find(Filters.and(filters)).iterator();		 
		while(documents!=null && documents.hasNext()) {
			 Document document = documents.next();
			 verses = document.getList("references", String.class);
		}
		return verses;
	}

	@Override
	public List<String> getRandomDailyVerse(String month, String day) {
		List<String> verses = null;
		List<Bson> filters = new ArrayList<>();
		filters.add(and(eq("month", month),eq("date", day)));
		MongoCursor<Document> documents = mongoTemplate.getCollection("RANDOM_DAILY_VERSE").find(Filters.and(filters)).iterator();		 
		while(documents!=null && documents.hasNext()) {
			 Document document = documents.next();
			 verses = document.getList("references", String.class);
		}
		return verses;
	}

}
