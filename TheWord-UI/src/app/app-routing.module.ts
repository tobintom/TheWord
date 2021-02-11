import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DailyVerseComponent } from './components/daily-verse/daily-verse.component';
import { HomeComponent } from './components/home/home.component';
import { SearchComponent } from './components/search/search.component';


const routes: Routes = [
  {component:HomeComponent, path:"home"},
  {component:DailyVerseComponent, path:"dailyVerse"},
  {component:SearchComponent, path:"search"},
  {path:"",redirectTo:"/home", pathMatch:"full"}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy',initialNavigation: 'disabled' })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
