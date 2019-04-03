import { Component, OnInit } from '@angular/core';
import { IndiceService } from 'src/app/services/indice/indice.service'
import { IndiceModule } from 'src/app/beans/indice/indice.module';
import { IndiceValModule } from 'src/app/beans/indiceval/indiceval.module';
import { ChartsModule, WavesModule } from '../../../../node_modules/angular-bootstrap-md'
import { TouchSequence } from 'selenium-webdriver';

@Component({
  selector: 'app-accueil',
  templateUrl: './accueil.component.html',
  styleUrls: ['./accueil.component.css']
})
export class AccueilComponent implements OnInit {

  datas : [IndiceModule[], IndiceValModule[]] = [undefined,undefined];
  indiceSelected : IndiceModule;

  labels = [];
  data = [];

  chartType: string = 'line';
  chartDatasets: Array<any> = undefined;
  chartLabels  : Array<any> = undefined;
  chartOptions: any = {responsive: true};

  constructor(private indiceService : IndiceService) { }

  ngOnInit() 
  {
    this.indiceService.getIndice().subscribe(
      (response)=>
        {
          this.datas[0]=response;
          this.indiceSelected = this.datas[0][0];
          console.log(this.indiceSelected);
          this.getIndiceVals();
        },
      (error)=>{console.log("ERREUR");}
    );
  }

  getIndiceVals()
  {
    this.indiceService.getIndiceVals(this.indiceSelected.id).subscribe(
      (response)=>
        {
          this.datas[1] = response;
          this.data = []; this.labels = [];
          this.datas[1].forEach(d=>{this.data.push(d.val);this.labels.push(d.date);})

          console.log(this.data);
          console.log(this.labels);

          this.chartDatasets  = [{ data:this.data, label: 'Test' }];
          this.chartLabels    = this.labels;
        },
      (error)=>{console.log("ERREUR");}
    );
  }

  clickIndice(indice:IndiceModule)
  {
    this.indiceSelected = indice;
    this.getIndiceVals();
  }

}
