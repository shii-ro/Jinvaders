public class Ppu {
    private byte RAM[];
    
    public Ppu(){
        RAM = new byte[7*1024];
    }
    
    public void write8(int addr, byte value){
        RAM[addr & 0x1BFF] = value;
    }
}
