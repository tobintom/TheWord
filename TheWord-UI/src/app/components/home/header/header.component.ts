import { Component, OnInit, ElementRef, Renderer2 } from '@angular/core';
import { Location, LocationStrategy, PathLocationStrategy } from '@angular/common';
import {environment} from './../../../../environments/environment';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  SWAGGER_URL: string;  
  private toggleButton: any;
  private nav: any;
    private sidebarVisible: boolean;

  constructor(public location: Location, private element : ElementRef, private renderer: Renderer2) { 
    this.sidebarVisible = false;
    this.SWAGGER_URL = environment.SWAGGER_URL;
  }

  ngOnInit() {
    const navbar: HTMLElement = this.element.nativeElement;
    this.toggleButton = navbar.getElementsByClassName('navbar-toggler')[0];
    this.nav = navbar.getElementsByClassName('navbar')[0];
    this.renderer.listen('window', 'scroll', (event) => {
      const number = window.scrollY;
      if (number > 100 || window.pageYOffset > 100) {
          // add logic
          this.nav.classList.remove('navbar-transparent');
      } else {
          // remove logic
          this.nav.classList.add('navbar-transparent');
      }
  });
  }

  sidebarOpen() {
    const toggleButton = this.toggleButton;
    const html = document.getElementsByTagName('html')[0];
    // console.log(html);
    // console.log(toggleButton, 'toggle');

    setTimeout(function(){
        toggleButton.classList.add('toggled');
    }, 500);
    html.classList.add('nav-open');

    this.sidebarVisible = true;
};
sidebarClose() {
    const html = document.getElementsByTagName('html')[0];
    // console.log(html);
    this.toggleButton.classList.remove('toggled');
    this.sidebarVisible = false;
    html.classList.remove('nav-open');
};
sidebarToggle() {
    // const toggleButton = this.toggleButton;
    // const body = document.getElementsByTagName('body')[0];
    if (this.sidebarVisible === false) {
        this.sidebarOpen();
    } else {
        this.sidebarClose();
    }
};
isHome() {
  var titlee = this.location.prepareExternalUrl(this.location.path());
  if(titlee.charAt(0) === '#'){
      titlee = titlee.slice( 1 );
  }
    if( titlee === '/home' ) {
        return true;
    }
    else {
        return false;
    }
}

isVerseOfDay() {
  var titlee = this.location.prepareExternalUrl(this.location.path());
  if(titlee.charAt(0) === '#'){
      titlee = titlee.slice( 1 );
  }
    if( titlee === '/dailyVerse' ) {
        return true;
    }
    else {
        return false;
    }
}

isSearch() {
  var titlee = this.location.prepareExternalUrl(this.location.path());
  if(titlee.charAt(0) === '#'){
      titlee = titlee.slice( 1 );
  }
    if( titlee === '/search' ) {
        return true;
    }
    else {
        return false;
    }
}

isDocumentation() {   
        return false;    
}


}
