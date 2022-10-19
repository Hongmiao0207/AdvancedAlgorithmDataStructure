package com.aads.tree;

import java.util.Comparator;

public class BinarySearchTree<E> { // <E extends Comparable> 排序接口，需要比较类实现该接口

    private BinaryNode<E> root;

    private Comparator<E> cmp; // 替代Comparable的方式，比较器，不需要修改类，只需要写一个方法

    // Constructor，new的时候不需要传比较器，我的比较规则会在本类中实现
    public BinarySearchTree() {
        this(null);
    }

    // Constructor，new的时候需要传比较器，通过这种方式构建的树会按照cmp的方式进行元素比较
    public BinarySearchTree(Comparator<E> cmp) {
        this.cmp = cmp;
    }

    // 比较大小
    private int myCompare(E e1, E e2) {
        // 外界已经定义类比较方式，直接通过外界传来的comparator来比较
        if (cmp != null) {
            return cmp.compare(e1, e2);
        } else { // 比较器是空，也就是外界没有传比较器，那么就需要强制判断比较元素是否用比较性，也就是将其强转为 Comparable<E>，再进行比较
            // 如果报错，证明其不具备比较性；这个位置强转大部分场合要优于直接在类上让E继承它好，因为如果在类上操作，每一个比较类都要重复继承Comparable
            return ((Comparable<E>) e1).compareTo(e2);
        }
    }

    // 制空
    public void makeEmpty() {
        root = null;
    }

    // 判空
    public boolean isEmpty() {
        return root == null;
    }

    // 判断是否含有e
    public boolean contains(E e) {
        return contains(e, root);
    }

    public E findMin() {
        BinaryNode<E> minValue = findExtremum(root, 0);
        return (minValue != null) ? minValue.element : null;
    }

    public E findMax() {
        BinaryNode<E> maxValue = findExtremum(root, 1);
        return (maxValue != null) ? maxValue.element : null;
    }

    public void insert(E e) {
        insert(e, root);
    }

    public void remove(E e) {
        remove(e, root);
    }

    public void printTree() {
        printTree(root);
    }


    private boolean contains(E e, BinaryNode<E> t) {
        // 递归调用，e<t，进左子树，e>t，进右子树；e=t，返回true，当t=null，返回false

        // 到达叶子节点的下一个节点，返回false
        if (t == null) {
            return false;
        }

        // 这个比较会被弃用，因为这个比较需要比较对象要实现Comparable接口才行
        // 实际使用中，对于每一个自定义需要比较的类都就要实现它
        // 比较et大小
        //int compareResult = e.compareTo(t.element);
        // 替换成：int compareResult = myCompare(e, t.element);

        // 递归地进行查找，性能略差，因为每执行一层都要走一遍所有的contains()方法
//        if (0 < compareResult) {        // 进右子树
//            return contains(e, t.right);
//        } else if (0 > compareResult) { // 进左子树
//            return contains(e, t.left);
//        } else {                        // 找到元素
//            return true;
//        }

        // while实现
        while (t != null) {
            int compareResult = myCompare(e, t.element);
            if (compareResult < 0) {           // 进入左子树
                t = t.left;
            } else if (compareResult > 0) { // 进入右子树
                t = t.right;
            } else {
                return true;
            }
        }
        return false;
    }

    private BinaryNode<E> findExtremum(BinaryNode<E> t, int extremum) {

        if (extremum == 0) { // 最小值
            while (t.left != null) {
                t = t.left;
            }
        }else if(extremum == 1){ // 最大值
            while (t.right != null) {
                t = t.right;
            }
        }
        return t;
    }


    private BinaryNode<E> insert(E e, BinaryNode<E> t) {
        // insert 不会考虑重新排序，它只会按照小左大右的原则把新元素插入到对应位置，不会改变原来树结构

        if (t == null) { // 插入之前是空树，插入元素则为root
            root = new BinaryNode<E>(e, null, null);
            return root;
        }


        // TODO 插入部分还可以优化

        BinaryNode<E> parent = root;
        // 记录插入元素和被插入的节点大小关系
        int compareResult = 0;
        while (true) {

            // 到达叶子节点是开始插入元素
            if (t == null) {
                t = new BinaryNode<E>(e, null, null);
                if (compareResult > 0) { // 父节点右边
                    parent.right = t;
                } else { // 父节点左边
                    parent.left = t;
                }
                break;
            }

            // 查找插入元素位置
            compareResult = myCompare(e, t.element);    // 比较大小
            if (compareResult < 0) {  // 左边
                parent = t;
                t = t.left;
            } else if (compareResult > 0) {    // 右边
                parent = t;
                t = t.right;
            } else { // 插入的元素已存在
                return t;
            }
        }
        return t;
    }

    private BinaryNode<E> remove(E e, BinaryNode<E> t) {
        // 删除时，找到该节点的左子树中最小的节点，替代它左该处节点，然后该子树中其他节点重新排序

        // 先递归试下
        if (t == null) {
            return null;
        }

        int comResult = myCompare(e, t.element);
        if (comResult < 0) {
            t.left = remove(e, t.left);
        } else if (comResult > 0) {
            t.right = remove(e, t.right);
        } else if (t.left != null && t.right != null) {
            t.element = findExtremum(t.right, 0).element;
            t.right = remove(t.element, t.right);
        } else {
            t = (t.left != null) ? t.left : t.right;
        }
        return t;
    }

    private BinaryNode<E> printTree(BinaryNode<E> t) {
        return null;
    }

    private static class BinaryNode<E> {
        E element;      // the data in the node
        BinaryNode<E> left;     // left child
        BinaryNode<E> right;    // right child

        // Constructors
        BinaryNode(E theElement) {
            this(theElement, null, null);
        }

        BinaryNode(E element, BinaryNode<E> left, BinaryNode<E> right) {
            this.element = element;
            this.left = left;
            this.right = right;
        }
    }

}


