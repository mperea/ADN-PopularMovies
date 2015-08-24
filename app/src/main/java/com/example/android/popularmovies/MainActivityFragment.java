package com.example.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private MovieAdapter mMoviesAdapter;
    private GridView myGridView;
    private List<Movie> movieList;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

        movieList = new ArrayList<>();
        mMoviesAdapter = new MovieAdapter(getActivity(), movieList);
        myGridView = (GridView) rootView.findViewById(R.id.gridview);
        myGridView.setAdapter(mMoviesAdapter);

        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) myGridView.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                intent.putExtra("id", movie.getId());
                intent.putExtra("posterPath", movie.getPosterPath());
                intent.putExtra("title", movie.getTitle());
                intent.putExtra("releaseDate", movie.getReleaseDate());
                intent.putExtra("overview", movie.getOverview());
                intent.putExtra("voteAverage", movie.getVoteAverage());
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void updateMovieList() {
        GetMovieListTask weatherTask = new GetMovieListTask();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortBy = prefs.getString(getString(R.string.pref_sort_by_key),
                getString(R.string.pref_sort_by_default));

        weatherTask.execute(sortBy);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovieList();
    }

    /**
     * The asynctask that will fetch the data from de TheMovieDB.com
     */
    public class GetMovieListTask extends AsyncTask<String, Void, List<Movie>> {

        private final String LOG_TAG = GetMovieListTask.class.getSimpleName();

        @Override
        protected List<Movie> doInBackground(String... params) {
            // The TheMovieDB.com API key.
            final String apiKey = "";   // Populate it with your own key please.

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieListJsonStr = null;

            try {

                final String TMDB_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_BY_PARAM = "sort_by";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY_PARAM, params[0])
                        .appendQueryParameter(API_KEY_PARAM, apiKey)
                        .build();

                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                movieListJsonStr = buffer.toString();

            } catch (IOException e) {

                Log.e(LOG_TAG, "Error ", e);
                return null;

            } finally {

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }

            }

            try {
                return getMoviesDataFromJson(movieListJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        /**
         * Takes the string with the movie list from TMDB.com in JSON format and constructs
         * a list of Movie objects with the data we need.
         *
         * @param movieListJsonStr  the JSON string result of fetching data from /discover/movie
         * @return  A list of Movie objects with the data we need from each movie in the JSON.
         */
        private List<Movie> getMoviesDataFromJson(String movieListJsonStr) throws JSONException {
            // The names of the JSON objects that need to be extracted.
            final String TMDB_RESULTS = "results";
            final String TMDB_ID = "id";
            final String TMDB_TITLE = "title";
            final String TMDB_OVERVIEW = "overview";
            final String TMDB_RELEASE_DATE = "release_date";
            final String TMDB_POSTER_PATH = "poster_path";
            final String TMDB_VOTE_AVERAGE = "vote_average";

            JSONObject moviesJson = new JSONObject(movieListJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(TMDB_RESULTS);

            List<Movie> movieList = new ArrayList<>();
            Movie movie;
            for(int i = 0; i < moviesArray.length(); i++) {
                JSONObject movieJson = moviesArray.getJSONObject(i);

                movie = new Movie();

                movie.setId(movieJson.getString(TMDB_ID));
                movie.setPosterPath(movieJson.getString(TMDB_POSTER_PATH));
                movie.setTitle(movieJson.getString(TMDB_TITLE));
                movie.setOverview(movieJson.getString(TMDB_OVERVIEW));
                movie.setVoteAverage(movieJson.getString(TMDB_VOTE_AVERAGE));
                movie.setReleaseDate(movieJson.getString(TMDB_RELEASE_DATE).split("-")[0]);

                movieList.add(movie);
            }
            return movieList;
        }

        @Override
        protected void onPostExecute(List<Movie> movieList) {
            // Add the data to the adapter.
            if (movieList != null) {
                mMoviesAdapter.clear();
                for(Movie movie : movieList) {
                    mMoviesAdapter.add(movie);
                }
            }
        }

    }

}
