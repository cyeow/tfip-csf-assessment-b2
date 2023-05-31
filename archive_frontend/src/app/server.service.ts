import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { Archive, Bundle } from "./models";
import { Observable, firstValueFrom } from "rxjs";

@Injectable()
export class ServerService {

    http = inject(HttpClient)

    uploadUrl: string = "/upload"
    bundleUrl: string = "/bundle"
    bundlesUrl: string = "/bundles"

    uploadFile(a: Archive): Observable<{bundleId: string}> {
        let fd = new FormData();
        fd.set("name", a.name)
        fd.set("title", a.title)
        fd.set("comments", a.comments)
        fd.set("archive", a.archive)
        return this.http.post<{bundleId: string}>(this.uploadUrl, fd)
    }

    loadBundle(bundleId: string): Observable<Bundle> {
        const bundleIdUrl = this.bundleUrl + '/' + bundleId
        const headers = new HttpHeaders({
            accept: 'application/json'
        })
        return this.http.get<Bundle>(bundleIdUrl, { headers })
    }

    loadBundles(): Observable<Bundle[]> {
        const headers = new HttpHeaders({
            accept: 'application/json'
        })
        return this.http.get<Bundle[]>(this.bundlesUrl, { headers })
    }
}