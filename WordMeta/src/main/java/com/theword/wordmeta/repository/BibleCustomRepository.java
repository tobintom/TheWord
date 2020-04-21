package com.theword.wordmeta.repository;

import com.theword.wordmeta.model.Bible;
import com.theword.wordmeta.model.Book;

public interface BibleCustomRepository {

	Bible getBibleBooks(String id);
	Book getBookChapters(String bibleId, String bookCode);
	Book getChapterVerses(String bibleId, String bookCode,String chapter);
}
