package edu.monash.fit4039.gym_fitness_workout_tracker;

import org.junit.Assert;
import org.junit.Test;

import edu.monash.fit4039.gym_fitness_workout_tracker.model.Profile;

/**
 * Created by wyd on 22/5/17.
 */

public class ProfileTest {
   Profile profile = new Profile("Eddie",12,170,50,"Male");

    @Test
    public void testCalculateBMI() {
        profile.setHeight(0);
        Assert.assertEquals("0.00", profile.formatBMI());

        profile.setHeight(170.678);
        profile.setWeight(0);
        Assert.assertEquals("0.00", profile.formatBMI());


        profile.setHeight(179);
        profile.setWeight(60);
        Assert.assertEquals("18.73", profile.formatBMI());

    }

    @Test
    public void testCalculateBMR() {
        profile.setWeight(0);
        profile.setHeight(170.98);
        Assert.assertEquals("0.00 CALs", profile.formatBMR());

        profile.setHeight(0);
        profile.setWeight(60.67);
        Assert.assertEquals("0.00 CALs", profile.formatBMR());


        profile.setHeight(179);
        profile.setWeight(60);
        Assert.assertEquals("1704.40 CALs", profile.formatBMR());
    }
}
