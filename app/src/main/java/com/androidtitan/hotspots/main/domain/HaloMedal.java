package com.androidtitan.hotspots.main.domain;

/**
 * Created by amohnacs on 3/15/16.
 */
public class HaloMedal {

    private String id;
    private String name;
    private String description;
    private String classification;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }
}
