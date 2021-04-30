package com.yu.algorithm.tree;

import lombok.val;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

/**
 * @Description: 树表
 * @Author: Yan XinYu
 * @Date: 2021-04-28 10:46
 */

public class TreeList {
    /**
     * java 例子
     */
    private TreeMap treeMap = new TreeMap();

    private TreeNode root;
    private Integer size;

    /**
     * 添加一个节点
     * @param val 值
     */
    public void add(Integer val){
        TreeNode t = root;
        if(root == null){
            root = new TreeNode(val);
            size ++;
            return ;
        }
        TreeNode parent;
        int comparator;
        do{
            parent = t;
            if(val > parent.val){
                t = parent.right;
                comparator = 1;
            } else if (val < parent.val ){
                t = parent.left;
                comparator = -1;
            } else {
                parent.val = val;
                return ;
            }

        } while ( t != null);

        TreeNode treeNode = new TreeNode(val);
        if(comparator == 1){
            parent.right = treeNode;
        } else {
            parent.left = treeNode;
        }
        // 红黑树在这里处理。
        size++;
    }

    /**
     * 移除一个节点
     * @param val 值
     */
    public void remove(Integer val){
        // 找到这个节点
        TreeNode node = getNode(val);
        // if is null 返回 null
        if(node == null){
            return ;
        }
        // 删除这个节点
        deleteNode(node);
    }

    /**
     * 获取一个节点
     * @param val 值
     * @return 节点
     */
    public TreeNode getNode(Integer val){
        TreeNode t = root;
        while (t != null){
            int comparator = Integer.compare(t.val,val);
            if(comparator > 0){
                t = t.right;
            } else if(comparator < 0){
                t = t.left;
            } else {
                return t;
            }
        }
        return null;
    }

    /**
     * 删除一个节点
     * 首先先找到左/右最近的节点，
     * @param node 被删除的节点
     */
    public void deleteNode(TreeNode node){

        if(node.left != null && node.right != null){

        }

    }

    /**
     * 获取列表
     * @return 所有元素值
     */
    public List<Integer> getList(){
        List<Integer> list = new LinkedList<>();
        preOrder(list,root);
        return list;
    }

    /**
     * 前序遍历,根-左-右
     * @param list 数组
     * @param treeNode 节点
     */
    public void preOrder(List<Integer> list,TreeNode treeNode){
        if(treeNode == null){
            return ;
        }
        // 添加元素
        list.add(treeNode.val);
        preOrder(list,treeNode.left);
        preOrder(list,treeNode.right);
    }

    /**
     * 中序遍历 ，左-根-右
     * @param list 数组
     * @param treeNode 节点
     */
    public void inOrder(List<Integer> list,TreeNode treeNode){
        if(treeNode == null){
            return ;
        }
        inOrder(list,treeNode.left);
        list.add(treeNode.val);
        inOrder(list,treeNode.right);
    }

    /**
     * 后序遍历 ，左-根-右
     * @param list 数组
     * @param treeNode 节点
     */
    public void afterOrder(List<Integer> list,TreeNode treeNode){
        if(treeNode == null){
            return ;
        }
        afterOrder(list,treeNode.left);
        afterOrder(list,treeNode.right);
        list.add(treeNode.val);
    }

}
