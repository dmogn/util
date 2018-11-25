/* 
 * OldPortal Utilites Library is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
 */
package com.github.dmogn.util;

import java.util.Vector;

/**
 *
 * @author Dmitry Ognyannikov
 */
public class TreeNode {

    private TreeNode parentNode = null;

    private Vector<TreeNode> childNodes = new Vector<TreeNode>();

    // constructors:
    /**
     * Creates a new instance of TreeNode
     */
    public TreeNode() {
    }

    public TreeNode(TreeNode _parentNode) {
        parentNode = _parentNode;
    }

    // methods:
    public void add(TreeNode node) {
        if (node.parentNode != null) {
            node.parentNode.remove(node);
        }

        node.parentNode = this;
        childNodes.add(node);
    }

    public void remove(TreeNode node) {
        if (node.parentNode != this) {
            if (node.parentNode != null) {
                node.parentNode.remove(node);
            }

            return;
        }
        node.parentNode = null;
        childNodes.remove(node);
    }

    public TreeNode getParentNode() {
        return parentNode;
    }

    public TreeNode[] getChilds() {
        TreeNode childs[] = new TreeNode[childNodes.size()];
        for (int i = 0; i < childNodes.size(); i++) {
            childs[i] = childNodes.get(i);
        }
        return childs;
    }

    public int getChildsCount() {
        return childNodes.size();
    }

    public TreeNode getChildByIndex(int index) {
        return childNodes.get(index);
    }

}
