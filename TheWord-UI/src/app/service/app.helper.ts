import { Injectable } from "@angular/core";

const LANG = 'lang';
const ALERT = 'alert';
const BOOK = 'book';
const CHAPTER = 'chapter';
 var showSplash = true;

@Injectable({
    providedIn: 'root'
  })
export class AppHelper{

    constructor(){}

    getLang(): string {
     if(localStorage.getItem(LANG)){
         return localStorage.getItem(LANG);
     }else{
         this.saveLang("eng"); 
         return 'eng';
     }
  }

  getAlert(): string {
    if(localStorage.getItem(ALERT)){
        return localStorage.getItem(ALERT);
    }else{
        this.saveAlert("Y"); 
        return 'Y';
    }
 }

 saveAlert(alert: string): void {
  localStorage.setItem(ALERT, alert);
}

  saveLang(lang: string): void {
    localStorage.setItem(LANG, lang);
  }

  removeLang(): void {
    localStorage.removeItem(LANG);
  }

  getBook(): string {
    if(localStorage.getItem(BOOK)){
        return localStorage.getItem(BOOK);
    }else{
        this.saveBook("01"); 
        return '01';
    }
 }

 saveBook(book: string): void {
   localStorage.setItem(BOOK, book);
 }

 removeBook(): void {
   localStorage.removeItem(BOOK);
 }

 getChapter(): string {
  if(localStorage.getItem(CHAPTER)){
      return localStorage.getItem(CHAPTER);
  }else{
      this.saveChapter("1"); 
      return '1';
  }
}

saveChapter(chapter: string): void {
 localStorage.setItem(CHAPTER, chapter);
}

removeChapter(): void {
 localStorage.removeItem(CHAPTER);
} 

  getShowSplash(): boolean{
      return showSplash;
  }

  setShowSplash(showSplash1: boolean): void{
      showSplash = showSplash1;
  }

  randomNumber(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
  }

 
}