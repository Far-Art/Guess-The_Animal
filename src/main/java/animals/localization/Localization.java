package animals.localization;

import java.util.Arrays;
import java.util.Locale;

public enum Localization {
    ENGLISH(English_lang.class.getName(), new Locale("en")),
    ESPERANTO(Esperanto_lang.class.getName(), new Locale("eo"));

    final String path;

    final Locale locale;

    Localization(String path, Locale locale) {
        this.path = path;
        this.locale = locale;
    }

    public static Localization getByLocale(Locale locale) {
        return Arrays.stream(Localization.values()).filter(l -> l.getLocale().equals(locale)).findFirst().orElse(null);
    }

    public String getPath() {
        return path;
    }

    public Locale getLocale() {
        return locale;
    }
}
