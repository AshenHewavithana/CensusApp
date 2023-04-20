package com.example.censusapp;

public class Model {
    private int id;
    private String name;
    private String age;
    private String gender;
    private byte[] img;

    public Model(int id, String name, String age, String gender, byte[] img) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }
}
