package com.androidtitan.culturedapp.model.newyorktimes;

/**
 * Created by amohnacs on 3/21/16.
 */
import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.androidtitan.culturedapp.main.provider.DatabaseContract;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Article implements Parcelable {

    private long id;

    @SerializedName("section")
    @Expose
    private String section;
    @SerializedName("subsection")
    @Expose
    private String subsection;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("abstract")
    @Expose
    private String _abstract;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("byline")
    @Expose
    private String byline;
    @SerializedName("thumbnail_standard")
    @Expose
    private String thumbnailStandard;
    @SerializedName("item_type")
    @Expose
    private String itemType;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("updated_date")
    @Expose
    private Date updatedDate;
    @SerializedName("created_date")
    @Expose
    private Date createdDate;
    @SerializedName("published_date")
    @Expose
    private Date publishedDate;
    @SerializedName("material_type_facet")
    @Expose
    private String materialTypeFacet;
    @SerializedName("kicker")
    @Expose
    private String kicker;
    @SerializedName("headline")
    @Expose
    private String headline;
    @SerializedName("des_facet")
    @Expose
    private List<Facet> desFacet;
    @SerializedName("org_facet")
    @Expose
    private List<Facet> orgFacet;
    @SerializedName("per_facet")
    @Expose
    private List<Facet> perFacet;
    @SerializedName("geo_facet")
    @Expose
    private List<Facet> geoFacet;
    @SerializedName("related_urls")
    @Expose
    private List<RelatedUrl> relatedUrls;
    @SerializedName("multimedia")
    @Expose
    private List<Multimedium> multimedia = new ArrayList<Multimedium>();
    @SerializedName("blog_name")
    @Expose
    private String blogName;

    private String singleDesFacet;
    private String singleOrgFacet;
    private String singlePerFacet;
    private String singleGeoFacet;
    private Multimedium singleMedia;

    public Article() {

    }

    public Article(String title, String section, String _abstract, String url, Date createdDate,
                   List<Facet> desFacet, List<Facet> orgFacet, List<Facet> perFacet,
                   List<Facet> geoFacet, List<Multimedium> multimedia) {

        this.title = title;
        this.section = section;
        this._abstract = _abstract;
        this.url = url;
        this.createdDate = createdDate;
        this.desFacet = desFacet;
        this.orgFacet = orgFacet;
        this.perFacet = perFacet;
        this.geoFacet = geoFacet;
        this.multimedia = multimedia;
    }

    public Article(String id, String title, String section, String _abstract, String url, Date createdDate,
                   List<Facet> desFacet, List<Facet> orgFacet, List<Facet> perFacet,
                   List<Facet> geoFacet, List<Multimedium> multimedia) {

        this.id = Integer.valueOf(id);
        this.title = title;
        this.section = section;
        this._abstract = _abstract;
        this.url = url;
        this.createdDate = createdDate;
        this.desFacet = desFacet;
        this.orgFacet = orgFacet;
        this.perFacet = perFacet;
        this.geoFacet = geoFacet;
        this.multimedia = multimedia;
    }


    //must be read these in the same order as your wrote them
    public Article(Parcel in) {
        section = in.readString();
        subsection = in.readString();
        title = in.readString();
        _abstract = in.readString();
        url = in.readString();
        byline = in.readString();
        thumbnailStandard = in.readString();
        itemType = in.readString();
        source = in.readString();

        updatedDate = (java.util.Date) in.readSerializable();
        createdDate = (java.util.Date) in.readSerializable();
        publishedDate = (java.util.Date) in.readSerializable();

        materialTypeFacet = in.readString();
        kicker = in.readString();
        headline = in.readString();

        /* todo
        desFacet = new ArrayList<Facet>();
        in.readStringList(desFacet);
        orgFacet = new ArrayList<Facet>();
        in.readStringList(orgFacet);
        perFacet = new ArrayList<Facet>();
        in.readStringList(perFacet);
        geoFacet = new ArrayList<Facet>();
        in.readStringList(geoFacet);
        */

        relatedUrls = new ArrayList<RelatedUrl>();
        in.readTypedList(relatedUrls, RelatedUrl.CREATOR);
        multimedia = new ArrayList<Multimedium>();
        in.readTypedList(multimedia, Multimedium.CREATOR);
        blogName = in.readString();

    }

    public ContentValues getArticleContentValues() {

        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.ArticleTable.ABSTRACT, _abstract);
        cv.put(DatabaseContract.ArticleTable.CREATED_DATE, createdDate.toString());
        cv.put(DatabaseContract.ArticleTable.SECTION, section);
        cv.put(DatabaseContract.ArticleTable.TITLE, title);
        cv.put(DatabaseContract.ArticleTable.URL, url);

        return cv;
    }

    /**
     *
     * @return
     * The section
     */
    public String getSection() {
        return section;
    }

    /**
     *
     * @param section
     * The section
     */
    public void setSection(String section) {
        this.section = section;
    }

    /**
     *
     * @return
     * The subsection
     */
    public String getSubsection() {
        return subsection;
    }

    /**
     *
     * @param subsection
     * The subsection
     */
    public void setSubsection(String subsection) {
        this.subsection = subsection;
    }

    /**
     *
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     * The _abstract
     */
    public String getAbstract() {
        return _abstract;
    }

    /**
     *
     * @param _abstract
     * The abstract
     */
    public void setAbstract(String _abstract) {
        this._abstract = _abstract;
    }

    /**
     *
     * @return
     * The url
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     * The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *
     * @return
     * The byline
     */
    public String getByline() {
        return byline;
    }

    /**
     *
     * @param byline
     * The byline
     */
    public void setByline(String byline) {
        this.byline = byline;
    }

    /**
     *
     * @return
     * The thumbnailStandard
     */
    public String getThumbnailStandard() {
        return thumbnailStandard;
    }

    /**
     *
     * @param thumbnailStandard
     * The thumbnail_standard
     */
    public void setThumbnailStandard(String thumbnailStandard) {
        this.thumbnailStandard = thumbnailStandard;
    }

    /**
     *
     * @return
     * The itemType
     */
    public String getItemType() {
        return itemType;
    }

    /**
     *
     * @param itemType
     * The item_type
     */
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    /**
     *
     * @return
     * The source
     */
    public String getSource() {
        return source;
    }

    /**
     *
     * @param source
     * The source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     *
     * @return
     * The updatedDate
     */
    public Date getUpdatedDate() {
        return updatedDate;
    }

    /**
     *
     * @param updatedDate
     * The updated_date
     */
    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     *
     * @return
     * The createdDate
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     *
     * @param createdDate
     * The created_date
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     *
     * @return
     * The publishedDate
     */
    public Date getPublishedDate() {
        return publishedDate;
    }

    /**
     *
     * @param publishedDate
     * The published_date
     */
    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    /**
     *
     * @return
     * The materialTypeFacet
     */
    public String getMaterialTypeFacet() {
        return materialTypeFacet;
    }

    /**
     *
     * @param materialTypeFacet
     * The material_type_facet
     */
    public void setMaterialTypeFacet(String materialTypeFacet) {
        this.materialTypeFacet = materialTypeFacet;
    }

    /**
     *
     * @return
     * The kicker
     */
    public String getKicker() {
        return kicker;
    }

    /**
     *
     * @param kicker
     * The kicker
     */
    public void setKicker(String kicker) {
        this.kicker = kicker;
    }

    /**
     *
     * @return
     * The headline
     */
    public String getheadline() {
        return headline;
    }

    /**
     *
     * @param headline
     * The headline
     */
    public void setheadline(String headline) {
        this.headline = headline;
    }

    /**
     *
     * @return
     * The desFacet
     */
    public List<Facet> getDesFacet() {
        return desFacet;
    }

    /**
     *
     * @param desFacet
     * The des_facet
     */
    public void setDesFacet(List<Facet> desFacet) {
        this.desFacet = desFacet;
    }

    /**
     *
     * @return
     * The orgFacet
     */
    public List<Facet> getOrgFacet() {
        return orgFacet;
    }

    /**
     *
     * @param orgFacet
     * The org_facet
     */
    public void setOrgFacet(List<Facet> orgFacet) {
        this.orgFacet = orgFacet;
    }

    /**
     *
     * @return
     * The perFacet
     */
    public List<Facet> getPerFacet() {
        return perFacet;
    }

    /**
     *
     * @param perFacet
     * The per_facet
     */
    public void setPerFacet(List<Facet> perFacet) {
        this.perFacet = perFacet;
    }

    /**
     *
     * @return
     * The geoFacet
     */
    public List<Facet> getGeoFacet() {
        return geoFacet;
    }

    /**
     *
     * @param geoFacet
     * The geo_facet
     */
    public void setGeoFacet(List<Facet> geoFacet) {
        this.geoFacet = geoFacet;
    }

    /**
     *
     * @return
     * The relatedUrls
     */
    public List<RelatedUrl> getRelatedUrls() {
        return relatedUrls;
    }

    /**
     *
     * @param relatedUrls
     * The related_urls
     */
    public void setRelatedUrls(List<RelatedUrl> relatedUrls) {
        this.relatedUrls = relatedUrls;
    }

    /**
     *
     * @return
     * The multimedia
     */
    public List<Multimedium> getMultimedia() {
        return multimedia;
    }

    /**
     *
     * @param multimedia
     * The multimedia
     */
    public void setMultimedia(List<Multimedium> multimedia) {
        this.multimedia = multimedia;
    }

    /**
     *
     * @return
     * The blogName
     */
    public String getBlogName() {
        return blogName;
    }

    /**
     *
     * @param blogName
     * The blog_name
     */
    public void setBlogName(String blogName) {
        this.blogName = blogName;
    }

    public String get_abstract() {
        return _abstract;
    }

    public void set_abstract(String _abstract) {
        this._abstract = _abstract;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return hashCode();
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(section);
        dest.writeString(subsection);
        dest.writeString(title);
        dest.writeString(_abstract);
        dest.writeString(url);
        dest.writeString(byline);
        dest.writeString(thumbnailStandard);
        dest.writeString(itemType);
        dest.writeString(source);

        dest.writeSerializable(updatedDate);
        dest.writeSerializable(createdDate);
        dest.writeSerializable(publishedDate);

        dest.writeString(materialTypeFacet);
        dest.writeString(kicker);
        dest.writeString(headline);
        /*todo
        dest.writeStringList(desFacet);
        dest.writeStringList(orgFacet);
        dest.writeStringList(perFacet);
        dest.writeStringList(geoFacet);
        */
        dest.writeTypedList(relatedUrls);
        dest.writeTypedList(multimedia);
        dest.writeString(blogName);

    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        /**
         * Create a new instance of the Parcelable class, instantiating it
         * from the given Parcel whose data had previously been written by
         * {@link Parcelable#writeToParcel Parcelable.writeToParcel()}.
         *
         * @param source The Parcel to read the object's data from.
         * @return Returns a new instance of the Parcelable class.
         */
        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        /**
         * Create a new array of the Parcelable class.
         *
         * @param size Size of the array.
         * @return Returns an array of the Parcelable class, with every entry
         * initialized to null.
         */
        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public void setId(String id) {
        this.id = Integer.valueOf(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

}
