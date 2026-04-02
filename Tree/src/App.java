public class App {
    public static void main(String[] args) {
        BinaryTree meTree = new BinaryTree(10);
        meTree.insertNode(meTree, 5);
        meTree.insertNode(meTree, 7);
        meTree.insertNode(meTree, 0);
        meTree.insertNode(meTree, 15);
        meTree.insertNode(meTree, 5);
        meTree.insertNode(meTree, 6);
        meTree.insertNode(meTree, 4);
        meTree.insertNode(meTree, 1);
        meTree.insertNode(meTree, 12);
        meTree.insertNode(meTree, 24);

        meTree.printBinaryTree(meTree, 0);
    }
}
