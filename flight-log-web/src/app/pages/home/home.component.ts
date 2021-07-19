import { Component, OnInit, AfterViewInit } from '@angular/core';
import * as L from 'leaflet';
import { AirportService } from "../../service/airport.service";
import {State} from "../../models/state";
import {Airports} from "../../models/airports";
import {MatSnackBar} from "@angular/material/snack-bar";

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
export class HomeComponent implements OnInit, AfterViewInit {
  public state: string = '';
  public states: State[] = []
  private map: any;
  private layerGroup: any;

  constructor(
    private airportService: AirportService,
    private snack: MatSnackBar,) { }

  ngAfterViewInit(): void {
    this.initMap();
  }

  private initMap(): void {
    this.map = L.map('map', {
      center: [ 39.8282, -98.5795 ],
      zoom: 9
    });
    this.layerGroup = L.layerGroup().addTo(this.map);

    const tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 18,
      minZoom: 5,
      attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    });

    tiles.addTo(this.map);
  }

  onStateValueChange(event: any) {
    console.log('onStateValueChange() <---' + event.value);
    this.airportService.getAirportsByRegion(event.value)
      .subscribe(response => {
        this.mapAirports(response.body);
      },
        error => this.handleError(error, "Unable to load airports at this time."));
  }

  mapAirports(airports: Airports) {
    console.log("mapAirports()");
    this.layerGroup.clearLayers();
    if (airports && airports.data) {
      const swCorner = L.latLng(airports.bounds.minLat, airports.bounds.maxLon);
      const neCorner = L.latLng(airports.bounds.maxLat, airports.bounds.minLon);
      const bounds: L.LatLngBounds = new L.LatLngBounds(swCorner, neCorner);
      this.map.flyToBounds(bounds);
      for (const airport of airports.data) {
        const marker = L.marker([airport.location.y, airport.location.x]);
        marker.addTo(this.layerGroup);
      }
    }

  }

  private handleError(error: Response, message: string) {
    this.snack.dismiss();
    this.snack.open(message, 'Close', {panelClass: ['style-error'], duration: 10});
  }

  ngOnInit(): void {
    this.states.push(new State("US-AL", "Alabama"));
    this.states.push(new State("US-AK", "Alaska"));
    this.states.push(new State("US-AZ", "Arizona"));
    this.states.push(new State("US-AR", "Arkansas"));
    this.states.push(new State("US-CA", "California"));
    this.states.push(new State("US-CO", "Colorado"));
    this.states.push(new State("US-CT", "Connecticut"));
    this.states.push(new State("US-DE", "Delaware"));
    this.states.push(new State("US-FL", "Florida"));
    this.states.push(new State("US-GA", "Georgia"));
    this.states.push(new State("US-HI", "Hawaii"));
    this.states.push(new State("US-ID", "Idaho"));
    this.states.push(new State("US-IL", "Illinois"));
    this.states.push(new State("US-IN", "Indiana"));
    this.states.push(new State("US-IA", "Iowa"));
    this.states.push(new State("US-KS", "Kansas"));
    this.states.push(new State("US-KY", "Kentucky"));
    this.states.push(new State("US-LA", "Louisiana"));
    this.states.push(new State("US-ME", "Maine"));
    this.states.push(new State("US-MD", "Maryland"));
    this.states.push(new State("US-MA", "Massachusetts"));
    this.states.push(new State("US-MI", "Michigan"));
    this.states.push(new State("US-MN", "Minnesota"));
    this.states.push(new State("US-MS", "Mississippi"));
    this.states.push(new State("US-MO", "Missouri"));
    this.states.push(new State("US-MT", "Montana"));
    this.states.push(new State("US-NE", "Nebraska"));
    this.states.push(new State("US-NV", "Nevada"));
    this.states.push(new State("US-NH", "New Hampshire"));
    this.states.push(new State("US-NJ", "New Jersey"));
    this.states.push(new State("US-NM", "New Mexico"));
    this.states.push(new State("US-NY", "New York"));
    this.states.push(new State("US-NC", "North Carolina"));
    this.states.push(new State("US-ND", "North Dakota"));
    this.states.push(new State("US-OH", "Ohio"));
    this.states.push(new State("US-OK", "Oklahoma"));
    this.states.push(new State("US-OR", "Oregon"));
    this.states.push(new State("US-PA", "Pennsylvania"));
    this.states.push(new State("US-RI", "Rhode Island"));
    this.states.push(new State("US-SC", "South Carolina"));
    this.states.push(new State("US-SD", "South Dakota"));
    this.states.push(new State("US-TN", "Tennessee"));
    this.states.push(new State("US-TX", "Texas"));
    this.states.push(new State("US-UT", "Utah"));
    this.states.push(new State("US-VT", "Vermont"));
    this.states.push(new State("US-VA", "Virginia"));
    this.states.push(new State("US-WA", "Washington"));
    this.states.push(new State("US-WV", "West Virginia"));
    this.states.push(new State("US-WI", "Wisconsin"));
    this.states.push(new State("US-WY", "Wyoming"));
    this.states.push(new State("US-DC", "District of Columbia"));
  }

}
