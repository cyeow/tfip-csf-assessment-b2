import { Component, ElementRef, OnInit, ViewChild, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ServerService } from '../server.service';
import { Router } from '@angular/router';
import { Archive } from '../models';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-archive-upload',
  templateUrl: './archive-upload.component.html',
  styleUrls: ['./archive-upload.component.css']
})
export class ArchiveUploadComponent implements OnInit {
  form!: FormGroup

  @ViewChild("archiveFile")
  archiveFile!: ElementRef

  fb = inject(FormBuilder)
  uploadSvc = inject(ServerService)
  router = inject(Router)

  ngOnInit(): void {
    this.form = this.createForm()
  }

  createForm(): FormGroup {
    return this.fb.group({
      name: this.fb.control<string>('', [Validators.required]),
      title: this.fb.control<string>('', [Validators.required]),
      comments: this.fb.control<string>(''),
      archive: this.fb.control<any>(null, [Validators.required])
    })
  }

  upload(): void {
    let a: Archive = { ... this.form.value }
    a.archive = this.archiveFile.nativeElement.files[0]
    firstValueFrom(this.uploadSvc.uploadFile(a))
      .then(r => {
        this.router.navigate(['/bundle', r.bundleId])
      })
      .catch(
        err => alert(err.error.error)
      )
  }
}
