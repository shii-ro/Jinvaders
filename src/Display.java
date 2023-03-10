import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

public class Display extends JPanel {
    private byte gfx[];
    private int displayScale;
    private KeyListener listener;
    private Graphics g;
    private static final int DISPLAY_WIDTH = 256;
    private static final int DISPLAY_HEIGTH = 224;
    private JFrame frame;
    private Keyboard key;

    public Display(byte[] gfx, int scale, Keyboard key) {
        this.displayScale = scale;
        this.gfx = gfx;
    }

    public void init(Keyboard key) {
        this.setPreferredSize(new Dimension(DISPLAY_HEIGTH * displayScale, DISPLAY_WIDTH * displayScale));
        JFrame window = new JFrame("Jinvaders");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(this);
        window.pack();
        window.setBackground(Color.WHITE);
        this.key = key;
        this.listener = key.Listener;
        addKeyListener(listener);
        window.setVisible(true);
        setFocusable(true);
    }
    @Override
    public void paint(Graphics g) {
        Color[] paletteW = {Color.BLACK, Color.WHITE};
        Color[] paletteG = {Color.BLACK, Color.GREEN};
        Color[] paletteR = {Color.BLACK, Color.RED};
        Color[] palette;
        //    Video hardware is very simple: 7168 bytes 1bpp bitmap (32 bytes per scanline).
        for (int y = 0; y < 224; y++) { /// 224 lines
            for (int x = 0; x < 32; x++) { // 32 bytes
                byte b = gfx[0x2400 + (y * 32) + x]; // 8 bits
                palette = paletteW;
                if(x >= 24 && x <= 27) palette = paletteR;
                else if(x >= 2 && x <=8) palette = paletteG;
                for (int i = 0; i < 8; i++) {
                    g.setColor(palette[((b >> i) & 0x1)]);
                    g.fillRect(y * displayScale, (256 - (x * 8 + i)) * displayScale, displayScale, displayScale);
                }
            }
        }
    }
}
