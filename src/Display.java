import javax.swing.*;
import java.awt.*;

public class Display extends JPanel {
    private byte gfx[];
    private int displayScale;
    private Graphics g;
    private static final int DISPLAY_WIDTH = 256;
    private static final int DISPLAY_HEIGTH = 224;
    private JFrame frame;

    public Display(byte[] gfx, int scale){
        this.displayScale = scale;
        this.gfx = gfx;
    }
    public void init(){
        this.setPreferredSize(new Dimension(DISPLAY_HEIGTH * displayScale, DISPLAY_WIDTH * displayScale));
        JFrame window = new JFrame("Jinvaders");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(this);
        window.pack();
        window.setVisible(true);
        window.setBackground(Color.WHITE);
        this.setFocusable(true);
    }
    @Override
    public void paint(Graphics g) {
        Color l[] = {Color.BLACK, Color.WHITE};
        //    Video hardware is very simple: 7168 bytes 1bpp bitmap (32 bytes per scanline).
        for (int y = 0; y < 224; y++) {
            for (int x = 0; x < 32; x++) {
                byte b = gfx[0x2400 + (y * 32) + x];
                for(int i = 0; i < 8; i++) {
                    g.setColor(l[((b >> i) & 0x1)]);
                    g.fillRect(y * displayScale, (256 - (x * 8 + i)) * displayScale, displayScale, displayScale);
                }
            }
        }
    }
}
