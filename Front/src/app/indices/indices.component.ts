import { Component, OnInit, OnDestroy } from '@angular/core';
import { IndiceService } from 'src/app/services/indice/indice.service'
import { IndiceModule } from 'src/app/beans/indice/indice.module';
import { IndiceValModule } from 'src/app/beans/indiceval/indiceval.module';

@Component({
  selector: 'app-indices',
  templateUrl: './indices.component.html',
  styleUrls: ['./indices.component.css']
})
export class IndicesComponent implements OnInit {

  datas : [IndiceModule[]] = [undefined];
  indiceSelected : IndiceModule;

  times : string[] = ["All time", "Year", "Month", "Today"];
  timeSelected : string = this.times[3];

  count:number = 0;
  timing;
  maxDate;
  maxVal;minVal;

  numbers:[string,string][]=[];

  chartType     : string      = 'line';
  chartDatasets : Array<any>  = undefined;
  chartLabels   : Array<any>  = undefined;
  chartOptions  : any         = 
  {
    animation: {duration: 0},
    hover: {animationDuration: 0},
    responsiveAnimationDuration: 0,
    elements:
    {
      line:{fill:false,tension:0},
      point:{radius:2}
    },
    scales:{xAxes:[{ticks:{fontColor:"white"}}],yAxes:[{ticks:{fontColor:"white"}}]},
  };

  constructor(private indiceService : IndiceService) { }

  ngOnInit() 
  {
    this.indiceService.getIndice().subscribe(
      (response)=>
        {
          this.datas[0]=response;
          this.indiceSelected = this.datas[0][0];
          this.getTotal();
          this.getIndiceVals();
        },
      (error)=>{console.log("ERREUR");}
    );
  }

  ngOnDestroy()
  {
    clearInterval(this.timing);  
  }

  getTotal()
  {
    this.indiceService.getTotal(this.indiceSelected.id).subscribe(
      (response)=>
      {
        let tmp = response;
        this.numbers = [
          ["Today values",    tmp[3]],
          ["Month values",    tmp[2]],
          ["Year values",     tmp[1]],
          ["All time values", tmp[0]],
        ]
      },
      (error)=>{console.log("ERREUR");}
    );
  }

  getIndiceVals()
  {
    this.indiceService.getIndiceVals(this.indiceSelected.id,this.timeSelected).subscribe(
      (response)=>
        {
          clearInterval(this.timing);
          let tmp = response;
          let data = tmp.map(function(o) { return o.val; });
          let labels;
          if(this.timeSelected=="Today")labels = tmp.map(function(o) { return o.date.toString().substring(11,16); });
          else labels = tmp.map(function(o) { return o.date });

          this.chartDatasets  = [{ data:data, label: 'Valeur'}];
          this.chartLabels    = labels;

          this.maxVal = this.getMax();
          this.minVal = this.getMin();

          if(tmp.length>0)
          {
            this.maxDate = tmp[tmp.length-1].date;
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
      this.count++;
      this.getNewVals();
    },5000)
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

            this.numbers.forEach(n=>n[1]=""+(parseInt(n[1])+tmp.length));

            this.maxVal = this.getMax();
            this.minVal = this.getMin();
          }
        },
      (error)=>{console.log("ERREUR");}
    );

    if(this.count%4==0)
    {
      this.indiceService.getIndice().subscribe(
        (response)=>
          {
            this.datas[0]=response;
          },
        (error)=>{console.log("ERREUR");}
      );
    }
  }

  changeTime(time)
  {
    this.timeSelected = time;
    this.getIndiceVals();
  }

  getMax(){return this.chartDatasets[0].data.reduce(function(a,b) {return Math.max(a, b);});}
  getMin(){return this.chartDatasets[0].data.reduce(function(a,b) {return Math.min(a, b);});;}
  

}
