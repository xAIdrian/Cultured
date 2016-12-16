package com.androidtitan.culturedapp.main.newsfeed;

import android.test.InstrumentationTestCase;

import com.androidtitan.culturedapp.BuildConfig;
import com.androidtitan.culturedapp.main.web.retrofit.NewsEndpoint;
import com.androidtitan.culturedapp.main.newsfeed.mock.MockFailedNewsEndpoint;
import com.androidtitan.culturedapp.main.newsfeed.mock.MockNewsEndpoint;
import com.androidtitan.culturedapp.model.newyorktimes.NewsResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.annotation.Annotation;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

/**
 * Created by amohnacs on 9/7/16.
 *
 * https://riggaroo.co.za/retrofit-2-mocking-http-responses/
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class NewsProviderTest extends InstrumentationTestCase {
    private final String TAG = getClass().getSimpleName();

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
        Call<NewsResponse> response = mockEndpointInterface.newsWireArticles(TEST_SECTION, TEST_LIMIT, TEST_OFFSET, TEST_API_KEY);
        Response<NewsResponse> newsResponse = response.execute();

        //asserting response
        assertTrue(newsResponse.isSuccessful());

    }

    // todo :: for these we need to visit NYT API and get the appropriate JSON?
    // or do we just need to check that isSuccessful() is false?x
    @Test
    public void testFailedArticlesResponseRetrieval() throws Exception {
        //404 error

        BehaviorDelegate<NewsEndpoint> delegate = mockRetrofit.create(NewsEndpoint.class);
        MockFailedNewsEndpoint mockFailedNewsEndpoint = new MockFailedNewsEndpoint(delegate);

        //Actual Test
        Call<NewsResponse> response = mockFailedNewsEndpoint.newsWireArticles(TEST_SECTION, TEST_LIMIT, TEST_OFFSET, TEST_API_KEY);
        Response<NewsResponse> newsResponse = response.execute();
        assertFalse(newsResponse.isSuccessful());

        Converter<ResponseBody, NewsResponse> errorConverter = retrofit.responseBodyConverter(NewsResponse.class, new Annotation[0]);
        NewsResponse error = errorConverter.convert(newsResponse.errorBody());

        //Asserting response
        assertEquals(404, newsResponse.code());

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