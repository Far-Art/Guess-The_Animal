package animals.localization;

import animals.beans.AnimalInfo;

import java.util.ListResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class English_lang extends ListResourceBundle implements Localized {
    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                {"locale", "en"},

                // WORDS START
                {"the", "The"},
                {"good", "Good"},
                // WORDS END

                // MenuItem START
                {"exit", "Exit"},
                {"play the guessing game", "Play the guessing game"},
                {"list of all animals", "List of all animals"},
                {"search for an animal", "Search for an animal"},
                {"calculate statistics", "Calculate statistics"},
                {"print the knowledge tree", "Print the Knowledge Tree"},
                // MenuItem END

                // AggregatedStatistics START
                {"the knowledge tree stats", "The Knowledge Tree stats"},
                {"root node", "root node"},
                {"total number of nodes", "total number of nodes"},
                {"total number of animals", "total number of animals"},
                {"total number of statements", "total number of statements"},
                {"height of the tree", "height of the tree"},
                {"minimum animal's depth", "minimum animal's depth"},
                {"average animal's depth", "average animal's depth"},
                // AggregatedStatistics END

                // GreetingService START //
                {"morning", "morning"},
                {"day", "day"},
                {"afternoon", "afternoon"},
                {"evening", "evening"},
                {"byeList", new String[]{
                        "See ya",
                        "Bye, bye!",
                        "See you later!",
                        "See you soon!",
                        "Have a nice one!"
                }},
                // GreetingService END //

                // AnimalApplication START //
                {"welcome to the animal expert system", "Welcome to the animal expert system"},
                {"here are the animals I know", "Here are the animals I know"},
                {"enter the animal", "Enter the animal"},
                {"what do you want to do", "What do you want to do"},
                {"hello", "Hello!"},
                // AnimalApplication END //

                // InteractionService START //
                {"yesList", new String[]{
                        "y",
                        "yes",
                        "yeah",
                        "yep",
                        "sure",
                        "right",
                        "affirmative",
                        "correct",
                        "indeed",
                        "you bet",
                        "exactly",
                        "you said it"
                }},
                {"noList", new String[]{
                        "n",
                        "no",
                        "no way",
                        "nah",
                        "nope",
                        "negative",
                        "I don't think so",
                        "yeah no"
                }},
                {"it", "it"},
                {"is it", "Is it"},
                {"has", "has"},
                {"hasn't", "hasn't"},
                {"does have", "does have"},
                {"doesn't have", "doesn't have"},
                {"which animal do you like most", "Which animal do you like most"},
                {"i want to learn about animals", "I want to learn about animals"},
                {"i give up. What animal do you have in mind", "I give up. What animal do you have in mind"},
                {"would you like to play again", "Would you like to play again"},
                {"is the statement correct for", "Is the statement correct for"},
                {"wonderful! I've learned so much about animals", "Wonderful! I've learned so much about animals"},
                {"you think of an animal, and I guess it", "You think of an animal, and I guess it"},
                {"press enter when you're ready", "Press enter when you're ready"},
                {"the examples of a statement", (Consumer<String>) (s) -> {
                    System.out.println("The examples of a statement:");
                    System.out.println(" - It can fly");
                    System.out.println(" - It has horn");
                    System.out.println(" - It is a mammal");
                }},
                {"print learned facts", (BiConsumer<AnimalInfo, AnimalInfo>) (animal, other) -> {
                    System.out.println("I learned the following facts about animals");
                    System.out.printf(" - The %s %s.%n", animal.getName(), animal.getLastFact());
                    System.out.printf(" - The %s %s.%n", other.getName(), other.getLastFact());
                }},
                {"print request for distinct facts", (BiConsumer<AnimalInfo, AnimalInfo>) (animal, other) -> {
                    System.out.printf("Specify a fact that distinguishes %s from %s.%nThe sentence should be of the format: 'It can/has/is ...'%n", animal, other);
                }},
                {"clarificationMessage", new String[]{
                        "I'm not sure I caught you: was it yes or no?",
                        "Funny, I still don't understand, is it yes or no?",
                        "Oh, it's too complicated for me: just tell me yes or no.",
                        "Could you please simply say yes or no?",
                        "yes or no please"
                }},
                {"i can distinguish these animals by asking the question", "I can distinguish these animals by asking the question"},
                // InteractionService END //

                // UnaryOperators START //
                {"operator.article", (UnaryOperator<String>) name -> {
                    if (name.matches("[aeiou].*")) {
                        return "an";
                    } else {
                        return "a";
                    }
                }},
                {"operator.article.fix", (UnaryOperator<String>) str -> {
                    String animal = str;
                    String article = "";
                    if (getPattern("regex.article").matcher(str).find()) {
                        String[] inputArr = str.split(" ", 2);
                        article = inputArr[0];
                        animal = inputArr[1];
                    }
                    if (article.toLowerCase().matches(String.format("^$|%s", getString("the").toLowerCase()))) {
                        article = getUnaryOperator("operator.article").apply(animal);
                    }
                    return String.format("%s %s", article, animal);
                }},
                {"operator.facts", (UnaryOperator<String>) animal -> String.format("Facts about the %s.", animal)},
                {"operator.facts.absent", (UnaryOperator<String>) animal -> String.format("No facts about the %s.", animal)},
                /* convert verbs like can or has to cant and hasn't respectively */
                {"operator.verb.oppose", (UnaryOperator<String>) verb -> verb + (verb.charAt(verb.length() - 1) == 'n' ? "'t" : "n't")},
                {"operator.tense.oppose", (UnaryOperator<String>) tense -> {
                    if (tense.contains("hasn't"))
                        return "doesn't have";
                    else if (tense.contains("has"))
                        return "does have";
                    return tense;
                }},
                {"operator.questionToFact", (UnaryOperator<String>) question -> "It " + question.replace("it ", "").replace("?", "").toLowerCase()},
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
                    return String.format("%s it %s%s?", verbArr[0], verbArr.length > 1 ? verbArr[1] + " " : "", fact);
                }},
                // Functions END //

                // Regex START //
                {"regex.answer.positive", Pattern.compile("\\b(y|yes|yeah|yep|sure|right|affirmative|correct|indeed|you bet|exactly|you said it)\\b", Pattern.CASE_INSENSITIVE)},
                {"regex.answer.negative", Pattern.compile("\\b(n|no|no way|nah|nope|negative|I don't think so|yeah no)\\b", Pattern.CASE_INSENSITIVE)},
                {"regex.vowels", Pattern.compile("[aeiouy]", Pattern.CASE_INSENSITIVE)},
                {"regex.article", Pattern.compile("^\\b(an?|the)\\b", Pattern.CASE_INSENSITIVE)},
                {"regex.pronoun.verb", Pattern.compile("\\b(is|has|does|can)\\b", Pattern.CASE_INSENSITIVE)},
                {"regex.pronoun.verb.negative", Pattern.compile("[\\w\\s]*(is|has|does|can)n?'t[\\w\\s]*", Pattern.CASE_INSENSITIVE)},
                {"regex.fact", Pattern.compile("^it\\s\\b(is|has|does|can)\\b\\s", Pattern.CASE_INSENSITIVE)},
                {"regex.it", Pattern.compile("[Ii]t")},
                // Regex END //
        };
    }
}
