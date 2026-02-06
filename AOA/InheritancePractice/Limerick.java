public class Limerick extends Poem{
    public int numLines(){
        return 5;
    }
    public int getSyllables(int k){
        if(k == 1) return 9;
        else if(k == 2) return 9;
        else if(k == 3) return 6;
        else if(k == 4) return 6;
        else return 9;
    }
}
