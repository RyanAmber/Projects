
import java.util.*;
public class Fibonacci{
    private static boolean[] Primes=new boolean[1000];
    public static void main(String[] args) {
        for (int i=0;i<1000;i++){
            System.out.println(i+" "+isPrime(i));
        }
        /*int n=10;
        for (int i=0;i<=n;i++){
            
            System.out.println(fibonacci(i));
            System.out.println(fibonacci2(i));
        } */
       //List<Integer> list=new ArrayList<Integer>();
       //System.out.println(duplicate(list));

    }
    static{
        for (int i=0;i<1000;i++){
            boolean value=true;
            for (int j=2;j<=Math.sqrt(i);j++){
                if(i%j==0){
                    value=false;
                }
            }
            Primes[i]=value;
        }
    }
    public static boolean isPrime(int i){
        return Primes[i];
    }
    public static boolean duplicate(List<Integer> list){
        for (int i=0;i<list.size();i++){
            for (int j=i+1;j<list.size();j++){
                if(
                    list.get(i)==list.get(j)){
                    return true;
                }
            }
        }
        return false;
    }
    public static long fibonacci(int n){
        if (n==1||n==0){
            return n;
        }
        return fibonacci(n-1)+fibonacci(n-2);
    }
    public static long fibonacci2(int n){
        int[] previous=new int[n+1];
        previous[0]=0;
        previous[1]=1;
        for (int i=2;i<=n;i++){
            previous[i]=previous[i-1]+previous[i-2];
        }
        return previous[n];
    }
}