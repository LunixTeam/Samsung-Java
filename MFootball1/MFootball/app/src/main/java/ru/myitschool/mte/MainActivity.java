package ru.myitschool.mte;

import static ru.myitschool.mte.Utils.API_KEY;
import static ru.myitschool.mte.Utils.API_VERSION;
import static ru.myitschool.mte.Utils.BASE_URL;
import static ru.myitschool.mte.Utils.BLANK_URL;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.myitschool.mte.data.Match;
import ru.myitschool.mte.data.MatchesResult;
import ru.myitschool.mte.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    TextView tvResult;
    EditText etMatch;
    Button btSearch;
    WebView wvResult;
    Retrofit retrofit;

    /**
     * Uses the Scorebat API
     * https://www.scorebat.com/video-api/
     * https://www.scorebat.com/video-api/v3/feed/?token=MjA0NDZfMTY1NDM2MzExMF84OTM2ODI3ODFhYTRlNjQ5YzczMTgyZmRhYzQ1OWYzNTVlNTljNWZm
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        etMatch = binding.content.etMatch;
        btSearch = binding.content.btFind;
        wvResult = binding.content.wvResult;

        wvResult.getSettings().setJavaScriptEnabled(true);
        wvResult.getSettings().setBuiltInZoomControls(true);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FootballService service = retrofit.create(FootballService.class);
        btSearch.setOnClickListener((View.OnClickListener) view -> {
            wvResult.loadUrl(BLANK_URL);
            Call<MatchesResult> call = service.getVideos(API_VERSION, API_KEY);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<MatchesResult> call, @NonNull Response<MatchesResult> response) {
                    if (response.isSuccessful()) {
                        MatchesResult matches = response.body();
                        for (Match m : matches.response) {
                            if (m.title.toLowerCase(Locale.ROOT).contains(etMatch.getText().toString().toLowerCase(Locale.ROOT))) {
                                String s = m.videos[0].embed;
                                String url = s.substring(s.indexOf("src")).split("'")[1];
                                wvResult.loadUrl(url);
                                Log.d("Tests", url);
                                return;
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MatchesResult> call, @NonNull Throwable t) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.connection_problems), Toast.LENGTH_SHORT).show();
                }
            });
        });

    }
}
