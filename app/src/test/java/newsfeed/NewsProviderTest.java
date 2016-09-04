package newsfeed;

import android.test.InstrumentationTestCase;

import com.androidtitan.hotspots.BuildConfig;
import com.androidtitan.hotspots.main.domain.retrofit.NewsEndpoint;
import com.androidtitan.hotspots.model.newyorktimes.NewsResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

import static org.mockito.Mockito.verify;

/**
 * Created by amohnacs on 9/1/16.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class NewsProviderTest extends InstrumentationTestCase{

    /*private NewsProvider newsProvider;

    @Mock Context mockContext;
    @Mock NewsEndpoint mockNewsService;

    @Mock NewsMvp.Provider.CallbackListener mockCallbackListener;
*/
    //todo: do we need these?
    private static final String TEST_SECTION = "testsection";
    private static final int TEST_LIMIT = 1;
    private static final int TEST_OFFSET = 0;
    private static final String TEST_API_KEY = "testapikey";

    private MockRetrofit mockRetrofit;
    private Retrofit retrofit;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);

        retrofit = new Retrofit.Builder().baseUrl("http://test.com")
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NetworkBehavior networkBehavior = NetworkBehavior.create();

        mockRetrofit = new MockRetrofit.Builder(retrofit)
                .networkBehavior(networkBehavior)
                .build();

    }

    @Test
    public void testFetchArticlesResponseRetrieval() throws Exception{

        BehaviorDelegate<NewsEndpoint> delegate = mockRetrofit.create(NewsEndpoint.class);
        NewsEndpoint mockEndpointInterface = new MockNewsEndpoint(delegate);

        //actual test
        Call<NewsResponse> response = mockEndpointInterface.articles(TEST_SECTION, TEST_LIMIT, TEST_OFFSET, TEST_API_KEY);
        Response<NewsResponse> newsResponse = response.execute();

        //asserting response
        assertTrue(newsResponse.isSuccessful());

    }

    // todo :: for these we need to visit NYT API and get the appropriate JSON?
        // or do we just need to check that isSuccessful() is false?x
    @Test
    public void testFailedArticlesResponseRetrieval() throws Exception {
        //404 error
        //https://riggaroo.co.za/retrofit-2-mocking-http-responses/

    }

    @Test
    public void testInvalidKeyResponse() throws Exception {
        //Invalid API key

    }

    @After
    public void tearDown() throws Exception {

        retrofit = null;
        mockRetrofit = null;
    }

}
