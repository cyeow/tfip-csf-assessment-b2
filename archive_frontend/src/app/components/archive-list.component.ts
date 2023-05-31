import { Component, OnInit, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Bundle } from '../models';
import { ServerService } from '../server.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-archive-list',
  templateUrl: './archive-list.component.html',
  styleUrls: ['./archive-list.component.css']
})
export class ArchiveListComponent implements OnInit {
  bundles$!: Observable<Bundle[]>

  serverSvc = inject(ServerService)
  router = inject(Router)

  ngOnInit(): void {
    this.bundles$ = this.serverSvc.loadBundles()
  }
}
