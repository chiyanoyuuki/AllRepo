import { Component, OnInit } from '@angular/core';
import { IndiceService } from 'src/app/services/indice/indice.service'
import { IndiceModule } from 'src/app/beans/indice/indice.module';
import { IndiceValModule } from 'src/app/beans/indiceval/indiceval.module';

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
  timing;
  maxDate;

  chartType: string = 'line';
  chartDatasets: Array<any> = undefined;
  chartLabels  : Array<any> = undefined;
  chartOptions: any = 
  {
    animation: {duration: 0},
    hover: {animationDuration: 0},
    responsiveAnimationDuration: 0,
    elements:
    {
      line:
        {
          fill:false,
          tension:0
        },
      point:
        {
          radius:2
        }
    },
    scales:{xAxes:[{ticks:{fontColor:"white"}}],yAxes:[{ticks:{fontColor:"white"}}]}
  };

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
          clearInterval(this.timing);
          this.datas[1] = response;
          this.data = this.datas[1].map(function(o) { return o.val; });
          this.labels = this.datas[1].map(function(o) { return o.date.toString().substring(11,16); });

          this.chartDatasets  = [{ data:this.data, label: 'Test' }];
          this.chartLabels    = this.labels;

          if(this.datas[1].length>0)
          {
            this.maxDate = this.datas[1][this.datas[1].length-1].date;
            this.startTiming();
          }
        },
      (error)=>{console.log("ERREUR");}
    );
  }

  clickIndice(indice:IndiceModule)
  {
    this.indiceSelected = indice;
    this.getIndiceVals();
  }

  startTiming()
  {
    this.timing = setInterval(() =>
    {
      this.getNewVals();
    },2000)
  }

  getNewVals()
  {
    this.indiceService.getIndiceNewVals(this.indiceSelected.id,this.maxDate).subscribe(
      (response)=>
        {
          let tmp = response;
          if(tmp.length>0)
          {
            tmp.forEach(e => {
              this.chartLabels.push(e.date.toString().substring(11,16));
              this.chartDatasets[0].data.push(e.val);
            });
            this.chartLabels = this.chartLabels.slice();
            this.chartDatasets = this.chartDatasets.slice();
            this.maxDate = tmp[tmp.length-1].date;
          }
        },
      (error)=>{console.log("ERREUR");}
    );

    this.indiceService.getIndice().subscribe(
      (response)=>
        {
          this.datas[0]=response;
        },
      (error)=>{console.log("ERREUR");}
    );
  }

}
