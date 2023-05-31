import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ArchiveUploadComponent } from './components/archive-upload.component';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ServerService } from './server.service';
import { ArchiveComponent } from './components/archive.component';
import { ArchiveListComponent } from './components/archive-list.component';

@NgModule({
  declarations: [
    AppComponent,
    ArchiveUploadComponent,
    ArchiveComponent,
    ArchiveListComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [ServerService],
  bootstrap: [AppComponent]
})
export class AppModule { }
