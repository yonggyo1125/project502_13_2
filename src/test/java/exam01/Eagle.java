package exam01;

public class Eagle implements Flyable, Speakable{
    @Override
    public void fly() {
        System.out.println("멋지게 날아간다.");
    }

    @Override
    public void speak() {
        System.out.println("짹짹");
    }
}
