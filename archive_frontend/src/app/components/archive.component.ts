import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ServerService } from '../server.service';
import { Observable } from 'rxjs';
import { Bundle } from '../models';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-archive',
  templateUrl: './archive.component.html',
  styleUrls: ['./archive.component.css']
})
export class ArchiveComponent implements OnInit {
  activatedRoute = inject(ActivatedRoute)
  title = inject(Title)
  serverSvc = inject(ServerService)

  bundleId!: string
  bundle$!: Observable<Bundle>

  ngOnInit(): void {
    this.bundleId = this.activatedRoute.snapshot.params['bundleId']
    this.bundle$ = this.serverSvc.loadBundle(this.bundleId)
  }


}
