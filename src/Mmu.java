
public class Mmu {
    byte[] memory = new byte[0x4000];
//
    public Mmu(){
    }

    public void init(){

    }

    public void loadRom(byte[] rom){
        for (int i =0; i < rom.length; i++) {
            memory[i] = rom[i];
        }
    }
    public void write8(int addr, int value){
        memory[addr] = (byte)value;
    }
    public int read8(int addr){
        return memory[addr] & 0xFF;
    }

    public int read16(int addr){
        return ((read8(addr + 1) << 8) | read8(addr)) & 0xFFFF;
    }
}
