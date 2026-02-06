package AOA.BinarySearchTree;
import java.util.*;
/**
* Test class for Binary Search Tree and AVL Tree implementations.
* Students should add their own test cases as well!
*/
public class BSTTest {
public static void main(String[] args) {
System.out.println("=== Binary Search Tree Tests ===\n");
testBasicOperations();
testRemoval();
testTraversals();
testIterator();
testEdgeCases();
System.out.println("\n=== AVL Tree Tests (Bonus) ===\n");
//testAVLBalance();
//testAVLRotations();
System.out.println("\n=== All Tests Complete ===");
}
/**
* Test basic BST operations: insert, contains, size, min, max
*/
public static void testBasicOperations() {
System.out.println("Test 1: Basic Operations");
BinarySearchTree<Integer> bst = new BinarySearchTree<>();
// Test isEmpty on empty tree
assert bst.isEmpty() : "New tree should be empty";
assert bst.size() == 0 : "New tree should have size 0";
// Insert elements
int[] values = {50, 30, 70, 20, 40, 60, 80};
for (int val : values) {
bst.insert(val);
}
// Test size
assert bst.size() == 7 : "Tree should have 7 elements";
assert !bst.isEmpty() : "Tree should not be empty";
// Test contains
assert bst.contains(50) : "Tree should contain 50";
assert bst.contains(20) : "Tree should contain 20";
assert bst.contains(80) : "Tree should contain 80";
assert !bst.contains(100) : "Tree should not contain 100";
assert !bst.contains(25) : "Tree should not contain 25";
// Test min and max
assert bst.findMin() == 20 : "Minimum should be 20";
assert bst.findMax() == 80 : "Maximum should be 80";
System.out.println(" Basic operations test passed!\n");
}
/**
* Test remove operation for all three cases
*/
public static void testRemoval() {
System.out.println("Test 2: Remove Operation");
BinarySearchTree<Integer> bst = new BinarySearchTree<>();
int[] values = {50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 65};
for (int val : values) {
bst.insert(val);
}
int initialSize = bst.size();
System.out.println(" Initial tree size: " + initialSize);
// Test 1: Remove leaf node (no children)
System.out.println("\n Test 2a: Removing leaf node (10)");
boolean removed = bst.remove(10);
assert removed : "Should successfully remove leaf node";
assert !bst.contains(10) : "Tree should not contain 10 after removal";
assert bst.size() == initialSize - 1 : "Size should decrease by 1";
List<Integer> inorder = bst.inorderTraversal();
System.out.println(" After removal: " + inorder);
assert isSorted(inorder) : "Tree should still be sorted after removal";
// Test 2: Remove node with one child
System.out.println("\n Test 2b: Removing node with one child (20)");
// Node 20 now has only left child (25) after removing 10
removed = bst.remove(20);
assert removed : "Should successfully remove node with one child";
assert !bst.contains(20) : "Tree should not contain 20 after removal";
assert bst.contains(25) : "Child node should still be present";
assert bst.size() == initialSize - 2 : "Size should decrease by 2 total";
inorder = bst.inorderTraversal();
System.out.println(" After removal: " + inorder);
assert isSorted(inorder) : "Tree should still be sorted after removal";
// Test 3: Remove node with two children
System.out.println("\n Test 2c: Removing node with two children (30)");
removed = bst.remove(30);
assert removed : "Should successfully remove node with two children";
assert !bst.contains(30) : "Tree should not contain 30 after removal";
assert bst.contains(25) : "Left subtree should be intact";
assert bst.contains(35) : "Right subtree should be intact";
assert bst.contains(40) : "Right subtree should be intact";
inorder = bst.inorderTraversal();
System.out.println(" After removal: " + inorder);
assert isSorted(inorder) : "Tree should still be sorted after removal";
// Test 4: Remove root node with two children
System.out.println("\n Test 2d: Removing root node (50)");
removed = bst.remove(50);
assert removed : "Should successfully remove root";
assert !bst.contains(50) : "Tree should not contain 50 after removal";
inorder = bst.inorderTraversal();
System.out.println(" After removal: " + inorder);
assert isSorted(inorder) : "Tree should still be sorted after removal";
// Test 5: Remove non-existent element
System.out.println("\n Test 2e: Removing non-existent element (999)");
int sizeBefore = bst.size();
removed = bst.remove(999);
assert !removed : "Should return false for non-existent element";
assert bst.size() == sizeBefore : "Size should not change";
// Test 6: Remove all remaining elements
System.out.println("\n Test 2f: Removing all remaining elements");
List<Integer> remaining = new ArrayList<>(bst.inorderTraversal());
for (Integer val : remaining) {
removed = bst.remove(val);
assert removed : "Should successfully remove element " + val;
assert !bst.contains(val) : "Element " + val + " should be gone";
}
assert bst.isEmpty() : "Tree should be empty after removing all elements";
assert bst.size() == 0 : "Size should be 0";
System.out.println(" Remove operation test passed!\n");
}
/**
* Helper method to check if a list is sorted
*/
private static boolean isSorted(List<Integer> list) {
for (int i = 1; i < list.size(); i++) {
if (list.get(i) < list.get(i - 1)) {
return false;
}
}
return true;
}
/**
* Test all three traversal methods
*/
public static void testTraversals() {
System.out.println("Test 3: Traversals");
BinarySearchTree<Integer> bst = new BinarySearchTree<>();
int[] values = {50, 30, 70, 20, 40, 60, 80};
for (int val : values) {
bst.insert(val);
}
// Test inorder traversal (should be sorted)
List<Integer> inorder = bst.inorderTraversal();
List<Integer> expectedInorder = Arrays.asList(20, 30, 40, 50, 60, 70, 80);
assert inorder.equals(expectedInorder) :
"Inorder traversal incorrect. Expected: " + expectedInorder +
", Got: " + inorder;
// Test preorder traversal
List<Integer> preorder = bst.preorderTraversal();
List<Integer> expectedPreorder = Arrays.asList(50, 30, 20, 40, 70, 60, 80);
assert preorder.equals(expectedPreorder) :
"Preorder traversal incorrect. Expected: " + expectedPreorder +
", Got: " + preorder;
// Test postorder traversal
List<Integer> postorder = bst.postorderTraversal();
List<Integer> expectedPostorder = Arrays.asList(20, 40, 30, 60, 80, 70,
50);
assert postorder.equals(expectedPostorder) :
"Postorder traversal incorrect. Expected: " + expectedPostorder +
", Got: " + postorder;
System.out.println(" Traversal test passed!");
System.out.println(" Inorder: " + inorder);
System.out.println(" Preorder: " + preorder);
System.out.println(" Postorder: " + postorder + "\n");
}
/**
* Test the Iterator implementation
*/
public static void testIterator() {
System.out.println("Test 4: Iterator");
BinarySearchTree<Integer> bst = new BinarySearchTree<>();
int[] values = {50, 30, 70, 20, 40, 60, 80};
for (int val : values) {
bst.insert(val);
}
// Test enhanced for-loop (uses iterator)
List<Integer> iteratedValues = new ArrayList<>();
for (Integer val : bst) {
iteratedValues.add(val);
}
List<Integer> expectedValues = Arrays.asList(20, 30, 40, 50, 60, 70, 80);
assert iteratedValues.equals(expectedValues) :
"Iterator should produce sorted order. Expected: " + expectedValues +
", Got: " + iteratedValues;
// Test hasNext and next directly
Iterator<Integer> iter = bst.iterator();
int count = 0;
while (iter.hasNext()) {
Integer val = iter.next();
assert val.equals(expectedValues.get(count)) :
"Iterator value mismatch at position " + count;
count++;
}
assert count == 7 : "Iterator should produce 7 values";
// Test that calling next() after exhaustion throws exception
try {
iter.next();
assert false : "Should throw NoSuchElementException";
} catch (NoSuchElementException e) {
// Expected
}
System.out.println(" Iterator test passed!");
System.out.println(" Iterated values: " + iteratedValues + "\n");
}
/**
* Test edge cases: empty tree, single node, duplicates
*/
public static void testEdgeCases() {
System.out.println("Test 5: Edge Cases");
// Test 1: Empty tree
BinarySearchTree<Integer> emptyTree = new BinarySearchTree<>();
assert emptyTree.isEmpty() : "Empty tree check failed";
assert emptyTree.size() == 0 : "Empty tree size should be 0";
try {
emptyTree.findMin();
assert false : "Should throw exception on empty tree";
} catch (NoSuchElementException e) {
// Expected
}
try {
emptyTree.findMax();
assert false : "Should throw exception on empty tree";
} catch (NoSuchElementException e) {
// Expected
}
// Test 2: Single node tree
BinarySearchTree<String> singleNode = new BinarySearchTree<>();
singleNode.insert("Hello");
assert singleNode.size() == 1 : "Single node tree size should be 1";
assert singleNode.contains("Hello") : "Should contain the single element";
assert singleNode.findMin().equals("Hello") : "Min should be the only element";
assert singleNode.findMax().equals("Hello") : "Max should be the only element";
// Test 3: Duplicate insertion
BinarySearchTree<Integer> bst = new BinarySearchTree<>();
bst.insert(50);
bst.insert(30);
bst.insert(50); // Duplicate
bst.insert(30); // Duplicate
assert bst.size() == 2 : "Duplicates should be ignored, size should be 2";
// Test 4: String tree (test with different type)
BinarySearchTree<String> stringTree = new BinarySearchTree<>();
String[] words = {"dog", "cat", "elephant", "ant", "zebra"};
for (String word : words) {
stringTree.insert(word);
}
List<String> sorted = stringTree.inorderTraversal();
List<String> expectedSorted = Arrays.asList("ant", "cat", "dog",
"elephant", "zebra");
assert sorted.equals(expectedSorted) :
"String tree should be sorted alphabetically";
System.out.println(" Edge cases test passed!\n");
}
/**
* Test AVL tree balancing property
*/
//public static void testAVLBalance() {
//System.out.println("Test 6: AVL Balance (Bonus)");
// Create an AVL tree and insert values that would create
// an unbalanced BST if no rotations occurred
//AVLTree<Integer> avl = new AVLTree<>();
// Insert in ascending order (would create right-skewed tree)
//for (int i = 1; i <= 7; i++) {
//avl.insert(i);
//}
// Check that tree is balanced
//assert avl.isBalanced() : "AVL tree should be balanced";
// Check that height is logarithmic (should be 3 for 7 nodes, not 7)
//int height = avl.getHeight();
//assert height <= 3 : "Height should be at most 3 for 7 nodes, got: " + height;
// Verify inorder still produces sorted output
/*List<Integer> inorder = avl.inorderTraversal();
List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
assert inorder.equals(expected) :
"AVL inorder should still be sorted";
System.out.println(" AVL balance test passed!");
System.out.println(" Tree height: " + height);
System.out.println(" Tree structure:");
avl.printTree();
System.out.println();
}*/
/**
* Test specific AVL rotation cases
*/
/*public static void testAVLRotations() {
System.out.println("Test 7: AVL Rotations (Bonus)");
// Test Left-Left case (requires right rotation)
System.out.println(" Testing Left-Left case:");
AVLTree<Integer> ll = new AVLTree<>();
ll.insert(30);
ll.insert(20);
ll.insert(10); // Should trigger right rotation
assert ll.isBalanced() : "Tree should be balanced after LL case";
ll.printTree();
// Test Right-Right case (requires left rotation)
System.out.println("\n Testing Right-Right case:");
AVLTree<Integer> rr = new AVLTree<>();
rr.insert(10);
rr.insert(20);
rr.insert(30); // Should trigger left rotation
assert rr.isBalanced() : "Tree should be balanced after RR case";
rr.printTree();
// Test Left-Right case (requires left then right rotation)
System.out.println("\n Testing Left-Right case:");
AVLTree<Integer> lr = new AVLTree<>();
lr.insert(30);
lr.insert(10);
lr.insert(20); // Should trigger LR rotation
assert lr.isBalanced() : "Tree should be balanced after LR case";
lr.printTree();
// Test Right-Left case (requires right then left rotation)
System.out.println("\n Testing Right-Left case:");
AVLTree<Integer> rl = new AVLTree<>();
rl.insert(10);
rl.insert(30);
rl.insert(20); // Should trigger RL rotation
assert rl.isBalanced() : "Tree should be balanced after RL case";
rl.printTree();
System.out.println("\n AVL rotation test passed!\n");✓
}
/**
* Optional: Additional test for students to implement
* Test with larger dataset and measure performance
*/
public static void testPerformance() {
System.out.println("Test 8: Performance Comparison (Optional)");
int n = 10000;
// Test BST with random insertions
BinarySearchTree<Integer> bst = new BinarySearchTree<>();
long startTime = System.nanoTime();
Random rand = new Random(42); // Fixed seed for reproducibility
for (int i = 0; i < n; i++) {
bst.insert(rand.nextInt(n * 2));
}
long bstTime = System.nanoTime() - startTime;
// Test AVL with same insertions
//AVLTree<Integer> avl = new AVLTree<>();
startTime = System.nanoTime();
rand = new Random(42); // Same seed
for (int i = 0; i < n; i++) {
//avl.insert(rand.nextInt(n * 2));
}
//long avlTime = System.nanoTime() - startTime;
System.out.println(" Inserted " + n + " random elements:");
System.out.println(" BST insertion time: " + bstTime / 1_000_000 + " ms");
//System.out.println(" AVL insertion time: " + avlTime / 1_000_000 + " ms");
// Test search performance
startTime = System.nanoTime();
for (int i = 0; i < 1000; i++) {
bst.contains(rand.nextInt(n * 2));
}
bstTime = System.nanoTime() - startTime;
startTime = System.nanoTime();
for (int i = 0; i < 1000; i++) {
//avl.contains(rand.nextInt(n * 2));
}
//avlTime = System.nanoTime() - startTime;
        System.out.println("\n Search time for 1000 queries:");
        System.out.println(" BST search time: " + bstTime / 1_000_000 + " ms");
        //System.out.println(" AVL search time: " + avlTime / 1_000_000 + " ms");
        System.out.println();
    }
}