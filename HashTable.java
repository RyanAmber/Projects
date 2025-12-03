/**
 * Simple HashTable class. Analagous to Java's version which is called HashMap<K, V>. 
 * 
 * @author CS240 Instructors - James Madison University
 * @author Ishan Khetarpal - Poolesville High School
 * @author Your Name - Poolesville High School
 */
public class HashTable<K, V> {

  // Answer the following questions as comments here:
  // 1. What is the function of MAX_LOAD?
  //Determines the maximum amount of values that can be stored in the hash table before resizing.
  // 2. If chaining is used, can MAX_LOAD be greater than 1? Why?
  //Yes, in chaining, multiple items can be in the same index
  // 3. If probing is used, can MAX_LOAD be greater than 1? Why?
  //No, in probing, each index can only hold one item
  // 4. What approach (chaining/probing) is used in this version of the hash table?
  // 5. What objects in Java return a hashCode() value? 
  // 6. Complete the missing parts of the code marked below with TODO.
  // 7. Thouroughly test your code. 
  // 8. Submit your code on MyMCPS/Canvas. 

  public static final int INITIAL_CAPACITY = 16; // must be a power of 2.
  public static final double MAX_LOAD = .5;

  Item[] table; // (Leave this non-private for testing.)
  private int size;

  /**
   * HashTable constructor.
   */
  public HashTable() {
    table = new HashTable.Item[INITIAL_CAPACITY];
    size = 0;
  }


  /**
   * Store the provided key/value pair.
   */
  public void put(K key, V value) {

    // TODO

    // IMPLEMENTATION ADVICE:
    // Start by coding the findKey method.
    // 
    // Use findKey to determine if the provided key is already in the table.
    // If so, handle that as a special case.
    //
    // Once you know that the key is *not* in the table, use linear probing to find
    // an available location (either a null or a tombstone).

  }

  /**
   * Return the value associated with the provided key, or null if no such value
   * exists.
   */
  public V get(K key) {
    // TODO
    return null;
  }

  /**
   * Remove the provided key from the hash table and return the associated value.
   * Returns null if the key is not stored in the table.
   */
  public V remove(K key) {

    // TODO
    return null;
  }

  /**
   * Return the number of items stored in the table.
   */
  public int size() {
    return size;
  }

  // PRIVATE HELPER METHODS BELOW THIS POINT----------


  /**
   * Double the size of the hash table and rehash all existing items.
   */
  private void resize() {
    // TODO
  }


  /**
   * Find the index of a key or return -1 if it can't be found. If removal is
   * implemented, this will skip over tombstone positions during the search.
   */
  private int findKey(K key) {
    // TODO -- THIS METHOD SHOULD BE HELPFUL FOR PUT, GET, AND REMOVE.

    // IMPLEMENTATION ADVICE: All java classes (including K!) have a hashCode method
    // that returns an integer. This could be ANY integer and probably *won't* be a
    // valid index into your table array. The hashCode for the key must be mapped to
    // a valid index. This can be done using the indexFor method included at the
    // bottom of this file.
    //
    // Your code in this method should handle collisions using simple linear
    // probing.

    return -1;
  }



  /**
   * Returns index for hash code h.
   */
  private int indexFor(int h, int length) {
    return hash(h) & (length - 1);
  }

  /**
   * Applies a supplemental hash function to a given hashCode, which defends
   * against poor quality hash functions. This is critical because HashMap uses
   * power-of-two length hash tables, that otherwise encounter collisions for
   * hashCodes that do not differ in lower bits.
   */
  private int hash(int h) {
    // This function ensures that hashCodes that differ only by
    // constant multiples at each bit position have a bounded
    // number of collisions (approximately 8 at default load factor).
    h ^= (h >>> 20) ^ (h >>> 12);
    return h ^ (h >>> 7) ^ (h >>> 4);
  }

  /**
   * Return a string representing the internal state of the hash table 
   * for debugging purposes.
   */
  public String debugString() {
    String result = "|";
    for (int i = 0; i < table.length; i++) {
      if (table[i] == null) {
        result += "   ";
      } else if (table[i].tombstone) {
        result += " ☠ ";
      } else {
        result += "(" + table[i].key + ": " + table[i].value + ")";
      }
      result += "|";
    }
    return result;
  }

  /**
   * Item class is a simple wrapper for key/value pairs.
   */
  class Item { // leave this non-private for testing.
    private K key;
    private V value;
    private boolean tombstone;

    /**
     * Create an Item object.
     */
    public Item(K key, V value) {
      this.key = key;
      this.value = value;
      this.tombstone = false;
    }

    /* Getters and setters */
    public K key() {
      return key;
    }

    public V value() {
      return value;
    }

    public void setValue(V value) {
      this.value = value;
    }

    public boolean isDeleted() {
      return tombstone;
    }

    public void delete() {
      tombstone = true;
    }
  }

}
