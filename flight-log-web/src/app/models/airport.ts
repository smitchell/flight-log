import {Point} from "leaflet";

export interface Airport {
  id: number;
  ident: string;
  type: string;
  name:string;
  location: Point;
  elevation: number;
  continent: string;
  country: string;
  region: string;
  municipality: string;
  scheduledService: boolean;
  gpsCode: string;
  ataCode: string;
  localCode: string;
  homeLink: string;
  wikipediaLink: string;
  keywords: string;
}
