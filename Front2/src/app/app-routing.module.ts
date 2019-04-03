import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AccueilComponent } from './components/accueil/accueil.component';

const routes: Routes = [
  { path: 'Accueil', component: AccueilComponent },
  { path: '', redirectTo: '/Accueil', pathMatch: 'full' },
  { path: '**', component: AccueilComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
