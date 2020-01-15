package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    EditText editText ;

    TextView textView ;

    public class DownloadTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... urls) {
            String result =  "";
            HttpURLConnection urlConnection = null;
            URL url;
            try{
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while(data!=-1){
                    char current = (char) data;
                    result+=current;
                    data=reader.read();
                }

            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {

                JSONObject jsonObject =  new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                Log.i("Weather Info",weatherInfo);
                JSONArray weatherArray = new JSONArray(weatherInfo);
                for(int i=0;i<weatherArray.length();i++){
                    JSONObject object = weatherArray.getJSONObject(i);
                    String out = "   "+object.getString("main") + " : " + object.getString("description")+"\r\n";
                    textView.setText(out);
                    Log.i("main",object.getString("main"));
                    Log.i("description",object.getString("description"));
                }
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "INVALID CITY ! PLEASE CHECK AGAIN ! ", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void getWeather(View view){
        try{
            String location = editText.getText().toString();
            String encodedLocation = URLEncoder.encode(location,"UTF-8");
            DownloadTask task = new DownloadTask();
            task.execute("https://api.openweathermap.org/data/2.5/weather?q="+encodedLocation+"&appid=2b2dbedd521b1cdb765988d8abca714f");

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(),0);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "INVALID CITY ! PLEASE CHECK AGAIN ! ", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById( R.id.editText);
        textView  = (TextView) findViewById( R.id.textView);


    }
}
