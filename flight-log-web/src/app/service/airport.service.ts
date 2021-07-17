import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import * as L from 'leaflet';


@Injectable({
  providedIn: 'root'
})
export class AirportService {
  airports: string = '/assets/data/airports.json';

  constructor(private http: HttpClient) {
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

}

interface Airport {
  id: number;
  ident: string;
  type: string;
  name: string;
  latitude_deg: number;
  longitude_deg: number;
  elevation_ft: number;
  continent: string;
  iso_country: string;
  iso_region: string;
  municipality: string;
  scheduled_service: string;
  gps_code: string;
  iata_code: string;
  local_code: string;
  home_link: string;
  wikipedia_link: string;
  keywords: string;
}
