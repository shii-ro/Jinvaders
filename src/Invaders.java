import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;


public class Invaders {
    public static void main(String[] args) {
        Path path = Paths.get("invaders.bin");
        byte[] rom;

        try {
            rom = Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Mmu mmu = new Mmu();
        Cpu cpu = new Cpu();


        mmu.loadRom(rom);
        cpu.init(mmu);

        for(int i = 0; i < 20; i++){
            cpu.cycle();
        }

    }
}
