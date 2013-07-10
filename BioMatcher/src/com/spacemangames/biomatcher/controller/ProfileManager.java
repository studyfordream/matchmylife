package com.spacemangames.biomatcher.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spacemangames.biomatcher.data.Profile;

public class ProfileManager {
    private static final String FILENAME = "profiles";
    private static final int BUFFER_SIZE = 4096;

    private static Set<IProfileUpdateListener> listeners = new HashSet<IProfileUpdateListener>();

    public static void addListener(IProfileUpdateListener listener) {
        listeners.add(listener);
    }

    public static void removeListener(IProfileUpdateListener listener) {
        listeners.remove(listener);
    }

    public static Set<Profile> getProfiles(Context context) {
        return loadProfiles(context);
    }

    public static void addProfile(Context context, Profile profile) {
        Set<Profile> profiles = loadProfiles(context);
        profiles.add(profile);
        notifyProfileAdded(profile);
        saveProfiles(context, profiles);
    }

    public static void removeProfile(Context context, Profile profile) {
        Set<Profile> profiles = loadProfiles(context);
        profiles.remove(profile);
        notifyProfileRemoved(profile);
        saveProfiles(context, profiles);
    }

    public static void editProfile(Context context, Profile profile) {
        Set<Profile> profiles = loadProfiles(context);
        profiles.remove(profile);
        profiles.add(profile);
        notifyProfileEdited(profile);
        saveProfiles(context, profiles);
    }

    private static void notifyProfileAdded(Profile profile) {
        for (IProfileUpdateListener listener : listeners) {
            listener.profileAdded(profile);
        }
    }

    private static void notifyProfileRemoved(Profile profile) {
        for (IProfileUpdateListener listener : listeners) {
            listener.profileRemoved(profile);
        }
    }

    private static void notifyProfileEdited(Profile profile) {
        for (IProfileUpdateListener listener : listeners) {
            listener.profileEdited(profile);
        }
    }

    private static void saveProfiles(Context context, Set<Profile> profiles) {
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);

            Gson gson = new Gson();
            String profilesJson = gson.toJson(profiles);

            fos.write(profilesJson.getBytes());
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private static Set<Profile> loadProfiles(Context context) {
        try {
            FileInputStream input = context.openFileInput(FILENAME);

            StringBuffer stringBuf = new StringBuffer();
            byte[] buffer = new byte[BUFFER_SIZE];
            int length = 0;
            while ((length = input.read(buffer)) != -1) {
                stringBuf.append(new String(buffer, 0, length));
            }
            input.close();
            stringBuf.trimToSize();

            Gson gson = new Gson();
            String string = stringBuf.toString();
            Type type = new TypeToken<Set<Profile>>() {
            }.getType();
            return gson.fromJson(string, type);
        } catch (IOException e) {
            return new HashSet<Profile>();
        }
    }
}
