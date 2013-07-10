package com.spacemangames.biomatcher.controller;

import com.spacemangames.biomatcher.data.Profile;

public interface IProfileUpdateListener {
    void profileAdded(Profile profile);

    void profileRemoved(Profile profile);

    void profileEdited(Profile profile);
}
