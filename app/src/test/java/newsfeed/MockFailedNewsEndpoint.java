package newsfeed;

import android.util.JsonWriter;
import android.util.Log;

import com.androidtitan.hotspots.main.domain.retrofit.NewsEndpoint;
import com.androidtitan.hotspots.model.ApiError;
import com.androidtitan.hotspots.model.newyorktimes.NewsResponse;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.Calls;

/**
 * Created by amohnacs on 9/4/16.
 */

//todo : currently we only handle 404. We need to build more mocks for various error status codes
public class MockFailedNewsEndpoint implements NewsEndpoint{
    private final String TAG = getClass().getSimpleName();

    private static final String TEST_STRING = "testString";
    private static final String TEST_SECTION = "testsection";
    private static final int TEST_LIMIT = 1;
    private static final int TEST_OFFSET = 0;
    private static final String TEST_API_KEY = "testapikey";

    private final BehaviorDelegate<NewsEndpoint> delegate;

    public MockFailedNewsEndpoint(BehaviorDelegate<NewsEndpoint> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Call<NewsResponse> articles(@Path("section") String string, @Query("limit") int count, @Query("offset") int offset, @Query("api-key") String yourKey) {

        ApiError error = new ApiError();
        error.setCode(404);
        error.setMessage(TEST_STRING);

        NewsResponse newsResponse = new NewsResponse();
        newsResponse.setApiError(error);

        //ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {

            Response response = Response.error(404, ResponseBody.create(MediaType.parse("application/json"), json));
            return delegate.returning(Calls.response(response))
                    .articles(TEST_SECTION, TEST_LIMIT, TEST_OFFSET, TEST_API_KEY);

        } catch (Exception e) {
            Log.e(TAG, e);
            return Calls.failure((IOException) e);
        }
    }
}
