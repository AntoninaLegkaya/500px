
package com.dbbest.a500px.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Photo {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("camera")
    @Expose
    private String camera;
    @SerializedName("lens")
    @Expose
    private String lens;
    @SerializedName("focal_length")
    @Expose
    private String focalLength;
    @SerializedName("iso")
    @Expose
    private String iso;
    @SerializedName("shutter_speed")
    @Expose
    private String shutterSpeed;
    @SerializedName("aperture")
    @Expose
    private String aperture;
    @SerializedName("times_viewed")
    @Expose
    private Integer timesViewed;
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("category")
    @Expose
    private Integer category;
    @SerializedName("location")
    @Expose
    private Object location;
    @SerializedName("latitude")
    @Expose
    private Object latitude;
    @SerializedName("longitude")
    @Expose
    private Object longitude;
    @SerializedName("taken_at")
    @Expose
    private Object takenAt;
    @SerializedName("hi_res_uploaded")
    @Expose
    private Integer hiResUploaded;
    @SerializedName("for_sale")
    @Expose
    private Boolean forSale;
    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("votes_count")
    @Expose
    private Integer votesCount;
    @SerializedName("favorites_count")
    @Expose
    private Integer favoritesCount;
    @SerializedName("comments_count")
    @Expose
    private Integer commentsCount;
    @SerializedName("nsfw")
    @Expose
    private Boolean nsfw;
    @SerializedName("sales_count")
    @Expose
    private Integer salesCount;
    @SerializedName("for_sale_date")
    @Expose
    private Object forSaleDate;
    @SerializedName("highest_rating")
    @Expose
    private Double highestRating;
    @SerializedName("highest_rating_date")
    @Expose
    private String highestRatingDate;
    @SerializedName("license_type")
    @Expose
    private Integer licenseType;
    @SerializedName("converted")
    @Expose
    private Integer converted;
    @SerializedName("collections_count")
    @Expose
    private Integer collectionsCount;
    @SerializedName("crop_version")
    @Expose
    private Integer cropVersion;
    @SerializedName("privacy")
    @Expose
    private Boolean privacy;
    @SerializedName("profile")
    @Expose
    private Boolean profile;
    @SerializedName("for_critique")
    @Expose
    private Boolean forCritique;
    @SerializedName("critiques_callout_dismissed")
    @Expose
    private Boolean critiquesCalloutDismissed;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("positive_votes_count")
    @Expose
    private Integer positiveVotesCount;
    @SerializedName("converted_bits")
    @Expose
    private Integer convertedBits;
    @SerializedName("watermark")
    @Expose
    private Boolean watermark;
    @SerializedName("image_format")
    @Expose
    private String imageFormat;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("licensing_requested")
    @Expose
    private Boolean licensingRequested;
    @SerializedName("licensing_suggested")
    @Expose
    private Boolean licensingSuggested;
    @SerializedName("is_free_photo")
    @Expose
    private Boolean isFreePhoto;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public String getLens() {
        return lens;
    }

    public void setLens(String lens) {
        this.lens = lens;
    }

    public String getFocalLength() {
        return focalLength;
    }

    public void setFocalLength(String focalLength) {
        this.focalLength = focalLength;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getShutterSpeed() {
        return shutterSpeed;
    }

    public void setShutterSpeed(String shutterSpeed) {
        this.shutterSpeed = shutterSpeed;
    }

    public String getAperture() {
        return aperture;
    }

    public void setAperture(String aperture) {
        this.aperture = aperture;
    }

    public Integer getTimesViewed() {
        return timesViewed;
    }

    public void setTimesViewed(Integer timesViewed) {
        this.timesViewed = timesViewed;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Object getLocation() {
        return location;
    }

    public void setLocation(Object location) {
        this.location = location;
    }

    public Object getLatitude() {
        return latitude;
    }

    public void setLatitude(Object latitude) {
        this.latitude = latitude;
    }

    public Object getLongitude() {
        return longitude;
    }

    public void setLongitude(Object longitude) {
        this.longitude = longitude;
    }

    public Object getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(Object takenAt) {
        this.takenAt = takenAt;
    }

    public Integer getHiResUploaded() {
        return hiResUploaded;
    }

    public void setHiResUploaded(Integer hiResUploaded) {
        this.hiResUploaded = hiResUploaded;
    }

    public Boolean getForSale() {
        return forSale;
    }

    public void setForSale(Boolean forSale) {
        this.forSale = forSale;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getVotesCount() {
        return votesCount;
    }

    public void setVotesCount(Integer votesCount) {
        this.votesCount = votesCount;
    }

    public Integer getFavoritesCount() {
        return favoritesCount;
    }

    public void setFavoritesCount(Integer favoritesCount) {
        this.favoritesCount = favoritesCount;
    }

    public Integer getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Boolean getNsfw() {
        return nsfw;
    }

    public void setNsfw(Boolean nsfw) {
        this.nsfw = nsfw;
    }

    public Integer getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(Integer salesCount) {
        this.salesCount = salesCount;
    }

    public Object getForSaleDate() {
        return forSaleDate;
    }

    public void setForSaleDate(Object forSaleDate) {
        this.forSaleDate = forSaleDate;
    }

    public Double getHighestRating() {
        return highestRating;
    }

    public void setHighestRating(Double highestRating) {
        this.highestRating = highestRating;
    }

    public String getHighestRatingDate() {
        return highestRatingDate;
    }

    public void setHighestRatingDate(String highestRatingDate) {
        this.highestRatingDate = highestRatingDate;
    }

    public Integer getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(Integer licenseType) {
        this.licenseType = licenseType;
    }

    public Integer getConverted() {
        return converted;
    }

    public void setConverted(Integer converted) {
        this.converted = converted;
    }

    public Integer getCollectionsCount() {
        return collectionsCount;
    }

    public void setCollectionsCount(Integer collectionsCount) {
        this.collectionsCount = collectionsCount;
    }

    public Integer getCropVersion() {
        return cropVersion;
    }

    public void setCropVersion(Integer cropVersion) {
        this.cropVersion = cropVersion;
    }

    public Boolean getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Boolean privacy) {
        this.privacy = privacy;
    }

    public Boolean getProfile() {
        return profile;
    }

    public void setProfile(Boolean profile) {
        this.profile = profile;
    }

    public Boolean getForCritique() {
        return forCritique;
    }

    public void setForCritique(Boolean forCritique) {
        this.forCritique = forCritique;
    }

    public Boolean getCritiquesCalloutDismissed() {
        return critiquesCalloutDismissed;
    }

    public void setCritiquesCalloutDismissed(Boolean critiquesCalloutDismissed) {
        this.critiquesCalloutDismissed = critiquesCalloutDismissed;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getPositiveVotesCount() {
        return positiveVotesCount;
    }

    public void setPositiveVotesCount(Integer positiveVotesCount) {
        this.positiveVotesCount = positiveVotesCount;
    }

    public Integer getConvertedBits() {
        return convertedBits;
    }

    public void setConvertedBits(Integer convertedBits) {
        this.convertedBits = convertedBits;
    }

    public Boolean getWatermark() {
        return watermark;
    }

    public void setWatermark(Boolean watermark) {
        this.watermark = watermark;
    }

    public String getImageFormat() {
        return imageFormat;
    }

    public void setImageFormat(String imageFormat) {
        this.imageFormat = imageFormat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getLicensingRequested() {
        return licensingRequested;
    }

    public void setLicensingRequested(Boolean licensingRequested) {
        this.licensingRequested = licensingRequested;
    }

    public Boolean getLicensingSuggested() {
        return licensingSuggested;
    }

    public void setLicensingSuggested(Boolean licensingSuggested) {
        this.licensingSuggested = licensingSuggested;
    }

    public Boolean getIsFreePhoto() {
        return isFreePhoto;
    }

    public void setIsFreePhoto(Boolean isFreePhoto) {
        this.isFreePhoto = isFreePhoto;
    }


}
