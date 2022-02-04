import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef,MAT_DIALOG_DATA } from "@angular/material/dialog";
import { AppHelper } from 'src/app/service/app.helper';

@Component({
  selector: 'app-dialog',
  templateUrl: './dialog.component.html',
  styleUrls: ['./dialog.component.scss']
})
export class DialogComponent implements OnInit {

  dir: string ="LTR";
  align: string = "LEFT";
  passages: Array<any>;
  lang: string;

  constructor(public dialogRef: MatDialogRef<DialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,private appHelper: AppHelper) { }

  ngOnInit(): void {
    this.dir = this.data.data["dir"];
    this.lang = this.data.data["id"];
    if(this.dir==='LTR'){
      this.align = "left";
    }else{
      this.align = "right";
    }
    this.passages = this.data.data["passages"];
  }

  isEng(){
    if(this.lang === 'ENG'){
      return true;
    }else{
      return false;
    }
  }

  close() {
    this.dialogRef.close();
  }

  chapterClick(book:any, chapter:any){    
    this.appHelper.saveBook(book);
    this.appHelper.saveChapter(chapter);
    this.dialogRef.close({event: 'refresh'});
  }

}
