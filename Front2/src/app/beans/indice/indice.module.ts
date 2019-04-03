import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ]
})
export class IndiceModule { 

  id: number;
  nom : string;

  IndiceModule(ID:number,NOM:string)
  {
    this.id = ID;
    this.nom = NOM;
  }

}
