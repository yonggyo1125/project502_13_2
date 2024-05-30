package exam03;

public class ComputerAssembler {
    public Monitor monitor() {
        return new Monitor();
    }

    public Body body() {
        return new Body();
    }

    public Keyboard keyboard() {
        return new Keyboard();
    }

    public Mouse mouse(){
        return new Mouse();
    }

    public GraphicCard card() {
        return new GraphicCard();
    }

    public Desktop desktop() {
        return new Desktop(monitor(), body(), keyboard(), mouse(), card());
    }
}
