import { Component, OnInit } from '@angular/core';
import { AppHelper } from 'src/app/service/app.helper';
import { WordContentService } from 'src/app/service/word-content.service';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  foods: String[] = ['ONE','TWO','THREE'];
  selectedValue: string;
  verses: Array<any>;
  books: Array<any>;
  chapters: Array<any>;
  dir: string;
  book: string;
  englishName: string;
  chapter: string;
  lang: string;
  bgurl: string;
  icon: string;
  align: string;
  anio: number = new Date().getFullYear();

  constructor(private contentService: WordContentService, private appHelper: AppHelper) { 
    this.bgurl = '../../../assets/images/bg/' + this.appHelper.randomNumber(1,16) + '.jpg';
  }

  ngOnInit(): void {

    this.contentService.getChapter(this.appHelper.getBook(),this.appHelper.getChapter()).subscribe(data=>{
      if(data && data["verses"]){
        this.book = data["name"];
        this.englishName = data["english"];
        this.chapter = data["chapter"];
        this.dir = data["dir"];     
        this.lang = data["id"];   
        this.verses = data["verses"];
        this.icon = '../../../assets/images/books/' + data["number"] + '.png';
        if(this.dir==='LTR'){
            this.align = "left";
        }else{
            this.align = "right";
        }
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
  
}

