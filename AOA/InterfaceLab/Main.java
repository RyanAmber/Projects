package AOA.InterfaceLab;

public class Main {
    public static void main(String[] args){
        Fish d = new Fish();
        Cat c = new Cat("Fluffy");
        Animal a = new Fish();
        Animal e = new Spider();
        Pet p = new Cat();
        System.out.println(p.getName());
        d.walk();
        c.walk();
        a.walk();
        e.walk();  
    }
}
