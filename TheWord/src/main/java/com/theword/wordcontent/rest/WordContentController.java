package com.theword.wordcontent.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.theword.wordcontent.model.Bible;
import com.theword.wordcontent.model.Content;
import com.theword.wordcontent.model.Verse;
import com.theword.wordcontent.repository.ContentRepository; 

@RestController
@RequestMapping("/theword/v1/bible")
public class WordContentController {
	
	@Autowired
	ContentRepository contentRepository;
	
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
