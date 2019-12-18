/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tum.cup2.util;

/* 
 * JGraphT : a free Java graph-theory library
 * 
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Creator:  Barak Naveh (barak_naveh@users.sourceforge.net)
 *
 * (C) Copyright 2003-2007, by Barak Naveh and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */
/* --------------------------
 * FibonnaciHeap.java
 * --------------------------
 * (C) Copyright 1999-2003, by Nathan Fiedler and Contributors.
 *
 * Original Author:  Nathan Fiedler
 * Contributor(s):   John V. Sichi
 *
 * $Id: FibonacciHeap.java 603 2008-06-28 07:51:50Z perfecthash $
 *
 * Changes
 * -------
 * 03-Sept-2003 : Adapted from Nathan Fiedler (JVS);
 *
 *      Name    Date            Description
 *      ----    ----            -----------
 *      nf      08/31/97        Initial version
 *      nf      09/07/97        Removed FibHeapData interface
 *      nf      01/20/01        Added synchronization
 *      nf      01/21/01        Made Node an inner class
 *      nf      01/05/02        Added clear(), renamed empty() to
 *                              isEmpty(), and renamed printHeap()
 *                              to toString()
 *      nf      01/06/02        Removed all synchronization
 *
 */

import java.util.*;


/**
 * This class implements a Fibonacci heap data structure. Much of the code in
 * this class is based on the algorithms in the "Introduction to Algorithms"by
 * Cormen, Leiserson, and Rivest in Chapter 21. The amortized running time of
 * most of these methods is O(1), making it a very fast data structure. Several
 * have an actual running time of O(1). removeMin() and delete() have O(log n)
 * amortized running times because they do the heap consolidation. If you
 * attempt to store nodes in this heap with key values of -Infinity
 * (Double.NEGATIVE_INFINITY) the <code>delete()</code> operation may fail to
 * remove the correct element.
 *
 * <p><b>Note that this implementation is not synchronized.</b> If multiple
 * threads access a set concurrently, and at least one of the threads modifies
 * the set, it <i>must</i> be synchronized externally. This is typically
 * accomplished by synchronizing on some object that naturally encapsulates the
 * set.</p>
 *
 * <p>This class was originally developed by Nathan Fiedler for the GraphMaker
 * project. It was imported to JGraphT with permission, courtesy of Nathan
 * Fiedler.</p>
 *
 * @author Nathan Fiedler
 */
public class FibonacciHeap<T>
{
    //~ Static fields/initializers ---------------------------------------------

    private static final double oneOverLogPhi =
        1.0 / Math.log((1.0 + Math.sqrt(5.0)) / 2.0);

    //~ Instance fields --------------------------------------------------------

    /**
     * Points to the minimum node in the heap.
     */
    private FibonacciHeapNode<T> minNode;

    /**
     * Number of nodes in the heap.
     */
    private int nNodes;

    //~ Constructors -----------------------------------------------------------

    /**
     * Constructs a FibonacciHeap object that contains no elements.
     */
    public FibonacciHeap()
    {
    } // FibonacciHeap

    //~ Methods ----------------------------------------------------------------

    /**
     * Tests if the Fibonacci heap is empty or not. Returns true if the heap is
     * empty, false otherwise.
     *
     * <p>Running time: O(1) actual</p>
     *
     * @return true if the heap is empty, false otherwise
     */
    public boolean isEmpty()
    {
        return minNode == null;
    }

    // isEmpty

    /**
     * Removes all elements from this heap.
     */
    public void clear()
    {
        minNode = null;
        nNodes = 0;
    }

    
    /**
     * Decreases the key value for a heap node, given the new value to take on.
     * The structure of the heap may be changed and will not be consolidated.
     *
     * <p>Running time: O(1) amortized</p>
     *
     * @param x node to decrease the key of
     * @param k new key value for node x
     *
     * @exception IllegalArgumentException Thrown if k is larger than x.key
     * value.
     */
    private void decreaseKey(FibonacciHeapNode<T> x, int k)
    {
        if (k > x.key) {
            throw new IllegalArgumentException(
                "decreaseKey() got larger key value");
        }

        x.key = k;

        FibonacciHeapNode<T> y = x.parent;

        if ((y != null) && (x.key < y.key)) {
            cut(x, y);
            cascadingCut(y);
        }

        if (x.key < minNode.key) {
            minNode = x;
        }
    }

    // decreaseKey

    private Map<T,FibonacciHeapNode<T>> toNode = new HashMap<T,FibonacciHeapNode<T>>();
    public FibonacciHeapNode<T> insert (T node, int key){
        FibonacciHeapNode<T> hn = new FibonacciHeapNode<T>(node,key);
        insert(hn,key);
        toNode.put(node, hn);
        return hn;
    }
      // clear

    public void decrementKey(T node){
        FibonacciHeapNode<T> hn = toNode.get(node);
        decreaseKey(hn, hn.key-1);
    }
  
    /**
     * Inserts a new data element into the heap. No heap consolidation is
     * performed at this time, the new node is simply inserted into the root
     * list of this heap.
     *
     * <p>Running time: O(1) actual</p>
     *
     * @param node new node to insert into heap
     * @param key key value associated with data object
     */
    private void insert(FibonacciHeapNode<T> node, int key)
    {
        node.key = key;

        // concatenate node into min list
        if (minNode != null) {
            node.left = minNode;
            node.right = minNode.right;
            minNode.right = node;
            node.right.left = node;

            if (key < minNode.key) {
                minNode = node;
            }
        } else {
            minNode = node;
        }

        nNodes++;
    }

    // insert

    /**
     * Returns the smallest element in the heap. This smallest element is the
     * one with the minimum key value.
     *
     * <p>Running time: O(1) actual</p>
     *
     * @return heap node with the smallest key
     */
    private FibonacciHeapNode<T> min()
    {
        return minNode;
    }

    public T extractMin(){
        FibonacciHeapNode<T> hn = removeMin();
        toNode.remove(hn.getData());
        return hn.getData();
    }
    
    // min

    /**
     * Removes the smallest element from the heap. This will cause the trees in
     * the heap to be consolidated, if necessary.
     *
     * <p>Running time: O(log n) amortized</p>
     *
     * @return node with the smallest key
     */
    private FibonacciHeapNode<T> removeMin()
    {
        FibonacciHeapNode<T> z = minNode;

        if (z != null) {
            int numKids = z.degree;
            FibonacciHeapNode<T> x = z.child;
            FibonacciHeapNode<T> tempRight;

            // for each child of z do...
            while (numKids > 0) {
                tempRight = x.right;

                // remove x from child list
                x.left.right = x.right;
                x.right.left = x.left;

                // add x to root list of heap
                x.left = minNode;
                x.right = minNode.right;
                minNode.right = x;
                x.right.left = x;

                // set parent[x] to null
                x.parent = null;
                x = tempRight;
                numKids--;
            }

            // remove z from root list of heap
            z.left.right = z.right;
            z.right.left = z.left;

            if (z == z.right) {
                minNode = null;
            } else {
                minNode = z.right;
                consolidate();
            }

            // decrement size of heap
            nNodes--;
        }

        return z;
    }

    // removeMin

    /**
     * Returns the size of the heap which is measured in the number of elements
     * contained in the heap.
     *
     * <p>Running time: O(1) actual</p>
     *
     * @return number of elements in the heap
     */
    public int size()
    {
        return nNodes;
    }

    // size

    /**
     * Creates a String representation of this Fibonacci heap.
     *
     * @return String of this.
     */
    @Override
    public String toString()
    {
        if (minNode == null) {
            return "FibonacciHeap=[]";
        }

        // create a new stack and put root on it
        Stack<FibonacciHeapNode<T>> stack = new Stack<FibonacciHeapNode<T>>();
        stack.push(minNode);

        StringBuffer buf = new StringBuffer(512);
        buf.append("FibonacciHeap=[");

        // do a simple breadth-first traversal on the tree
        while (!stack.empty()) {
            FibonacciHeapNode<T> curr = stack.pop();
            buf.append(curr);
            buf.append(", ");

            if (curr.child != null) {
                stack.push(curr.child);
            }

            FibonacciHeapNode<T> start = curr;
            curr = curr.right;

            while (curr != start) {
                buf.append(curr);
                buf.append(", ");

                if (curr.child != null) {
                    stack.push(curr.child);
                }

                curr = curr.right;
            }
        }

        buf.append(']');

        return buf.toString();
    }

    // toString

    /**
     * Performs a cascading cut operation. This cuts y from its parent and then
     * does the same for its parent, and so on up the tree.
     *
     * <p>Running time: O(log n); O(1) excluding the recursion</p>
     *
     * @param y node to perform cascading cut on
     */
    private void cascadingCut(FibonacciHeapNode<T> y)
    {
        FibonacciHeapNode<T> z = y.parent;

        // if there's a parent...
        if (z != null) {
            // if y is unmarked, set it marked
            if (!y.mark) {
                y.mark = true;
            } else {
                // it's marked, cut it from parent
                cut(y, z);

                // cut its parent as well
                cascadingCut(z);
            }
        }
    }

    // cascadingCut

    private void consolidate()
    {
        int arraySize =
            ((int) Math.floor(Math.log(nNodes) * oneOverLogPhi)) + 1;

        List<FibonacciHeapNode<T>> array =
            new ArrayList<FibonacciHeapNode<T>>(arraySize);

        // Initialize degree array
        for (int i = 0; i < arraySize; i++) {
            array.add(null);
        }

        // Find the number of root nodes.
        int numRoots = 0;
        FibonacciHeapNode<T> x = minNode;

        if (x != null) {
            numRoots++;
            x = x.right;

            while (x != minNode) {
                numRoots++;
                x = x.right;
            }
        }

        // For each node in root list do...
        while (numRoots > 0) {
            // Access this node's degree..
            int d = x.degree;
            FibonacciHeapNode<T> next = x.right;

            // ..and see if there's another of the same degree.
            for (;;) {
                FibonacciHeapNode<T> y = array.get(d);
                if (y == null) {
                    // Nope.
                    break;
                }

                // There is, make one of the nodes a child of the other.
                // Do this based on the key value.
                if (x.key > y.key) {
                    FibonacciHeapNode<T> temp = y;
                    y = x;
                    x = temp;
                }

                // FibonacciHeapNode<T> y disappears from root list.
                link(y, x);

                // We've handled this degree, go to next one.
                array.set(d, null);
                d++;
            }

            // Save this node for later when we might encounter another
            // of the same degree.
            array.set(d, x);

            // Move forward through list.
            x = next;
            numRoots--;
        }

        // Set min to null (effectively losing the root list) and
        // reconstruct the root list from the array entries in array[].
        minNode = null;

        
        for (FibonacciHeapNode<T> y :array){
            if (y == null) {
                continue;
            }

            // We've got a live one, add it to root list.
            if (minNode != null) {
                // First remove node from root list.
                y.left.right = y.right;
                y.right.left = y.left;

                // Now add to root list, again.
                y.left = minNode;
                y.right = minNode.right;
                minNode.right = y;
                y.right.left = y;

                // Check if this is a new min.
                if (y.key < minNode.key) {
                    minNode = y;
                }
            } else {
                minNode = y;
            }
        }
    }

    // consolidate

    /**
     * The reverse of the link operation: removes x from the child list of y.
     * This method assumes that min is non-null.
     *
     * <p>Running time: O(1)</p>
     *
     * @param x child of y to be removed from y's child list
     * @param y parent of x about to lose a child
     */
    private void cut(FibonacciHeapNode<T> x, FibonacciHeapNode<T> y)
    {
        // remove x from childlist of y and decrement degree[y]
        x.left.right = x.right;
        x.right.left = x.left;
        y.degree--;

        // reset y.child if necessary
        if (y.child == x) {
            y.child = x.right;
        }

        if (y.degree == 0) {
            y.child = null;
        }

        // add x to root list of heap
        x.left = minNode;
        x.right = minNode.right;
        minNode.right = x;
        x.right.left = x;

        // set parent[x] to nil
        x.parent = null;

        // set mark[x] to false
        x.mark = false;
    }

    // cut

    /**
     * Make node y a child of node x.
     *
     * <p>Running time: O(1) actual</p>
     *
     * @param y node to become child
     * @param x node to become parent
     */
    protected void link(FibonacciHeapNode<T> y, FibonacciHeapNode<T> x)
    {
        // remove y from root list of heap
        y.left.right = y.right;
        y.right.left = y.left;

        // make y a child of x
        y.parent = x;

        if (x.child == null) {
            x.child = y;
            y.right = y;
            y.left = y;
        } else {
            y.left = x.child;
            y.right = x.child.right;
            x.child.right = y;
            y.right.left = y;
        }

        // increase degree[x]
        x.degree++;

        // set mark[y] false
        y.mark = false;
    }

    // link
}

// FibonacciHeap
/* 
 * JGraphT : a free Java graph-theory library
 * 
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Creator:  Barak Naveh (barak_naveh@users.sourceforge.net)
 *
 * (C) Copyright 2003-2007, by Barak Naveh and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */
/* --------------------------
 * FibonnaciHeapNode.java
 * --------------------------
 * (C) Copyright 1999-2007, by Nathan Fiedler and Contributors.
 *
 * Original Author:  Nathan Fiedler
 * Contributor(s):   John V. Sichi
 *
 * $Id: FibonacciHeapNode.java 568 2007-09-30 00:12:18Z perfecthash $
 *
 * Changes
 * -------
 * 03-Sept-2003 : Adapted from Nathan Fiedler (JVS);
 *
 *      Name    Date            Description
 *      ----    ----            -----------
 *      nf      08/31/97        Initial version
 *      nf      09/07/97        Removed FibHeapData interface
 *      nf      01/20/01        Added synchronization
 *      nf      01/21/01        Made Node an inner class
 *      nf      01/05/02        Added clear(), renamed empty() to
 *                              isEmpty(), and renamed printHeap()
 *                              to toString()
 *      nf      01/06/02        Removed all synchronization
 *      JVS     06/24/06        Generics
 *
 */

/**
 * Implements a node of the Fibonacci heap. It holds the information necessary
 * for maintaining the structure of the heap. It also holds the reference to the
 * key value (which is used to determine the heap structure).
 *
 * @author Nathan Fiedler
 */
class FibonacciHeapNode<T>
{
    //~ Instance fields --------------------------------------------------------

    /**
     * Node data.
     */
    T data;

    /**
     * first child node
     */
    FibonacciHeapNode<T> child;

    /**
     * left sibling node
     */
    FibonacciHeapNode<T> left;

    /**
     * parent node
     */
    FibonacciHeapNode<T> parent;

    /**
     * right sibling node
     */
    FibonacciHeapNode<T> right;

    /**
     * true if this node has had a child removed since this node was added to
     * its parent
     */
    boolean mark;

    /**
     * key value for this node
     */
    int key;

    /**
     * number of children of this node (does not count grandchildren)
     */
    int degree;

    //~ Constructors -----------------------------------------------------------

    /**
     * Default constructor. Initializes the right and left pointers, making this
     * a circular doubly-linked list.
     *
     * @param data data for this node
     * @param key initial key for node
     */
    public FibonacciHeapNode(T data, int key)
    {
        right = this;
        left = this;
        this.data = data;
        this.key = key;
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    
    
    //~ Methods ----------------------------------------------------------------

    /**
     * Obtain the key for this node.
     *
     * @return the key
     */
    public final int getKey()
    {
        return key;
    }

    /**
     * Obtain the data for this node.
     */
    public final T getData()
    {
        return data;
    }

    /**
     * Return the string representation of this object.
     *
     * @return string representing this object
     */
    @Override
    public String toString()
    {
        if (true) {
            return Integer.toString(key);
        } else {
            StringBuffer buf = new StringBuffer();
            buf.append("Node=[parent = ");

            if (parent != null) {
                buf.append(Integer.toString(parent.key));
            } else {
                buf.append("---");
            }

            buf.append(", key = ");
            buf.append(Integer.toString(key));
            buf.append(", degree = ");
            buf.append(Integer.toString(degree));
            buf.append(", right = ");

            if (right != null) {
                buf.append(Integer.toString(right.key));
            } else {
                buf.append("---");
            }

            buf.append(", left = ");

            if (left != null) {
                buf.append(Integer.toString(left.key));
            } else {
                buf.append("---");
            }

            buf.append(", child = ");

            if (child != null) {
                buf.append(Integer.toString(child.key));
            } else {
                buf.append("---");
            }

            buf.append(']');

            return buf.toString();
        }
    }

    // toString
}

// End FibonacciHeapNode.java
