package AOA.ComparatorPractice;
import java.util.*;
public class Main {
    public static void main(String[] args){
        QuadraticComparator c1=new QuadraticComparator();
        QuadraticComparator c2=new QuadraticComparator(10);
        QuadraticFunction f1=new QuadraticFunction(1,2,4);
        QuadraticFunction f2=new QuadraticFunction(3,2,0);
        QuadraticFunction f3=new QuadraticFunction(1,6,4);
        QuadraticFunction f4=new QuadraticFunction(2,8,0);
        QuadraticFunction[] arr=new QuadraticFunction[]{f1,f2,f3,f4};
        Arrays.sort(arr,c1);
        System.out.println(Arrays.toString(arr));
        Arrays.sort(arr,c2);
        System.out.println(Arrays.toString(arr));
    }
}
