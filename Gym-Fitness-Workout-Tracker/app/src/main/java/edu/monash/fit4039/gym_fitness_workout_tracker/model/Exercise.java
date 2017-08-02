package edu.monash.fit4039.gym_fitness_workout_tracker.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wyd on 16/4/17.
 */

public class Exercise implements Parcelable {


    // Instance variables
    public String name;
    public String difficulty;
    public String impactLevel;
    public String focusArea;
    public String userId;
    public String url;

    // Constructor
    public Exercise() {
        // Default constructor required for calls to DataSnapshot.getValue(Exercise.class)
    }

    public Exercise(String name, String difficulty, String impactLevel, String focusArea, String url) {
        this.name = name;
        this.difficulty = difficulty;
        this.impactLevel = impactLevel;
        this.focusArea = focusArea;
        this.url = url;
    }

    public Exercise(String name, String difficulty, String impactLevel, String focusArea, String userId,String url) {
        this.name = name;
        this.difficulty = difficulty;
        this.impactLevel = impactLevel;
        this.focusArea = focusArea;
        this.userId = userId;
        this.url = url;
    }

    // Setter and getter method
    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getImpactLevel() {
        return impactLevel;
    }

    public String getFocusArea() {
        return focusArea;
    }

    public String getUrl() {
        return url;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setImpactLevel(String impactLevel) {
        this.impactLevel = impactLevel;
    }

    public void setFocusArea(String focusArea) {
        this.focusArea = focusArea;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("difficulty", difficulty);
        result.put("impactLevel", impactLevel);
        result.put("focusArea", focusArea);
        result.put("url", url);

        return result;
    }

    // Implement parcelable
    protected Exercise(Parcel in) {
        name = in.readString();
        difficulty = in.readString();
        impactLevel = in.readString();
        focusArea = in.readString();
        userId = in.readString();
        url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(difficulty);
        dest.writeString(impactLevel);
        dest.writeString(focusArea);
        dest.writeString(userId);
        dest.writeString(url);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Exercise> CREATOR = new Parcelable.Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };
}