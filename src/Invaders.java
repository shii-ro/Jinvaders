import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import static java.lang.Thread.*;


public class Invaders {
    private static final int CYCLES_PER_FRAME = 33333;
    private static final int CYCLES_PER_MIDFRAME = 16667;
    public static void main(String[] args) throws InterruptedException {
        Path path = Paths.get("./invaders.bin");
        byte[] rom;

        try {
            rom = Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Mmu mmu = new Mmu();
        Cpu cpu = new Cpu();
        Display display = new Display(mmu.memory, 2);

        mmu.loadRom(rom);
        cpu.init(mmu);
        display.init();
        display.repaint();

        while(!cpu.error){
            int total_cycles = 0;
            while(total_cycles < CYCLES_PER_MIDFRAME && !cpu.error) {
                cpu.cycle();
                total_cycles += cpu.cycles;
            }
            // mid frame interrupt
            cpu.midFrameInterruptHandler();
            while(total_cycles < CYCLES_PER_MIDFRAME && !cpu.error) {
                cpu.cycle();
                total_cycles += cpu.cycles;
            }
            cpu.vblankInterruptHandler();
            sleep(10);
            display.repaint();
        }

    }
}
