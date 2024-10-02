package com.eternalcode.combat;

public enum WhitelistBlacklistMode {
    WHITELIST,
    BLACKLIST;

    public boolean shouldBlock(boolean isInList) {
        return switch (this) {
            case WHITELIST -> !isInList;
            case BLACKLIST -> isInList;
        };
    }
}
