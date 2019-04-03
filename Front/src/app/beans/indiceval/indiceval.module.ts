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
  date: Date;

  IndiceModule(val:number,date:Date)
  {
    this.val = val;
    this.date = date;
  }

}
