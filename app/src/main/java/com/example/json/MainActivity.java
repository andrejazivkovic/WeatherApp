package com.example.json;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    public class DownloadJson extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls) {

            String rezzultat="";
            HttpsURLConnection connection;

            try {
                URL url = new URL(urls[0]);
                connection = (HttpsURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data!=-1){
                    char character = (char) data;
                    rezzultat+=character;
                    data=reader.read();
                }

                return rezzultat;
            }

            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }
    }
    EditText city;
    TextView description;
    TextView temp;
    TextView real_temp;
    DecimalFormat df2= new DecimalFormat("#.##");
    @SuppressLint("SetTextI18n")
    public void clicked(View view) throws ExecutionException, InterruptedException {
        city=(EditText) findViewById(R.id.editText);
        description=(TextView) findViewById(R.id.textView4);
        temp=(TextView) findViewById(R.id.temp);
        real_temp=(TextView) findViewById(R.id.real_temp);


        DownloadJson downloadJson = new DownloadJson();
        String rezz;
        try {
            rezz = downloadJson.execute("https://api.openweathermap.org/data/2.5/weather?q="+city.getText().toString()+"&appid=486064cd49babb6799248ac61c6c2cd5").get();
            JSONObject object = new JSONObject(rezz);
            String temperatura=object.getString("main");
            String[] tepMC = temperatura.split(",");
            String[] tempMMC=tepMC[0].split(":");
            String real_tepm=tempMMC[1];
            double kelvin= Double.parseDouble(real_tepm);
            double celzius= kelvin - 273.15;
            String weatherInfo = object.getString("weather");
            JSONArray arr = new JSONArray(weatherInfo);
            String descriptionE="";
            String tempE="";
            for(int i=0; i<arr.length();i++){
                JSONObject jsonObject = arr.getJSONObject(i);
                descriptionE=jsonObject.getString("description");
                tempE=jsonObject.getString("main");
            }
            temp.setText(tempE);
            description.setText(descriptionE);
            real_temp.setText(df2.format(celzius) + " C");


        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

}
