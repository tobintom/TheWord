package com.theword.wordcontent.repository;

import com.theword.wordcontent.model.Bible;
import com.theword.wordcontent.model.Content;

public interface CustomContentRepository {
	
	Content getChapterVerses(String bibleId, String bookCode,String chapter);
	Content getChapterVerse(String bibleId, String bookCode,String chapter,String verse);
	Bible getBible(String bibleId);

}
