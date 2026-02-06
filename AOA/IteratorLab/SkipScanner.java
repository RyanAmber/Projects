package AOA.IteratorLab;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Class that allows iteration through an array of Strings while skipping to every N'th element.
 * 
 * For example, iterating through the array {"A", "B", "C", "D", "E"} with a skip value of 2, would
 * return "A", then "C" then "E". Iterating with a skip value of 3 would return "A", then "D".
 * 
 *
 */
public class SkipScanner implements Iterable<String> {

  private String[] elements;
  private int skip;

  /**
   * Construct a SkipScanner.
   * 
   * @param elements The array to iterate over
   * @param skip The scanner will visit every skip'th element
   */
  public SkipScanner(String[] elements, int skip) {
    this.elements = elements;
    this.skip = skip;
  }

  /**
   * @return An appropriate iterator object.
   */
  public Iterator<String> iterator() {
    return new SkipIterator();
  }

  private class SkipIterator implements Iterator<String>{
    int index;
    /**
     * 
     * @param elements The array to iterate over.
     * @param skip
     */
    private SkipIterator(){
      index=0;
    }

    public boolean hasNext(){
      return index<elements.length;
    }
    public String next(){
      if (!hasNext()){
        throw new NoSuchElementException();
      }

      String result=elements[index];
      index+=skip;
      return result;
    }

    public void remove(){
      String[] newElements = new String[elements.length - 1];
      for (int i = 0, j = 0; i < elements.length; i++) {
        if (i != index) {
          newElements[j++] = elements[i];
        }
      }
      elements = newElements;
      index--;
    }
  }

}
