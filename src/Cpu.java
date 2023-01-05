public class Cpu {
    private Disassembler dasm;
    private boolean[] parityFlagLUT;
    private Mmu mmu;
    private int PC;
    private int OC;
    private int SP;
    private int A, B, C, D, E, H, L;
    private int cycles;
    private boolean signFlag;
    private boolean zeroFlag;
    private boolean parityFlag;
    private boolean carryFlag;
    public Cpu() {
        this.dasm = new Disassembler();
        this.parityFlagLUT = new boolean[0x100];
        this.OC = 0;
        this.PC = 0;
    }

    public void init(Mmu mmu) {
        this.mmu = mmu;
        this.dasm.init(mmu, this);
        this.fillParityFlagLUT();
    }

    private void fillParityFlagLUT(){
        for(int x = 0; x < 0x100; x++){
            int y = x^(x>>1);
            y = y^(y>>2);
            y = y^(y>>4);
            parityFlagLUT[x] = ((y & 1) == 1) ? true : false;
        }
    }


    private int next8() {
        return mmu.read8(this.PC++);
    }
    private int next16() {
        int value = mmu.read16(this.PC);
        this.PC += 2;
        return value;
    }

//    private pop8(){
//
//    }
    private void push8(int value){
        mmu.write8(--this.SP, value);
    }
    private void push16(int value){
        this.push8(value >> 8);
        this.push8(value & 0xFF);
    }


//    enum ConditionCode  {NZ, Z, NC, C, PO, PE, P, M};
//    enum DestSrc {B, C, D, E, H, L, HL, A};
//    enum RegisterPair {BC, DE, HL, SP}
    private int DCR(int value){
        int newValue = value - 1;
        signFlag = ((newValue & 0x80) == 1) ? true : false;
        zeroFlag = ((newValue & 0xFF) == 0) ? false : true;
        // aux ? does space invaders rlly use this ?
        parityFlag = parityFlagLUT[newValue & 0xFF];

        return newValue;
    }
    void cycle(){
//        D   = Destination register (8 bit)
//        S   = Source register (8 bit)
//        RP  = Register pair (16 bit)
//        #   = 8 or 16 bit immediate operand
//        a   = 16 bit Memory address
//                p   = 8 bit port address
//                ccc = Conditional
//        int d;
//        int s;
//        int rp;
//        int o;
//        int a;
//        int p;
//        int ccc;
        this.OC = mmu.read8(this.PC);
        System.out.printf("%04X %s\n", this.PC++, dasm.getMnemonic(OC));

        switch (OC){
            case 0x00: ; break;// NOP
            case 0x01: BC(next16()); cycles = 10; break;// LXI B d16
            case 0x05: B(DCR(B())); cycles = 5; break; // DCR B
            case 0x06: B(next8()); cycles = 7; break; // MVI B, d8
            case 0x11: D(next8()); cycles = 7; break; // LXI D
            case 0x13: D(D() + 1); cycles = 5; break; // INX DE
            case 0x1A: A(M(DE())); cycles = 7; break; // LDAX DE
            case 0x1B: BC(BC() - 1); cycles = 5; break; // DCX BC
            case 0x21: HL(next16()); cycles = 10; break; // LXI HL
            case 0x23: HL(HL() + 1); cycles = 5; break; // INX HL
            case 0x31: SP(next16()); cycles = 10; break; // LXI SP d16
            case 0x33: SP(SP() + 1); cycles = 5; break; // INX SP
            case 0x40: B(B()); cycles = 5; break;
            case 0x41: B(C()); cycles = 5; break;
            case 0x42: B(D()); cycles = 5; break;
            case 0x43: B(E()); cycles = 5; break;
            case 0x44: B(H()); cycles = 5; break;
            case 0x45: B(L()); cycles = 5; break;
            case 0x46: B(M(HL())); cycles = 7; break;
            case 0x47: B(A()); cycles = 5; break;
            case 0x48: C(B()); cycles = 5; break;
            case 0x49: C(C()); cycles = 5; break;
            case 0x4A: C(D()); cycles = 5; break;
            case 0x4B: C(E()); cycles = 5; break;
            case 0x4C: C(H()); cycles = 5; break;
            case 0x4D: C(L()); cycles = 5; break;
            case 0x4E: C(M(HL())); cycles = 7; break;
            case 0x4F: C(A()); cycles = 5; break;
            case 0x50: D(B()); cycles = 5; break;
            case 0x51: D(C()); cycles = 5; break;
            case 0x52: D(D()); cycles = 5; break;
            case 0x53: D(E()); cycles = 5; break;
            case 0x54: D(H()); cycles = 5; break;
            case 0x55: D(L()); cycles = 5; break;
            case 0x56: D(M(HL())); cycles = 7; break;
            case 0x57: D(A()); cycles = 5; break;
            case 0x58: E(B()); cycles = 5; break;
            case 0x59: E(C()); cycles = 5; break;
            case 0x5A: E(D()); cycles = 5; break;
            case 0x5B: E(E()); cycles = 5; break;
            case 0x5C: E(H()); cycles = 5; break;
            case 0x5D: E(L()); cycles = 5; break;
            case 0x5E: E(M(HL())); cycles = 7; break;
            case 0x5F: E(A()); cycles = 5; break;
            case 0x60: H(B()); cycles = 5; break;
            case 0x61: H(C()); cycles = 5; break;
            case 0x62: H(D()); cycles = 5; break;
            case 0x63: H(E()); cycles = 5; break;
            case 0x64: H(H()); cycles = 5; break;
            case 0x65: H(L()); cycles = 5; break;
            case 0x66: H(M(HL())); cycles = 7; break;
            case 0x67: H(A()); cycles = 5; break;
            case 0x68: L(B()); cycles = 5; break;
            case 0x69: L(C()); cycles = 5; break;
            case 0x6A: L(D()); cycles = 5; break;
            case 0x6B: L(E()); cycles = 5; break;
            case 0x6C: L(H()); cycles = 5; break;
            case 0x6D: L(L()); cycles = 5; break;
            case 0x6E: L(M(HL())); cycles = 7; break;
            case 0x6F: L(A()); cycles = 5; break;
            case 0x70: M(HL(), B()); cycles = 5; break;
            case 0x71: M(HL(), C()); cycles = 5; break;
            case 0x72: M(HL(), D()); cycles = 5; break;
            case 0x73: M(HL(), E()); cycles = 5; break;
            case 0x74: M(HL(), H()); cycles = 5; break;
            case 0x75: M(HL(), L()); cycles = 5; break;
            case 0x76: ; break;
            case 0x77: M(HL(), A()); cycles = 5; break;
            case 0x78: A(B()); cycles = 5; break;
            case 0x79: A(C()); cycles = 5; break;
            case 0x7A: A(D()); cycles = 5; break;
            case 0x7B: A(E()); cycles = 5; break;
            case 0x7C: A(H()); cycles = 5; break;
            case 0x7D: A(L()); cycles = 5; break;
            case 0x7E: A(M(HL())); cycles = 7; break;
            case 0x7F: A(A()); cycles = 5; break;
            case 0xC2: PC(zeroFlag ? PC() : next16()); cycles = 10; break;
            case 0xC3: PC(next16()); cycles = 10; break; // JMP a16
            case 0xCD: push16(this.PC); PC(next16()); cycles = 17; break;// CALL a16
            default: System.out.printf("Opcode not implemented: 0x%02X\n", (byte)this.OC); break;
        }
    }

    public int A() {return A;} public void A(int value) {A = value;}
    public int B() {return B;} public void B(int value) {B = value;}
    public int C() {return C;} public void C(int value) {C = value;}
    public int D() {return D;} public void D(int value) {D = value;}
    public int E() {return E;} public void E(int value) {E = value;}
    public int H() {return H;} public void H(int value) {H = value;}
    public int L() {return L;} public void L(int value) {L = value;}
    public int M(int addr) {return mmu.read8(addr);} public void M(int addr, int value) {mmu.write8(addr, value);}

    public int BC() {return B << 8| C;} public void BC(int value) {B = (value >> 8) & 0xFF; C = value & 0xFF;}
    public int HL() {return H << 8| L;} public void HL(int value) {H = (value >> 8) & 0xFF; L = value & 0xFF;}
    public int DE() {return D << 8| E;} public void DE(int value) {D = (value >> 8) & 0xFF; E = value & 0xFF;}
    public int SP() {return this.SP;}  public void SP(int value) {this.SP = value & 0xFFFF;}
    public int PC() {return this.PC;} public void PC(int value) {this.PC = value & 0xFFFF;}
}
