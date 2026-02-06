package AOA.InterfaceLab;

public abstract class Animal {
    protected int legs;
    protected Animal(int legs) {
        this.legs = legs;
    }
    protected abstract void eat();
    protected void walk() {
        System.out.println("This animal walks on " + legs + " legs.");
    }
}
