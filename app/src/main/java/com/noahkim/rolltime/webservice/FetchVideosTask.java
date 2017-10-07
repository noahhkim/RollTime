package com.noahkim.rolltime.webservice;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.noahkim.rolltime.BuildConfig;
import com.noahkim.rolltime.adapters.VideoAdapter;
import com.noahkim.rolltime.data.Video;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import timber.log.Timber;

/**
 * Created by Noah on 10/5/2017.
 */

public class FetchVideosTask extends AsyncTask<String, Void, List<Video>> {

    private Context mContext;
    private List<Video> mVideos;
    private VideoAdapter mVideoAdapter;

    public FetchVideosTask(Context context, VideoAdapter videoAdapter) {
        mContext = context;
        mVideoAdapter = videoAdapter;
    }

    @Override
    protected List<Video> doInBackground(String... params) {
//        if (params.length == 0) {
//            return null;
//        }
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(mContext.getString(R.string.youtube_base_url))
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        final Api videoApi = retrofit.create(Api.class);
//        Call<List<Video>> videoCall = videoApi.getVideos();
//        try {
//            videoCall.execute();
//            Log.d(LOG_TAG, "Video call: " + videoCall.toString());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;

        if (params.length == 0) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String videoJsonString;

        Uri uri = new Uri.Builder()
                .scheme("https")
                .authority("www.googleapis.com")
                .appendPath("youtube")
                .appendPath("v3")
                .appendPath("search")
                .appendQueryParameter("part", "snippet")
                .appendQueryParameter("maxResults", "10")
                .appendQueryParameter("q", "brazilian+jiu+jitsu")
                .appendQueryParameter("key", BuildConfig.YOUTUBE_API_KEY)
                .build();

        try {
            URL url = new URL(uri.toString());
            Timber.d(url.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return null;
            }
            videoJsonString = buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Video> videos) {
        if (videos != null && mVideoAdapter != null) {
            mVideoAdapter.setVideos(videos);
        }
        super.onPostExecute(videos);
    }
}
