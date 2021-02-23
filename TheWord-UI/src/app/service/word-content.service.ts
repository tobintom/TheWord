import { HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { tap,catchError  } from 'rxjs/operators';
import { AppHelper } from './app.helper';
import { AuthService } from './auth.service';

const API_URL = 'http://192.168.99.100:5000/theword/v1/bible/';

@Injectable({
  providedIn: 'root'
})
export class WordContentService {

 constructor(private http: HttpClient, 
    private authService: AuthService,
    private helper: AppHelper) { }

  getDailyVerse(){   
    return this.http.get(API_URL+ 
                    this.helper.getLang() +
                          '/dailyverse');
  }

  getRandomDailyVerse(){   
    return this.http.get(API_URL+ 
                    this.helper.getLang() +
                          '/randomDailyVerse');
  }

  getChapter(book: string, chapter: string){       
    var url = API_URL + this.helper.getLang();
    if(book && chapter){
      url = url + '/' + book + '/' + chapter;
    }else{
      url = url + '/01/1';
    }
    return this.http.get(url);
  }

  getLangChapter(book: string, chapter: string, lang:string){       
    var url = API_URL;
    if(book && chapter && lang){
      url = url +lang+ '/' + book + '/' + chapter;
      return this.http.get(url);
    }else
    return null;    
  }

  getNextChapter(book: string, chapter: string){       
    var url = API_URL + this.helper.getLang() + '/nextChapter';
    if(book && chapter){
      url = url +'?bookId='+book +'&chapter=' + chapter;
    }else{
      url = url +'?bookId=01&chapter=1';
    }
     
    return this.http.get(url);
  }

  getPreviousChapter(book: string, chapter: string){       
    var url = API_URL + this.helper.getLang() + '/previousChapter';
    if(book && chapter){
      url = url +'?bookId='+book +'&chapter=' + chapter;
    }else{
      url = url +'?bookId=01&chapter=1';
    }
    return this.http.get(url);
  }
  
  getCrossReferences(book: string, chapter: string, verse: string){
    var url = API_URL + this.helper.getLang() + '/crossreference?verse=';
    if(book && chapter && verse){
      url = url + book+' '+chapter+':'+verse
    }
    return this.http.get(url);
  }

  getPassages(passage: string){
    var url = API_URL + this.helper.getLang() + '/passage?passage=';
    if(passage){
      url = url + passage
    }
    return this.http.get(url);
  }

  getSearch(search: string){
    var url = API_URL + this.helper.getLang() + '/search?key=';
    if(search){
      url = url + search;
    }
    return this.http.get(url);
  }

}
