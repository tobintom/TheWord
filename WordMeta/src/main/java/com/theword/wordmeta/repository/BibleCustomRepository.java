package com.theword.wordmeta.repository;

import java.util.List;

import com.theword.wordmeta.model.Bible;
import com.theword.wordmeta.model.Book;
import com.theword.wordmeta.model.CrossReference;

public interface BibleCustomRepository {

	Bible getBibleBooks(String id);
	Book getBookChapters(String bibleId, String bookCode);
	Book getChapterVerses(String bibleId, String bookCode,String chapter);
	List<CrossReference> getRawCrossReference(String book, String chapter, String verse);
}
