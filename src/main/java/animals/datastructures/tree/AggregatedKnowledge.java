package animals.datastructures.tree;

import java.util.ArrayList;
import java.util.List;

public class AggregatedKnowledge<T extends Comparable<T>> {

    private T target;
    private List<Fact> facts = new ArrayList<>();

    public T getTarget() {
        return target;
    }

    public void setTarget(T target) {
        this.target = target;
    }

    public List<Fact> getFacts() {
        return facts;
    }

    public void setFacts(List<Fact> facts) {
        this.facts = facts;
    }

    public void addPositiveFact(T positive) {
        facts.add(new Fact(positive, true));
    }

    public void addNegativeFact(T negative) {
        facts.add(new Fact(negative, false));
    }

    public boolean isAbsent() {
        return facts.isEmpty();
    }

    public class Fact {
        private T value;
        private boolean positive;

        public Fact(T value, boolean positive) {
            this.value = value;
            this.positive = positive;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public boolean isPositive() {
            return positive;
        }

        public void setPositive(boolean positive) {
            this.positive = positive;
        }

        public boolean isNegative() {
            return !positive;
        }
    }
}
