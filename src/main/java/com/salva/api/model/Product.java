package com.salva.api.model;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.Data;

@Data
public class Product {

    @DocumentId
    private String id;
    private String imageUrl;
    private String basicInfo;
    private String name;
    private String description;
    private String tastingNotes;
    private String marketingSummary;

    public String getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getBasicInfo() {
        return basicInfo;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTastingNotes() {
        return tastingNotes;
    }

    public String getMarketingSummary() {
        return marketingSummary;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setBasicInfo(String basicInfo) {
        this.basicInfo = basicInfo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTastingNotes(String tastingNotes) {
        this.tastingNotes = tastingNotes;
    }

    public void setMarketingSummary(String marketingSummary) {
        this.marketingSummary = marketingSummary;
    }
}