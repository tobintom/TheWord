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
import com.theword.wordcontent.config.ChapterResponse;
import com.theword.wordcontent.config.PassageResponse;
import com.theword.wordcontent.model.Bible;
import com.theword.wordcontent.model.Book;
import com.theword.wordcontent.model.Content;
import com.theword.wordcontent.model.Verse;
import com.theword.wordcontent.model.VerseText;
import com.theword.wordcontent.repository.ContentRepository;
import com.theword.wordcontent.repository.DataComponent;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/theword/v1/bible")
@Api(produces="application/json",value="The Word Content API")
public class WordContentController {
	
	@Autowired
	ContentRepository contentRepository;
	
	@Autowired
	DataComponent dataComponent;
	
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
    @ApiOperation(value="Retrieves the verse text of the selected Book, chapter and verse.",notes="Returns the verse text of the selected Bible language, Book, chapter and verse."
    		+ " The Bible ID is the language code, the bookid is the book code, chapter and verse number. e.g /ENG/01/1/1 retrieves the english"
    		+ " text of Genesis 1:1")
    @ApiResponses({
	    @ApiResponse(code=200,message="success",response=ChapterResponse.class)
    })
	@RequestMapping(value="/{bibleId}/{bookId}/{chapter}/{verseNumber}",method=RequestMethod.GET,produces={"application/json; charset=UTF-8"})
	public ResponseEntity<Object> getChapterVerse(@ApiParam(value = "Code that represents the language",
	        required = true, example="ENG") 
	@PathVariable String bibleId,
	@ApiParam(value = "Code that represents the Bible Book", required = true, example="01")
	@PathVariable String bookId,
	@ApiParam(value = "Chapter of Bible Book",required = true, example="1")
	@PathVariable String chapter,
	@ApiParam(value = "Verse to be retrieved",required = true, example="1")
	@PathVariable String verseNumber){
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
				objectNode.put("english", dataComponent.getEnglishBooks().get(content.getBookCode()));
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
    @ApiOperation(value="Retrieves all the verses of a Bible chapter.",notes="Returns all the verse text of the selected Bible language, Book and chapter."
    		+ " The Bible ID is the language code, the bookid is the book code and the selected chapter. e.g. /ENG/01/1 retrieves the english text of Genesis 1.")
    @ApiResponses({
	    @ApiResponse(code=200,message="success",response=ChapterResponse.class)
    })
    @RequestMapping(value="/{bibleId}/{bookId}/{chapter}",method=RequestMethod.GET,produces={"application/json; charset=UTF-8"})
	public ResponseEntity<Object> getChapterVerses(
			@ApiParam(value = "Code that represents the language",required = true, example="ENG") 
			@PathVariable String bibleId,
			@ApiParam(value = "Code that represents the Bible Book",required = true, example="01") 
			@PathVariable String bookId,
			@ApiParam(value = "Chapter of the Bible Book",required = true, example="1") 
			@PathVariable String chapter){
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
				objectNode.put("english", dataComponent.getEnglishBooks().get(content.getBookCode()));
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
	 * Returns verses for the next chapter
	 * @param bibleId
	 * @param bookId
	 * @param chapter
	 * @return
	 */
    @ApiOperation(value="Retrieves all the verses of the next Bible chapter, from the submitted one.",notes="Returns all the verse text of the next chapter of selected Bible language, Book and chapter."
    		+ " The Bible ID is the language code. Request parameters are the Book Code and current chapter. E.g. /ENG/nextChapter?bookId=01&chapter=1,  returns"
    		+ " all the english text of Genesis chapter 2.")
    @ApiResponses({
	    @ApiResponse(code=200,message="success",response=ChapterResponse.class)
    })
    @RequestMapping(value="/{bibleId}/nextChapter",method=RequestMethod.GET,produces={"application/json; charset=UTF-8"})
	public ResponseEntity<Object> getNextChapterVerses(
			@ApiParam(value = "Code that represents the language",required = true, example="ENG") 
			@PathVariable String bibleId,
			@ApiParam(value = "Code that represents the Bible Book",required = true, example="01") 
			@RequestParam(required=true,name="bookId") String bookId, 
			@ApiParam(value = "Current Chapter of the Bible Book",required = true, example="1") 
			@RequestParam(required=true,name="chapter") String chapter){
		Content content = null;
		Bible bible = null;
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objectNode = mapper.createObjectNode();
		try {
			String newNav = getNext(bookId, chapter);
			String newBook = newNav!=null?newNav.substring(0, newNav.indexOf(":")):"";
			String newChapter = newNav!=null?newNav.substring(newNav.indexOf(":")+1):"";
			content = contentRepository.getChapterVerses(bibleId.toUpperCase(),newBook.toUpperCase(),newChapter.toUpperCase());	
			bible = contentRepository.getBible(bibleId.toUpperCase());
			if(content!=null) {		
				objectNode.put("status", "success");
				objectNode.put("id", bibleId.toUpperCase());
				objectNode.put("language", bible.getLanguage());
				objectNode.put("dir", bible.getDir());
				objectNode.put("number", newBook.toUpperCase());
				objectNode.put("name", content.getBookName());
				objectNode.put("english", dataComponent.getEnglishBooks().get(content.getBookCode()));
				objectNode.put("chapter", newChapter.toUpperCase());
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
		
	private String getNext(String book, String chapter) {
		String newNavigation = "";
		if(book!=null && book.trim().equalsIgnoreCase("66") &&
				chapter!=null && chapter.trim().equalsIgnoreCase("22")) {
			return "01:1";
		}
		int total =  dataComponent.getBibleChapters().get(book);
		if(total>Integer.valueOf(chapter)) {
			newNavigation = book+":"+ (Integer.valueOf(chapter)+1);
		}else {
			newNavigation = dataComponent.getBibleChapters().higherKey(book)+":1";
		}		
		return newNavigation;
	}
	
	/**
	 * Returns verses for the previous chapter
	 * @param bibleId
	 * @param bookId
	 * @param chapter
	 * @return
	 */
	@ApiOperation(value="Retrieves all the verses of the previous Bible chapter, from the submitted one.",notes="Returns all the verse text of the previous chapter of selected Bible language, Book and chapter."
    		+ " The Bible ID is the language code. Request parameters are the Book Code and current chapter. E.g. /ENG/previousChapter?bookId=01&chapter=2, returns"
    		+ " the english text of Genesis chapter 1.")
	@ApiResponses({
	    @ApiResponse(code=200,message="success",response=ChapterResponse.class)
    })
	@RequestMapping(value="/{bibleId}/previousChapter",method=RequestMethod.GET,produces={"application/json; charset=UTF-8"})
	public ResponseEntity<Object> getPreviousChapterVerses(
			@ApiParam(value = "Code that represents the language",required = true, example="ENG") 
			@PathVariable String bibleId,
			@ApiParam(value = "Code that represents the Bible Book",required = true, example="01") 
			@RequestParam(required=true,name="bookId") String bookId, 
			@ApiParam(value = "Current Chapter of the Bible Book",required = true, example="2") 
			@RequestParam(required=true,name="chapter") String chapter){
		Content content = null;
		Bible bible = null;
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objectNode = mapper.createObjectNode();
		try {
			String newNav = getPrevious(bookId, chapter);
			String newBook = newNav!=null?newNav.substring(0, newNav.indexOf(":")):"";
			String newChapter = newNav!=null?newNav.substring(newNav.indexOf(":")+1):"";
			content = contentRepository.getChapterVerses(bibleId.toUpperCase(),newBook.toUpperCase(),newChapter.toUpperCase());	
			bible = contentRepository.getBible(bibleId.toUpperCase());
			if(content!=null) {		
				objectNode.put("status", "success");
				objectNode.put("id", bibleId.toUpperCase());
				objectNode.put("language", bible.getLanguage());
				objectNode.put("dir", bible.getDir());
				objectNode.put("number", newBook.toUpperCase());
				objectNode.put("name", content.getBookName());
				objectNode.put("english", dataComponent.getEnglishBooks().get(content.getBookCode()));
				objectNode.put("chapter", newChapter.toUpperCase());
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

	private String getPrevious(String book, String chapter) {
		String newNavigation = "";
		if(book!=null && book.trim().equalsIgnoreCase("01") &&
			chapter!=null && chapter.trim().equalsIgnoreCase("1")){
				return "66:22";
			}
		int total = dataComponent.getBibleChapters().get(book);
		if(total>=Integer.valueOf(chapter) && Integer.valueOf(chapter)>1) {
			newNavigation = book+":"+ (Integer.valueOf(chapter)-1);
		}else {
			String key = dataComponent.getBibleChapters().lowerKey(book);
			newNavigation = key+":"+dataComponent.getBibleChapters().get(key);
		}
		
		return newNavigation;
	}
	
	/**
	 * Returns cross references for a verse
	 * @param bibleId
	 * @param bookId
	 * @param chapter
	 * @return
	 */
	@ApiOperation(value="Retrieves all the cross references in the Bible of a selected verse.",notes="Retrieves all the cross references in the Bible of a selected verse. The cross reference has to be a Book Chapter:verse."
			+ " The book has to be the book code. e.g. ENG/crossreference?verse=01 1:1, returns all the available cross references in english for "
			+ "Genesis 1:1.")
	@ApiResponses({
	    @ApiResponse(code=200,message="success",response=PassageResponse.class)
    })
	@RequestMapping(value="/{bibleId}/crossreference",method=RequestMethod.GET,produces={"application/json; charset=UTF-8"})
	public ResponseEntity<Object> getCrossRefence(
			@ApiParam(value = "Code that represents the language",required = true, example="ENG") 
			@PathVariable(required=true,name="bibleId") String bibleId,
			@ApiParam(value = "Verse of the Bible for Cross Reference Lookup",required = true, example="01 1:1") 
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
	@ApiOperation(value="Retrieves all the passages submitted.",notes="Retrieves all the passages submitted. The input can be with book code or book name (in corresponding language)."
			+ " Multiple passages "
			+ "need to be separated by ;"
			+ "e.g /ENG/passage?passage=john 1:1,2-5;01 1:12-25;genesis 1:1  returns the english text of John 1 verses 1 and 2 to 5,"
			+ " Genesis 1 verses 12 to 25 and also Genesis 1 verse 1.")
	@ApiResponses({
	    @ApiResponse(code=200,message="success",response=PassageResponse.class)
    })
	@RequestMapping(value="/{bibleId}/passage",method=RequestMethod.GET,produces={"application/json; charset=UTF-8"})
	public ResponseEntity<Object> getPassageText(
			@ApiParam(value = "Code that represents the language",required = true, example="ENG") 
			@PathVariable(required=true,name="bibleId") String bibleId,
			@ApiParam(value = "Passage(s) to be retrieved",required = true, example="01 1:1,2;John 3:16;Matthew 1:1-10") 
			@RequestParam(required=true,name="passage") String passage){ 
		List<String> passageCollection;
		String regExpression = "\\d{2}\\s+\\d{1,3}:((\\d{1,3}\\,(?=\\d{1,3}))|(\\d{1,3}\\-(?=\\d))|\\d{1,3})+";
		String regChapterVerse = "\\s+\\d{1,3}:((\\d{1,3}\\,(?=\\d{1,3}))|(\\d{1,3}\\-(?=\\d))|\\d{1,3})+";
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
	@ApiOperation(value="Retrieves all the passages that contain a key word submitted.",notes="Retrieves all the passages that contain a key word submitted. "
			+ "e.g /ENG/search?key=ten horns  returns the english text of all passages that "
			+ "contain the phrase 'ten horns'. Options parameters: strict (default true) indicates strict/partial search."
			+ " limit (default 20) the number of maximum passages to return.")
	@ApiResponses({
	    @ApiResponse(code=200,message="success",response=PassageResponse.class)
    })
	@RequestMapping(value="/{bibleId}/search",method=RequestMethod.GET,produces={"application/json; charset=UTF-8"})
	public ResponseEntity<Object> getSearchPassages(
			@ApiParam(value = "Code that represents the language",required = true, example="ENG") 
			@PathVariable(required=true,name="bibleId") String bibleId,
			@ApiParam(value = "Key Phrase to search",required = true, example="Ten Horns") 
			@RequestParam(required=true,name="key") String key, 
			@ApiParam(value = "Flag to determine strict search",required = false, example="true", defaultValue="true") 
			@RequestParam(required=false,name="strict") String strict,
			@ApiParam(value = "Maximum matched verses to return",required = false, example="25", defaultValue="20") 
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
	@ApiOperation(value="Retrieves a Bible verse of the day.",notes="Retrieves a Bible verse of the day. Date parameter is optional.")
	@ApiResponses({
	    @ApiResponse(code=200,message="success",response=PassageResponse.class)
    })
	@RequestMapping(value="/{bibleId}/dailyverse",method=RequestMethod.GET,produces={"application/json; charset=UTF-8"})
	public ResponseEntity<Object> getDailyVerse(
			@ApiParam(value = "Code that represents the language",required = true, example="ENG") 
			@PathVariable(required=true,name="bibleId") String bibleId,
			@ApiParam(value = "Date",required = false, example="01/01/2020") 
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
			    	day = cal.getDayOfMonth()<10?"0"+cal.getDayOfMonth():String.valueOf(cal.getDayOfMonth());			    			
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
	 * Returns verses for a passage
	 * @param bibleId
	 * @param bookId
	 * @param chapter
	 * @return
	 */
	@ApiOperation(value="Retrieves a random Bible verse.",notes="Retrieves a random Bible verse. Date parameter is optional.")
	@ApiResponses({
	    @ApiResponse(code=200,message="success",response=PassageResponse.class)
    })
	@RequestMapping(value="/{bibleId}/randomDailyVerse",method=RequestMethod.GET,produces={"application/json; charset=UTF-8"})
	public ResponseEntity<Object> getRandomDailyVerse(
			@ApiParam(value = "Code that represents the language",required = true, example="ENG") 
			@PathVariable(required=true,name="bibleId") String bibleId,
			@ApiParam(value = "Date",required = false, example="01/01/2020") 
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
			    	day = cal.getDayOfMonth()<10?"0"+cal.getDayOfMonth():String.valueOf(cal.getDayOfMonth());			    			
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
				passageCollection = contentRepository.getRandomDailyVerse(month, day);				
					
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
