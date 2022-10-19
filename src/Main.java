import com.aads.tree.BinarySearchTree;
import com.aads.tree.demo.BST;
import com.aads.tree.demo.print.BinaryTrees;


public class Main {
    public static void main(String[] args) {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
//        for (int i = 1; i < 11; i++) {
//            tree.insert(i);
//        }
//
//        System.out.println(tree.toString());
        Integer data[] = new Integer[] {
                7, 4, 9, 2, 5, 8, 11, 3, 12, 1
        };

        BST<Integer> bst = new BST<>();
        for (int i = 0; i < data.length; i++) {
            tree.insert(data[i]);
            bst.add(data[i]);
        }

        bst.add(6);
        bst.remove(6);
        BinaryTrees.println(bst);

        tree.insert(6);
        tree.remove(6);

        System.out.println(tree.findMax()+" "+ tree.findMin());
        System.out.println(tree.isEmpty());
        System.out.println(tree.contains(6));
    }
}