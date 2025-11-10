import { ChangeDetectorRef, Component, HostListener } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MonacoEditorModule } from 'ngx-monaco-editor-v2';
import { isPlatformBrowser } from '@angular/common';
import { Inject, PLATFORM_ID } from '@angular/core';
import { CodeDto, CompilerService } from '../services/compiler.service';
import { response } from 'express';
import { error } from 'console';
import { TerminalComponent } from "../terminal/terminal.component";
import { InWebDataService } from '../services/in-web-data.service';

@Component({
  selector: 'app-code-editor',
  imports: [FormsModule, MonacoEditorModule, TerminalComponent],
  templateUrl: './code-editor.component.html',
  styleUrl: './code-editor.component.css'
})
export class CodeEditorComponent {

  isBrowser: boolean;
  isTerminal:boolean=true;
  outputFetched:boolean=false;
  attachment={'python':'.py','java':'.java'}
  displayOutput=false;
  fileName="main.py"


  constructor(@Inject(PLATFORM_ID) private platformId: Object, private compilerServices:CompilerService, private webdata:InWebDataService, private cdr:ChangeDetectorRef) {
    this.isBrowser = isPlatformBrowser(this.platformId);
  }

  language:string="python"
  input_txt=""
  output:any;

  code = `# Write your python code
print('Hello world')
`;

  editorOptions = {
    language: 'python',
    minimap: { enabled: false },
    scrollBeyondLastLine: false,
    lineHeight: 20,
    fontSize: 14,
    wordWrap: 'on',
    wrappingIndent: 'indent'
  };

  setTerminal(bool:boolean){
    this.isTerminal=bool
    this.cdr.detectChanges()
  }

  run(){

    
    const containerName=this.webdata.getContainerName()
    const input=this.input_txt.split('\n').map(line => line.trim()).filter(line => line !== '');
    const codeFile = new CodeDto(this.code,this.language,this.fileName,input)
    // console.log(codeFile)
    const webSocket = this.webdata.getWebSocket()
    // console.log(this.language+containerName)
    if(this.isTerminal){
      this.compilerServices.runCodeWithTerminal(codeFile, this.language+containerName).subscribe(
      response=>{
        // console.log(response)
        webSocket?.send("start-execution "+this.language+containerName+" "+this.fileName+" "+this.language)
      },
      error=>{
        console.log(error)
      }
    )
    }

    else{
      this.compilerServices.runCodeWithTestCase(codeFile, this.language+containerName).subscribe(
      response=>{
        // console.log(response)
        this.output=response
        this.outputFetched=true
        this.displayOutput=true
      },
      error=>{
        console.log(error)
      }
    )
    }
    
  }

  setDisplayOutput(boll:boolean){
    this.displayOutput=boll
  }


  changesonLanguage(lang:string){
    if(lang=='python'){
      this.code = `# Write your python code
print('Hello world')
`;
      this.cdr.detectChanges()

      this.fileName='main.py'
    }

    else if(lang=='java'){
      this.code=`// Write your java code
public class Helloworld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
      `
      this.fileName='Helloworld.java'
      this.cdr.detectChanges()
    }

    // console.log(this.code,this.fileName)
  }

  


}
