package com.example.smartbabycot_44version;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MainActivity2 extends AppCompatActivity {

    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;
    private EditText editText;
    private TextView Text1;
    private ImageView micButton;
    private  TextView temp1;
    private  TextView weight1;
    private  TextView humidity1;

    private Button connect;


    String firstCommand = "hey baby";
    String secondCommand = "how are you baby";

    String firstCommand1 = "Hey baby";
    String secondCommand1 = "How are you baby";

    String mode1 = "Change to online mode";
    String mode2 = "change to online mode";

    String mode11 = "Change to offline mode";
    String mode22 = "change to offline mode";

    String Data = "";
    String signal = "";
    String temp = "";
    String weight = "";
    String humidity = "";
    String DataGet = "";
    String status = "B";
    String URL ="";

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    TextToSpeech T;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        temp1 = findViewById(R.id.textView3);
        weight1 = findViewById(R.id.textView5);
        humidity1 = findViewById(R.id.textView7);

        connect = findViewById(R.id.button2);

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }

        editText = findViewById(R.id.text);
        Text1 = findViewById(R.id.textView2);
        micButton = findViewById(R.id.button);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        T = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    T.setLanguage(Locale.ENGLISH);
                }
            }
        });

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                editText.setText("");
                editText.setHint("Listening...");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                micButton.setImageResource(R.mipmap.audio);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                editText.setText(data.get(0));
//                String res = String.valueOf(editText.getText());

                String res = String.valueOf(data.get(0));

                Toast.makeText(getApplicationContext(),"Working 1",Toast.LENGTH_SHORT).show();
                if (res.equals(firstCommand) || res.equals(firstCommand1)) {
                    Toast.makeText(getApplicationContext(),"working",Toast.LENGTH_SHORT).show();
                    if (Objects.equals(status, "A")) {
                        sendAndRequestResponse();
                        Toast.makeText(getApplicationContext(),"command activated",Toast.LENGTH_SHORT).show();
                    }else if (Objects.equals(status, "B")) {
                        postDataUsingVolley("A");
                        Toast.makeText(getApplicationContext(),"command activated",Toast.LENGTH_SHORT).show();
                    }

                }
                if (Objects.equals((res), secondCommand) || res.equals(secondCommand1)) {
                    if (Objects.equals(status, "A")) {
                        sendAndRequestResponse();
                        Toast.makeText(getApplicationContext(),"command activated",Toast.LENGTH_SHORT).show();
                    }else if (Objects.equals(status, "B")) {
                        postDataUsingVolley("A");
                        Toast.makeText(getApplicationContext(),"command activated",Toast.LENGTH_SHORT).show();
                    }
                }

                switch (res) {
                    case "Change to online mode":
                        T.speak("Changing to online mode", TextToSpeech.QUEUE_FLUSH, null);
                        connect.setText("Offline");
                        status = "A";
                        postDataUsingVolley("B");
                        Toast.makeText(getApplicationContext(),URL,Toast.LENGTH_SHORT).show();

                        break;
//                    case "change to online mode":
//                        T.speak("Changing to online mode", TextToSpeech.QUEUE_FLUSH, null);
//                        connect.setText("Offline");
//                        status = "A";
//                        postDataUsingVolley("B");
//                        Toast.makeText(getApplicationContext(),URL,Toast.LENGTH_SHORT).show();
//
//                        break;
                    case "Change to offline mode":
                        T.speak("Changing to offline mode", TextToSpeech.QUEUE_FLUSH, null);
                        connect.setText("Online");
                        status = "B";
                        postDataUsingVolley("C");
                        Toast.makeText(getApplicationContext(),URL,Toast.LENGTH_SHORT).show();

                        break;

//                    case "change to offline mode":
//                        T.speak("Changing to offline mode", TextToSpeech.QUEUE_FLUSH, null);
//                        connect.setText("Online");
//                        status = "B";
//                        postDataUsingVolley("C");
//                        Toast.makeText(getApplicationContext(),URL,Toast.LENGTH_SHORT).show();
//
//                        break;
                }




            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });


        Handler handler = new Handler();

        Runnable r=new Runnable() {
            public void run() {
                micButton.setImageResource(R.mipmap.audio);
                speechRecognizer.startListening(speechRecognizerIntent);
                handler.postDelayed(this, 1000);
            }
        };

        handler.post(r);

        micButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    micButton.setImageResource(R.mipmap.audio_white);
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
                return false;
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cmdText = null;
                String btnState = connect.getText().toString().toLowerCase();
                switch (btnState) {
                    case "online":
                        connect.setText("Offline");
                        status = "A";
                        postDataUsingVolley("B");
                        Toast.makeText(getApplicationContext(),URL,Toast.LENGTH_SHORT).show();
                        break;
                    case "offline":
                        connect.setText("Online");
                        status = "B";
                        postDataUsingVolley("C");
                        Toast.makeText(getApplicationContext(),URL,Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
    }

    private void postDataUsingVolley(String name) {

//        String url = "http://192.168.4.1/post";
        String url = "http://192.168.4.1/post";
        URL = "http://192.168.4.1/post";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(MainActivity2.this);
//        String URL = ("http://" + url);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Data = response;

                try {
                    // get JSONObject from JSON file
                    JSONObject obj = new JSONObject(Data);
                    // fetch JSONObject named employee
//                    JSONObject employee = obj.getJSONObject("time");
                    // get employee name and salary

                    signal = obj.getString("signal");
                    temp = obj.getString("temp");
                    weight = obj.getString("weight");
                    humidity = obj.getString("humidity");

                    temp1.setText(temp);
                    weight1.setText(weight);
                    humidity1.setText(humidity);


//                    salary = employee.getString("salary");
//                    // set employee name and salary in TextView's
//                    employeeName.setText("Name: "+name);
//                    employeeSalary.setText("Salary: "+salary);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Text1.setText(DataGet);
                if (Objects.equals(signal, "B")) {
                    T.speak("Am fine. My temperature is" + temp+ "degrees, my weight is" + weight + "kg, humidity here is" + humidity + "%", TextToSpeech.QUEUE_FLUSH, null);
//                    Toast.makeText(getApplicationContext(),signal,Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),"Am fine ma",Toast.LENGTH_SHORT).show();
                    signal = "C";
                }
//                Toast.makeText(getApplicationContext(),signal,Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"Response :" + Data.toString(), Toast.LENGTH_LONG).show();//display the response on screen

            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override

            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(MainActivity2.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.

                params.put("data", name);


                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    private void sendAndRequestResponse() {
        String url = "https://smartbabybed.000webhostapp.com/get.php";
        URL = "https://smartbabybed.000webhostapp.com/get.php";
        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String responseGET) {
                DataGet = responseGET;
                try {
                    // get JSONObject from JSON file
                    JSONObject obj = new JSONObject(DataGet);
                    // fetch JSONObject named employee
//                    JSONObject employee = obj.getJSONObject("time");
                    // get employee name and salary

                    signal = obj.getString("signal");
                    temp = obj.getString("temp");
                    weight = obj.getString("weight");
                    humidity = obj.getString("humidity");

                    temp1.setText(temp);
                    weight1.setText(weight);
                    humidity1.setText(humidity);


//                    salary = employee.getString("salary");
//                    // set employee name and salary in TextView's
//                    employeeName.setText("Name: "+name);
//                    employeeSalary.setText("Salary: "+salary);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Text1.setText(DataGet);
                if (Objects.equals(signal, "B")) {
                    T.speak("Am fine. My temperature is" + temp+ "degrees, my weight is" + weight + "kg, humidity here is" + humidity + "%", TextToSpeech.QUEUE_FLUSH, null);
//                    Toast.makeText(getApplicationContext(),signal,Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),"Am fine ma",Toast.LENGTH_SHORT).show();
                    signal = "C";
                }
//                Toast.makeText(getApplicationContext(),signal,Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"Response :" + DataGet.toString(), Toast.LENGTH_LONG).show();//display the response on screen

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"Response :" + error.toString(), Toast.LENGTH_LONG).show();//display the response on screen
            }
        });

        mRequestQueue.add(mStringRequest);
    }

}