package AOA.ComparatorPractice;
public class QuadraticFunction{
    private int a;
    private int b;
    private int c;
    QuadraticFunction(int a, int b, int c){
        this.a=a;
        this.b=b;
        this.c=c;
    }
    public double valueAt(double x){
        return a*x*x+b*x+c;
    }
    public String toString(){
        String str="";
        if (a!=0){
            if (a==1||a==-1){
                str+="x^2";
            }else{
                str+=a+"x^2";
            }
        }
        if (b!=0){
            if (b>0){
                str+="+";
            }
            if (b==1||b==-1){
                str+="x";
            }else{
                str+=b+"x";
            }
        }
        if (c!=0){
            if (c>0){
                str+="+";
            }
            str+=c;
        }
        return str;
    }
    public boolean equals(Object other){
        if (!(other instanceof QuadraticFunction)){
            return false;
        }
        QuadraticFunction otherfunction=(QuadraticFunction) other;
        return a==otherfunction.a&&b==otherfunction.b&&c==otherfunction.c;
    }
    public int compareTo(QuadraticFunction other){
        if (a>other.a){
            return 1;
        }else if(a<other.a){
            return -1;
        }else{
            if (b>other.b){
                return 1;
            }else if (b<other.b){
                return -1;
            }else{
                if (c>other.c){
                    return 1;
                }else if(c<other.c){
                    return -1;
                }
                return 0;
            }
        }
    }
}