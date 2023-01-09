package animals;

import animals.beans.AggregatedStatistics;
import animals.beans.AnimalInfo;
import animals.beans.AnswerType;
import animals.beans.MenuItem;
import animals.datastructures.tree.AggregatedKnowledge;
import animals.datastructures.tree.BinaryKnowledgeTree;
import animals.localization.Localization;
import animals.localization.Localized;
import animals.services.*;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Locale;

// eagerly loaded singleton, just for brevity
public class AnimalApplication implements Localized {

    private final static AnimalApplication instance = new AnimalApplication();
    private final GreetingService greetingService = GreetingService.getInstance();
    private final InputProcessService inputProcessService = InputProcessService.getInstance();
    private final InteractionService interactionService = InteractionService.getInstance();
    private final UserInputService inputService = UserInputService.getInstance();
    private final KnowledgeSaveService saveService = new KnowledgeSaveService();
    private BinaryKnowledgeTree<String> knowledgeData = new BinaryKnowledgeTree<>();
    private AnimalInfo prevAnimal = null;

    private AnimalApplication() {
    }

    public static AnimalApplication getInstance() {
        return instance;
    }

    public void startApplication() {
        initLocale();
        loadData();

        greetingService.greetByTime();

        if (knowledgeData == null || knowledgeData.isEmpty()) {
            interactionService.printIWantToLearnMessage();
            askForFavoriteAnimal();
        }

        System.out.printf("%s!%n%n", getString("welcome to the animal expert system"));

        startRoutine();

        stopApplication();
    }

    private void startRoutine() {
        boolean active = true;
        while (active) {
            printMenu();
            MenuItem menuItem = MenuItem.values()[(Integer.parseInt(inputService.getInput()))];
            switch (menuItem) {
                case PLAY_GAME -> startGuessingGame();
                case LIST_ANIMALS -> listAllAnimals();
                case SEARCH -> searchForAnimal();
                case CALCULATE_STATS -> calcStats();
                case PRINT_KNOWLEDGE -> printKnowledge();
                case EXIT -> active = false;
            }
        }
    }

    private void askForFavoriteAnimal() {
        interactionService.askForFavoriteAnimal();
        AnimalInfo favoriteAnimal = inputProcessService.parseAnimalInfo(inputService.getInput());
        prevAnimal = favoriteAnimal;
        knowledgeData = new BinaryKnowledgeTree<>();
        knowledgeData.addRight(favoriteAnimal.toString());
        interactionService.printCheerfulLearningMessage();
    }

    private void listAllAnimals() {
        System.out.printf("%s:%n", getString("here are the animals I know"));
        new AggregatedStatistics(knowledgeData).getLeaves().stream().map(i -> i.replaceFirst(getPattern("regex.article").pattern() + " ", "")).map(i -> " - " + i).sorted().forEach(System.out::println);
    }

    private void searchForAnimal() {
        System.out.printf("%s:%n", getString("enter the animal"));
        AnimalInfo animal = inputProcessService.parseAnimalInfo(inputService.getInput());
        AggregatedKnowledge<String> knowledge = knowledgeData.aggregateKnowledge(animal.toString());
        if (knowledge.isAbsent()) {
            System.out.println(getUnaryOperator("operator.facts.absent").apply(animal.getName()));
        } else {
            System.out.println(getUnaryOperator("operator.facts").apply(animal.getName()));
            knowledge.getFacts().stream().map(f -> {
                String value = inputProcessService.questionToFact(f.getValue());
                return f.isNegative() ? inputProcessService.opposeFact(value) : value;
            }).forEach(f -> System.out.printf(" - %s.%n", f));
        }
    }

    private void calcStats() {
        new AggregatedStatistics(knowledgeData).printStatistics();
    }

    private void printKnowledge() {
        knowledgeData.printTree();
    }

    private void startGuessingGame() {
        boolean active = true;
        while (active) {
            knowledgeData.reset();
            interactionService.printGameRules();
            interactionService.printPressWhenReadyMessage();
            inputService.getInput();
            String guessedAnimal = guessAnimal();
            if (guessedAnimal != null) {
                interactionService.printGuessAnimalMessage(guessedAnimal);
            }

            AnswerType userAnswer = inputProcessService.getAnswer();
            if (userAnswer == AnswerType.NEGATIVE) {
                interactionService.printUnGuessedMessage();
                AnimalInfo newAnimal = inputProcessService.parseAnimalInfo(inputService.getInput());
                AnimalInfo[] animalInfoArr = askForAnimalFacts(prevAnimal, newAnimal);
                interactionService.printDistinctAnimalFacts(prevAnimal, newAnimal);
                interactionService.printCanDistinguishBy(prevAnimal, newAnimal);
                String parsedQuestion = interactionService.getQuestionToDistinctBy(prevAnimal, newAnimal);
                knowledgeData.replace(parsedQuestion, animalInfoArr[0].toString(), animalInfoArr[1].toString());
                prevAnimal = newAnimal;
            }
            interactionService.printCheerfulLearningMessage();
            active = askIfPlayerWantsAnotherGame();
        }
    }

    private String guessAnimal() {
        if (knowledgeData.isLeaf()) {
            return knowledgeData.getCurrentValue();
        }

        interactionService.printQuestion(knowledgeData.getCurrentValue());
        AnswerType userAnswer = inputProcessService.getAnswer();
        if (userAnswer == AnswerType.POSITIVE) {
            prevAnimal = inputProcessService.parseAnimalInfo(knowledgeData.getRight());
        } else if (userAnswer == AnswerType.NEGATIVE) {
            prevAnimal = inputProcessService.parseAnimalInfo(knowledgeData.getLeft());
        }
        return guessAnimal();
    }

    private AnimalInfo[] askForAnimalFacts(AnimalInfo previous, AnimalInfo current) {
        AnimalInfo[] animalInfoArr = new AnimalInfo[2];
        interactionService.printRequestForDistinctFact(previous, current);
        String fact = inputProcessService.getFact();

        interactionService.askIfApplicableForOtherAnimal(current);
        fact = inputProcessService.parseInput(fact);
        fact = fact.replaceFirst(getPattern("regex.it").pattern() + " ", "");
        String opposedFact = inputProcessService.opposeFact(fact);

        AnswerType answerType = inputProcessService.getAnswer();
        if (answerType == AnswerType.POSITIVE) {
            previous.addFact(opposedFact);
            current.addFact(fact);
            animalInfoArr[0] = current;
            animalInfoArr[1] = previous;
        } else if (answerType == AnswerType.NEGATIVE) {
            previous.addFact(fact);
            current.addFact(opposedFact);
            animalInfoArr[0] = previous;
            animalInfoArr[1] = current;
        }
        return animalInfoArr;
    }

    private boolean askIfPlayerWantsAnotherGame() {
        if (knowledgeData != null) {
            knowledgeData.reset();
        }
        interactionService.printWantToPlayAgainMessage();
        return inputProcessService.getAnswer().booleanValue();
    }

    private void printMenu() {
        System.out.printf("%s:%n%n", getString("what do you want to do"));
        MenuItem[] items = MenuItem.values();
        for (int i = 1; i < items.length; i++) {
            System.out.printf("%d. %s%n", i, items[i].getDescription());
        }
        System.out.printf("%d. %s%n", 0, items[0].getDescription());
    }

    private void loadData() {
        try {
            knowledgeData = saveService.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void stopApplication() {
        try {
            if (knowledgeData != null) {
                knowledgeData.reset();
                saveService.save(knowledgeData);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("could not save knowledge data");
        }
        greetingService.sayGoodBye();
    }

    public AnimalApplication withArgs(String[] args) {
        if (args.length > 1) {
            for (int i = 0; i < args.length; i += 2) {
                if (args[i].equalsIgnoreCase("-type")) {
                    KnowledgeSaveService.setFileType(args[i + 1]);
                } else if (args[i].equalsIgnoreCase("-Duser.language")) {
                    Localization localization = Localization.getByLocale(new Locale(args[i + 1]));
                    if (localization != null) {
                        setLocale(localization);
                    } else {
                        throw new InputMismatchException("No locale found for " + args[i + 1]);
                    }
                }
            }
        }
        return this;
    }

    private void initLocale() {
        if (Locale.getDefault().equals(Localization.ESPERANTO.getLocale())) {
            setLocale(Localization.ESPERANTO);
        }
    }
}
