import { Injectable } from '@angular/core';
import { DataService } from '../data.service';
import { IndiceModule } from 'src/app/beans/indice/indice.module';
import { Observable } from 'rxjs';
import { IndiceValModule } from 'src/app/beans/indiceval/indiceval.module';

@Injectable({providedIn: 'root'})

export class IndiceService 
{
  constructor(private dataService : DataService) {}

  getIndice():Observable<IndiceModule[]>
  {return this.dataService.get("Indices");}
  
  getIndiceVals(ID:number,TIME:string):Observable<IndiceValModule[]>
  {return this.dataService.get("IndicesVals?ID="+ID+"&TIME="+TIME);}
  
  getIndiceNewVals(ID:number,DATE:Date):Observable<IndiceValModule[]>
  {return this.dataService.get("IndicesNewVals?ID="+ID+"&DATE="+DATE);}

  getTotal(ID:number):Observable<string[]>
  {return this.dataService.get("IndicesTotal?ID="+ID);}
}
