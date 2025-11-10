(self as any).MonacoEnvironment = {
  getWorkerUrl: function (_moduleId: string, label: string) {
    return window.location.origin + '/onlinecompiler/browser/assets/monaco/min/vs/base/worker/workerMain.js';
  }
};


import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
