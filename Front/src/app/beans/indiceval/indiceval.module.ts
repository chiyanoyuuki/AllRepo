import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ]
})
export class IndiceValModule 
{ 
  val: number;
  date: string;

  IndiceModule(val:number,date:string)
  {
    this.val = val;
    this.date = date;
  }

}
