package AOA.IteratorLab;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Class that allows backward iteration through an array of Strings.
 * 
 * @author Nathan Sprague
 */
public class BackScanner implements Iterable<String> {

  private String[] elements;

  public BackScanner(String[] elements) {
    this.elements = elements;
  }

  /**
   * @return an appropriate iterator object
   */
  public Iterator<String> iterator() {
    return new BackIterator();
  }

  // -----------------------------------------------------------
  // BackIterator is a private inner class: it can only be instantiated by
  // BackScanner.
  // --------------------------------------------------------------
  private class BackIterator implements Iterator<String> {
    int index;

    /*
     * Note that Iterators are just normal Java classes, and CAN have constructors. However, it is
     * fairly common to just initialize the instance variables when they are declared.
     */
    /**
     * Initialize the index to the end of the array.
     * 
     * @param elements The array to iterate over.
     */
    private BackIterator() {
      index = elements.length - 1;
    }

    /**
     * @return true if we haven't gone past the beginning.
     */
    public boolean hasNext() {
      return index >= 0;
    }

    /**
     * Return the next string and update the index.
     */
    public String next() {

      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      String result = elements[index];
      index--;
      return result;
    }

    /**
     * Not implemented.
     */
    public void remove() {
      String[] newElements = new String[elements.length - 1];
      for (int i = 0, j = 0; i < elements.length; i++) {
        if (i != index) {
          newElements[j++] = elements[i];
        }
      }
      elements = newElements;
    }
  }
}
