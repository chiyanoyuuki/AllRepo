import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({providedIn: 'root'})

export class DataService 
{
  constructor(private httpClient : HttpClient) {}

  get(link:string){return this.httpClient.get<any>(environment.path+link);}
}
