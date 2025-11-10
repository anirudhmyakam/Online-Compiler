import { Routes } from '@angular/router';
import { CodeEditorComponent } from './code-editor/code-editor.component';
import { TerminalComponent } from './terminal/terminal.component';

export const routes: Routes = [
    {path:"", component:CodeEditorComponent},
    {path:"terminal",component:TerminalComponent}
];
