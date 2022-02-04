import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AppHelper } from 'src/app/service/app.helper';
import { WordContentService } from 'src/app/service/word-content.service';


@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {
  search: string = 'false';
  searchInput: string;
  verses: Array<any>;   
  book: string;
  englishName: string;
  chapter: string;
  bgurl: string;
  dir: string ="LTR";
  align: string = "left";
  passages: Array<any> = [];
  lang: string;
  anio: number = new Date().getFullYear();
  
  constructor(private contentService: WordContentService, private appHelper: AppHelper,private router: Router) { 
    this.bgurl = '../../../assets/images/bg/' + this.appHelper.randomNumber(1,16) + '.jpg';
    this.search = 'false';
  }

  ngOnInit(): void {
  }

  searchVal(val: any){
    this.search = 'true';
    var doSearch = true; 
    this.passages = [];   
    let regExpression = new RegExp('\\d{2}\\s+\\d{1,3}:((\\d{1,3}\\,(?=\\d{1,3}))|(\\d{1,3}\\-(?=\\d))|\\d{1,3})+');
    let regChapterVerse = new RegExp('\\s+\\d{1,3}:((\\d{1,3}\\,(?=\\d{1,3}))|(\\d{1,3}\\-(?=\\d))|\\d{1,3})+');
    //check whether to do search
    if(val){
      val.split(";").forEach(element => {
        if(!regExpression.test(element) && !regChapterVerse.test(element)){
          doSearch = false;
        }
      });
    }else{
      doSearch = false;
    }

    if(val && val.trim().length>0){      
    this.contentService.getSearch(val).subscribe(data=>{
        if(data && data["passages"]){
          this.dir = data["dir"];
          this.passages=(data["passages"]);
          if(this.dir==='LTR'){
            this.align = "left";
          }else{
            this.align = "right";
          } 
        }
      },
      error =>{
      }
    );
    }
    if(doSearch){
      this.contentService.getPassages(val).subscribe(data=>{
        if(data && data["passages"]){
          this.dir = data["dir"];          
          this.passages=(data["passages"]);
          if(this.dir==='LTR'){
            this.align = "left";
          }else{
            this.align = "right";
          } 
        }
      },
      error =>{          
        });
      }
      
    this.ngOnInit();
  }

  isEng(){
    if(this.appHelper.getLang() === 'ENG'){
      return true;
    }else{
      return false;
    }
  }

  chapterClick(book:any, chapter:any){    
    this.appHelper.saveBook(book);
    this.appHelper.saveChapter(chapter);
    this.router.navigateByUrl('/home');
  }

}
