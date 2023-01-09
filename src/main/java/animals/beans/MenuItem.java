package animals.beans;

import animals.localization.Localized;

public enum MenuItem implements Localized {

    EXIT("exit"),
    PLAY_GAME("play the guessing game"),
    LIST_ANIMALS("list of all animals"),
    SEARCH("search for an animal"),
    CALCULATE_STATS("calculate statistics"),
    PRINT_KNOWLEDGE("print the knowledge tree");

    final String description;

    MenuItem(String description) {
        this.description = description;
    }

    public String getDescription() {
        return getString(description);
    }
}