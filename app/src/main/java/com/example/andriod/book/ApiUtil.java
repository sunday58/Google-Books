package com.example.andriod.book;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class ApiUtil {
    private ApiUtil(){}

    public static final String BASE_API_URL =
            "https://www.googleapis.com/books/v1/volumes";
    public static final String QUERY_PARAMETER_KEY = "q";
    public static final String Key = "key";
    public static final String API_KEY = "ADD YOUR GOOGLE BOOKS API KEY HERE";
    public static final String TITLE = "intitle:";
    public static final String AUTHOR = "inauthor:";
    public static final String PUBLISHER = "inpublisher:";
    public static final String ISBN = "inisbn:";

    public static URL buildUrl(String title) {

        URL url = null;
        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAMETER_KEY, title)
                .appendQueryParameter(Key, API_KEY)
                .build();
        try {
            url = new URL(uri.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildUrl(String title, String author, String publisher, String isbn){
    URL url = null;

        StringBuilder sb = new StringBuilder();
                if(!title.isEmpty()) sb.append(TITLE + title + "+");
                if (!author.isEmpty()) sb.append(AUTHOR + author + "+");
                if (!publisher.isEmpty()) sb.append(PUBLISHER + publisher + "+");
                if (!isbn.isEmpty()) sb.append(ISBN + isbn + "+");

                sb.setLength(sb.length()-1);
                String querry = sb.toString();
                Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAMETER_KEY, querry)
                        .appendQueryParameter(Key, API_KEY)
                        .build();
       try {
           url = new URL(uri.toString());
       }catch (Exception e){
           e.printStackTrace();
       }
       return url;
    }

    public static String getJson(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            InputStream stream = connection.getInputStream();
            Scanner scanner = new Scanner(stream);
            scanner.useDelimiter("\\A");
            boolean hasData = scanner.hasNext();
            if (hasData) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.d("Error", e.toString());
            return null;
        } finally {
            connection.disconnect();
        }
    }

    public class booksQuerryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL  searchUrl = urls[0];
            String result = null;
            try{
                result = ApiUtil.getJson(searchUrl);
            } catch (IOException e) {
                Log.d("error", e.getMessage());
            }
            return result;
        }
    }

    public static ArrayList<Book> getBooksFromJson(String json) {

        final String ID = "id";
        final String TITLE = "title";
        final String AUTHORS = "authors";
        final String SUBTITLE = "subtitle";
        final String PUBLISHER = "publisher";
        final String PUBLISHED_DATE = "publishedDate";
        final String ITEMS = "items";
        final String VOLUMEINFO = "volumeInfo";
        final String DESCRIPTION = "description";
        final String IMAGELINKS = "imageLinks";
        final String THUMBNAIL = "thumbNail";

        ArrayList<Book> books = new ArrayList<>();

        try {
            JSONObject jsonBooks = new JSONObject(json);
            JSONArray arrayBooks = jsonBooks.getJSONArray(ITEMS);
            int numberOfBooks = arrayBooks.length();

            for (int i=0; i<numberOfBooks; i++){
                JSONObject bookJson = arrayBooks.getJSONObject(i);
                JSONObject volumeInfoJson = bookJson.getJSONObject(VOLUMEINFO);
                JSONObject imageLinksJson = null;
                if (volumeInfoJson.has(IMAGELINKS)) {
                  imageLinksJson = volumeInfoJson.getJSONObject(IMAGELINKS);
                }
                int authorNum;
                try {
                     authorNum = volumeInfoJson.getJSONArray(AUTHORS).length();
                }catch (Exception e){
                    authorNum = 0;
                }

                String[] authors = new String[authorNum];
                for (int j=0; j<authorNum; j++){
                    authors[j] = volumeInfoJson.getJSONArray(AUTHORS).get(j).toString();
                }
                Book book = new Book(
                        bookJson.getString(ID), volumeInfoJson.getString(TITLE),
                        (volumeInfoJson.isNull(SUBTITLE) ? "": volumeInfoJson.getString(SUBTITLE)),
                        authors,
                        (volumeInfoJson.isNull(PUBLISHER)? "": volumeInfoJson.getString(PUBLISHER)),
                        (volumeInfoJson.isNull(PUBLISHED_DATE)? "": volumeInfoJson.getString(PUBLISHED_DATE)),
                        (volumeInfoJson.isNull(DESCRIPTION) ? "": volumeInfoJson.getString(DESCRIPTION)),
                        (imageLinksJson==null)?"": imageLinksJson.getString(THUMBNAIL)
                );

                books.add(book);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return books;
    }
}
