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

  getNextChapter(book: string, chapter: string){       
    var url = API_URL + this.helper.getLang() + '/nextChapter';
    if(book && chapter){
      url = url + '/' + book + '/' + chapter;
    }else{
      url = url + '/01/1';
    }
     
    return this.http.get(url);
  }

  getPreviousChapter(book: string, chapter: string){       
    var url = API_URL + this.helper.getLang() + '/previousChapter';
    if(book && chapter){
      url = url + '/' + book + '/' + chapter;
    }else{
      url = url + '/01/1';
    }
    return this.http.get(url);
  }
  
}
