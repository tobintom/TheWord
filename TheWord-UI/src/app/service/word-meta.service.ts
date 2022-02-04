import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AppHelper } from './app.helper';
import { AuthService } from './auth.service';
import {environment} from './../../environments/environment';
 

@Injectable({
  providedIn: 'root'
})
export class WordMetaService {
  public responseCache = new Map();

  constructor(private http: HttpClient, 
    private authService: AuthService,
    private helper: AppHelper) { }

    getLanguages(){         
     return this.http.get<any>(environment.META_API_URL+'/list');           
    }

    getBooks(){
      var url = environment.META_API_URL +'/'+ this.helper.getLang()+'/books';
      return this.http.get(url);
    }

    getChapters(){
      var url = environment.META_API_URL +'/'+ this.helper.getLang()+'/'+this.helper.getBook()+'/chapters';
      return this.http.get(url);
    }
}
