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
  label: Date;

  IndiceModule(val:number,label:Date)
  {
    this.val = val;
    this.label = label;
  }

}
