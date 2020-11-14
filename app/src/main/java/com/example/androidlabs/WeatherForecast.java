package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {

    TextView UV;
    TextView max;
    TextView min;
    TextView values;
    ProgressBar bar;
    ImageView weather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        max = findViewById(R.id.max7);
        min = findViewById(R.id.min9);
        values = findViewById(R.id.temp2);
        UV = findViewById(R.id.UvRating12);
        weather = findViewById(R.id.weather2);
        bar = findViewById(R.id.progressBar4);
        bar.setVisibility(View.VISIBLE);
        ForecastQuery req = new ForecastQuery();
        req.execute("https://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");
    }
        public class ForecastQuery extends AsyncTask <String, Integer, String> {
            String UVRating ;
            String minimum ;
             String maximum ;
            String currentTemperature ;
            Bitmap bitMap;
            String readerLine;
            String solution;
            StringBuilder subSolution;
            BufferedReader readerAll;
            URL l;
            String icon_Name;
            @Override
            protected String doInBackground(String... Strings) {

                    try {
                    URL url = new URL(Strings[0]);
                    HttpURLConnection urlConnection =
                            (HttpURLConnection)
                            url.openConnection();
                    InputStream r=
                            urlConnection.getInputStream();


                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(false);
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput( r , "UTF-8");

                    icon_Name = null;
                    int eventType = xpp.getEventType();
                        while(eventType != XmlPullParser.END_DOCUMENT) {
                            if(eventType == XmlPullParser.START_TAG) {
                            if(xpp.getName().equals("temperature")) {
                                currentTemperature = xpp.getAttributeValue(null, "value");
                                publishProgress(25);
                                minimum = xpp.getAttributeValue(null, "min");
                                publishProgress(50);
                                maximum= xpp.getAttributeValue(null, "max");
                                publishProgress(75);
                            }
                          else  if(xpp.getName().equals("weather")) {
                              icon_Name = xpp.getAttributeValue(null, "icon");

                                if(fileExistence(icon_Name + ".png")){
                                    Log.i(icon_Name, "the  image exists ");
                                    FileInputStream fis = null;
                                    try {    fis = openFileInput(icon_Name + ".png");   }
                                    catch (FileNotFoundException e) { e.printStackTrace();  }
                                    bitMap = BitmapFactory.decodeStream(fis);

                                }else {
                                    Log.i(icon_Name, "if does not exist, download from URL");
                                    URL imageUrl = new URL("http://openweathermap.org/img/w/"+ icon_Name + ".png");
                                    urlConnection = (HttpURLConnection) imageUrl.openConnection();
                                    urlConnection.connect();
                                    int responseCode = urlConnection.getResponseCode();
                                    if (responseCode == 200) {
                                        bitMap = BitmapFactory.decodeStream(urlConnection.getInputStream());
                                    }
                                    FileOutputStream outputStream  = openFileOutput(icon_Name + ".png", Context.MODE_PRIVATE);
                                    bitMap.compress(Bitmap.CompressFormat.PNG, 80, outputStream );
                                    outputStream .flush();
                                    outputStream .close();
                                    publishProgress(100);
                                }
                            }
                        }    eventType = xpp.next();
                    }

                     l= new URL("http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");
                    HttpURLConnection check_Connection = (HttpURLConnection)
                            l.openConnection();
                    r = check_Connection.getInputStream();

                     readerAll = new BufferedReader(new InputStreamReader(r, "UTF-8"), 8);
                    subSolution = new StringBuilder();


                    while ((readerLine = readerAll.readLine()) != null) {
                        subSolution.append(readerLine).append("\n");
                    }
                    solution=   subSolution.toString();

                    JSONObject jObject = new JSONObject(  solution);
                    float value = (float) jObject.getDouble("value");
                    UVRating = String.valueOf(value);
                    Log.i("", "Uv : " + UV);
                }


                catch (Exception e) {
                    Log.i("", "Warning");
                }
                return "Done";
            }

            //Type 2
            public void onProgressUpdate(Integer ... args)
            {
           bar.setVisibility(View.VISIBLE);
                bar.setProgress(args[0]);
            }
            //Type3
            public void onPostExecute(String fromDoInBackground) {
                Log.i("HTTP", fromDoInBackground);
             bar.setVisibility(View.INVISIBLE);
                values.setText("temperature: " + currentTemperature + "C");
                min.setText("Min: " + minimum+ "C");
                max.setText("Max: " + maximum + "C");
                UV.setText("UV rating: " + UVRating  );
                weather.setImageBitmap(bitMap);
            }
            public boolean fileExistence(String fname){
                File file = getBaseContext().getFileStreamPath(fname);
                return file.exists();
            }

        }
}