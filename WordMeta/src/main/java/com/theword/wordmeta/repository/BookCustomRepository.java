package com.theword.wordmeta.repository;

import com.theword.wordmeta.model.Book;

public interface BookCustomRepository {
	
	Book getBookChapters(String bibleId, String bookCode);

}
