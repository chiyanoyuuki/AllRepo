import { Component, OnInit, ViewChild  } from '@angular/core';
import { IndiceService } from 'src/app/services/indice/indice.service'
import { IndiceModule } from 'src/app/beans/indice/indice.module';
import { IndiceValModule } from 'src/app/beans/indiceval/indiceval.module';
import * as CanvasJS from '../../../../node_modules/canvasjs-2.3.1/canvasjs.min';

@Component({
  selector: 'app-accueil',
  templateUrl: './accueil.component.html',
  styleUrls: ['./accueil.component.css']
})
export class AccueilComponent implements OnInit {

  
		
	

  datas : [IndiceModule[], IndiceValModule[]] = [undefined,undefined];
  indiceSelected : IndiceModule;

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
          console.log(this.datas[1]);
          this.createChart();
        },
      (error)=>{console.log("ERREUR");}
    );
  }

  createChart()
  {
    let chart = new CanvasJS.Chart("chartContainer", {zoomEnabled:true,animationEnabled: true,exportEnabled: true,
      title: {
        text: this.indiceSelected.nom
      },
      data: [{
        type: "spline",
        dataPoints: this.datas[1]
      }]
    });
    chart.render();
  }

}
