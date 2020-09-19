package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.*;

public class User {

    @NotNull
    @Size(max = 8)
    @JsonProperty(value = "user_name")
    private String userName;

    @NotNull
    @JsonProperty(value = "user_gender")
    private String gender;

    @NotNull
    @Min(18)
    @Max(100)
    @JsonProperty(value = "user_age")
    private int age;

    @Email
    @JsonProperty(value = "user_email")
    private String email;

    @Pattern(regexp = "1\\d{10}")
    @JsonProperty(value = "user_phone")
    private String phone;

    @JsonProperty(value = "user_voteNum")
    private int voteNum = 10;

    public User(String userName, String gender, int age, String email, String phone) {
        this.userName = userName;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }

}
