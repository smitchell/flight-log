import {Airport} from "./airport";
import {BoundsMinMax} from "./bounds-min-max";

export interface Airports {
  bounds: BoundsMinMax;
  data: Airport[];
}
