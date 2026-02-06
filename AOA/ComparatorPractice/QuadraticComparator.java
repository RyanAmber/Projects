package AOA.ComparatorPractice;
import java.util.*;
public class QuadraticComparator implements Comparator<QuadraticFunction> {
    private double x;
    QuadraticComparator(){
        x=0.0;
    }
    QuadraticComparator(double x){
        this.x=x;
    }
    public int compare(QuadraticFunction f1, QuadraticFunction f2){
        double v1=f1.valueAt(x);
        double v2=f2.valueAt(x);
        if (v1>v2){
            return 1;
        }else if(v2>v1){
            return -1;
        }
        return 0;
    }
}