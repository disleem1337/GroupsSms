package com.example.groupssms;

import java.util.List;

public class GroupModel {
    private String name, description, image, uid;
    private List<String> numbers;

    public GroupModel() {
    }

    public GroupModel(String name, String description, String image, List<String> numbers, String uid) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.numbers = numbers;
        this.uid = uid;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<String> numbers) {
        this.numbers = numbers;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
