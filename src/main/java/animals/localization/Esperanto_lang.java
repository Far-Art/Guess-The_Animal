package animals.localization;

import animals.beans.AnimalInfo;

import java.util.ListResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class Esperanto_lang extends ListResourceBundle implements Localized {

    @Override
    protected Object[][] getContents() {

        return new Object[][]{
                {"locale", "eo"},

                // WORDS START
                {"the", "La"},
                {"good", "Bone"},
                // WORDS END

                // MenuItem START
                {"exit", "Eliri"},
                {"play the guessing game", "Ludi la divenludon"},
                {"list of all animals", "Listo de ĉiuj bestoj"},
                {"search for an animal", "Serĉi beston"},
                {"calculate statistics", "Kalkuli statistikon"},
                {"print the knowledge tree", "Presu la Scion-Arbon"},
                // MenuItem END

                // AggregatedStatistics START
                {"the knowledge tree stats", "La statistiko"},
                {"root node", "radika nodo"},
                {"total number of nodes", "totala nombro de nodoj"},
                {"total number of animals", "totala nombro de bestoj"},
                {"total number of statements", "totala nombro de deklaroj"},
                {"height of the tree", "alteco de la arbo"},
                {"minimum animal's depth", "minimuma profundo de besto"},
                {"average animal's depth", "averaĝa profundo de besto"},
                // AggregatedStatistics END

                // GreetingService START //
                {"morning", "mateno"},
                {"day", "tago"},
                {"afternoon", "posttagmezo"},
                {"evening", "vespero"},
                {"byeList", new String[]{
                        "Ĝis!",
                        "Ĝis revido!",
                        "Estis agrable vidi vin!"
                }},
                // GreetingService END //

                // AnimalApplication START //
                {"welcome to the animal expert system", "Bonvenon al la sperta sistemo de la besto"},
                {"here are the animals I know", "Jen la bestoj, kiujn mi konas"},
                {"enter the animal", "Enigu la besto"},
                {"what do you want to do", "Kion vi volas fari"},
                {"hello", "Saluton!"},
                // AnimalApplication END //

                // InteractionService START //
                {"yesList", new String[]{
                        "j",
                        "je",
                        "jes",
                        "certe",
                        "ĝuste",
                        "jesa",
                        "ĝusta",
                        "fakte",
                        "vi vetas",
                        "vi diris ĝin"
                }},
                {"noList", new String[]{
                        "n",
                        "ne",
                        "neniel",
                        "nah",
                        "negativa",
                        "mi ne pensas tiel",
                        "jes ne"
                }},
                {"it", "Ĝi"},
                {"is it", "Ĉu ĝi estas"},
                {"which animal do you like most", "Kiun beston vi plej ŝatas"},
                {"i want to learn about animals", "Mi volas lerni pri bestoj"},
                {"i give up. What animal do you have in mind", "Mi rezignas. kiun beston vi havas en la kapo"},
                {"would you like to play again", "Ĉu vi ŝatus ludi denove"},
                {"is the statement correct for", "Ĉu la aserto ĝustas por la"},
                {"wonderful! I've learned so much about animals", "Mirinda! Mi lernis multe pri bestoj"},
                {"you think of an animal, and I guess it", "Vi pensu pri besto, kaj mi divenos ĝin"},
                {"press enter when you're ready", "Premu enen kiam vi pretas"},
                {"the examples of a statement", (Consumer<String>) (s) -> {
                    System.out.println("La ekzemploj de deklaro:");
                    System.out.println(" - Ĝi povas flugi");
                    System.out.println(" - Ĝi havas kornon");
                    System.out.println(" - Ĝi estas mamulo");
                }},
                {"print learned facts", (BiConsumer<AnimalInfo, AnimalInfo>) (animal, other) -> {
                    System.out.println("Mi lernis la jenajn faktojn pri bestoj");
                    System.out.printf(" - La %s %s.%n", animal.getName(), animal.getLastFact());
                    System.out.printf(" - La %s %s.%n", other.getName(), other.getLastFact());
                }},
                {"print request for distinct facts", (BiConsumer<AnimalInfo, AnimalInfo>) (animal, other) -> {
                    System.out.printf("Indiku fakton, kiu distingas %s de %s.%nLa frazo devus esti de la formato: 'Ĝi povas/havas/estas ...'%n", animal, other);
                }},
                {"clarificationMessage", new String[]{
                        "Mi ne certas, ke mi kaptis vin: ĉu jes aŭ ne?",
                        "Amuze, mi ankoraŭ ne komprenas, ĉu jes aŭ ne?",
                        "Ho, ĝi estas tro komplika por mi: nur diru al mi jes aŭ ne.",
                        "Ĉu vi bonvolus simple diri jes aŭ ne?"
                }},
                {"i can distinguish these animals by asking the question", "Mi povas distingi ĉi tiujn bestojn farante la demandon"},
                // InteractionService END //

                // UnaryOperators START //
                {"operator.article", (UnaryOperator<String>) name -> name},
                {"operator.article.fix", (UnaryOperator<String>) str -> str},
                {"operator.facts", (UnaryOperator<String>) animal -> String.format("Faktoj pri la %s.", animal)},
                {"operator.facts.absent", (UnaryOperator<String>) animal -> String.format("Neniuj faktoj pri la %s.", animal)},
                /* convert verbs like can or has to cant and hasn't respectively */
                {"operator.verb.oppose", (UnaryOperator<String>) verb -> "ne " + verb},
                {"operator.tense.oppose", (UnaryOperator<String>) tense -> tense},
                {"operator.questionToFact", (UnaryOperator<String>) question -> "Ĝi " + question.replace("ĝi ", "").replace("?", "").toLowerCase()},
                // UnaryOperators END //

                // Functions START //
                {"function.distinctByQuestion", (BiFunction<AnimalInfo, AnimalInfo, String>) (animal, other) -> {
                    String fact = animal.getLastFact();
                    String pattern = getPattern("regex.pronoun.verb.negative").toString();
                    if (fact.matches(pattern)) {
                        fact = other.getLastFact();
                    }
                    String verb = fact.split(" ", 2)[0];
                    fact = fact.split(" ", 2)[1];
                    verb = getUnaryOperator("operator.tense.oppose").apply(verb);
                    verb = verb.substring(0, 1).toUpperCase() + verb.substring(1);
                    String[] verbArr = verb.split(" ");
                    return String.format("%s %s%s?", verbArr[0], verbArr.length > 1 ? verbArr[1] + " " : "", fact);
                }},
                // Functions END //

                // Regex START //
                {"regex.answer.positive", Pattern.compile("\\b(j|je|jes|certe|ĝuste|jesa|ĝusta|fakte|vi vetas|vi diris ĝin)\\b", Pattern.CASE_INSENSITIVE)},
                {"regex.answer.negative", Pattern.compile("\\b(n|ne|neniel|nah|negativa|mi ne pensas tiel)\\b", Pattern.CASE_INSENSITIVE)},
                {"regex.vowels", Pattern.compile("[aeiouy]", Pattern.CASE_INSENSITIVE)},
                {"regex.article", Pattern.compile("^\\b(an?|la)\\b", Pattern.CASE_INSENSITIVE)},
                {"regex.pronoun.verb", Pattern.compile("\\b(estas|havas|faras|povas|loĝas)\\b", Pattern.CASE_INSENSITIVE)},
                {"regex.pronoun.verb.negative", Pattern.compile("[\\w\\s]*ne (estas|havas|faras|povas|loĝas)[\\w\\s]*", Pattern.CASE_INSENSITIVE)},
                {"regex.fact", Pattern.compile("^([Ĝĝ]i)\\s\\b(estas|havas|faras|povas|loĝas)\\b\\s", Pattern.CASE_INSENSITIVE)},
                {"regex.it", Pattern.compile("[Ĝĝ]i")},
                // Regex END //
        };
    }
}
