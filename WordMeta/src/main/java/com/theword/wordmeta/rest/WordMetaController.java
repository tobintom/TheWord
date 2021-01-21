package com.theword.wordmeta.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.theword.wordmeta.model.Bible;
import com.theword.wordmeta.model.Book;
import com.theword.wordmeta.model.CrossReference;
import com.theword.wordmeta.repository.BibleRepository;
import com.theword.wordmeta.repository.DataComponent;

@RestController
@RequestMapping("/theword/v1/bibles")
public class WordMetaController {

	@Autowired
	BibleRepository _bibleRepository;
	
	@Autowired
	DataComponent _dataComponent;
	
	/**
	 * Returns List of Supported Bible Languages
	 * @return
	 */
	/**
	 * @param bibleId
	 * @return
	 */
	@RequestMapping(value="",method=RequestMethod.GET, produces={"application/json; charset=UTF-8"})
	public ResponseEntity<Object> getBibles(){		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		ArrayNode bibleJSONArray = mapper.createArrayNode();
		try {
			//List<Bible> bibles = _bibleRepository.findAll(Sort.by(Direction.ASC, "language"));	
			List<Bible> bibles = _dataComponent.getBibles();
	        if(bibles!=null && bibles.size()>0) {
	        	node.put("status", "success");
				for (Bible b : bibles) {
		        	ObjectNode objectNode = mapper.createObjectNode();		        	 
		            objectNode.put("id", b.getbId());
		            objectNode.put("language", b.getLanguage());
		            objectNode.put("direction", b.getDir());		
		            bibleJSONArray.add(objectNode);
		        }
				node.putArray("bibles").addAll(bibleJSONArray);
	        }else {
	        	node = generateNoDataNode("No Bible informaton found");
	        }
		}catch (Exception e) {
			return new ResponseEntity<Object>(generateErrorNode(e), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
        return new ResponseEntity<Object>(node, HttpStatus.OK);		
	}
	
	/**
	 * Returns books of a specified Bible
	 * @param bibleId
	 * @return
	 */
	@RequestMapping(value="/{bibleId}/books",method=RequestMethod.GET,produces={"application/json; charset=UTF-8"})
	public ResponseEntity<Object> getBibleBooks(@PathVariable String bibleId){
		Bible bible = null;		 
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objectNode = mapper.createObjectNode();
		Map<String, String> engMap = new HashMap<String, String>();
		try {
			bible = _bibleRepository.getBibleBooks(bibleId.toUpperCase());
			engMap = _dataComponent.getEnglishBooks();
			
			if(bible!=null && bible.getBooks()!=null) {
				objectNode.put("status", "success");
				objectNode.put("id", bible.getbId());
				objectNode.put("direction", bible.getDir());
				objectNode.put("language", bible.getLanguage());
				ArrayNode bibleJSONArray = mapper.createArrayNode();
				for(Book b:bible.getBooks()) {
					ObjectNode book = mapper.createObjectNode();
					book.put("number",b.getBookCode());
					book.put("name", b.getBookName());
					book.put("english", engMap.get(b.getBookCode()) );
					bibleJSONArray.add(book);
				}			
				objectNode.putArray("books").addAll(bibleJSONArray);
			}else
				objectNode = generateNoDataNode("No books found for submitted Bible");
		}catch (Exception e) {
			return new ResponseEntity<Object>(generateErrorNode(e), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(objectNode,HttpStatus.OK);
	}
	
	
	/**
	 * Returns Chapters for a Book
	 * @param bibleId
	 * @param bookId
	 * @return
	 */
	@RequestMapping(value="/{bibleId}/{bookId}/chapters",method=RequestMethod.GET,produces={"application/json; charset=UTF-8"})
	public ResponseEntity<Object> getBookChapters(@PathVariable String bibleId,@PathVariable String bookId){
		Book book = null;
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objectNode = mapper.createObjectNode();
		Set<Integer> chapterSet = new TreeSet<Integer>();
		try {
			book = _bibleRepository.getBookChapters(bibleId.toUpperCase(),bookId.toUpperCase());	
			if(book!=null) {
				Map<String, String> engMap = _dataComponent.getEnglishBooks();
				objectNode.put("status", "success");
				objectNode.put("id", bibleId.toUpperCase());
				objectNode.put("number", bookId.toUpperCase());
				objectNode.put("name", book.getBookName());
				objectNode.put("english", engMap.get(book.getBookCode()));
				ArrayNode bibleJSONArray = mapper.createArrayNode();
				for(String chapter:book.getChapters()) {
					JsonNode jsonNode = mapper.readTree(chapter);
					String chapterString = jsonNode.get("chapter").asText();
					chapterSet.add(Integer.valueOf(chapterString));
				}
				for(int c:chapterSet) {
					bibleJSONArray.add(c);
				}
				objectNode.putArray("chapters").addAll(bibleJSONArray);
			}else
				objectNode = generateNoDataNode("No Chapters found for the Bible and Book submitted.");
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
	@RequestMapping(value="/{bibleId}/{bookId}/{chapter}/verses",method=RequestMethod.GET,produces={"application/json; charset=UTF-8"})
	public ResponseEntity<Object> getChapterVerses(@PathVariable String bibleId,@PathVariable String bookId,@PathVariable String chapter){
		Book book = null;
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objectNode = mapper.createObjectNode();
		Set<Integer> chapterSet = new TreeSet<Integer>();
		try {
			book = _bibleRepository.getChapterVerses(bibleId.toUpperCase(),bookId.toUpperCase(),chapter.toUpperCase());	
			if(book!=null) {		
				objectNode.put("status", "success");
				objectNode.put("id", bibleId.toUpperCase());
				objectNode.put("number", bookId.toUpperCase());
				objectNode.put("name", book.getBookName());
				objectNode.put("chapter", chapter.toUpperCase());
				ArrayNode bibleJSONArray = mapper.createArrayNode();
				for(String verse:book.getVerses()) {
					JsonNode jsonNode = mapper.readTree(verse);
					String verseString = jsonNode.get("verse").asText();
					chapterSet.add(Integer.valueOf(verseString));
				}
				for(int c:chapterSet) {
					bibleJSONArray.add(c);
				}
				objectNode.putArray("verses").addAll(bibleJSONArray);
			}else
				objectNode = generateNoDataNode("No verses found for the submitted Bible, Book and Chapter combination.");
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
	@RequestMapping(value="/rawcrossreference",method=RequestMethod.GET,produces={"application/json; charset=UTF-8"})
	public ResponseEntity<Object> getRawCrossRefence(@RequestParam(required=true,name="verse") String verse){
		CrossReference crossReference = new CrossReference();		  
		String regEx = "\\d{2}\\s+\\d{1,3}:\\d{1,3}";
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objectNode = mapper.createObjectNode();
		try {
			if(verse!=null && verse.matches(regEx)){				
				String book = verse.split("\\s+")[0];
				String chapter = verse.split("\\s+")[1].split(":")[0];
				String verseNumber = verse.split("\\s+")[1].split(":")[1];
				List<CrossReference> crossReferenceList = _bibleRepository.getRawCrossReference(book, chapter, verseNumber);
				if(crossReferenceList!=null && crossReferenceList.size()>0) {
					crossReference = crossReferenceList.get(0);
					objectNode.put("status", "success");
					objectNode.put("book", crossReference.getBook());
					objectNode.put("chapter", crossReference.getChapter());
					objectNode.put("verse", crossReference.getVerse());
					ArrayNode bibleJSONArray = mapper.createArrayNode();
					crossReference.getReferences().forEach(i ->{
						bibleJSONArray.add(i);
					});					
					objectNode.putArray("references").addAll(bibleJSONArray);
				}else
					objectNode = generateNoDataNode("No cross references found for the selected passage. ");
			}else {
				return new ResponseEntity<Object>(generateErrorNode(new Exception("Invalid pattern for input verse. Please use 'BB CC:VV'")), 
						HttpStatus.INTERNAL_SERVER_ERROR);
			} 
		}catch (Exception e) {
			return new ResponseEntity<Object>(generateErrorNode(e), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(objectNode,HttpStatus.OK);
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
