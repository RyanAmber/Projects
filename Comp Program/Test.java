import java.util.*;
public class Test{
    public static void main(String[] args){
        Scanner s=new Scanner(System.in);
        int tests=s.nextInt();
        int k=s.nextInt();
        for (int test=0;test<tests;test++){
            String solution="";
            boolean flip=false;
            int length=s.nextInt();
            String moo=s.next();
            for (int i=length-1;i>=0;i--){
                String letter=moo.substring(i,i+1);
                if(flip){
                    letter=letter.equals("M")?"O":"M";
                }
                if(letter.equals("O")){
                    flip=!flip;
                }
                solution=letter+solution;
            }
            System.out.println("YES");
            if(k==1){
                System.out.println(solution);
            }
        }
        s.close();
    }
}
