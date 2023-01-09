package animals.beans;

import animals.datastructures.tree.BinaryKnowledgeTree;
import animals.datastructures.tree.Node;
import animals.localization.Localized;
import animals.services.InputProcessService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class AggregatedStatistics implements Localized {

    private final InputProcessService process = InputProcessService.getInstance();

    private final Node<String> root;

    private List<AnimalMetadata> leaves;

    private int totalNodes = 0;

    private int treeHeight = 0;

    public AggregatedStatistics(BinaryKnowledgeTree<String> knowledgeTree) {
        root = knowledgeTree != null ? knowledgeTree.getRoot() : null;
    }

    public void printStatistics() {
        aggregate();
        // TODO make dynamic alignment
        System.out.printf("%s%n%n", getString("the knowledge tree stats"));
        System.out.printf("- %s                    %s%n", getString("root node"), process.questionToFact(root.getValue()));
        System.out.printf("- %s        %d%n", getString("total number of nodes"), totalNodes);
        System.out.printf("- %s      %d%n", getString("total number of animals"), leaves.size());
        System.out.printf("- %s   %d%n", getString("total number of statements"), totalNodes - leaves.size());
        System.out.printf("- %s           %d%n", getString("height of the tree"), treeHeight);
        System.out.printf("- %s       %d%n", getString("minimum animal's depth"), getMinAnimalDepth());
        System.out.printf("- %s       %.1f%n", getString("average animal's depth"), getAverageAnimalDepth());
    }

    public List<String> getLeaves() {
        aggregate();
        return leaves.stream().map(a -> a.animal).toList();
    }

    private void aggregate() {
        leaves = new ArrayList<>();
        if (root == null) {
            return;
        }
        Queue<Node<String>> nodes = new LinkedList<>();

        nodes.add(root);
        totalNodes++;
        while (!nodes.isEmpty()) {
            Node<String> node = nodes.remove();

            if (node.getRight() == null && node.getLeft() == null) {
                leaves.add(new AnimalMetadata(node.getValue(), treeHeight));
            }

            if (node.getLeft() != null || node.getRight() != null) {
                if (node.getLeft() != null) {
                    nodes.add(node.getLeft());
                    totalNodes++;
                }
                if (node.getRight() != null) {
                    nodes.add(node.getRight());
                    totalNodes++;
                }
                treeHeight++;
            }
        }
    }

    private int getMinAnimalDepth() {
        return leaves.stream().mapToInt(i -> i.depth).min().orElse(0);
    }

    private double getAverageAnimalDepth() {
        double sum = leaves.stream().map(i -> (double) i.depth).reduce(Double::sum).orElse(0.0);
        return sum / leaves.size();
    }

    private static class AnimalMetadata {
        String animal;
        int depth;

        public AnimalMetadata(String animal, int depth) {
            this.animal = animal;
            this.depth = depth;
        }
    }
}
