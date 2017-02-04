package se.lightside.tinyjsonexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public class MainActivity extends AppCompatActivity {

    private TextView messageView;
    private TextView subtitleView;

    static class Message {
        public String message;
        public String subtitle;
    }

    public interface FoaaService {

        @GET("/awesome/{from}")
        @Headers("Accept: application/json")
        Call<Message> getStuffFromServer(@Path("from") String from);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageView = (TextView) findViewById(R.id.message);
        subtitleView = (TextView) findViewById(R.id.subtitle);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.foaas.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        FoaaService restService = retrofit.create(FoaaService.class);

        Call<Message> call = restService.getStuffFromServer("A name here");

        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                Message parsedJson = response.body();
                messageView.setText(parsedJson.message);
                subtitleView.setText(parsedJson.subtitle);
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                // Handle errors here
            }
        });
    }
}
