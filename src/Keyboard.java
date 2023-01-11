
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard extends KeyAdapter{
    public KeyListener Listener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            switch(keyCode){
                case KeyEvent.VK_NUMPAD7: p1Keyboard |= 0b00000100; break; // 2P START
                case KeyEvent.VK_NUMPAD9: p1Keyboard |= 0b00000010; break; // 1P START
                case KeyEvent.VK_NUMPAD4: p1Keyboard |= 0b00100000; break; // 1P LEFT
                case KeyEvent.VK_NUMPAD6: p1Keyboard |= 0b01000000; break; // 1P RIGHT
                case KeyEvent.VK_NUMPAD8: p1Keyboard |= 0b00010000; break; // 1P SHOOT
                case KeyEvent.VK_NUMPAD1: p1Keyboard |= 0b00000001; break; // CREDIT
            }
//            System.out.println("keyPressed="+KeyEvent.getKeyText(e.getKeyCode()));
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int keyCode = e.getKeyCode();
            switch(keyCode){
                case KeyEvent.VK_NUMPAD7: p1Keyboard &= ((~0b00000100) & 0xFF); break;
                case KeyEvent.VK_NUMPAD9: p1Keyboard &= ((~0b00000010) & 0xFF); break;
                case KeyEvent.VK_NUMPAD4: p1Keyboard &= ((~0b00100000) & 0xFF); break;
                case KeyEvent.VK_NUMPAD6: p1Keyboard &= ((~0b01000000) & 0xFF); break;
                case KeyEvent.VK_NUMPAD8: p1Keyboard &= ((~0b00010000) & 0xFF); break;
                case KeyEvent.VK_NUMPAD1: p1Keyboard &= ((~0b00000001) & 0xFF); break;
            }
//            System.out.println("keyReleased="+KeyEvent.getKeyText(e.getKeyCode()));
        }
    };
    //    public static final int[]
    private Cpu cpu;
    public static final int INVADERS_QUIT = KeyEvent.VK_ESCAPE;
    private int currentKeyPressed = 0;
    private int p1Keyboard = 0b00001000; // BIT 3 always set

    public int getP1Keyboard(){
        return p1Keyboard & 0xFF;
    }

    public void init(Cpu cpu) {
        this.cpu = cpu;
    }
}
