import { Movie } from "./Movie";

export interface MovieInfo extends Movie{
  rated :string;
  released :string;
  runtime :string;
  genre :string;
  director :string;
  actors :string;
  plot :string;
  awards :string;
  imdbRating :string;

}
