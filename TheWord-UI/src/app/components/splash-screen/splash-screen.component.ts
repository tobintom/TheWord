import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { Router } from '@angular/router';
import { AppHelper } from 'src/app/service/app.helper';
import { AuthService } from 'src/app/service/auth.service';
import { WordContentService } from 'src/app/service/word-content.service';

@Component({
  selector: 'app-splash-screen',
  templateUrl: './splash-screen.component.html',
  styleUrls: ['./splash-screen.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class SplashScreenComponent implements OnInit {
  windowWidth: string;
  showSplash: boolean = true;
  showDiv: boolean = true;
  opacityChange:number = 1;
  splashTransition: string;

  animationDuration:number = 1.1;
  duration:number = 3.5;
  backgroundUrl: string;
  dailyVerse: string;
  book: string;
  chapter: string;
  verse: string;
  dir: string;
  chapterVerse: string;
  
  constructor(private contentService: WordContentService, private appHelper: AppHelper,private router: Router) {
    this.backgroundUrl = '../../../assets/images/bg/' + this.appHelper.randomNumber(1,16) + '.jpg';       
   }

  ngOnInit(): void {
    this.contentService.getRandomDailyVerse().subscribe(data=>{
      if(data && data['passages']){
        this.book = data['passages'][0]['name'];
        this.chapter = data['passages'][0]['content'][0]['chapter'];        
        this.dailyVerse = '';
        this.verse = '';
        for(let verse of data['passages'][0]['content'][0]['verses']){         
          this.dailyVerse += verse['text'];
          if(this.verse.length>0){
            this.verse += ';'+verse['verse'];
          }else{
            this.verse += verse['verse'];
          }
        }      
        this.dir = data['dir'];
        this.chapterVerse = this.book +' '+this.chapter +':'+this.verse;

        setTimeout(() => {
          this.splashTransition = 'opacity ' + this.animationDuration + 's';
          this.opacityChange = 0;
          this.router.navigate(["home"]);
          setTimeout(() => {
            this.showSplash = !this.showSplash;       
          }, this.animationDuration * 1000);
        },this.duration * 1000);
      }
    })     
  } 
}
 
