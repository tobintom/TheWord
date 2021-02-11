import { Component, OnInit } from '@angular/core';
import { AppHelper } from 'src/app/service/app.helper';
import { WordContentService } from 'src/app/service/word-content.service';


@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {
  verses: Array<any>;
  dir: string;
  book: string;
  englishName: string;
  chapter: string;
  bgurl: string;
  anio: number = new Date().getFullYear();
  
  constructor(private contentService: WordContentService, private appHelper: AppHelper) { 
    this.bgurl = '../../../assets/images/bg/' + this.appHelper.randomNumber(1,16) + '.jpg';
  }

  ngOnInit(): void {
  }

}
