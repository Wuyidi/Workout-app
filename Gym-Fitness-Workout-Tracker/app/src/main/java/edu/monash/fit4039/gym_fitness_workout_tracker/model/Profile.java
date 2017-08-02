package edu.monash.fit4039.gym_fitness_workout_tracker.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wyd on 19/4/17.
 */

public class Profile implements Parcelable {

    // Instance variable
    public String name;
    public int age;
    public double height;
    public double weight;
    public String gender;
    // Constant variable
    public static final double SEDENTARY = 1.1;
    public static final double LIGHTLY_ACTIVE = 1.275;
    public static final double MODERATELY_ACTIVE = 1.35;
    public static final double ACTIVE = 1.525;


    public Profile() {
        // Default constructor required for calls to DataSnapshot.getValue(Profile.class)
    }

    public Profile(String name, int age, double height, double weight, String gender) {
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
    }

    // Setter and getter method
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String formatHeight() {
        return format(height) + " cm";
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String formatWeight() {
        return format(weight) + " Kg";
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    // Calculate the BMI by height and weight
    private double calculateBMI() {
        if (height != 0) {
            return weight / (height / 100 * height / 100);
        } else {
            return 0;
        }
    }

    // Calculate the BMR by height and weight
    private double calculateBMR() {
        if (gender.equals("Male")) {
            if (height != 0 && weight != 0)
                return 66 + 13.75 * weight + 5 * height - 6.8 * age;
            else return 0;

        } else if (gender.equals("Female")) {
            if (height != 0 && weight != 0)
                return 655 + 9.6 * weight + 1.8 * height - 4.7 * age;
            else
                return 0;

        } else {
            return 0;
        }
    }
    // Calculate the daily calories
    private double calculateDailyCalorie(double activityFactor) {
        if (activityFactor == SEDENTARY) {
            return calculateBMR() * SEDENTARY;
        } else if (activityFactor == LIGHTLY_ACTIVE) {
            return calculateBMR() * LIGHTLY_ACTIVE;
        } else if (activityFactor == MODERATELY_ACTIVE) {
            return calculateBMR() * MODERATELY_ACTIVE;
        } else if (activityFactor == ACTIVE) {
            return calculateBMR() * ACTIVE;
        } else {
            return calculateBMR();
        }
    }

    // Return a BMI value
    public String formatBMI() {
        return format(calculateBMI());
    }

    // Return a BMR value
    public String formatBMR() {
        return format(calculateBMR()) + " CALs";
    }

    // Return daily calories value
    public String getDailyCalorie(double activityFactor) {
        return format(calculateDailyCalorie(activityFactor));
    }

    // Reformat the double value
    public static String format(double value) {
        return String.format("%.2f", value).toString();
    }

    // Implement parcelable
    protected Profile(Parcel in) {
        name = in.readString();
        age = in.readInt();
        height = in.readDouble();
        weight = in.readDouble();
        gender = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
        dest.writeDouble(height);
        dest.writeDouble(weight);
        dest.writeString(gender);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Profile> CREATOR = new Parcelable.Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };
}