package AOA.IteratorLab;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class allows us to scan through the "n-grams" that are represented by an array of Strings.
 * An n-gram is a sequence of n words appearing in order. For example, the sentence:
 * 
 * {"I", "love", "computer", "science"}
 * 
 * Contains the 2-grams (also called bigrams): "I love", "love computer", and "computer science".
 * 
 * It contains the 3-grams (also called trigrams): "I love computer", and "love computer science".
 *
 */
public class NGramScanner implements Iterable<String> {
  private String[] elements;
  private int nValue;

  /**
   * Construct an NGramScanner.
   * 
   * @param elements The array of words
   * @param nValue The value of n to use in constructing n-grams.
   */
  public NGramScanner(String[] elements, int nValue) {
    this.elements = elements;
    this.nValue = nValue;
  }

  @Override
  public Iterator<String> iterator() {
    return new NGramIterator();
  }

  private class NGramIterator implements Iterator<String> {
    int index;

    private NGramIterator() {
      index = 0;
    }

    public boolean hasNext() {
      return index + nValue - 1 < elements.length;
    }

    public String next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      StringBuilder nGram = new StringBuilder();
      for (int i = 0; i < nValue; i++) {
        nGram.append(elements[index + i]);
        if (i < nValue - 1) {
          nGram.append(" ");
        }
      }
      index++;
      return nGram.toString();
    }

    public void remove() {
      String[] newElements = new String[elements.length - nValue];
      for (int i = 0, j = 0; i < elements.length; i++) {
        if (i < index || i >= index + nValue) {
          newElements[j++] = elements[i];
        }
      }
      elements = newElements;
      index--;
    }
  }

}
