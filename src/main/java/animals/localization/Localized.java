package animals.localization;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public interface Localized {

    Localization DEFAULT = Localization.ENGLISH;

    AtomicReference<ResourceBundle> resourceBundle = new AtomicReference<>(ResourceBundle.getBundle(DEFAULT.getPath()));

    default void setLocale(Localization localization) {
        resourceBundle.set(ResourceBundle.getBundle(localization.getPath()));
        Locale.setDefault(localization.getLocale());
    }

    default String getString(String key) {
        return resourceBundle.get().getString(key);
    }

    default List<String> getStringList(String key) {
        return Arrays.asList(resourceBundle.get().getStringArray(key));
    }

    default <T> T getObject(String key) {
        return (T) resourceBundle.get().getObject(key);
    }

    default UnaryOperator<String> getUnaryOperator(String key) {
        return getObject(key);
    }

    default Pattern getPattern(String key) {
        return getObject(key);
    }

}
