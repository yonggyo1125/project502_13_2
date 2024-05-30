package exam03;

public class Desktop {
    private Monitor monitor;
    private Body body;
    private Keyboard keyboard;
    private Mouse mouse;
    private GraphicCard card;

    public Desktop(Monitor monitor, Body body, Keyboard keyboard, Mouse mouse, GraphicCard card) {
        this.monitor = monitor;
        this.body = body;
        this.keyboard = keyboard;
        this.mouse = mouse;
        this.card = card;
    }

    public void run() {
        // monitor, body, keyboard, mouse 객체를 활용 ...
    }
}
