package com.aptatek.pkulab.domain.interactor.reader;

import java.util.List;

public class MissingPermissionsError extends Throwable {

    private final boolean showRationale;
    private final List<String> missingPermissions;

    public MissingPermissionsError(final boolean showRationale,
                                   final List<String> missingPermissions) {
        this.showRationale = showRationale;
        this.missingPermissions = missingPermissions;
    }

    public boolean shouldShowRationale() {
        return showRationale;
    }

    public List<String> getMissingPermissions() {
        return missingPermissions;
    }
}
