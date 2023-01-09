package animals.beans;

public enum AnswerType {
    POSITIVE(Boolean.TRUE), NEGATIVE(Boolean.FALSE), UNKNOWN(null);

    final Boolean booleanValue;

    AnswerType(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public Boolean booleanValue() {
        return booleanValue;
    }
}
