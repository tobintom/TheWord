import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { BehaviorSubject, Observable, Subscription, throwError } from 'rxjs';
import { AuthService } from './auth.service';
import { JwtHelperService } from "@auth0/angular-jwt";
import { catchError, filter, finalize, switchMap, take, tap } from 'rxjs/operators';
import { SpinnerServiceService } from './spinner-service.service';
import { AppHelper } from './app.helper';

const helper = new JwtHelperService();

@Injectable()
export class ServiceInterceptorInterceptor implements HttpInterceptor {
  urlsToNotUse: Array<string>;

  constructor(public auth: AuthService,
    private readonly spinnerOverlayService: SpinnerServiceService,
    private appHelper: AppHelper) {
    this.urlsToNotUse= [
      'oauth/token' 
    ];
  }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    if (this.isValidRequestForInterceptor(request.url)) {
      const spinnerSubscription: Subscription = this.spinnerOverlayService.spinner$.subscribe();        
          return next.handle(this.addToken(request,this.auth.getToken())).pipe(
            catchError(error => {
               console.error(error)
              if (error instanceof HttpErrorResponse && error.status===401) {                
                this.auth.authenticate().subscribe(()=>{
                   location.reload();
                });
              }if (error instanceof HttpErrorResponse && error.status===500) {                
                 //Reset parameters
                 this.appHelper.saveLang('ENG');
                 this.appHelper.saveBook('01');
                 this.appHelper.saveChapter('1');
                   location.reload();
                
              }
               else {
                return throwError(error);
              }
              return throwError(error);
            }), 
            finalize(()=> spinnerSubscription.unsubscribe())
          );             
    }
    return next.handle(request);
  }

private isRefreshing = false;
private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);

private isAuthRefreshing = false;
private authRefreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);

private handle401Error(request: HttpRequest<any>, next: HttpHandler) {
  if (!this.isAuthRefreshing) {
    this.isAuthRefreshing = true;
    this.authRefreshTokenSubject.next(null);

    return this.auth.authenticate().pipe(
      tap(token => {
        this.isAuthRefreshing = false;
        this.authRefreshTokenSubject.next(token.access_token);
        location.reload();
      }));

  } else {
    return this.authRefreshTokenSubject.pipe(
      filter(token => token != null),
      take(1),
      switchMap(jwt => {
        return next.handle(this.addToken(request, jwt));
      }));
  }
}

private handleRefreshToken(request: HttpRequest<any>, next: HttpHandler) {
  if (!this.isRefreshing) {
    this.isRefreshing = true;
    this.refreshTokenSubject.next(null);

    return this.auth.refreshToken().pipe(
      switchMap((token: any) => {
        this.isRefreshing = false;
        this.refreshTokenSubject.next(token.access_token);
        return next.handle(this.addToken(request, token.access_token));
      }));

  } else {
    return this.refreshTokenSubject.pipe(
      filter(token => token != null),
      take(1),
      switchMap(jwt => {
        return next.handle(this.addToken(request, jwt));
      }));
  }
}

  private addToken(request: HttpRequest<any>, token: string) {
    return request.clone({
      setHeaders: {
        'Content-Type': 'application/x-www-form-urlencoded',
        'Access-Control-Allow-Origin':'*',
        'Access-Control-Allow-Methods':'DELETE, POST, GET, OPTIONS',
        'Access-Control-Allow-Headers':'Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With',
        'Authorization': `Bearer ${token}`
      }
    });
  }

  private isValidRequestForInterceptor(requestUrl: string): boolean {
    let positionIndicator: string = 'theword/';
    let position = requestUrl.indexOf(positionIndicator);
    if (position > 0) {
      let destination: string = requestUrl.substr(position + positionIndicator.length);
      for (let address of this.urlsToNotUse) {
        if (new RegExp(address).test(destination)) {
          return false;
        }
      }
    }
    return true;
  }
}
