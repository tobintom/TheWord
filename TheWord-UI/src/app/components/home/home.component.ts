import { Component, OnInit  } from '@angular/core';
import { AppHelper } from 'src/app/service/app.helper';
import { WordContentService } from 'src/app/service/word-content.service';
import { WordMetaService } from 'src/app/service/word-meta.service';
import {MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { DialogComponent } from '../dialog/dialog.component';
import { Router } from '@angular/router';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  
  selectedValue: string;
  languages: Array<any>;
  verses: Array<any>;
  books: Array<any>;
  chapters: Array<any>;
  dir: string;
  book: string;
  selectedbook: string;
  selectedchapter: any;
  englishName: string;
  chapter: string;
  lang: string = '';
  bgurl: string;
  icon: string;
  align: string;
  anio: number = new Date().getFullYear();

  constructor(private contentService: WordContentService, private appHelper: AppHelper,
    private metaService: WordMetaService,private matDialog: MatDialog,
    private router:Router) { 
       this.bgurl = '../../../assets/images/bg/' + this.appHelper.randomNumber(1,16) + '.jpg';   
       this.lang = this.appHelper.getLang();    
       this.selectedbook = this.appHelper.getBook();
       this.selectedchapter = this.appHelper.getChapter();
  }

  ngOnInit(): void {    
    this.lang = this.appHelper.getLang();
    this.selectedbook = this.appHelper.getBook();
    this.selectedchapter = this.appHelper.getChapter();

    //Get Content for selected languages/book/chapter
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

    //get languages
    this.metaService.getLanguages().subscribe(data=>{
      if(data && data["bibles"]){
        this.languages = data["bibles"];
      }
    });

    //get Books for selected language
    this.metaService.getBooks().subscribe(data=>{
      if(data && data["books"]){
        this.books = data["books"];
      }
    });
  //Get Chapters of the selected book
  this.metaService.getChapters().subscribe(data=>{
    if(data && data["chapters"]){
      this.chapters =  data["chapters"].map(elt => String(elt));     
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

  previousChapter(){
    var localBook = this.appHelper.getBook();
    this.contentService.getPreviousChapter(this.appHelper.getBook(),this.appHelper.getChapter()).subscribe(data=>{
      if(data && data["verses"]){
        //Save State
        this.appHelper.saveBook(data["number"]);
        this.appHelper.saveChapter(data["chapter"]);
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
        this.selectedbook = this.appHelper.getBook();
        this.selectedchapter = this.appHelper.getChapter();
        if(localBook!=data["number"]){
          this.metaService.getChapters().subscribe(data=>{
            if(data && data["chapters"]){
             this.chapters =  data["chapters"].map(elt => String(elt));     
           }
          });
        }
      }
    });     
  }

  nextChapter(){
    var localBook = this.appHelper.getBook();    
    this.contentService.getNextChapter(this.appHelper.getBook(),this.appHelper.getChapter()).subscribe(data=>{
      if(data && data["verses"]){
        //Save State
        this.appHelper.saveBook(data["number"]);
        this.appHelper.saveChapter(data["chapter"]);               
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
        this.selectedbook = this.appHelper.getBook();
        this.selectedchapter = this.appHelper.getChapter();
        if(localBook!=data["number"]){
          this.metaService.getChapters().subscribe(data=>{
            if(data && data["chapters"]){
             this.chapters =  data["chapters"].map(elt => String(elt));     
           }
          });
        }
      }
    });    
  }

  changeLang(val: any){
    this.lang = val.value;
    this.appHelper.saveLang(this.lang);
    this.appHelper.saveBook(this.selectedbook);    
    this.appHelper.saveChapter(this.selectedchapter);

    //Get Content for selected languages/book/chapter
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

    //get Books for selected language
    this.metaService.getBooks().subscribe(data=>{
      if(data && data["books"]){
        this.books = data["books"];
      }
    });
  //Get Chapters of the selected book
  this.metaService.getChapters().subscribe(data=>{
    if(data && data["chapters"]){
      this.chapters =  data["chapters"].map(elt => String(elt));     
    }
  });     
  }

  changeBook(val: any){
    this.selectedbook = val.value;
    this.appHelper.saveBook(this.selectedbook);
    this.selectedchapter = "1";
    this.appHelper.saveChapter(this.selectedchapter);
    this.ngOnInit();
  }
  
  changeChapter(val: any){     
    this.selectedchapter = val.value;
    this.appHelper.saveChapter(this.selectedchapter);
    //Get Content for selected languages/book/chapter
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

  showCrossRef(verse: any){
       this.contentService.getCrossReferences(this.appHelper.getBook(),
       this.appHelper.getChapter(), verse).subscribe(data=>{
        if(data && data["passages"]){
          const dialogConfig = new MatDialogConfig();
          dialogConfig.panelClass = "dialogClass";
          dialogConfig.data = { data:data,
                                 err:'',
                                 meta:{ ref:this.book +' '+this.appHelper.getChapter()+':'+verse,
                                        lang: this.appHelper.getLang(),
                                         eng: this.englishName
                                      } 
                                };
          const dialogRef = this.matDialog.open(DialogComponent, dialogConfig);
          dialogRef.afterClosed().subscribe(
            data => {
              if(data && data.event && data.event==='refresh'){
                this.ngOnInit();
              }
            }
          );  
        }else{
          const dialogConfig = new MatDialogConfig();             
          dialogConfig.data = { data:data,
                                 err: 'No Cross References Found.',
                                 meta:{ ref:this.book +' '+this.appHelper.getChapter()+':'+verse,
                                        lang: this.appHelper.getLang(),
                                         eng: this.englishName
                                      } 
                                };
          this.matDialog.open(DialogComponent, dialogConfig);
        }
        this.router.events
            .subscribe(() => {
              this.matDialog.closeAll();
        });
       });
       
  }
}

