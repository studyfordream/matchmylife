package com.spacemangames.biomatcher.controller;

import java.util.Set;

import junit.framework.Assert;
import android.test.AndroidTestCase;

import com.spacemangames.biomatcher.data.Profile;

public class ProfileManagerTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        // make sure all profile files are gone
        getContext().deleteFile("profiles");

        super.setUp();
    }

    public void testAddProfile() {
        Set<Profile> profiles = ProfileManager.getProfiles(getContext());
        Assert.assertTrue(profiles.isEmpty());

        Profile profile = new Profile("Naam", 1, 2, 3);
        ProfileManager.addProfile(getContext(), profile);

        profiles = ProfileManager.getProfiles(getContext());
        Assert.assertEquals(1, profiles.size());
        Assert.assertTrue(profiles.contains(profile));
    }

    public void testDeleteProfile() {
        Set<Profile> profiles = ProfileManager.getProfiles(getContext());
        Assert.assertTrue(profiles.isEmpty());

        Profile profile = new Profile("Naam", 1, 2, 3);
        ProfileManager.addProfile(getContext(), profile);

        profiles = ProfileManager.getProfiles(getContext());
        Assert.assertEquals(1, profiles.size());
        Assert.assertTrue(profiles.contains(profile));

        ProfileManager.removeProfile(getContext(), profile);
        profiles = ProfileManager.getProfiles(getContext());
        Assert.assertTrue(profiles.isEmpty());
    }

    public void testEditProfile() {
        Set<Profile> profiles = ProfileManager.getProfiles(getContext());
        Assert.assertTrue(profiles.isEmpty());

        Profile profile = new Profile("Naam", 1, 2, 3);
        ProfileManager.addProfile(getContext(), profile);

        profiles = ProfileManager.getProfiles(getContext());
        Assert.assertEquals(1, profiles.size());
        Assert.assertEquals("Naam", profiles.iterator().next().getName());

        profile.setName("Edited");
        ProfileManager.editProfile(getContext(), profile);
        profiles = ProfileManager.getProfiles(getContext());
        Assert.assertEquals(1, profiles.size());
        Assert.assertEquals("Edited", profiles.iterator().next().getName());
    }

    public void testSaveAndRestore() {
        Set<Profile> profiles = ProfileManager.getProfiles(getContext());
        Assert.assertTrue(profiles.isEmpty());

        Profile profile = new Profile("Naam", 1, 2, 3);
        ProfileManager.addProfile(getContext(), profile);

        profiles = ProfileManager.getProfiles(getContext());
        Assert.assertEquals(1, profiles.size());
        Assert.assertEquals(profile, profiles.iterator().next());
        Assert.assertEquals("Naam", profiles.iterator().next().getName());
        Assert.assertEquals(1, profiles.iterator().next().getYear());
        Assert.assertEquals(2, profiles.iterator().next().getMonth());
        Assert.assertEquals(3, profiles.iterator().next().getDay());
    }

    public void testSaveAndRestoreMultiple() {
        Set<Profile> profiles = ProfileManager.getProfiles(getContext());
        Assert.assertTrue(profiles.isEmpty());

        Profile profile1 = new Profile("Naam1", 1, 2, 3);
        Profile profile2 = new Profile("Naam2", 5, 6, 78);
        Profile profile3 = new Profile("Naam3", 9, 0, 12);

        ProfileManager.addProfile(getContext(), profile1);
        ProfileManager.addProfile(getContext(), profile2);
        ProfileManager.addProfile(getContext(), profile3);

        profiles = ProfileManager.getProfiles(getContext());
        Assert.assertEquals(3, profiles.size());
        Assert.assertTrue(profiles.contains(profile1));
        Assert.assertTrue(profiles.contains(profile2));
        Assert.assertTrue(profiles.contains(profile3));
    }
}
