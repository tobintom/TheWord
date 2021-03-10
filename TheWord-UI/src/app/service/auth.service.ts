import { HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { tap,catchError  } from 'rxjs/operators';
import { JwtHelperService } from "@auth0/angular-jwt";

const helper = new JwtHelperService();

const ACCESS_TOKEN = 'access_token';
const REFRESH_TOKEN = 'refresh_token';
const OAUTH_CLIENT = 'theWord-client';
const OAUTH_SECRET = 'secret';
const USER_NAME = '56ae9949-fd44-4b8f-a92e-d70301063902';
const PASSWORD = 'dRx2FZEEPWQ3JWvJceJf8Bvm9vWqxXhA';
const API_URL = 'http://192.168.99.100:5000/theword/';

const HTTP_OPTIONS = {
  headers: new HttpHeaders({
    'Content-Type': 'application/x-www-form-urlencoded',
    'Access-Control-Allow-Origin':'*',
    'Access-Control-Allow-Methods':'DELETE, POST, GET, OPTIONS',
    'Access-Control-Allow-Headers':'Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With',
    Authorization: 'Basic ' + btoa(OAUTH_CLIENT + ':' + OAUTH_SECRET)
  })
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private refreshTokenTimeout;
  private tokenTimeout;

  constructor(private http: HttpClient) { }

  authenticate() {
    this.removeToken();
    this.removeRefreshToken();
    const body = new HttpParams()
      .set('username', USER_NAME)
      .set('password', PASSWORD)
      .set('grant_type', 'password');
      
    return this.http.post<any>(API_URL + 'oauth/token', body, HTTP_OPTIONS)
      .pipe(
        tap(res => {
          this.saveToken(res.access_token);          
          this.saveRefreshToken(res.refresh_token);
          this.startRefreshTokenTimer();
          this.startTokenTimer();
        }),
        catchError(AuthService.handleError)
      );
  }

  refreshToken(){    
    const body = new HttpParams()
      .set('refresh_token', this.getRefreshToken())
      .set('grant_type', 'refresh_token');
    return this.http.post<any>(API_URL + 'oauth/token', body, HTTP_OPTIONS)
      .pipe(
        tap(res => {
          this.removeToken();
           this.removeRefreshToken();
          this.saveToken(res.access_token);
          this.saveRefreshToken(res.refresh_token);
          this.startTokenTimer();
        }),
        catchError(AuthService.handleError)
      );
  }

  private startTokenTimer() {
    
    // set a timeout to refresh the token a minute before it expires
    const expires = helper.getTokenExpirationDate(this.getToken());
    const timeout = expires.getTime() - Date.now() - (60 * 1000);
    this.tokenTimeout = setTimeout(() => this.refreshToken().subscribe(), timeout);
}

private stopRefreshTokenTimer() {
    clearTimeout(this.refreshTokenTimeout);
}

  private startRefreshTokenTimer() {
   // set a timeout to refresh the token a minute before it expires
    const expires = helper.getTokenExpirationDate(this.getRefreshToken())
    const timeout = expires.getTime() - Date.now() - (60 * 1000);
    this.refreshTokenTimeout = setTimeout(() => this.authenticate().subscribe(), timeout);
}

private stopTokenTimer() {
    clearTimeout(this.tokenTimeout);
}
  private static handleError(error: HttpErrorResponse): any {
    if (error.error instanceof ErrorEvent) {
      console.error('An error occurred:', error.error.message);
    } else {
      console.error(
        'Backend returned code ${error.status}, ' +
        'body was: ${error.error}');
    }
    return throwError(
      'Something bad happened; please try again later.');
  }

  private static log(message: string): any {
    console.log(message);
  }

  getToken(): string {
    return localStorage.getItem(ACCESS_TOKEN);
  }

  getRefreshToken(): string {
    return localStorage.getItem(REFRESH_TOKEN);
  }

  saveToken(token): void {
    localStorage.setItem(ACCESS_TOKEN, token);
  }

  saveRefreshToken(refreshToken): void {
    localStorage.setItem(REFRESH_TOKEN, refreshToken);
  }

  removeToken(): void {
    localStorage.removeItem(ACCESS_TOKEN);
  }

  removeRefreshToken(): void {
    localStorage.removeItem(REFRESH_TOKEN);
  }

}
