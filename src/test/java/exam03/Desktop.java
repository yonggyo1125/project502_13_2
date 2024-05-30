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
        // Desktop 객체는 구성 객체(monitor, body, keyboard, mouse)의 변화에 영향을 받는 관계  -> 통제가 필요  -> 캡슐화 : 폐쇄
        body.run();
    }
}
