import { Component, ElementRef, ViewChild } from '@angular/core';
import { OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InWebDataService } from '../services/in-web-data.service';
import { AppConstants } from '../app.constants';

@Component({
  selector: 'app-terminal',
  imports: [FormsModule],
  templateUrl: './terminal.component.html',
  styleUrl: './terminal.component.css'
})
export class TerminalComponent {

  constructor(@Inject(PLATFORM_ID) private platformId: Object, private dataService:InWebDataService){}

  @ViewChild('terminalRef') terminalElement!: ElementRef;
  command: string = '';
  private socket!: WebSocket;

  ngOnInit(): void {
  if (isPlatformBrowser(this.platformId)) {
    this.socket = new WebSocket(`${AppConstants.WSURL}/ws/code`);
    this.socket.onmessage = (event) => {
      let message:string=event.data;
      let messageLst=message.split('$_$')
      // console.log(message,messageLst)
      if(messageLst[0]=="HERE IS THE CONTAINER NAME"){
        this.dataService.setContainerName(messageLst[1]);
      }
      else{
        const terminal: HTMLElement = this.terminalElement.nativeElement;
        terminal.textContent += event.data + '\n';
        terminal.scrollTop = terminal.scrollHeight;

      }
    };

    this.socket.onerror = (err) => {
      // console.error("WebSocket error:", err);
    };

    this.socket.onopen = () => {
      // console.log("session created")
      this.dataService.setWebSocket(this.socket)
    }

  }
}


  sendCommand(): void {
  if (!this.socket || this.socket.readyState !== WebSocket.OPEN) {
    // console.warn('Socket is not ready');
    return;
  }

  if (this.command.trim() !== '') {
    // console.log("message sent : "+this.command)
    this.socket.send(this.command);
    this.command = '';
  }
}

  ngOnDestroy(): void {
    if (this.socket) {
      this.socket.close();
    }
  }

}
