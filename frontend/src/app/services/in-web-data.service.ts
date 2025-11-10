import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class InWebDataService {

  constructor() { }

  websocket:WebSocket | undefined;
  containerName:string="container"

  setWebSocket(websocket:WebSocket){
    this.websocket=websocket;
  }

  getWebSocket(){
    return this.websocket;
  }

  setContainerName(name:string){
    this.containerName=name
  }

  getContainerName(){
    return this.containerName
  }


}
