import java.util.*;
public class Test2{
    public static void main(String[] args) {
        Scanner s=new Scanner(System.in);
        int size=s.nextInt();
        int pic=s.nextInt();
        int iter=s.nextInt();
        Map<Integer, Integer> cows=new HashMap<Integer, Integer>();
        for (int ite=0;ite<iter;ite++){
            int row=s.nextInt()-1;
            int col=s.nextInt()-1;
            int beauty=s.nextInt();
            cows.put(row*size+col,beauty);
            //System.out.println(cows);
            System.out.println(best(size, pic, cows));
        }
        s.close();
    }
    public static int best(int size, int pic, Map<Integer, Integer> cows){
        int max=0;
        for (int r=0;r<=size-pic;r++){
            for (int c=0;c<=size-pic;c++){
                int total=0;
                for (int i=r;i<r+pic;i++){
                    for (int j=c;j<c+pic;j++){
                        if(cows.containsKey(i*size+j)){
                            total+=cows.get(i*size+j);
                            //System.out.println(cows.get(i*size+j));
                        }
                    }
                }
                max=Math.max(max,total);
            }
        }
        return max;
    }
}