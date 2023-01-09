package animals.datastructures.tree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BinaryKnowledgeTree<T extends Comparable<T>> {

    private Node<T> root;

    private Node<T> current;

    public void addRight(T value) {
        add(value, Direction.RIGHT);
    }

    public void addLeft(T value) {
        add(value, Direction.LEFT);
    }

    public void replace(T value, T right, T left) {
        current.setValue(value);
        current.setRight(right == null ? null : new Node<>(right));
        current.setLeft(left == null ? null : new Node<>(left));
    }

    @JsonIgnore
    public T getLeft() {
        return current != null ? get(Direction.LEFT).getValue() : null;
    }

    @JsonIgnore
    public T getRight() {
        return current != null ? get(Direction.RIGHT).getValue() : null;
    }

    public void reset() {
        current = root;
    }

    @JsonIgnore
    public T getCurrentValue() {
        if (current == null) {
            return null;
        }
        return current.getValue();
    }

    @JsonIgnore
    public boolean isLeaf() {
        if (current == null) {
            return true;
        }
        return current.getRight() == null && current.getLeft() == null;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return root == null;
    }

    private void add(T value, Direction direction) {
        if (root == null) {
            root = new Node<>(value);
            current = root;
            return;
        }
        Node<T> parent = current;
        switch (direction) {
            case LEFT -> {
                current.setLeft(new Node<>(value));
                current = current.getLeft();
            }
            case RIGHT -> {
                current.setRight(new Node<>(value));
                current = current.getRight();
            }
        }
        current.setParent(parent);
    }

    public Node<T> getRoot() {
        return root;
    }

    public void setRoot(Node<T> root) {
        this.root = root;
    }

    @JsonIgnore
    public Node<T> getCurrent() {
        return current;
    }

    public void setCurrent(Node<T> current) {
        this.current = current;
    }

    public void printTree() {
        if (root == null) {
            return;
        }

        String corner = "└";
        String vertical = "│";
        String junction = "├";
        boolean yes = false;
        boolean no = false;

        Queue<Node<T>> nodes = new LinkedList<>();
        nodes.add(root);

        while (!nodes.isEmpty()) {
            Node<T> node = nodes.remove();

            no = node.getLeft() != null;
            if (no) {
                nodes.add(node.getLeft());
            }

            yes = node.getRight() != null;
            if (yes) {
                nodes.add(node.getRight());
            }

            if (no && yes) {
                System.out.printf("%s %s%n", vertical, node.getValue());
            }
            if (no && !yes) {
                System.out.printf("%s %s%n", junction, node.getValue());
            }
            if (!no && yes) {
                System.out.printf("%s %s%n", vertical, node.getValue());
            }
            // a leaf
            if (!no && !yes) {
                System.out.printf("%s %s%n", corner, node.getValue());
            }
        }
    }

    public void levelOrderTraverse() {
        if (root == null) {
            return;
        }
        Queue<Node<T>> nodes = new LinkedList<>();
        nodes.add(root);
        while (!nodes.isEmpty()) {
            Node<T> node = nodes.remove();
            System.out.println(node.getValue());
            if (node.getLeft() != null) {
                nodes.add(node.getLeft());
            }
            if (node.getRight() != null) {
                nodes.add(node.getRight());
            }
        }
    }

    public void depthOrderTraverse(Node<T> node) {
        if (node != null) {
            System.out.println(node.getValue());
            depthOrderTraverse(node.getLeft());
            depthOrderTraverse(node.getRight());
        }
    }

    public AggregatedKnowledge<T> aggregateKnowledge(T target) {
        AggregatedKnowledge<T> knowledge = new AggregatedKnowledge<>();
        knowledge.setTarget(target);
        recursiveAggregate(root, target, knowledge);
        Collections.reverse(knowledge.getFacts());
        return knowledge;
    }

    private boolean recursiveAggregate(Node<T> node, T target, AggregatedKnowledge<T> knowledge) {
        if (node != null) {
            if (node.getValue().equals(target)) {
                return true;
            }
            if (recursiveAggregate(node.getLeft(), target, knowledge)) {
                knowledge.addNegativeFact(node.getValue());
                return true;
            }
            if (recursiveAggregate(node.getRight(), target, knowledge)) {
                knowledge.addPositiveFact(node.getValue());
                return true;
            }

        }
        return false;
    }

    private Node<T> get(Direction direction) {
        switch (direction) {
            case RIGHT -> {
                if (current.getRight() != null) {
                    current = current.getRight();
                }
                return current;
            }
            case LEFT -> {
                if (current.getLeft() != null) {
                    current = current.getLeft();
                }
                return current;
            }
        }
        return current;
    }

    private enum Direction {
        RIGHT, LEFT
    }

}
