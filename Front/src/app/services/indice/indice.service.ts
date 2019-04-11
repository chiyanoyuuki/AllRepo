import { Injectable } from '@angular/core';
import { DataService } from '../data.service';
import { IndiceModule } from 'src/app/beans/indice/indice.module';
import { Observable } from 'rxjs';
import { IndiceValModule } from 'src/app/beans/indiceval/indiceval.module';

@Injectable({providedIn: 'root'})

export class IndiceService 
{
  constructor(private dataService : DataService) {}

  getIndice(type:string):Observable<IndiceModule[]>
  {return this.dataService.get("Indices?type="+type);}
  
  getIndiceVals(ID:number,TIME:string,type:string):Observable<IndiceValModule[]>
  {return this.dataService.get("IndicesVals?ID="+ID+"&TIME="+TIME+"&type="+type);}
  
  getIndiceNewVals(ID:number,DATE:Date,type:string):Observable<IndiceValModule[]>
  {return this.dataService.get("IndicesNewVals?ID="+ID+"&DATE="+DATE+"&type="+type);}

  getTotal(ID:number,type:string):Observable<string[]>
  {return this.dataService.get("IndicesTotal?ID="+ID+"&type="+type);}
}
