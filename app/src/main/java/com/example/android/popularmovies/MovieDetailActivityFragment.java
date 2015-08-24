package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
        // Assign the data from the movie to view objects.
        TextView txtMovieTitle = (TextView) rootView.findViewById(R.id.movie_title);
        txtMovieTitle.setText(intent.getStringExtra("title"));
        ImageView imgMovieImage = (ImageView) rootView.findViewById(R.id.movie_image);
        Picasso.with(getActivity())
                .load("http://image.tmdb.org/t/p/w342/" + intent.getStringExtra("posterPath"))
                .into(imgMovieImage);
        TextView txtMovieYear = (TextView) rootView.findViewById(R.id.movie_year);
        txtMovieYear.setText(intent.getStringExtra("releaseDate"));
        TextView txtMovieRate = (TextView) rootView.findViewById(R.id.movie_rate);
        txtMovieRate.setText(intent.getStringExtra("voteAverage") + "/10");
        TextView txtMovieSynopsis = (TextView) rootView.findViewById(R.id.movie_synopsis);
        txtMovieSynopsis.setText(intent.getStringExtra("overview"));

        return rootView;
    }
}
