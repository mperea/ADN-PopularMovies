package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Intent intent = getActivity().getIntent();
        Movie movie = intent.getParcelableExtra("movie");

        // Assign the data from the movie to view objects.
        TextView txtMovieTitle = (TextView) rootView.findViewById(R.id.movie_title);
        txtMovieTitle.setText(movie.getTitle());
        ImageView imgMovieImage = (ImageView) rootView.findViewById(R.id.movie_image);
        Picasso.with(getActivity())
                .load("http://image.tmdb.org/t/p/w342/" + movie.getPosterPath())
                .placeholder(R.drawable.poster_placeholder)
                .error(R.drawable.poster_placeholder_error)
                .into(imgMovieImage);
        TextView txtMovieYear = (TextView) rootView.findViewById(R.id.movie_year);
        txtMovieYear.setText(movie.getReleaseDate());
        TextView txtMovieRate = (TextView) rootView.findViewById(R.id.movie_rate);
        txtMovieRate.setText(movie.getVoteAverage() + "/10");
        TextView txtMovieSynopsis = (TextView) rootView.findViewById(R.id.movie_synopsis);
        txtMovieSynopsis.setText(movie.getOverview());

        return rootView;
    }
}
