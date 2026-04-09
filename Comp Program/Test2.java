import java.util.*;
public class Test2{
    public static void main(String[] args) {
        Scanner s=new Scanner(System.in);
        int tests=s.nextInt();
        for (int i=0;i<tests;i++){
            String num=s.next();
            StringBuilder nums=new StringBuilder(num);
            nums.reverse();
            num=nums.toString();
            long total=0;
            int add=0;
            long mult=1;
            for (int j=0;j<num.length();j++){
                int val=Integer.parseInt(num.substring(j,j+1));
                if (val%2==1){
                    total+=mult%1000000007;
                }
                if (val>1){
                    add=1;
                }
                mult*=2;
                if(mult==2){
                    mult=3;
                }
                mult%=1000000007;
                //System.out.println(total);
            }
            System.out.println((total+add)%1000000007);
        }
        s.close();
    }
}