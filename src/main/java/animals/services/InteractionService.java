package animals.services;

import animals.beans.AnimalInfo;
import animals.localization.Localized;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

// eagerly loaded singleton, just for brevity
public class InteractionService implements Localized {

    private final static InteractionService instance = new InteractionService();

    private final Random random = new Random();

    private InteractionService() {
    }

    public static InteractionService getInstance() {
        return instance;
    }

    public void askForFavoriteAnimal() {
        System.out.printf("%s?%n", getString("which animal do you like most"));
    }

    public void printIWantToLearnMessage() {
        System.out.printf("%s.%n", getString("i want to learn about animals"));
    }

    public void askIfApplicableForOtherAnimal(AnimalInfo animal) {
        System.out.printf("%s %s?%n", getString("is the statement correct for"), animal);
    }

    public void printGuessAnimalMessage(String animal) {
        System.out.printf("%s %s?%n", getString("is it"), animal);
    }

    public void printCheerfulLearningMessage() {
        System.out.printf("%s!%n", getString("wonderful! I've learned so much about animals"));
    }

    public void printGameRules() {
        System.out.printf("%s.%n", getString("you think of an animal, and I guess it"));
    }

    public void printQuestion(String question) {
        System.out.println(question);
    }

    public void printUnGuessedMessage() {
        System.out.printf("%s?%n", getString("i give up. What animal do you have in mind"));
    }

    public void printWantToPlayAgainMessage() {
        System.out.printf("%s?%n", getString("would you like to play again"));
    }

    public void printPressWhenReadyMessage() {
        System.out.printf("%s.%n", getString("press enter when you're ready"));
    }

    public void printFactExamples() {
        Consumer<String> consumer = getObject("the examples of a statement");
        consumer.accept(null);
    }

    public void printClarificationMessage() {
        List<String> messages = getStringList("clarificationMessage");
        System.out.println(messages.get(random.nextInt(messages.size())));
    }

    public void printDistinctAnimalFacts(AnimalInfo animal, AnimalInfo otherAnimal) {
        BiConsumer<AnimalInfo, AnimalInfo> printer = getObject("print learned facts");
        printer.accept(animal, otherAnimal);
    }

    public void printCanDistinguishBy(AnimalInfo animal, AnimalInfo otherAnimal) {
        System.out.printf("%s:%n", getString("i can distinguish these animals by asking the question"));
        System.out.printf(" - %s%n", getQuestionToDistinctBy(animal, otherAnimal));
    }

    public String getQuestionToDistinctBy(AnimalInfo animal, AnimalInfo otherAnimal) {
        BiFunction<AnimalInfo, AnimalInfo, String> question = getObject("function.distinctByQuestion");
        return question.apply(animal, otherAnimal);
    }

    public void printRequestForDistinctFact(AnimalInfo animal, AnimalInfo otherAnimal) {
        BiConsumer<AnimalInfo, AnimalInfo> printer = getObject("print request for distinct facts");
        printer.accept(animal, otherAnimal);
    }

    public CharacterType startWith(String word) {
        return getPattern("regex.vowels").matcher(word.substring(0, 1).toLowerCase()).matches() ? CharacterType.VOWEL : CharacterType.CONSONANT;
    }

    public String opposeTense(String tense) {
        return getUnaryOperator("operator.tense.oppose").apply(tense);
    }

    public enum CharacterType {
        VOWEL, CONSONANT
    }
}
