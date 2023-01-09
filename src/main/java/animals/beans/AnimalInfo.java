package animals.beans;

import java.util.LinkedHashSet;
import java.util.Set;

public class AnimalInfo {

    private final String name;

    private final String article;

    private final Set<String> facts = new LinkedHashSet<>() {
    };

    public AnimalInfo(String name, String article) {
        this.name = name;
        this.article = article;
    }

    public void addFact(String fact) {
        facts.add(fact);
    }

    public Set<String> getFacts() {
        return facts;
    }

    public String getLastFact() {
        return facts.stream().toList().get(facts.size() - 1);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnimalInfo that = (AnimalInfo) o;

        if (!name.equals(that.name)) return false;
        return article.equals(that.article);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + article.hashCode();
        return result;
    }

    public String getArticle() {
        return article;
    }

    @Override
    public String toString() {
        if (article == null || article.isBlank())
            return name;
        return article + " " + name;
    }
}
