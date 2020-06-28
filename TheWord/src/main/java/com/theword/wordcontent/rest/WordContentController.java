package com.theword.wordcontent.rest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.theword.wordcontent.model.Bible;
import com.theword.wordcontent.model.Book;
import com.theword.wordcontent.model.Content;
import com.theword.wordcontent.model.Verse;
import com.theword.wordcontent.model.VerseText;
import com.theword.wordcontent.repository.ContentRepository; 

@RestController
@RequestMapping("/theword/v1/bible")
public class WordContentController {
	
	@Autowired
	ContentRepository contentRepository;
	
	@Autowired
    private RestTemplate restTemplate;
	
    @Autowired
    private EurekaClient eurekaClient;
    
    @Value("${theword.metaservice.serviceId}")
    private String theWordMetaServiceId;

	
	/**
	 * Returns verses for a chapter
	 * @param bibleId
	 * @param bookId
	 * @param chapter
	 * @return
	 */
	@RequestMapping(value="/{bibleId}/{bookId}/{chapter}/{verseNumber}",method=RequestMethod.GET,produces={"application/json; charset=UTF-8"})
	public ResponseEntity<Object> getChapterVerse(@PathVariable String bibleId,@PathVariable String bookId,@PathVariable String chapter,@PathVariable String verseNumber){
		Content content = null;
		Bible bible = null;
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objectNode = mapper.createObjectNode();
		try {
			content = contentRepository.getChapterVerse(bibleId.toUpperCase(),bookId.toUpperCase(),chapter.toUpperCase(),verseNumber.toUpperCase());	
			bible = contentRepository.getBible(bibleId.toUpperCase());
			if(content!=null) {		
				objectNode.put("status", "success");
				objectNode.put("id", bibleId.toUpperCase());
				objectNode.put("language", bible.getLanguage());
				objectNode.put("dir", bible.getDir());
				objectNode.put("number", bookId.toUpperCase());
				objectNode.put("name", content.getBookName());
				objectNode.put("chapter", chapter.toUpperCase());
				ArrayNode bibleJSONArray = mapper.createArrayNode();
				for(Verse verse:content.getVerses()) {
					ObjectNode jsonNode = mapper.createObjectNode();
					jsonNode.put("verse",verse.getVerse());
					jsonNode.put("text",verse.getText());
					bibleJSONArray.add(jsonNode);
				}
				 
				objectNode.putArray("verses").addAll(bibleJSONArray);
			}else{
				objectNode = generateNoDataNode("No verse content found");
			}
		}catch (Exception e) {
			return new ResponseEntity<Object>(generateErrorNode(e), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(objectNode,HttpStatus.OK);
	}
	
	/**
	 * Returns verses for a chapter
	 * @param bibleId
	 * @param bookId
	 * @param chapter
	 * @return
	 */
	@RequestMapping(value="/{bibleId}/{bookId}/{chapter}",method=RequestMethod.GET,produces={"application/json; charset=UTF-8"})
	public ResponseEntity<Object> getChapterVerses(@PathVariable String bibleId,@PathVariable String bookId,@PathVariable String chapter){
		Content content = null;
		Bible bible = null;
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objectNode = mapper.createObjectNode();
		try {
			content = contentRepository.getChapterVerses(bibleId.toUpperCase(),bookId.toUpperCase(),chapter.toUpperCase());	
			bible = contentRepository.getBible(bibleId.toUpperCase());
			if(content!=null) {		
				objectNode.put("status", "success");
				objectNode.put("id", bibleId.toUpperCase());
				objectNode.put("language", bible.getLanguage());
				objectNode.put("dir", bible.getDir());
				objectNode.put("number", bookId.toUpperCase());
				objectNode.put("name", content.getBookName());
				objectNode.put("chapter", chapter.toUpperCase());
				ArrayNode bibleJSONArray = mapper.createArrayNode();
				for(Verse verse:content.getVerses()) {
					ObjectNode jsonNode = mapper.createObjectNode();
					jsonNode.put("verse",verse.getVerse());
					jsonNode.put("text",verse.getText());
					bibleJSONArray.add(jsonNode);
				}
				 
				objectNode.putArray("verses").addAll(bibleJSONArray);
			}else
				objectNode = generateNoDataNode("No verses found for the book and chapter.");
		}catch (Exception e) {
			return new ResponseEntity<Object>(generateErrorNode(e), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(objectNode,HttpStatus.OK);
	}
	
	/**
	 * Returns cross references for a verse
	 * @param bibleId
	 * @param bookId
	 * @param chapter
	 * @return
	 */
	@RequestMapping(value="/{bibleId}/crossreference",method=RequestMethod.GET,produces={"application/json; charset=UTF-8"})
	public ResponseEntity<Object> getCrossRefence(@PathVariable(required=true,name="bibleId") String bibleId,
			@RequestParam(required=true,name="verse") String verse){ 
		List<String> passageCollection;
		String regExpression = "\\d{2}\\s+\\d{1,3}:\\d{1,3}";
		String regChapterVerse = "\\s+\\d{1,3}:\\d{1,3}";
		Pattern pattern = Pattern.compile(regChapterVerse);
		List<VerseText> verses = new ArrayList<VerseText>();
		List<Book> books = null;
		Bible bible = null;
		Map<String, String> bookMap = new HashMap<String, String>();
		Map<String, String> engBookMap = new HashMap<String, String>();
		ObjectNode objectNode = null;
		try {
			if(verse!=null && verse.trim().length()>0){
				//get the Bible Books from Cache
				//Bible Language
				books = contentRepository.getBibleBooks(bibleId.toUpperCase());
				books.forEach(i ->{
					bookMap.put(i.getBookCode(), i.getBookName());
				});
				//English Equivalent
				books = contentRepository.getBibleBooks("ENG");
				books.forEach(i ->{
					engBookMap.put(i.getBookCode(), i.getBookName());
				});
				//Get Bible language details
				bible = contentRepository.getBible(bibleId.toUpperCase());
				passageCollection = new ArrayList<String>();
				if(verse!=null && verse.matches(regExpression)) {
					passageCollection.add(verse);
				}else {
					//check that the book is text
					Matcher matcher =pattern.matcher(verse);
					if(verse!=null && matcher.find()) {
						String rest = matcher.group();
						String book = verse.replace(matcher.group(),"").trim();
						
						//find the language book number						
						bookMap.forEach((k,v) ->{
							if(v.equalsIgnoreCase(book)) {
								passageCollection.add(k + rest);
							}
						});
						
						//Find English Language equivalent book number
						//find the language book number						
						engBookMap.forEach((k,v) ->{
							if(v.equalsIgnoreCase(book)) {
								passageCollection.add(k + rest);
							}
						});
					}
				}
				if(passageCollection.size()>0) {		
					//Call meta Micro-service to get raw cross references
					Application application = eurekaClient.getApplication(theWordMetaServiceId);
					InstanceInfo instanceInfo = application.getInstances().get(0);
					//String url = "http://" + instanceInfo.getIPAddr() + ":" + instanceInfo.getPort() + "theword/v1/bibles/rawcrossreference?verse={v}" ;
					String url = instanceInfo.getHomePageUrl() + "theword/v1/bibles/rawcrossreference?verse={v}";
										
				    ObjectNode metaResponse =  restTemplate.getForObject(url, ObjectNode.class,passageCollection.get(0));
					
					if(metaResponse!=null && metaResponse.get("status").asText().equalsIgnoreCase("success") && metaResponse.withArray("references").size()>0) {
						ArrayNode arraynode = metaResponse.withArray("references");  						
					    //call DB
						verses = contentRepository.getPassages(new ObjectMapper().convertValue(arraynode, ArrayList.class), bibleId);
						if(verses!=null && verses.size()>0)
							objectNode = retrieveJSONResponse(verses, bookMap, engBookMap, bible);
						else
							objectNode = generateNoDataNode("No cross reference verses found.");
					}else
						objectNode = generateNoDataNode("No cross reference verses found.");
				}else					
						return new ResponseEntity<Object>(generateErrorNode(new Exception("Invalid pattern for input passage."
								+ " Please use 'BB CC:VV;BB CC:VV' or complete book name in place of BB")), 
								HttpStatus.INTERNAL_SERVER_ERROR);			
			}else {
				return new ResponseEntity<Object>(generateErrorNode(new Exception("Invalid pattern for input verse. Please use 'BB CC:VV'"
						+ " or use full book name in place of BB")), 
						HttpStatus.INTERNAL_SERVER_ERROR);
			} 
		}catch (Exception e) {e.printStackTrace();
			return new ResponseEntity<Object>(generateErrorNode(e), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(objectNode,HttpStatus.OK);
	}
	
	
	/**
	 * Returns verses for a passage
	 * @param bibleId
	 * @param bookId
	 * @param chapter
	 * @return
	 */
	@RequestMapping(value="/{bibleId}/passage",method=RequestMethod.GET,produces={"application/json; charset=UTF-8"})
	public ResponseEntity<Object> getPassageText(@PathVariable(required=true,name="bibleId") String bibleId,
			@RequestParam(required=true,name="passage") String passage){ 
		List<String> passageCollection;
		String regExpression = "\\d{2}\\s+\\d{1,3}:\\d{1,3}(-\\d{1,3})?";
		String regChapterVerse = "\\s+\\d{1,3}:\\d{1,3}(-\\d{1,3})?";
		Pattern pattern = Pattern.compile(regChapterVerse);
		List<VerseText> verses = new ArrayList<VerseText>();
		List<Book> books = null;
		Bible bible = null;
		Map<String, String> bookMap = new HashMap<String, String>();
		Map<String, String> engBookMap = new HashMap<String, String>();
		ObjectNode objectNode = null;
		try {			
			if(passage!=null && passage.trim().length()>0) {
				//get the Bible Books from Cache
				//Bible Language
				books = contentRepository.getBibleBooks(bibleId.toUpperCase());
				books.forEach(i ->{
					bookMap.put(i.getBookCode(), i.getBookName());
				});
				//English Equivalent
				books = contentRepository.getBibleBooks("ENG");
				books.forEach(i ->{
					engBookMap.put(i.getBookCode(), i.getBookName());
				});
				//Get Bible language details
				bible = contentRepository.getBible(bibleId.toUpperCase());
				passageCollection = new ArrayList<String>();
				
				//Check the passage format. Should support raw and native script
				String[] parts = passage.split(";"); 
				for(String part:parts) {
					if(part!=null && part.matches(regExpression)) {
						passageCollection.add(part);
					}else {
						//check that the book is text
						Matcher matcher =pattern.matcher(part);
						if(part!=null && matcher.find()) {
							String rest = matcher.group();
							String book = part.replace(matcher.group(),"").trim();
							
							//find the language book number						
							bookMap.forEach((k,v) ->{
								if(v.equalsIgnoreCase(book)) {
									passageCollection.add(k + rest);
								}
							});
							
							//Find English Language equivalent book number
							//find the language book number						
							engBookMap.forEach((k,v) ->{
								if(v.equalsIgnoreCase(book)) {
									passageCollection.add(k + rest);
								}
							});
							
						}else					
							return new ResponseEntity<Object>(generateErrorNode(new Exception("Invalid pattern for input passage."
									+ " Please use 'BB CC:VV;BB CC:VV' or complete book name in place of BB")), 
									HttpStatus.INTERNAL_SERVER_ERROR);
					}				
				}	
				if(passageCollection!=null && passageCollection.size()>0) {
					//call DB
					verses = contentRepository.getPassages(passageCollection, bibleId);
					if(verses!=null && verses.size()>0)
						objectNode = retrieveJSONResponse(verses, bookMap, engBookMap, bible);
					else
						objectNode = generateNoDataNode("No passage verses found.");
				}else
					objectNode = generateNoDataNode("No valid passages in the request.");
			}else {
				return new ResponseEntity<Object>(generateErrorNode(new Exception("Invalid pattern for input passage."
						+ " Please use 'BB CC:VV;BB CC:VV' or complete book name in place of BB")), 
						HttpStatus.INTERNAL_SERVER_ERROR);
			}		  
		}catch (Exception e) {
			return new ResponseEntity<Object>(generateErrorNode(e), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(objectNode,HttpStatus.OK);
	}
	
	/**
	 * Returns verses for a chapter
	 * @param bibleId
	 * @param bookId
	 * @param chapter
	 * @return
	 */
	@RequestMapping(value="/{bibleId}/search",method=RequestMethod.GET,produces={"application/json; charset=UTF-8"})
	public ResponseEntity<Object> getSearchPassages(@PathVariable(required=true,name="bibleId") String bibleId,
			@RequestParam(required=true,name="key") String key, @RequestParam(required=false,name="strict") String strict,
			@RequestParam(required=false,name="limit") String limit){ 
		
		List<VerseText> verses = new ArrayList<VerseText>();
		List<Book> books = null;
		Bible bible = null;
		Map<String, String> bookMap = new HashMap<String, String>();
		Map<String, String> engBookMap = new HashMap<String, String>();
		ObjectNode objectNode = null;
		try {			
			if(key!=null && key.trim().length()>0) {
				//get the Bible Books from Cache
				//Bible Language
				books = contentRepository.getBibleBooks(bibleId.toUpperCase());
				books.forEach(i ->{
					bookMap.put(i.getBookCode(), i.getBookName());
				});
				//English Equivalent
				books = contentRepository.getBibleBooks("ENG");
				books.forEach(i ->{
					engBookMap.put(i.getBookCode(), i.getBookName());
				});
				//Get Bible language details
				bible = contentRepository.getBible(bibleId.toUpperCase()); 
				 	
				//call DB
				verses = contentRepository.getSearchPassages(key, strict, bibleId,limit);
				if(verses!=null && verses.size()>0)
					objectNode = retrieveJSONResponse(verses, bookMap, engBookMap, bible);				
				else
					objectNode = generateNoDataNode("No search results for the search criteria.");
			}else {
				return new ResponseEntity<Object>(generateErrorNode(new Exception("Please submit a search request value.")), 
						HttpStatus.INTERNAL_SERVER_ERROR);
			}		  
		}catch (Exception e) {
			return new ResponseEntity<Object>(generateErrorNode(e), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(objectNode,HttpStatus.OK);
	}
	
	/**
	 * Returns verses for a passage
	 * @param bibleId
	 * @param bookId
	 * @param chapter
	 * @return
	 */
	@RequestMapping(value="/{bibleId}/dailyverse",method=RequestMethod.GET,produces={"application/json; charset=UTF-8"})
	public ResponseEntity<Object> getDailyVerse(@PathVariable(required=true,name="bibleId") String bibleId,
			@RequestParam(required=false,name="date") String date){ 
		List<String> passageCollection;
		String regex = "^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}$";		
		List<VerseText> verses = new ArrayList<VerseText>();
		List<Book> books = null;
		Bible bible = null;
		Map<String, String> bookMap = new HashMap<String, String>();
		Map<String, String> engBookMap = new HashMap<String, String>();
		ObjectNode objectNode = null;
		String month = "";
		String day = "";
		try {			
			    if(date!=null && date.matches(regex)) {
			    	month = date.split("/")[0];
			    	day = date.split("/")[1];
			    }else {
			    	LocalDate cal = LocalDate.now();
			    	month = (cal.getMonthValue()<10)?"0"+cal.getMonthValue():String.valueOf(cal.getMonthValue());
			    	day = String.valueOf(cal.getDayOfMonth());			    			
			    }
			
				//get the Bible Books from Cache
				//Bible Language
				books = contentRepository.getBibleBooks(bibleId.toUpperCase());
				books.forEach(i ->{
					bookMap.put(i.getBookCode(), i.getBookName());
				});
				//English Equivalent
				books = contentRepository.getBibleBooks("ENG");
				books.forEach(i ->{
					engBookMap.put(i.getBookCode(), i.getBookName());
				});
				//Get Bible language details
				bible = contentRepository.getBible(bibleId.toUpperCase());
						
				//Get Collection
				passageCollection = contentRepository.getDailyVerse(month, day);				
					
				if(passageCollection!=null && passageCollection.size()>0) {
					Random rand = new Random();
				    int randomNum = rand.nextInt((1 - 0) + 1) + 0;
				    passageCollection.remove(randomNum);
				    List<String> passageList = new ArrayList<String>();
				    passageCollection.forEach(p ->{
				    	String[] lists = p.split(";");
				    	for(String list:lists) {
				    		passageList.add(list);
				    	}
				    });
					//call DB
					verses = contentRepository.getPassages(passageList, bibleId);
					if(verses!=null && verses.size()>0)
						objectNode = retrieveJSONResponse(verses, bookMap, engBookMap, bible);
					else
						objectNode = generateNoDataNode("No passage verses found.");
				}else
					objectNode = generateNoDataNode("No valid passages in the request.");
			  
		}catch (Exception e) {
			return new ResponseEntity<Object>(generateErrorNode(e), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(objectNode,HttpStatus.OK);
	}
	
	/**
	 * @param verses
	 * @param bookMap
	 * @return
	 * @throws Exception
	 */
	private ObjectNode retrieveJSONResponse(List<VerseText> verses, 
			Map<String, String> bookMap, Map<String, String> engBookMap, Bible bible) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objectNode = mapper.createObjectNode();
		//Compose Response
		if(verses!=null) {
			Map<String, Map<Integer, List<ObjectNode>>> parentMap = new TreeMap<String, Map<Integer,List<ObjectNode>>>();			
			verses.forEach(i->
			{
				String parentKey = i.getBook();
				Integer childKey = Integer.valueOf(i.getChapter());
				ObjectNode jsonNode = mapper.createObjectNode();
				jsonNode.put("verse",i.getVerse());
				jsonNode.put("text",i.getText());
				if(parentMap.containsKey(parentKey)) { 
					if(parentMap.get(parentKey).containsKey(childKey)) {
						parentMap.get(parentKey).get(childKey).add(jsonNode);
					}else {						 
						List<ObjectNode> o =  new ArrayList<ObjectNode>();
						o.add(jsonNode);
						parentMap.get(parentKey).put(childKey, o);
					}				
				}else {
					Map<Integer, List<ObjectNode>> tempMap = new TreeMap<Integer, List<ObjectNode>>();
					List<ObjectNode> o =  new ArrayList<ObjectNode>();
					o.add(jsonNode);
					tempMap.put(childKey,o);
					parentMap.put(parentKey, tempMap);
				}
			});					
			
			//Generate Content JSON
			objectNode.put("status", "success");
			objectNode.put("id", bible.getbId());
			objectNode.put("language", bible.getLanguage());
			objectNode.put("dir", bible.getDir()); 
			ArrayNode bibleJSONArray = mapper.createArrayNode();
			parentMap.forEach((k,v)->{				
				ObjectNode jsonNode = mapper.createObjectNode();
				jsonNode.put("book",k);
				jsonNode.put("name",bookMap.get(k));
				jsonNode.put("english",engBookMap.get(k));
				ArrayNode chapterJSONArray = mapper.createArrayNode();
				v.forEach((ck,cv)->{
					ObjectNode cNode = mapper.createObjectNode();
					cNode.put("chapter", ck);
					cv.sort((v1,v2)->{ 
						return Integer.valueOf(v1.get("verse").asText()) - 
								Integer.valueOf(v2.get("verse").asText());
					});
					cNode.putArray("verses").addAll(cv);
					chapterJSONArray.add(cNode);
				});
				jsonNode.putArray("content").addAll(chapterJSONArray);	
				bibleJSONArray.add(jsonNode);
			});					 
			objectNode.putArray("passages").addAll(bibleJSONArray);						
		}
		
		return objectNode;
	}
	  
	/**
	 * @param e
	 * @return
	 */
	private ObjectNode generateNoDataNode(String message) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode errorNode = mapper.createObjectNode();
		errorNode.put("status", "Error");
		errorNode.put("ErrorMessage", "No Data found");
		errorNode.put("errorDescription", message);
		return errorNode;
	}
	
	/**
	 * @param e
	 * @return
	 */
	private Object generateErrorNode(Exception e) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode errorNode = mapper.createObjectNode();
		errorNode.put("status", "Error");
		errorNode.put("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.toString());
		errorNode.put("ErrorMessage", e.getLocalizedMessage());
		errorNode.put("errorDescription", e.getMessage());
		return errorNode;
	}

}
