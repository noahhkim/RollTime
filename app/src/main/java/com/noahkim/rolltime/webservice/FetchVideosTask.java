package com.noahkim.rolltime.webservice;

/**
 * Created by Noah on 10/5/2017.
 */

//public class FetchVideosTask extends AsyncTask<String, Void, List<Video>> {
//
//    private Context mContext;
//    private List<Video> mVideos;
//    private VideoAdapter mVideoAdapter;
//    private FirebaseDatabase mFirebaseDatabase;
//    private DatabaseReference mVideoReference;
//
//    public FetchVideosTask(Context context, VideoAdapter videoAdapter) {
//        mContext = context;
//        mVideoAdapter = videoAdapter;
//    }
//
//    @Override
//    protected List<Video> doInBackground(String... params) {
//
//        HttpURLConnection urlConnection = null;
//        BufferedReader reader = null;
//        String videoJsonString;
//
//        Uri uri = new Uri.Builder()
//                .scheme("https")
//                .authority("www.googleapis.com")
//                .appendPath("youtube")
//                .appendPath("v3")
//                .appendPath("search")
//                .appendQueryParameter("part", "snippet")
//                .appendQueryParameter("maxResults", "10")
//                .appendQueryParameter("q", "brazilian+jiu+jitsu")
////                .appendQueryParameter("key", mContext.getString(R.string.youtube_api_key))
//                .build();
//
//        try {
//            URL url = new URL(uri.toString());
//            Timber.d(url.toString());
//
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.connect();
//
//            InputStream inputStream = urlConnection.getInputStream();
//            StringBuffer buffer = new StringBuffer();
//            if (inputStream == null) {
//                return null;
//            }
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                buffer.append(line + "\n");
//            }
//            if (buffer.length() == 0) {
//                return null;
//            }
//
//            // Get response as a string and extract data from JSON
//            videoJsonString = buffer.toString();
//            getVideoDataFromJson(videoJsonString);
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } finally {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return null;
//    }
//
//    private void getVideoDataFromJson(String videoJson) throws JSONException {
//        JSONObject baseJsonResponse = new JSONObject(videoJson);
//        JSONArray videoArray = baseJsonResponse.getJSONArray("items");
//
////        final Map<String, Object> videoValues = null;
//
//        for (int i = 0; i < videoArray.length(); i++) {
//            JSONObject currentVideo = videoArray.getJSONObject(i);
//            JSONObject id = currentVideo.getJSONObject("id");
//            final String videoId = id.getString("videoId");
//            JSONObject videoSnippet = currentVideo.getJSONObject("snippet");
//            String videoTitle = videoSnippet.getString("title");
//            JSONObject thumbnails = videoSnippet.getJSONObject("thumbnails");
//            JSONObject defaultSize = thumbnails.getJSONObject("default");
//            String thumbnailUrl = defaultSize.getString("url");
//
//            final Video video = new Video(videoTitle, thumbnailUrl, videoId);
//
//            // Send data to Firebase
//            mFirebaseDatabase = FirebaseDatabase.getInstance();
//            mVideoReference = mFirebaseDatabase.getReference();
//            mVideoReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    if (!dataSnapshot.hasChild("videos")) {
//                        DatabaseReference videoReference = mFirebaseDatabase.getReference().child("videos");
//                        videoReference.push().setValue(video);
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        }
//    }
//
//
//    @Override
//    protected void onPostExecute(List<Video> videos) {
//        super.onPostExecute(videos);
//    }
//}
