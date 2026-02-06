import java.util.*;
public class Test2{
    public static void main(String[] args) {
        Scanner s=new Scanner(System.in);
        int n=s.nextInt();
        int mooves=s.nextInt();
        List<Base> sols=new ArrayList<Base>();
        for (int i=0;i<(int) Math.pow(2,n);i++){
            List<Boolean> vals=new ArrayList<Boolean>();
            int temp=i;
            while(temp>0){
                vals.add(temp%2==1);
                temp/=2;
            }
            while(vals.size()<n){
                vals.add(false);
            }
            sols.add(new Base(vals,n));
        }
        for (int moove=0;moove<mooves;moove++){
            int val1=s.nextInt();
            int val2=s.nextInt();
            int val3=s.nextInt();
            for (Base b:sols){
                b.contains(val1,val2,val3);
            }
        }
        int max=0;
        int count=0;
        for (Base b:sols){
            //System.out.println(b);
            int val=b.count;
            if(val>max){
                count=1;
                max=val;
            }else if(val==max){
                count++;
            }
        }
        System.out.println(max+" "+count);
        s.close();
    }
}
class Base{
    List<Boolean> vals;
    int count;
    int size;
    public Base(List<Boolean> vals,int n){
        this.vals=vals;
        count=0;
        size=n;
    }

    public void contains(int val1,int val2, int val3){
        if (vals.get(val1-1)&&!vals.get(val2-1)&&!vals.get(val3-1))
        count++;
    }
    public String toString(){
        return vals+" "+count;
    }
}