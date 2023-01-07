
public class Mmu {
    public byte[] memory = new byte[0x8000];
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
        if(addr < 0x1FFF) return;
        memory[addr & 0x3FFF] = (byte)value;
    }

    public void write16(int addr, int value){
        write8(addr + 1, (value >> 8) & 0xFF);
        write8(addr, value & 0xFF);
    }
    public int read8(int addr){
        return memory[addr & 0x3FFF] & 0xFF;
    }

    public int read16(int addr){
        return ((read8(addr + 1) << 8) | read8(addr)) & 0xFFFF;
    }
}
