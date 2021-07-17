import { Component, OnInit, AfterViewInit } from '@angular/core';
import * as L from 'leaflet';
import { AirportService } from "../../service/airport.service";

const iconRetinaUrl = 'assets/marker-icon.png';
const iconUrl = 'assets/marker-icon.png';
const shadowUrl = 'assets/marker-shadow.png';
const iconDefault = L.icon({
  iconRetinaUrl,
  iconUrl,
  shadowUrl,
  iconSize: [12, 20],
  iconAnchor: [6, 10],
  popupAnchor: [1, -34],
  tooltipAnchor: [8, -14],
  shadowSize: [20, 20]
});
L.Marker.prototype.options.icon = iconDefault;

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements AfterViewInit {
  private map: any;

  constructor(private airportService: AirportService) { }

  ngAfterViewInit(): void {
    this.initMap();
    this.airportService.makeAirportMarkers(this.map);
  }

  private initMap(): void {
    this.map = L.map('map', {
      center: [ 39.8282, -98.5795 ],
      zoom: 9
    });

    const tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 18,
      minZoom: 5,
      attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    });

    tiles.addTo(this.map);
  }

}
