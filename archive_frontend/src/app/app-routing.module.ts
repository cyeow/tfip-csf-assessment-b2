import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ArchiveUploadComponent } from './components/archive-upload.component';
import { ArchiveComponent } from './components/archive.component';
import { ArchiveListComponent } from './components/archive-list.component';

const routes: Routes = [
  {path: '', component: ArchiveListComponent},
  {path: 'upload', component: ArchiveUploadComponent},
  {path: 'bundle/:bundleId', component: ArchiveComponent},
  {path: '**', redirectTo: '/', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
