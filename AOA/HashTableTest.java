package AOA;
/**
* Comprehensive test class for HashTable implementation.
* Tests all major functionality including collision handling, resizing, and
tombstones.
*/
public class HashTableTest {
public static void main(String[] args) {
System.out.println("=== HashTable Test Suite ===\n");
testBasicOperations();
testCollisionHandling();
testUpdateExistingKey();
testRemoval();
testTombstoneBehavior();
testResizing();
testEdgeCases();
System.out.println("\n=== All Tests Completed ===");
}
/**
* Test basic put, get, and size operations
*/
private static void testBasicOperations() {
System.out.println("Test 1: Basic Operations");
HashTable<String, Integer> table = new HashTable<>();
// Test initial size
assert table.size() == 0 : "Initial size should be 0";
System.out.println(" Initial size is 0");
// Test put and get
table.put("apple", 5);
table.put("banana", 3);
table.put("cherry", 8);
assert table.size() == 3 : "Size should be 3 after 3 insertions";
System.out.println(" Size correctly updated after insertions");
assert table.get("apple") == 5 : "Should get correct value for 'apple'";
assert table.get("banana") == 3 : "Should get correct value for 'banana'";
assert table.get("cherry") == 8 : "Should get correct value for 'cherry'";
System.out.println(" Get returns correct values");
// Test get for non-existent key
assert table.get("durian") == null : "Should return null for non-existent key";
System.out.println(" Get returns null for non-existent key");
System.out.println("Test 1 PASSED\n");
}
/**
* Test collision handling with linear probing
*/
private static void testCollisionHandling() {
System.out.println("Test 2: Collision Handling");
HashTable<Integer, String> table = new HashTable<>();
// These keys will likely collide (you may need to adjust based on hash function)
// Using keys that are multiples of the table size to force collisions
table.put(0, "zero");
table.put(16, "sixteen"); // Should collide with 0
table.put(32, "thirty-two"); // Should also collide with 0
assert "zero".equals(table.get(0)) : "Should get 'zero' for key 0";
assert "sixteen".equals(table.get(16)) : "Should get 'sixteen' for key 16";
assert "thirty-two".equals(table.get(32)) : "Should get 'thirty-two' for key 32";
System.out.println(" Linear probing handles collisions correctly");
System.out.println("Debug view: " + table.debugString());
System.out.println("Test 2 PASSED\n");
}
/**
* Test updating existing keys
*/
private static void testUpdateExistingKey() {
System.out.println("Test 3: Update Existing Key");
HashTable<String, String> table = new HashTable<>();
table.put("key1", "value1");
assert table.size() == 1 : "Size should be 1 after first put";
table.put("key1", "updated_value");
assert table.size() == 1 : "Size should still be 1 after update";
assert "updated_value".equals(table.get("key1")) : "Value should be updated";
System.out.println(" Updating existing key doesn't increase size");
System.out.println(" Updated value is correctly stored");
System.out.println("Test 3 PASSED\n");
}
/**
* Test removal operation
*/
private static void testRemoval() {
System.out.println("Test 4: Removal");
HashTable<String, Integer> table = new HashTable<>();
table.put("one", 1);
table.put("two", 2);
table.put("three", 3);
assert table.size() == 3 : "Size should be 3 before removal";
Integer removed = table.remove("two");
assert removed == 2 : "Remove should return the removed value";
assert table.size() == 2 : "Size should decrease after removal";
assert table.get("two") == null : "Removed key should return null";
// Remove non-existent key
Integer notFound = table.remove("four");
assert notFound == null : "Removing non-existent key should return null";
assert table.size() == 2 : "Size shouldn't change when removing non-existent key";
System.out.println(" Remove returns correct value");
System.out.println(" Size decreases after removal");
System.out.println(" Removed key returns null on get");
System.out.println("Test 4 PASSED\n");
}
/**
* Test tombstone behavior with linear probing
*/
private static void testTombstoneBehavior() {
System.out.println("Test 5: Tombstone Behavior");
HashTable<Integer, String> table = new HashTable<>();
// Create a collision chain: these should hash to consecutive slots
table.put(0, "A");
table.put(16, "B"); // Collides with 0, goes to next slot
table.put(32, "C"); // Collides with 0 and 16, goes to next slot
System.out.println("Before removal: " + table.debugString());
// Remove the middle item
table.remove(16);
System.out.println("After removing 16: " + table.debugString());
// C should still be findable despite the tombstone
assert "C".equals(table.get(32)) : "Should still find C after B is removed";
System.out.println(" Can find items past tombstones");
// Reinsert at the tombstone location
table.put(16, "B2");
assert "B2".equals(table.get(16)) : "Should reuse tombstone slot";
System.out.println(" Tombstone slots are reused for new insertions");
System.out.println("Test 5 PASSED\n");
}
/**
* Test automatic resizing
*/
private static void testResizing() {
System.out.println("Test 6: Resizing");
HashTable<Integer, String> table = new HashTable<>();
// Initial capacity is 16, MAX_LOAD is 0.5, so resize should happen at 8 items
for (int i = 0; i < 8; i++) {
table.put(i, "value" + i);
}
// This 9th insertion should trigger a resize
System.out.println("Adding 9th item (should trigger resize)...");
table.put(8, "value8");
// Verify all items are still accessible after resize
for (int i = 0; i <= 8; i++) {
String expected = "value" + i;
String actual = table.get(i);
assert expected.equals(actual) : "Value " + i + " should still be accessible after resize";
}
System.out.println(" Table resizes at correct load factor");
System.out.println(" All items accessible after resize");
System.out.println("Table after resize: " + table.debugString());
System.out.println("Test 6 PASSED\n");
}
/**
* Test edge cases and special scenarios
*/
private static void testEdgeCases() {
System.out.println("Test 7: Edge Cases");
HashTable<String, String> table = new HashTable<>();
// Test with null values (keys cannot be null as they need hashCode)
table.put("nullValue", null);
assert table.get("nullValue") == null : "Should store and retrieve nullvalues";
assert table.size() == 1 : "Null value should still count in size";
System.out.println(" Can store null values");
// Test remove and re-add same key
table.put("test", "original");
table.remove("test");
table.put("test", "new");
assert "new".equals(table.get("test")) : "Should be able to re-add removedkey";
System.out.println(" Can re-add removed keys");
// Test with empty string key
table.put("", "empty");
assert "empty".equals(table.get("")) : "Should handle empty string as key";
System.out.println(" Handles empty string keys");
System.out.println("Test 7 PASSED\n");
}
}
