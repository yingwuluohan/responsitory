package com.java.towTree;

import java.util.Stack;

/**
 * Created by yingwuluohan on 2017/9/5.
 */
public class VisitBinaryTree {
    //中序遍历的非递归算法
    public void inOrder(BinaryTree root) {

        if(root!=null) {

            Stack<BinaryTree> stack = new Stack<BinaryTree>();

            for (BinaryTree node = root; !stack.empty() || node != null; ) {

                //寻找最左的左子树节点,并将遍历的左节点进栈
                while(node!=null) {
                    stack.push(node);
                    node = node.getLeftChild();
                }

                if(!stack.empty()) {
                    node = stack.pop();      //出栈
                    node.visit();            //读取节点值
                    node = node.getRightChild();
                }
            }
        }
    }

    //中序遍历的递归算法
    public void inOrderRecursion (BinaryTree root) {

        if(root!=null) {
            inOrderRecursion(root.getLeftChild());
            root.visit();
            inOrderRecursion(root.getRightChild());
        }

    }

    public static void main(String args[]) {

        BinaryTree node = new BinaryTree('A');
        BinaryTree root = node;
        BinaryTree nodeL1;
        BinaryTree nodeL;
        BinaryTree nodeR;
        node.setLeftChild(new BinaryTree('B'));
        node.setRightChild(new BinaryTree('C'));

        nodeL1 = node.getLeftChild();
        nodeL1.setLeftChild(new BinaryTree('D'));
        nodeL1.setRightChild(new BinaryTree('E'));

        nodeL = nodeL1.getLeftChild();
        nodeL.setLeftChild(new BinaryTree('F'));

        node = node.getRightChild();
        node.setLeftChild(new BinaryTree('G'));
        node.setRightChild(new BinaryTree('H'));

        nodeR = node.getLeftChild();
        nodeR.setLeftChild(new BinaryTree('I'));
        nodeR.setRightChild(new BinaryTree('J'));

        VisitBinaryTree vt= new VisitBinaryTree();

        //中序遍历递归和非递归测试
        vt.inOrder(root);
        vt.inOrderRecursion(root);

    }
}
