/*
* Utilites Library.
* Copyright (C) Dmitry Ognyannikov, 2005
* E-Mail: sirius_plus@yahoo.com , dmitry@oldportal.com
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details.

* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/


package com.oldportal.util;

import java.util.Vector;

/**
 *
 * @author Dmitry Ognyannikov
 */
public class TreeNode {
    // constructors:
    /** Creates a new instance of TreeNode */
    public TreeNode() {
    }

    public TreeNode(TreeNode _parentNode) {
        parentNode = _parentNode;
    }

    // members:
    private TreeNode parentNode = null;

    private Vector<TreeNode> childNodes = new Vector<TreeNode>();

    // methods:
    public void add(TreeNode node)
    {
        if (node.parentNode != null)
            node.parentNode.remove(node);

        node.parentNode = this;
        childNodes.add(node);
    }

    public void remove(TreeNode node)
    {
        if (node.parentNode != this)
        {
            if (node.parentNode != null)
                node.parentNode.remove(node);

            return;
        }
        node.parentNode = null;
        childNodes.remove(node);
    }

    public TreeNode getParentNode()
    {
        return parentNode;
    }

    public TreeNode[] getChilds()
    {
         TreeNode childs[] = new TreeNode[childNodes.size()];
         for (int i=0; i<childNodes.size(); i++)
         {
             childs[i] = childNodes.get(i);
         }
         return childs;
    }

    public int getChildsCount()
    {
        return childNodes.size();
    }

    public TreeNode getChildByIndex(int index)
    {
        return childNodes.get(index);
    }

}