package com.java.towTree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * 二叉树的前序遍历的递归调用 --- 根节点 -- 左孩子--右孩子
 * 该方法很简单，根据需要遍历节点的顺序，递归的将遍历到的节点值放入list中。
 * 即：每次递归都是先放头结点，再遍历左子树，最后是右子树。
 * 递归调用简单，当节点过多时，其效率就会很差，而且比较耗费空间
 */
public class BinaryTree {
    char data;                  //根节点
    BinaryTree leftChild;       //左孩子
    BinaryTree rightChild;      //右孩子

    public BinaryTree() {

    }

    public void visit() {
        System.out.println(this.data);
    }

    public BinaryTree(char data) {
        this.data = data;
        this.leftChild = null;
        this.rightChild = null;
    }

    public BinaryTree getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(BinaryTree leftChild) {
        this.leftChild = leftChild;
    }

    public BinaryTree getRightChild() {
        return rightChild;
    }

    public void setRightChild(BinaryTree rightChild) {
        this.rightChild = rightChild;
    }

    public char getData() {
        return data;
    }

    public void setData(char data) {
        this.data = data;
    }
}
