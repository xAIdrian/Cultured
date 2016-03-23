package com.androidtitan.hotspots.main.util;

import com.androidtitan.hotspots.main.model.newyorktimes.Article;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amohnacs on 3/22/16.
 */
public class ArticleJsonDeserializer implements JsonDeserializer<List<Article>> {
    /**
     * Gson invokes this call-back method during deserialization when it encounters a field of the
     * specified type.
     * <p>In the implementation of this call-back method, you should consider invoking
     * {@link JsonDeserializationContext#deserialize(JsonElement, Type)} method to create objects
     * for any non-trivial field of the returned object. However, you should never invoke it on the
     * the same type passing {@code json} since that will cause an infinite loop (Gson will call your
     * call-back method again).
     *
     * @param json    The Json data being deserialized
     * @param typeOfT The type of the Object to deserialize to
     * @param context
     * @return a deserialized object of the specified type typeOfT which is a subclass of {@code T}
     * @throws JsonParseException if json is not in the expected format of {@code typeofT}
     */
    @Override
    public List<Article> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final String TAG = getClass().getSimpleName();

        List<Article> articles = new ArrayList<Article>();
         return articles;
    }


    /*private Article getOrCreate(final String name) {
        Article author = cache.get().get(name);
        if (author == null) {
            author = new Article();
            cache.get().put(name, author);
        }
        return author;
    }*/
}
