package animals.services;

import animals.localization.Localized;

import java.time.LocalTime;
import java.util.List;
import java.util.Random;

// eagerly loaded singleton, just for brevity
public class GreetingService implements Localized {

    private final static GreetingService instance = new GreetingService();
    private final Random random = new Random();

    private GreetingService() {
    }

    public static GreetingService getInstance() {
        return instance;
    }

    public void greetByTime() {
        LocalTime now = LocalTime.now();

        LocalTime morning = LocalTime.of(5, 0);
        LocalTime afternoon = LocalTime.of(12, 0);
        LocalTime evening = LocalTime.of(18, 0);

        String timeOfDay;
        if (now.isAfter(evening)) {
            timeOfDay = getString("evening");
        } else if (now.isAfter(afternoon)) {
            timeOfDay = getString("afternoon");
        } else if (now.isAfter(morning)) {
            timeOfDay = getString("morning");
        } else {
            timeOfDay = getString("day");
        }
        System.out.printf("%s %s%n%n", getString("good"), timeOfDay);
    }

    public void sayGoodBye() {
        List<String> byes = getStringList("byeList");
        System.out.println("\n" + byes.get(random.nextInt(byes.size())) + "!");
    }
}
