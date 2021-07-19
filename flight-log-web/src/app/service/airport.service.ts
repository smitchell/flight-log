import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import * as L from 'leaflet';
import {Airports} from "../models/airports";
import {Observable} from "rxjs";
import { map, filter } from 'rxjs/operators';
import {RequestBuilder} from "./request-builder";
import {StrictHttpResponse} from "./strict-http-response";


@Injectable({
  providedIn: 'root'
})
export class AirportService {
  private baseUrl: string = 'http://localhost:8080'
  private airports: string = '/assets/data/airports.json';

  constructor(private http: HttpClient) {
  }

  public getAirportsByRegion(region: string): Observable<StrictHttpResponse<Airports>> {
    const rb = new RequestBuilder(this.baseUrl, '/api/airports-by-region', 'get');
    rb.query('region', region, {});

    return this.http.request(rb.build({
      responseType: 'json',
      accept: 'application/json'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<Airports>;
      })
    );
    }

  makeAirportMarkers(map: L.Map): void {
    console.log('makeAirportMarkers()');
    this.http.get(this.airports).subscribe((res: any) => {
      let firstRow: boolean = true;
      for (const airport of res) {
        if (!firstRow) {
          const lon = airport.longitude_deg;
          const lat = airport.latitude_deg;
          const marker = L.marker([lat, lon]);

          marker.addTo(map);
        }
        firstRow = false;
      }
    });
  }

  getStandardHeaders(): HttpHeaders {
    return new HttpHeaders()
      .set('Content-Type', 'application/json;charset=UTF-8')
      .set('LANGUAGE', "en-us");
  }

}

