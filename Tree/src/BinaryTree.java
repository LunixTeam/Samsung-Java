import java.util.ArrayList;
import java.util.List;

public class BinaryTree {
    private int value;
    private BinaryTree lChild;
    private BinaryTree rChild;

    public BinaryTree(int value) {
        this.value = value;
        this.lChild = null;
        this.rChild = null;
    }

    public BinaryTree insertNode(BinaryTree node, int targetValue) {
        if (node == null){
            node = new BinaryTree(targetValue);
            return node;
        }
        if (node.value > targetValue) {
            if (node.lChild == null)
                return node.lChild = new BinaryTree(targetValue);
            else
                return insertNode(node.lChild, targetValue);
        } else if (node.value < targetValue) {
            if (node.rChild == null)
                return node.rChild = new BinaryTree(targetValue);
            else
                return insertNode(node.rChild, targetValue);
        }
        return null;
    }

    public void printBinaryTree(BinaryTree node, int level) {
        if (node == null) return;
        if (level == 0) {
            int h = getHeight(node);
            java.util.ArrayList<BinaryTree> list = new java.util.ArrayList<>();
            list.add(node);
            printLevels(list, 1, h);
        }
    }

    private int getHeight(BinaryTree node) {
        if (node == null) return 0;
        int left = getHeight(node.lChild);
        int right = getHeight(node.rChild);
        if (left > right) return left + 1;
        else return right + 1;
    }

    private void printLevels(java.util.ArrayList<BinaryTree> nodes, int lvl, int maxLvl) {
        if (nodes.size() == 0) return;

        boolean empty = true;
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i) != null) {
                empty = false;
                break;
            }
        }
        if (empty) return;

        int down = maxLvl - lvl;

        int spaces1 = 1;
        for (int i = 0; i < down; i++) spaces1 = spaces1 * 2;
        spaces1 = spaces1 * 2 - 2;

        int spaces2 = 1;
        for (int i = 0; i < down + 1; i++) spaces2 = spaces2 * 2;
        spaces2 = spaces2 * 2 - 2;

        for (int i = 0; i < spaces1; i++) System.out.print(" ");

        java.util.ArrayList<BinaryTree> next = new java.util.ArrayList<>();

        for (int i = 0; i < nodes.size(); i++) {
            BinaryTree now = nodes.get(i);

            if (now != null) {
                System.out.print(now.value);
                if (now.value < 10) System.out.print(" ");
                next.add(now.lChild);
                next.add(now.rChild);
            } else {
                System.out.print("  ");
                next.add(null);
                next.add(null);
            }

            for (int j = 0; j < spaces2; j++) System.out.print(" ");
        }

        System.out.println();
        System.out.println();

        if (lvl < maxLvl) {
            printLevels(next, lvl + 1, maxLvl);
        }
    }
}