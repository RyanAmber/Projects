package AOA.BinarySearchTree;
import java.util.*;
/**
* A generic Binary Search Tree implementation.
* Elements must implement Comparable for ordering.
*
* -Ryan Diehl
*
* @param <E> the type of elements stored in the tree
*/
public class BinarySearchTree<E extends Comparable<E>> implements
Iterable<E> {
    private TreeNode<E> root;
    private int size;
/**
* Constructs an empty Binary Search Tree.
*/
    public BinarySearchTree() {
        root = null;
        size = 0;
    }
/**
* Inserts a new element into the tree.
* Duplicates are ignored.
*
* @param data the element to insert
*/
public void insert(E data) {
    root = insertHelper(root, data);
    size++;
}
/**
* Recursive helper method for insertion.
*
* @param node the current node
* @param data the element to insert
* @return the node after insertion
*/
private TreeNode<E> insertHelper(TreeNode<E> node, E data) {
    if(node==null){
        return new TreeNode<E>(data);
    }else{
        if(node.data.compareTo(data)<0){
            node.left=insertHelper(node.left,data);
        }else{
            node.right=insertHelper(node.right,data);
        }
    }
return node;
}
/**
* Checks if the tree contains the specified element.
*
* @param data the element to search for
* @return true if the element is found, false otherwise
*/
public boolean contains(E data) {
    return containsHelper(root,data);
}
/**
* Recursive helper method for searching.
*
* @param node the current node
* @param data the element to search for
* @return true if found, false otherwise
*/
private boolean containsHelper(TreeNode<E> node, E data) {
    if (node == null) {
        return false;
        }
    int cmp = data.compareTo(node.data);
    if (cmp < 0) {
        return containsHelper(node.left, data);
    } else if (cmp > 0) {
        return containsHelper(node.right, data);
    } else {
        return true;
    }
}
/**
* Removes the specified element from the tree.
*
* @param data the element to remove
* @return true if the element was found and removed, false
otherwise
*/
public boolean remove(E data) {
    if(!contains(data))
    return false;
    root = removeHelper(root, data);
    size--;
return true;
}
/**
* Recursive helper method for removal.
*
* @param node the current node
* @param data the element to remove
* @return the node after removal
*/
private TreeNode<E> removeHelper(TreeNode<E> node, E data) {
if(node==null){
    return node;
}
int cmp = data.compareTo(node.data);
if (cmp < 0) {
    node.left = removeHelper(node.left, data);
} else if (cmp > 0) {
    node.right = removeHelper(node.right, data);
} else {
    if(node.left==null&&node.right==null){
        return null;
    }else if(node.left==null){ 
        return node.right;
    }else if(node.right==null){
        return node.left;
    }else{
        TreeNode<E> successor=findMinNode(node.right);
        node.data=successor.data;
        node.right=removeHelper(node.right,successor.data);
    }
}
return node;
// // Case 1: No children (leaf node)
// // Return null
//
// // Case 2: One child
// // Return the non-null child
//
// // Case 3: Two children
// // Find inorder successor (min of right subtree)
// // Replace node's data with successor's data
// // Remove the successor from right subtree
// }
}
/**
* Helper method to find minimum node in a subtree.
* Useful for remove operation.
*
* @param node the root of the subtree
* @return the node with minimum value
*/
private TreeNode<E> findMinNode(TreeNode<E> node) {
    if(node.left!=null){
        return findMinNode(node.left);
    }
    return node;
}
/**
* Finds and returns the minimum element in the tree.
*
* @return the minimum element
* @throws NoSuchElementException if the tree is empty
*/
public E findMin() {
    if(root==null)
throw new NoSuchElementException("Tree is empty");
return findMinNode(root).data;
}
/**
* Finds and returns the maximum element in the tree.
*
* @return the maximum element
* @throws NoSuchElementException if the tree is empty
*/
public E findMax() {
    if(root==null)
    throw new NoSuchElementException("Tree is empty");
    else{
        TreeNode<E> temp=root;
        while(temp.right!=null){
            temp=temp.right;
        }
        return temp.data;
    }   
}
/**
* Returns the number of elements in the tree.
*
* @return the size of the tree
*/
public int size() {
return size;
}
/**
* Checks if the tree is empty.
*
* @return true if the tree has no elements, false otherwise
*/
public boolean isEmpty() {
return size == 0;
}
/**
* Returns a list of elements in preorder traversal.
* Preorder: root, left, right
*
* @return list of elements in preorder
*/
public List<E> preorderTraversal() {
List<E> result = new ArrayList<>();
if(root==null){
    return result;
}
preorderHelper(root,result);
return result;
}
/**
* Recursive helper for preorder traversal.
*/
private void preorderHelper(TreeNode<E> node, List<E> result)
{
    result.add(node.data);
    if(node.left!=null){
        preorderHelper(node.left,result);
    }
    if(node.right!=null){
        preorderHelper(node.right,result);
    }
}
/**
* Returns a list of elements in inorder traversal.
* Inorder: left, root, right (produces sorted order for BST)
*
* @return list of elements in inorder
*/
public List<E> inorderTraversal() {
List<E> result = new ArrayList<>();
if(root==null){
    return result;
}
inorderHelper(root,result);
return result;
}
/**
* Recursive helper for inorder traversal.
*/
private void inorderHelper(TreeNode<E> node, List<E> result) {
    if(node.left!=null){
        inorderHelper(node.left,result);
    }
    result.add(node.data);
    if(node.right!=null){
        inorderHelper(node.right,result);
    }
}
/**
* Returns a list of elements in postorder traversal.
* Postorder: left, right, root
*
* @return list of elements in postorder
*/
public List<E> postorderTraversal() {
    List<E> result = new ArrayList<>();
    if(root==null){
       return result;
    }
    postorderHelper(root,result);
    return result;
}
/**
* Recursive helper for postorder traversal.
*/
private void postorderHelper(TreeNode<E> node, List<E> result)
{
    if(node.left!=null){
        postorderHelper(node.left,result);
    }
    if(node.right!=null){
        postorderHelper(node.right,result);
    }
    result.add(node.data);
}
/**
* Returns an iterator over elements in the tree (inorder).
*
* @return an iterator
*/
@Override
public Iterator<E> iterator() {
return new BSTIterator();
}
/**
* Inner class that implements Iterator for the Binary Search
Tree.
* Iterates through elements in sorted (inorder) order.
*/
private class BSTIterator implements Iterator<E> {
private Stack<TreeNode<E>> stack;
/**
* Constructs an iterator starting at the minimum element.
*/
public BSTIterator() {
    stack = new Stack<>();
    pushLeft(root);
}
/**
* Helper method to push all left children onto the stack.
*/
private void pushLeft(TreeNode<E> node) {
    if(node.left!=null)
    pushLeft(node.left);
    stack.push(node);
    if(node.right!=null)
    pushLeft(node.right);
}
/**
* Checks if there are more elements to iterate.
*
* @return true if more elements exist
*/
@Override
public boolean hasNext() {
return !stack.empty();
}
/**
* Returns the next element in the iteration.
*
* @return the next element
* @throws NoSuchElementException if no more elements
exist
*/
@Override
public E next() {
    if(hasNext()){
E value=stack.pop().data;
return value;
    }
    return null;
}
}
/**
* Optional: Helper method to print the tree structure.
* Useful for debugging!
*/
public void printTree() {
    printTreeHelper(root, "", true);
}
private void printTreeHelper(TreeNode<E> node, String prefix, boolean isTail) {
    if (node == null) return;
    System.out.println(prefix + (isTail ? "": " ") + node.data);
    if (node.left != null || node.right != null) {
        if (node.left != null) {
            printTreeHelper(node.left, prefix + (isTail ? "" : " "), true);
        }
    }
}
}
/**
* Represents a node in the Binary Search Tree.
*
* @param <E> the type of data stored in the node
*/
class TreeNode<E> {
E data;
TreeNode<E> left;
TreeNode<E> right;
/**
* Constructs a new TreeNode with the given data.
*
* @param data the data to store
*/
public TreeNode(E data) {
    this.data=data;
    this.left=null;
    this.right=null;
}
/**
* Constructs a new TreeNode with data and children.
*
* @param data the data to store
* @param left the left child
* @param right the right child
*/
public TreeNode(E data, TreeNode<E> left, TreeNode<E> right) {
    this.data=data;
    this.left=left;
    this.right=right;
}
}
