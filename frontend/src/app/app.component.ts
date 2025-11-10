import { Component, HostListener } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { CodeEditorComponent } from "./code-editor/code-editor.component";
import { InWebDataService } from './services/in-web-data.service';
import { CompilerService } from './services/compiler.service';
import { response } from 'express';
import { error } from 'console';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'onlinecompiler';

  constructor(private compiler:CompilerService, private webdata:InWebDataService){}


  ngOnInit(): void{

  }
// @HostListener('window:beforeunload', ['$event'])
//   handleBeforeUnload(event: Event) {
//     this.callYourApiSync();
//   }


  // ngOnDestroy(): void {

  //   const containerName=this.webdata.getContainerName()


  //     this.compiler.test().subscribe(
  //     response =>{
  //       console.log(response)
  //     },
  //     error =>{
  //       console.log(error)
  //     }
  //     )

  //     this.compiler.test().subscribe(
  //     response =>{
  //       console.log(response)
  //     },
  //     error =>{
  //       console.log(error)
  //     }
  //     )
  // }
    
}
