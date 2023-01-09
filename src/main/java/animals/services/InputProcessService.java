package animals.services;

import animals.beans.AnimalInfo;
import animals.beans.AnswerType;
import animals.localization.Localized;

import java.util.function.UnaryOperator;
import java.util.regex.Matcher;

// eagerly loaded singleton, just for brevity
public class InputProcessService implements Localized {

    private final static InputProcessService instance = new InputProcessService();

    private final InteractionService interactionService = InteractionService.getInstance();

    private final UserInputService inputService = UserInputService.getInstance();

    private InputProcessService() {
    }

    public static InputProcessService getInstance() {
        return instance;
    }


    public String correctArticle(String str) {
        UnaryOperator<String> fixer = getUnaryOperator("operator.article.fix");
        return fixer.apply(str);
    }

    public String getFact() {
        String fact = null;
        boolean isFact = false;
        while (!isFact) {
            fact = inputService.getInput();
            isFact = isFact(fact);
            if (!isFact) {
                interactionService.printFactExamples();
            }
        }
        return fact;
    }

    public AnswerType getAnswer() {
        AnswerType answerType = AnswerType.UNKNOWN;
        while (answerType == AnswerType.UNKNOWN) {
            String answer = inputService.getInput();
            answerType = getAnswerType(answer);
            if (answerType == AnswerType.UNKNOWN) {
                interactionService.printClarificationMessage();
            }
        }
        return answerType;
    }

    public AnimalInfo parseAnimalInfo(String animal) {
        String[] result = correctArticle(animal).toLowerCase().split(" ", 2);
        if (result.length > 1)
            return new AnimalInfo(result[1], result[0]);
        return new AnimalInfo(result[0], null);
    }

    public String parseInput(String input) {
        return input.replaceFirst("[?!.]", "");
    }

    public String opposeFact(String fact) {
        Matcher matcher = getPattern("regex.pronoun.verb").matcher(fact);
        if (matcher.find()) {
            String verb = matcher.group();
            verb = getUnaryOperator("operator.verb.oppose").apply(verb);
            fact = fact.replaceFirst(getPattern("regex.pronoun.verb").pattern(), interactionService.opposeTense(verb));
        }
        return fact;
    }

    public AnswerType getAnswerType(String answer) {
        answer = parseInput(answer);
        if (getPattern("regex.answer.positive").matcher(answer).matches()) {
            return AnswerType.POSITIVE;
        }
        if (getPattern("regex.answer.negative").matcher(answer).matches()) {
            return AnswerType.NEGATIVE;
        }
        return AnswerType.UNKNOWN;
    }

    public boolean isFact(String fact) {
        return getPattern("regex.fact").matcher(fact).find();
    }

    public String questionToFact(String question) {
        return getUnaryOperator("operator.questionToFact").apply(question);
    }

}
