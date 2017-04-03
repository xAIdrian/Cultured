package com.androidtitan.culturedapp.main.newsfeed.ui;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.common.structure.BasePresenter;
import com.androidtitan.culturedapp.main.newsfeed.NewsFeedMvp;
import com.androidtitan.culturedapp.main.newsfeed.NewsFeedPresenter;
import com.androidtitan.culturedapp.main.newsfeed.adapter.NewsFeedAdapter;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.androidtitan.culturedapp.model.newyorktimes.Facet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

import static com.androidtitan.culturedapp.main.newsfeed.ui.NewsViewPagerActivity.ARTICLE_BOOKMARKED;
import static com.androidtitan.culturedapp.main.newsfeed.ui.NewsViewPagerActivity.ARTICLE_EXTRA;
import static com.androidtitan.culturedapp.main.newsfeed.ui.NewsViewPagerActivity.ARTICLE_GEO_FACETS;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeedFragment extends FeedFragment implements NewsFeedMvp.View, ErrorFragmentInterface{

    private static final String TAG = NewsFeedFragment.class.getSimpleName();

    public static final String ERROR_MESSAGE = "errorfragment.errormessage";
    public static final String ERROR_MAP = "errorfragment.errormap";

    private NewsFeedPresenter presenter;
    private ActivityUserInterfaceInteractor activityUserInterfaceInteractor;

    ErrorFragment errorFragment;

    private Handler handler;
    private Animation fadeAnim;

    private LinearLayoutManager linearLayoutManager;
    private StaggeredGridLayoutManager staggeredLayoutManager;
    private NewsFeedAdapter adapter;

    private List<Article> articles;
    private HashMap<String, Boolean> bookmarkedArticles;

    private boolean loading = true;
    private int pastVisibleItems;
    private int visibleItemCount;
    private int totalItemCount;
    public int adapterLoadOffset = 6;

    @Bind(R.id.newsList)
    RecyclerView recyclerView;

    public NewsFeedFragment() {
        // Required empty public constructor
    }

    public NewsFeedFragment newInstance() {
        NewsFeedFragment fragment = new NewsFeedFragment();
        //Bundle bundle = new Bundle();
        //arguments set to bundle
        //fragment.setArgs
        return fragment;
    }

    @Override
    public BasePresenter getPresenter() {
        presenter = ((NewsViewPagerActivity) getActivity()).getPresenter();
        presenter.bindView(this);
        return presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bookmarkedArticles = presenter.getBookmarkedArticles();
        articles = new ArrayList<>();

        if (((NewsViewPagerActivity)getActivity()).getScreenSize() == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            articles = presenter.loadArticles(10);
        } else {
            articles = presenter.loadArticles(5);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int[] array = null;
                activityUserInterfaceInteractor.scrollViewParallax(dy);

                // logic for hiding and showing the actionbar shadow when the list is fully scrolled down
                try {
                    if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                        activityUserInterfaceInteractor.setAppBarElevation(0);
                    }
                } catch (Exception e) {
                    array = staggeredLayoutManager.findFirstCompletelyVisibleItemPositions(array);

                    if (array[0] == 0 || array[1] == 1) {
                        activityUserInterfaceInteractor.setAppBarElevation(0);
                    }
                }

                if (dy > 0) { //check for active scrolling
                    activityUserInterfaceInteractor.hideToolbarBy(dy);

                    if (((NewsViewPagerActivity)getActivity()).getScreenSize() == Configuration.SCREENLAYOUT_SIZE_XLARGE ||
                            getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

                        visibleItemCount = staggeredLayoutManager.getChildCount();
                        totalItemCount = staggeredLayoutManager.getItemCount();
                        int[] firstVisibleItems = null;
                        firstVisibleItems = staggeredLayoutManager.findFirstVisibleItemPositions(firstVisibleItems);

                        if (firstVisibleItems != null && firstVisibleItems.length > 0) {
                            pastVisibleItems = firstVisibleItems[0];
                        }

                        if (loading) {

                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                loading = false;

                                presenter.loadOffsetArticles(10, adapterLoadOffset);
                                adapterLoadOffset += 10;

                                activityUserInterfaceInteractor.showColoredSnackbar();
                            }
                        }
                    } else {

                        visibleItemCount = linearLayoutManager.getChildCount();
                        totalItemCount = linearLayoutManager.getItemCount();
                        pastVisibleItems = linearLayoutManager.findFirstCompletelyVisibleItemPosition();

                        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

                            if (loading) {

                                if ((visibleItemCount + pastVisibleItems + 1) >= totalItemCount) {
                                    //the +1 accounts for having one less card visible to add

                                    loading = false;
                                    Log.d(TAG, "appending data...");

                                    presenter.loadOffsetArticles(5, adapterLoadOffset);
                                    adapterLoadOffset += 5;

                                    activityUserInterfaceInteractor.showColoredSnackbar();
                                }
                            }

                        } else {

                            if (loading) {

                                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {

                                    loading = false;
                                    Log.d(TAG, "appending data...");

                                    presenter.loadOffsetArticles(5, adapterLoadOffset);
                                    adapterLoadOffset += 5;

                                    activityUserInterfaceInteractor.showColoredSnackbar();
                                }
                            }
                        }
                    }
                } else {
                    activityUserInterfaceInteractor.showToolbarBy(dy);
                }
            }
        });

        return getView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ErrorFragmentInterface) {
            activityUserInterfaceInteractor = (ActivityUserInterfaceInteractor) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement activityUserInterfaceInteractor");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.news_feed_fragment;
    }

    @Override
    public void initializeAnimation() {
        handler = new Handler();
        fadeAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
    }

    @Override
    protected void setupRecyclerView() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                && ((NewsViewPagerActivity)getActivity()).getScreenSize() == Configuration.SCREENLAYOUT_SIZE_XLARGE) {

                staggeredLayoutManager = new StaggeredGridLayoutManager(3, 1);
                recyclerView.setLayoutManager(staggeredLayoutManager);

        } else if (((NewsViewPagerActivity)getActivity()).getScreenSize() == Configuration.SCREENLAYOUT_SIZE_XLARGE ||
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

                staggeredLayoutManager = new StaggeredGridLayoutManager(2, 1);
                recyclerView.setLayoutManager(staggeredLayoutManager);

        } else {
                linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
        }

        adapter = new NewsFeedAdapter(getActivity(), articles);
        recyclerView.setAdapter(adapter);
    }



    @Override
    public void startDetailActivity(Article article, ImageView articleImage) {
        Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
        intent.putExtra(ARTICLE_EXTRA, article);
        intent.putStringArrayListExtra(ARTICLE_GEO_FACETS, getGeoFacetArrayList(article));
        intent.putExtra(ARTICLE_BOOKMARKED, isArticleBookmarked(article.getTitle()));

        startActivity(intent);
    }

    @Override
    public ArrayList<String> getGeoFacetArrayList(@NonNull Article article) {
        ArrayList<String> facets = new ArrayList<>();
        for (Facet facet : article.getGeoFacet()) {
            facets.add(facet.getFacetText());
        }
        return facets;
    }

    @Override
    public boolean isArticleBookmarked(@NonNull String articleTitle) {
        if(bookmarkedArticles.get(articleTitle) != null) {
            return bookmarkedArticles.get(articleTitle);
        }
        return false;
    }

    @Override
    public void appendAdapterItem(Article article) {
        adapter.appendToAdapter(article);
    }

    @Override
    public void insertAdapterItem(int index, Article article) {
        adapter.insertIntoAdapter(index, article);
    }

    @Override
    public void insertAdapterItems(int index, ArrayList<Article> articles) {
        adapter.insertIntoAdapter(index, articles);
    }

    @Override
    public List<Article> getArticles() {
        return articles;
    }

    @Override
    public void displayError(String message, Map<String, Object> additionalProperties) {
        Bundle args = new Bundle();
        args.putString(ERROR_MESSAGE, message);

        errorFragment = ErrorFragment.newInstance(message, additionalProperties);
        errorFragment.setArguments(args);

        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.main_content, errorFragment).commit();
    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    public boolean getAboutCardStatus() {
        return adapter.getAboutCardStatus();
    }

    public void resetOnboardingCard() {
        adapter.resetOnboardingCard();
    }

    public void showAboutCard() {
        adapter.showAboutCard();
    }

    @Override
    public void restartArticleLoad() {
        getActivity().getSupportFragmentManager()
                .beginTransaction().remove(errorFragment).commit();
        presenter.loadArticles(10);
    }
}
