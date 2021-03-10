import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AppHelper } from './app.helper';
import { AuthService } from './auth.service';

const API_URL = 'http://192.168.99.100:5000/theword/v1/bibles';

@Injectable({
  providedIn: 'root'
})
export class WordMetaService {
  public responseCache = new Map();

  constructor(private http: HttpClient, 
    private authService: AuthService,
    private helper: AppHelper) { }

    getLanguages(){         
     return this.http.get<any>(API_URL+'/list');           
    }

    getBooks(){
      var url = API_URL +'/'+ this.helper.getLang()+'/books';
      return this.http.get(url);
    }

    getChapters(){
      var url = API_URL +'/'+ this.helper.getLang()+'/'+this.helper.getBook()+'/chapters';
      return this.http.get(url);
    }
}
