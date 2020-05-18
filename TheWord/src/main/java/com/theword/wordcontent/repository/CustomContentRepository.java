package com.theword.wordcontent.repository;

import java.util.List;

import com.theword.wordcontent.model.Bible;
import com.theword.wordcontent.model.Book;
import com.theword.wordcontent.model.Content;
import com.theword.wordcontent.model.VerseText;

public interface CustomContentRepository {
	
	Content getChapterVerses(String bibleId, String bookCode,String chapter);
	Content getChapterVerse(String bibleId, String bookCode,String chapter,String verse);
	Bible getBible(String bibleId);	
	List<Book> getBibleBooks(String language);
	List<VerseText> getPassages(List<String> passages,String bibleID);
	List<VerseText> getSearchPassages(String key,String strict,String bibleID, String limit);
}