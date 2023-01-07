public class Cpu {
    public boolean error;
    private int shift0;
    private int shift1;
    private int shiftAmount;

    private Disassembler dasm;
    private boolean[] parityFlagLUT;
    private static final int opcodesCyclesLUT[] = {
            4, 10, 7, 5, 5, 5, 7, 4, 4, 10, 7, 5, 5, 5, 7, 4,  // 0
            4, 10, 7, 5, 5, 5, 7, 4, 4, 10, 7, 5, 5, 5, 7, 4,  // 1
            4, 10, 16, 5, 5, 5, 7, 4, 4, 10, 16, 5, 5, 5, 7, 4,  // 2
            4, 10, 13, 5, 10, 10, 10, 4, 4, 10, 13, 5, 5, 5, 7, 4,  // 3
            5, 5, 5, 5, 5, 5, 7, 5, 5, 5, 5, 5, 5, 5, 7, 5,  // 4
            5, 5, 5, 5, 5, 5, 7, 5, 5, 5, 5, 5, 5, 5, 7, 5,  // 5
            5, 5, 5, 5, 5, 5, 7, 5, 5, 5, 5, 5, 5, 5, 7, 5,  // 6
            7, 7, 7, 7, 7, 7, 7, 7, 5, 5, 5, 5, 5, 5, 7, 5,  // 7
            4, 4, 4, 4, 4, 4, 7, 4, 4, 4, 4, 4, 4, 4, 7, 4,  // 8
            4, 4, 4, 4, 4, 4, 7, 4, 4, 4, 4, 4, 4, 4, 7, 4,  // 9
            4, 4, 4, 4, 4, 4, 7, 4, 4, 4, 4, 4, 4, 4, 7, 4,  // A
            4, 4, 4, 4, 4, 4, 7, 4, 4, 4, 4, 4, 4, 4, 7, 4,  // B
            5, 10, 10, 10, 11, 11, 7, 11, 5, 10, 10, 10, 11, 17, 7, 11, // C
            5, 10, 10, 10, 11, 11, 7, 11, 5, 10, 10, 10, 11, 17, 7, 11, // D
            5, 10, 10, 18, 11, 11, 7, 11, 5, 5, 10, 4, 11, 17, 7, 11, // E
            5, 10, 10, 4, 11, 11, 7, 11, 5, 5, 10, 4, 11, 17, 7, 11  // F
    };
    private Mmu mmu;
    private int PC;
    private int OC;
    private int SP;
    private int A, B, C, D, E, H, L;
    public int cycles;
    private boolean signFlag;
    private boolean zeroFlag;
    private boolean auxFlag;
    private boolean parityFlag;
    private boolean carryFlag;
    private boolean interruptEnable;
    public Cpu() {
        this.dasm = new Disassembler();
        this.parityFlagLUT = new boolean[0x100];
        this.OC = 0;
        this.PC = 0;
        this.zeroFlag = false;
    }

    public void init(Mmu mmu) {
        this.mmu = mmu;
        this.dasm.init(mmu, this);
        this.fillParityFlagLUT();
        this.shiftAmount = 0;

    }

    public void midFrameInterruptHandler(){
        push16(PC());
        PC(0x8);
        this.interruptEnable = false;
    }

    public void vblankInterruptHandler(){
        push16(PC());
        PC(0x10);
        this.interruptEnable = false;
    }

    private void fillParityFlagLUT(){
        for(int x = 0; x < 0x100; x++){
            int y = x^(x>>1);
            y = y^(y>>2);
            y = y^(y>>4);
            parityFlagLUT[x] = ((y & 1) == 0);
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

    private int pop8(){
        return mmu.read8(this.SP++);
    }

    private int pop16(){
        return pop8() | pop8() << 8;
    }
    private void push8(int value){
        mmu.write8(--this.SP, value);
    }
    private void push16(int value){
        this.push8(value >> 8);
        this.push8(value & 0xFF);
    }

    private int DCR(int value){
        int newValue = value - 1;
        signFlag = ((newValue & 0x80) == 1) ? true : false;
        zeroFlag = ((newValue & 0xFF) == 0) ? true : false;
        // aux ? does space invaders rlly use this ?
        parityFlag = parityFlagLUT[newValue & 0xFF];
        return newValue;
    }

    private int INR(int value){
        int newValue = value + 1;
        signFlag = ((newValue & 0x80) == 1) ? true : false;
        zeroFlag = ((newValue & 0xFF) == 0) ? true : false;
        // aux ? does space invaders rlly use this ?
        parityFlag = parityFlagLUT[newValue & 0xFF];
        return newValue;
    }

    void CP8(int value){
        int temp = A - value;
        signFlag = ((temp & 0x80) == 0x80);
        zeroFlag = ((temp & 0xFF) == 0x00);
        // needs ?
        parityFlag = parityFlagLUT[temp & 0xFF];
        carryFlag = (value > A());
    }

    void DAD(int value){
        int tmp = HL() + value;
        HL(tmp & 0xFFFF);
        carryFlag = (tmp > 0xFFFF) ? true : false;
    }

    private void OUT(int value){
        switch(value){
            case 0x02: shiftAmount = A() & 0x7; break;
            case 0x04: shift0 = shift1; shift1 = A(); break;
            default: System.out.printf("Not implemented OUT: %02X\n", value);
        }
    }

    private void IN(int value){
        switch (value){
            case 0x03:
                int v = ((shift1<<8) | shift0);
                int shifted = (v << shiftAmount) & 0xFFFF;
                A((shifted >> 8) & 0xFF); break;
            default: System.out.printf("Not implemented IN: %02X\n", value);
        }
    }

    private void RLC() {
        int value = A << 1;
        int cy = (A >> 7) & 0x1;
        A((value & 0xFF) | cy);
        carryFlag = (cy == 1);

    }

    private void RRC(){
        int value = A >> 1;
        int cy = A & 0x1;
        A((value & 0xFF) | (cy << 7));
        carryFlag = (cy == 1);
    }

    private void RAR(){
        int value = A >> 1;
        int oldCarry = carryFlag ?  1 : 0;
        int newCarry = A & 0x1;
        A((value & 0xFF) | (oldCarry << 7));
        carryFlag = (newCarry == 1);
    }

    private void RAL() {
        int value = A << 1;
        int oldCarry = carryFlag ? 1 : 0;
        int newCarry = A & 0x80;
        A((value & 0xFF) | oldCarry);
        carryFlag = (newCarry == 0x80);
    }


    void AN8(int value){
        int temp = A & value;
        signFlag = ((temp & 0x80) == 0x80) ? true : false;
        zeroFlag = ((temp & 0xFF) == 0x00) ? true : false;
        parityFlag = parityFlagLUT[temp & 0xFF];
        carryFlag = false;
        A = temp & 0xFF;
    }

    void OR8(int value){
        int temp = A | value;
        signFlag = ((temp & 0x80) == 0x80) ? true : false;
        zeroFlag = ((temp & 0xFF) == 0x00) ? true : false;
        parityFlag = parityFlagLUT[temp & 0xFF];
        carryFlag = false;
        A = temp & 0xFF;
    }

    void XR8(int value){
        int temp = A ^ value;
        signFlag = ((temp & 0x80) == 0x80) ? true : false;
        zeroFlag = ((temp & 0xFF) == 0x00) ? true : false;
        parityFlag = parityFlagLUT[temp & 0xFF];
        carryFlag = false;
        A = temp & 0xFF;
    }

    void AD8(int value){
        int temp = A + value;
        signFlag = ((temp & 0x80) == 0x80);
        zeroFlag = ((temp & 0xFF) == 0x00);
//        auxFlag
        parityFlag = parityFlagLUT[temp & 0xFF];
        carryFlag = (temp > 255);
        A = temp & 0xFF;
    }

    void SU8(int value){
        int temp = A - value;
        signFlag = ((temp & 0x80) == 0x80);
        zeroFlag = ((temp & 0xFF) == 0x00);
//        auxFlag
        parityFlag = parityFlagLUT[temp & 0xFF];
        carryFlag = (value > A);
        A = temp & 0xFF;
    }

    void cycle(){
        this.OC = mmu.read8(this.PC++);
//        System.out.printf("%04X: %02X %s  A: %02X F: %02x SP: %04X\n", this.PC++, this.OC, dasm.getMnemonic(OC), this.A(), this.F(), this.SP());

        switch (OC){
            // Misc / Control instructions
            case 0x00: ; break;// NOP
            case 0xD3: OUT(next8()); break;
            case 0xDB: IN(next8()); break;
            case 0xF3: interruptEnable = false; break;
            case 0xFB: interruptEnable = true; break;
            // Jump / Calls
            case 0xC2: PC(!zeroFlag ? next16(): PC() + 2); break; // JNZ a16
            case 0xCA: PC(zeroFlag ? next16() : PC() + 2); break; // JZ
            case 0xD2: PC(!carryFlag ? next16() : PC() + 2); break; // JNC
            case 0xDA: PC(carryFlag ? next16() : PC() + 2); break; // JC
            case 0xE2: PC(!parityFlag ? next16() : PC() + 2); break; // JPO
            case 0xEA: PC(parityFlag ? next16() : PC() + 2); break; // JPE
            case 0xF2: PC(!signFlag ? next16() : PC() + 2); break;
            case 0xFA: PC(signFlag ? next16() : PC() + 2); break;
            case 0xC3: PC(next16()); break; // JMP a16
            case 0xE9: PC(HL()); break; // PCHL

            case 0xC0: if(!zeroFlag) PC(pop16()); break; // RNZ
            case 0xC8: if(zeroFlag) PC(pop16()); break; // RZ
            case 0xD0: if(!carryFlag) PC(pop16()); break; // RNC
            case 0xD8: if(carryFlag) PC(pop16()); break; // RC
            case 0xE0: if(!parityFlag) PC(pop16()); break; // RPO
            case 0xE8: if(parityFlag) PC(pop16()); break; // RPE
            case 0xF0: if(!signFlag) PC(pop16()); break; // RP
            case 0xF8: if(signFlag) PC(pop16()); break; // RM
            case 0xC9: PC(pop16()); break; // RET
            case 0xCD: push16(PC() + 2); PC(next16()); break;// CALL a16
            case 0xC4: if(!zeroFlag) {push16(PC() + 2); PC(next16());} else PC(PC() + 2); break;
            case 0xCC: if(zeroFlag) {push16(PC() + 2); PC(next16());} else PC(PC() + 2); break;
            case 0xD4: if(!carryFlag) {push16(PC() + 2); PC(next16());} else PC(PC() + 2); break;
            case 0xDC: if(carryFlag) {push16(PC() + 2); PC(next16());} else PC(PC() + 2); break;
            case 0xE4: if(!parityFlag) {push16(PC() + 2); PC(next16());} else PC(PC() + 2); break;
            case 0xEC: if(parityFlag) {push16(PC() + 2); PC(next16());} else PC(PC() + 2); break;
            case 0xF4: if(!signFlag) {push16(PC() + 2); PC(next16());} else PC(PC() + 2); break;
            case 0xFC: if(signFlag) {push16(PC() + 2); PC(next16());} else PC(PC() + 2); break;
            case 0xC7: PC(0 * 8); break;
            case 0xCF: PC(1 * 8); break;
            case 0xD7: PC(2 * 8); break;
            case 0xDF: PC(3 * 8); break;
            case 0xE7: PC(4 * 8); break;
            case 0xEF: PC(5 * 8); break;
            case 0xF7: PC(6 * 8); break;
            case 0xFF: PC(7 * 8); break;
            // 8 bit arithmetic / logical
            case 0x27: break; //DAA
            case 0x05: B(DCR(B())); break; // DCR B
            case 0x0D: C(DCR(C())); break; // DCR C
            case 0x15: D(DCR(D())); break; // DCR D
            case 0x1D: E(DCR(E())); break; // DCR E
            case 0x25: H(DCR(H())); break; // DCR H
            case 0x2D: L(DCR(L())); break; // DCR L
            case 0x35: M(HL(), DCR(M(HL()))); break; // DCR M
            case 0x3D: A(DCR(A())); break; // DCR A

            case 0x04: B(INR(B())); break; // INR B
            case 0x0C: C(INR(C())); break; // INR C
            case 0x14: D(INR(D())); break; // INR D
            case 0x1C: E(INR(E())); break; // INR E
            case 0x24: H(INR(H())); break; // INR H
            case 0x2C: L(INR(L())); break; // INR L
            case 0x34: M(HL(), INR(M(HL()))); break; // INR HL
            case 0x3C: A(INR(A())); break; // INR A

            case 0xA0: AN8(B()); break;
            case 0xA1: AN8(C()); break;
            case 0xA2: AN8(D()); break;
            case 0xA3: AN8(E()); break;
            case 0xA4: AN8(H()); break;
            case 0xA5: AN8(L()); break;
            case 0xA6: AN8(M(HL())); break;
            case 0xA7: AN8(A()); break;
            case 0xE6: AN8(next8()); break; // ANI d8

            case 0xA8: XR8(B()); break;
            case 0xA9: XR8(C()); break;
            case 0xAA: XR8(D()); break;
            case 0xAB: XR8(E()); break;
            case 0xAC: XR8(H()); break;
            case 0xAD: XR8(L()); break;
            case 0xAE: XR8(M(HL())); break;
            case 0xAF: XR8(A()); break;
            case 0xEE: XR8(next8()); break;

            case 0xB0: OR8(B()); break;
            case 0xB1: OR8(C()); break;
            case 0xB2: OR8(D()); break;
            case 0xB3: OR8(E()); break;
            case 0xB4: OR8(H()); break;
            case 0xB5: OR8(L()); break;
            case 0xB6: OR8(M(HL())); break;
            case 0xB7: OR8(A()); break;
            case 0xF6: OR8(next8()); break;


            case 0x80: AD8(B()); break;
            case 0x81: AD8(C()); break;
            case 0x82: AD8(D()); break;
            case 0x83: AD8(E()); break;
            case 0x84: AD8(H()); break;
            case 0x85: AD8(L()); break;
            case 0x86: AD8(M(HL())); break;
            case 0x87: AD8(A()); break;
            case 0xC6: AD8(next8()); break;

            case 0x90: SU8(B()); break;
            case 0x91: SU8(C()); break;
            case 0x92: SU8(D()); break;
            case 0x93: SU8(E()); break;
            case 0x94: SU8(H()); break;
            case 0x95: SU8(L()); break;
            case 0x96: SU8(M(HL())); break;
            case 0x97: SU8(A()); break;
            case 0xD6: SU8(next8()); break;

            case 0x98: SU8(B() + (carryFlag ? 1 : 0)); break;
            case 0x99: SU8(C()+ (carryFlag ? 1 : 0)); break;
            case 0x9A: SU8(D()+ (carryFlag ? 1 : 0)); break;
            case 0x9B: SU8(E()+ (carryFlag ? 1 : 0)); break;
            case 0x9C: SU8(H()+ (carryFlag ? 1 : 0)); break;
            case 0x9D: SU8(L()+ (carryFlag ? 1 : 0)); break;
            case 0x9E: SU8(M(HL())+ (carryFlag ? 1 : 0)); break;
            case 0x9F: SU8(A()+ (carryFlag ? 1 : 0)); break;
            case 0xDE: SU8(next8() +  (carryFlag ? 1 : 0)); break;

            case 0xB8: CP8(B()); break;
            case 0xB9: CP8(C()); break;
            case 0xBA: CP8(D()); break;
            case 0xBB: CP8(E()); break;
            case 0xBC: CP8(H()); break;
            case 0xBD: CP8(L()); break;
            case 0xBE: CP8(M(HL())); break;
            case 0xBF: CP8(A()); break;
            case 0xFE: CP8(next8()); break; // CPI

            case 0x88: AD8(B() + (carryFlag ? 1 : 0)); break;
            case 0x89: AD8(C() + (carryFlag ? 1 : 0)); break;
            case 0x8A: AD8(D() + (carryFlag ? 1 : 0)); break;
            case 0x8B: AD8(E() + (carryFlag ? 1 : 0)); break;
            case 0x8C: AD8(H() + (carryFlag ? 1 : 0)); break;
            case 0x8D: AD8(L() + (carryFlag ? 1 : 0)); break;
            case 0x8E: AD8(M(HL()) + (carryFlag ? 1 : 0)); break;
            case 0x8F: AD8(A() + (carryFlag ? 1 : 0)); break;
            case 0xCE: AD8(next8() + (carryFlag ? 1 : 0)); break;

            case 0x37: carryFlag = true; break;
            case 0x3F: carryFlag = !carryFlag; break;
            case 0x17: RAL(); break;
            case 0x2F: A(~A() & 0xFF); break; // CMA
            case 0x07: RLC(); break;
            case 0x0F: RRC(); break;
            case 0x1F: RAR(); break;

            //	16bit load/store/move instructions
            case 0x01: BC(next16());  break;// LXI B d16
            case 0x11: DE(next16()); break; // LXI DE
            case 0x21: HL(next16());  break; // LXI HL
            case 0x31: SP(next16());  break; // LXI SP d16

            case 0xC1: BC(pop16()); break;
            case 0xD1: DE(pop16()); break;
            case 0xE1: HL(pop16()); break; // POP HL
            case 0xF1: AF(pop16()); break; // POP PSW

            case 0xC5: push16(BC()); break; // PUSH BC
            case 0xD5: push16(DE()); break; // PUSH DE
            case 0xE5: push16(HL()); break; // PUSH HL
            case 0xF5: push16(AF()); break; // PUSH PSW

            case 0x2A: HL(mmu.read16(next16())); break;
            case 0x02: M(BC(), A()); break; // STAX BC
            case 0x12: M(DE(), A()); break;
            case 0x22: mmu.write16(next16(), HL()); break; // SHLD
            case 0xF9: SP(HL()); break;
            case 0xE3: int _HL = HL(); HL(mmu.read16(SP())); mmu.write16(SP(), _HL); break;
            //16 bit arithmetic/ logic instructions
            case 0x03: BC(BC() + 1); break; // INX BC
            case 0x13: DE(DE() + 1); break; // INX DE
            case 0x23: HL(HL() + 1); break; // INX HL
            case 0x33: SP(SP() + 1); break; // INX SP

            case 0x0B: BC(BC() - 1); break; // DCX BC
            case 0x1B: DE(DE() - 1); break; // DCX DE
            case 0x2B: HL(HL() - 1); break; // DCX HL
            case 0x3B: SP(SP() - 1); break; // DCX SP

            case 0x09: DAD(BC()); break; // DAD HL BC
            case 0x19: DAD(DE()); break; // DAD HL DE
            case 0x29: DAD(HL()); break; // DAD HL HL
            case 0x39: DAD(SP()); break; // DAD HL SP

            // 8-bit load/store/move
            case 0x06: B(next8()); break; // MVI B, d8
            case 0x0E: C(next8()); break; // MVI C, d8
            case 0x16: D(next8()); break; // MVI D, d8
            case 0x1E: E(next8()); break; // MVI E, d8
            case 0x26: H(next8()); break; // MVI H, d8
            case 0x2E: L(next8()); break; // MVI L, d8
            case 0x36: M(HL(), next8()); break; // MVI M, d8
            case 0x3E: A(next8()); break; // MVI A, d8
            case 0x0A: A(M(BC())); break; // LDAX BC
            case 0x1A: A(M(DE())); break; // LDAX DE
            case 0x32: M(next16(), A()); break; // STA
            case 0x3A: A(M(next16())); break;// LDA A16
            case 0x40: B(B()); break;
            case 0x41: B(C()); break;
            case 0x42: B(D()); break;
            case 0x43: B(E()); break;
            case 0x44: B(H()); break;
            case 0x45: B(L()); break;
            case 0x46: B(M(HL())); break;
            case 0x47: B(A()); break;
            case 0x48: C(B()); break;
            case 0x49: C(C()); break;
            case 0x4A: C(D()); break;
            case 0x4B: C(E()); break;
            case 0x4C: C(H()); break;
            case 0x4D: C(L()); break;
            case 0x4E: C(M(HL())); break;
            case 0x4F: C(A()); break;
            case 0x50: D(B()); break;
            case 0x51: D(C()); break;
            case 0x52: D(D()); break;
            case 0x53: D(E()); break;
            case 0x54: D(H()); break;
            case 0x55: D(L()); break;
            case 0x56: D(M(HL())); break;
            case 0x57: D(A()); break;
            case 0x58: E(B()); break;
            case 0x59: E(C()); break;
            case 0x5A: E(D()); break;
            case 0x5B: E(E()); break;
            case 0x5C: E(H()); break;
            case 0x5D: E(L()); break;
            case 0x5E: E(M(HL())); break;
            case 0x5F: E(A()); break;
            case 0x60: H(B()); break;
            case 0x61: H(C()); break;
            case 0x62: H(D()); break;
            case 0x63: H(E()); break;
            case 0x64: H(H()); break;
            case 0x65: H(L()); break;
            case 0x66: H(M(HL())); break;
            case 0x67: H(A()); break;
            case 0x68: L(B()); break;
            case 0x69: L(C()); break;
            case 0x6A: L(D()); break;
            case 0x6B: L(E()); break;
            case 0x6C: L(H()); break;
            case 0x6D: L(L()); break;
            case 0x6E: L(M(HL())); break;
            case 0x6F: L(A()); break;
            case 0x70: M(HL(), B()); break;
            case 0x71: M(HL(), C()); break;
            case 0x72: M(HL(), D()); break;
            case 0x73: M(HL(), E()); break;
            case 0x74: M(HL(), H()); break;
            case 0x75: M(HL(), L()); break;
//            case 0x76: ; break;
            case 0x77: M(HL(), A()); break;
            case 0x78: A(B()); break;
            case 0x79: A(C()); break;
            case 0x7A: A(D()); break;
            case 0x7B: A(E()); break;
            case 0x7C: A(H()); break;
            case 0x7D: A(L()); break;
            case 0x7E: A(M(HL())); break;
            case 0x7F: A(A()); break;



            case 0xEB: int tmp = HL(); HL(DE()); DE(tmp); break; // XCHG


            default: error  = true; System.out.printf("Opcode not implemented: 0x%02X\n", (byte)this.OC); break;
        }
        cycles = opcodesCyclesLUT[OC];
    }

    public int A() {return A;} public void A(int value) {A = value & 0xFF;}
    public int F() {return (signFlag ? 1 : 0) << 7 | (zeroFlag ? 1 : 0) << 6 | 0 << 5| (auxFlag ? 1 : 0) << 4 | 0 << 3| (parityFlag ? 1 : 0) << 2 | 1 << 1| (carryFlag ? 1 : 0);}
    public void F(int value) {signFlag = (value & 0x80) == 0x80; zeroFlag = (value & 0x40) == 0x40; auxFlag = (value & 0x10) == 0x10; parityFlag = (value & 0x4) == 0x4; carryFlag = (value & 0x1) == 0x1;}
    public int B() {return B;} public void B(int value) {B = value & 0xFF;}
    public int C() {return C;} public void C(int value) {C = value & 0xFF;}
    public int D() {return D;} public void D(int value) {D = value & 0xFF;}
    public int E() {return E;} public void E(int value) {E = value & 0xFF;}
    public int H() {return H;} public void H(int value) {H = value & 0xFF;}
    public int L() {return L;} public void L(int value) {L = value & 0xFF;}
    public int M(int addr) {return mmu.read8(addr);} public void M(int addr, int value) {mmu.write8(addr, value);}

    public int AF() {return A << 8 | F();} public void AF(int value) {A = (value >> 8) & 0xFF; F(value & 0xFF);}
    public int BC() {return B << 8| C;} public void BC(int value) {B = (value >> 8) & 0xFF; C = value & 0xFF;}
    public int HL() {return H << 8| L;} public void HL(int value) {H = (value >> 8) & 0xFF; L = value & 0xFF;}
    public int DE() {return D << 8| E;} public void DE(int value) {D = (value >> 8) & 0xFF; E = value & 0xFF;}
    public int SP() {return this.SP;}  public void SP(int value) {this.SP = value & 0xFFFF;}
    public int PC() {return this.PC;} public void PC(int value) {this.PC = value & 0xFFFF;}
}
