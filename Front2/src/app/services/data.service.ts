import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IndiceModule } from '../beans/indice/indice.module';

@Injectable({providedIn: 'root'})

export class DataService 
{
  constructor(private httpClient : HttpClient) {}

  get(link:string){return this.httpClient.get<any>("http://localhost:8080/"+link);}
}
