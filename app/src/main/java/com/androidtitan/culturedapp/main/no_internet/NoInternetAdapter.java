package com.androidtitan.culturedapp.main.no_internet;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.common.CollectionUtils;
import com.androidtitan.culturedapp.databinding.NoInternetListitemBinding;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by amohnacs on 9/6/17.
 */

public class NoInternetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = NoInternetAdapter.class.getSimpleName();

    private Context context;
    private List<Article> offlineArticles;

    public NoInternetAdapter(Context context, List<Article> offlineArticles) {
        this.context = context;
        this.offlineArticles = offlineArticles;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        NoInternetListitemBinding listBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.no_internet_listitem, parent, false);
        return new XLargeArticleViewHolder(listBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Article article = offlineArticles.get(position);
        XLargeArticleViewHolder bigHolder = (XLargeArticleViewHolder) holder;
        bigHolder.bind(article);
    }

    @Override
    public int getItemCount() {
        return offlineArticles.size();
    }

    public static class XLargeArticleViewHolder extends RecyclerView.ViewHolder {

       private final NoInternetListitemBinding binding;

        public XLargeArticleViewHolder(NoInternetListitemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Article article) {

           binding.setArticle(article);

            if (!CollectionUtils.isEmpty(article.getMultimedia())) {
                binding.setMultimedium(article.getMultimedia().get(0));
            }

           binding.executePendingBindings();
        }
    }

    public void onItemClick() {
        // TODO: 9/27/17 no time for this now (https://developer.android.com/topic/libraries/data-binding/index.html#event_handling)
    }
}
