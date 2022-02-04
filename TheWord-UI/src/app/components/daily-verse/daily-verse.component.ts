import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AppHelper } from 'src/app/service/app.helper';
import { WordContentService } from 'src/app/service/word-content.service';

@Component({
  selector: 'app-daily-verse',
  templateUrl: './daily-verse.component.html',
  styleUrls: ['./daily-verse.component.scss']
})
export class DailyVerseComponent implements OnInit {
  verses: Array<any>;
  lang: string;
  dir: string;
  book: string;
  englishName: string;
  chapter: string; 
  bgurl: string;
  bookID: string;
  align: string;
  anio: number = new Date().getFullYear();

  constructor(private contentService: WordContentService, private appHelper: AppHelper, private router: Router) { 
    this.bgurl = '../../../assets/images/bg/' + this.appHelper.randomNumber(1,16) + '.jpg';
  }

  ngOnInit(): void {
    this.contentService.getDailyVerse().subscribe(data=>{
      if(data && data["passages"]){
        this.bookID = data["passages"][0]["book"];
        this.book = data["passages"][0]["name"];
        this.englishName = data["passages"][0]["english"];
        this.lang = data["id"];
        this.dir = data["dir"]; 
        if(this.dir==='LTR'){
          this.align = "left";
       }else{
          this.align = "right";
        }
        this.chapter = data["passages"][0]["content"][0]["chapter"];               
        this.verses = data["passages"][0]["content"][0]["verses"];
      }
    });
  }

  isEng(){
    if(this.lang === 'ENG'){
      return true;
    }else{
      return false;
    }
  }

  chapterClick(){
    this.appHelper.saveBook(this.bookID);
    this.appHelper.saveChapter(this.chapter);
    this.router.navigateByUrl('/home');
  }
}
