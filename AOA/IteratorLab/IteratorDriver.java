package AOA.IteratorLab;
import java.util.Iterator;

public class IteratorDriver {

  public static void main(String[] args) {
    String[] words = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"};

    SkipScanner sc = new SkipScanner(words,2);
    Iterator<String> it = sc.iterator();
    System.out.print("SkipScanner with skip of 2: ");
    while (it.hasNext()) {
      String current = it.next();
      System.out.print(current+", ");
    }
    System.out.println();
    System.out.print("SkipScanner with skip of 3: ");
    sc = new SkipScanner(words,3);
    it = sc.iterator();

    while (it.hasNext()) {
      String current = it.next();
      System.out.print(current+", ");
    }
    System.out.println();
    System.out.print("BackScanner: ");
    BackScanner backsc = new BackScanner(words);
    it = backsc.iterator();

    while (it.hasNext()) {
      String current = it.next();
      System.out.print(current+", ");
    }
    System.out.println();
    System.out.print("2- Ngram Scanner: ");
    NGramScanner nsc = new NGramScanner(words,2);
    it = nsc.iterator();

    while (it.hasNext()) {
      String current = it.next();
      System.out.print(current+", ");
    }
    System.out.println();
    System.out.print("3- Ngram Scanner: ");
    nsc = new NGramScanner(words,3);
    it = nsc.iterator();

    while (it.hasNext()) {
      String current = it.next();
      System.out.print(current+", ");
    }


  }

}
