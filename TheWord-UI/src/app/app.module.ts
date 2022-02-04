import { BrowserModule } from '@angular/platform-browser';
import { APP_INITIALIZER, NgModule } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { SplashScreenComponent } from './components/splash-screen/splash-screen.component';
import { ServiceInterceptorInterceptor } from './service/service-interceptor.interceptor';
import { appInitializer } from './service/app.initializer';
import { AuthService } from './service/auth.service';
import { HomeComponent } from './components/home/home.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatSelectModule} from '@angular/material/select';
import {MatDialogModule} from '@angular/material/dialog';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import { HeaderComponent } from './components/home/header/header.component';
import { DailyVerseComponent } from './components/daily-verse/daily-verse.component';
import { SearchComponent } from './components/search/search.component';
import { FormsModule } from '@angular/forms';
import { DialogComponent } from './components/dialog/dialog.component';
import { SpinnerOverlayComponent } from './components/spinner-overlay/spinner-overlay.component';
import { BoldSpanPipe } from './service/bold-span.pipe';


@NgModule({
  declarations: [
    AppComponent,
    SplashScreenComponent,
    HomeComponent,
    HeaderComponent,
    DailyVerseComponent,
    SearchComponent,
    DialogComponent,
    SpinnerOverlayComponent,
    BoldSpanPipe
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    NgbModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatSelectModule,
    MatDialogModule,
    MatProgressSpinnerModule,
    FormsModule
  ],
  providers: [
    { provide: APP_INITIALIZER, 
      useFactory: appInitializer, 
      multi: true, 
      deps: [AuthService] },

    {
      provide: HTTP_INTERCEPTORS,
      useClass: ServiceInterceptorInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent],
  entryComponents:[DialogComponent]
})
export class AppModule { }
