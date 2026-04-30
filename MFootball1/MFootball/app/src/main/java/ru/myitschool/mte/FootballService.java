package ru.myitschool.mte;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.myitschool.mte.data.MatchesResult;

public interface FootballService {
    @GET("/video-api/v{version}/feed/")
    Call<MatchesResult> getVideos(
            @Path("version") int version,
            @Query("token") String token
    );
}