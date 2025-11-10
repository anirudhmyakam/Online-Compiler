import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { timeStamp } from 'console';
import { AppConstants } from '../app.constants';

@Injectable({
  providedIn: 'root'
})
export class CompilerService {

  constructor(private http:HttpClient) { }

  runCodeWithTestCase(codeFile:CodeDto, containerName:string){
      return this.http.post(`${AppConstants.HTTPURL}/run-testcases/${containerName}`,codeFile)
  }

  runCodeWithTerminal(codeFile:CodeDto, containerName:string){
    return this.http.post(`${AppConstants.HTTPURL}/run-terminal/${containerName}`,codeFile)
  }

  removeContainer(containerName:string){
    return this.http.get(`${AppConstants.HTTPURL}/remove-container/${containerName}`)
  }

  test(){
    return this.http.get(`${AppConstants.HTTPURL}/test`)
  }


}

export class CodeDto{
  code:string=""
  language:string=""
  fileName:string=""
  inputs:string[]=[];

  constructor(code:string, language:string, fileName:string, inputs:string[]){
    this.code=code
    this.language=language
    this.fileName=fileName
    this.inputs=inputs
  }
}
