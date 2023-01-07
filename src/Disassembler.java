public class Disassembler {
    Mmu mmu;
    Cpu cpu;
    public void init(Mmu mmu, Cpu cpu){
        this.mmu = mmu;
        this.cpu = cpu;
    }
    String getMnemonic(int opcode){
        switch(opcode){
            case 0x00: return mnemonicTable[opcode];
            case 0x01: return String.format("%s#$%04X", mnemonicTable[opcode], mmu.read16(cpu.PC()));
            case 0x03: return mnemonicTable[opcode];
            case 0x05: return mnemonicTable[opcode];
            case 0x06: return String.format("%s$%02X", mnemonicTable[opcode], mmu.read8(cpu.PC()));
            case 0x09: return mnemonicTable[opcode];
            case 0x0D: return mnemonicTable[opcode];
            case 0x0E: return String.format("%s$%02X", mnemonicTable[opcode], mmu.read8(cpu.PC()));
            case 0x0F: return mnemonicTable[opcode];
            case 0x11: return String.format("%s#$%04X", mnemonicTable[opcode], mmu.read16(cpu.PC()));
            case 0x13: return mnemonicTable[opcode];
            case 0x15: return mnemonicTable[opcode];
            case 0x1A: return mnemonicTable[opcode];
            case 0x1B: return mnemonicTable[opcode];
            case 0x19: return mnemonicTable[opcode];
            case 0x1D: return mnemonicTable[opcode];
            case 0x21: return String.format("%s#$%04X", mnemonicTable[opcode], mmu.read16(cpu.PC()));
            case 0x23: return mnemonicTable[opcode];
            case 0x25: return mnemonicTable[opcode];
            case 0x26: return String.format("%s#$%02X", mnemonicTable[opcode], mmu.read8(cpu.PC()));
            case 0x29: return mnemonicTable[opcode];
            case 0x2D: return mnemonicTable[opcode];
            case 0x31: return String.format("%s#$%04X", mnemonicTable[opcode], mmu.read16(cpu.PC()));
            case 0x32: return String.format("%s $%04X", mnemonicTable[opcode], mmu.read16(cpu.PC()));
            case 0x33: return mnemonicTable[opcode];
            case 0x35: return mnemonicTable[opcode];
            case 0x36: return String.format("%s#$%02X", mnemonicTable[opcode], mmu.read8(cpu.PC()));
            case 0x39: return mnemonicTable[opcode];
            case 0x3D: return mnemonicTable[opcode];
            case 0x40: return mnemonicTable[opcode];
            case 0x41: return mnemonicTable[opcode];
            case 0x42: return mnemonicTable[opcode];
            case 0x43: return mnemonicTable[opcode];
            case 0x44: return mnemonicTable[opcode];
            case 0x45: return mnemonicTable[opcode];
            case 0x46: return mnemonicTable[opcode];
            case 0x47: return mnemonicTable[opcode];
            case 0x48: return mnemonicTable[opcode];
            case 0x49: return mnemonicTable[opcode];
            case 0x4A: return mnemonicTable[opcode];
            case 0x4B: return mnemonicTable[opcode];
            case 0x4C: return mnemonicTable[opcode];
            case 0x4D: return mnemonicTable[opcode];
            case 0x4E: return mnemonicTable[opcode];
            case 0x4F: return mnemonicTable[opcode];
            case 0x50: return mnemonicTable[opcode];
            case 0x51: return mnemonicTable[opcode];
            case 0x52: return mnemonicTable[opcode];
            case 0x53: return mnemonicTable[opcode];
            case 0x54: return mnemonicTable[opcode];
            case 0x55: return mnemonicTable[opcode];
            case 0x56: return mnemonicTable[opcode];
            case 0x57: return mnemonicTable[opcode];
            case 0x58: return mnemonicTable[opcode];
            case 0x59: return mnemonicTable[opcode];
            case 0x5A: return mnemonicTable[opcode];
            case 0x5B: return mnemonicTable[opcode];
            case 0x5C: return mnemonicTable[opcode];
            case 0x5D: return mnemonicTable[opcode];
            case 0x5E: return mnemonicTable[opcode];
            case 0x5F: return mnemonicTable[opcode];
            case 0x60: return mnemonicTable[opcode];
            case 0x61: return mnemonicTable[opcode];
            case 0x62: return mnemonicTable[opcode];
            case 0x63: return mnemonicTable[opcode];
            case 0x64: return mnemonicTable[opcode];
            case 0x65: return mnemonicTable[opcode];
            case 0x66: return mnemonicTable[opcode];
            case 0x67: return mnemonicTable[opcode];
            case 0x68: return mnemonicTable[opcode];
            case 0x69: return mnemonicTable[opcode];
            case 0x6A: return mnemonicTable[opcode];
            case 0x6B: return mnemonicTable[opcode];
            case 0x6C: return mnemonicTable[opcode];
            case 0x6D: return mnemonicTable[opcode];
            case 0x6E: return mnemonicTable[opcode];
            case 0x6F: return mnemonicTable[opcode];
            case 0x70: return mnemonicTable[opcode];
            case 0x71: return mnemonicTable[opcode];
            case 0x72: return mnemonicTable[opcode];
            case 0x73: return mnemonicTable[opcode];
            case 0x74: return mnemonicTable[opcode];
            case 0x75: return mnemonicTable[opcode];
            case 0x76: return mnemonicTable[opcode];
            case 0x77: return mnemonicTable[opcode];
            case 0x78: return mnemonicTable[opcode];
            case 0x79: return mnemonicTable[opcode];
            case 0x7A: return mnemonicTable[opcode];
            case 0x7B: return mnemonicTable[opcode];
            case 0x7C: return mnemonicTable[opcode];
            case 0x7D: return mnemonicTable[opcode];
            case 0x7E: return mnemonicTable[opcode];
            case 0x7F: return mnemonicTable[opcode];
            case 0x80: return String.format("%s #$%02X", mnemonicTable[opcode], mmu.read8(cpu.PC()));
            case 0x81: return String.format("%s #$%02X", mnemonicTable[opcode], mmu.read8(cpu.PC()));
            case 0x82: return String.format("%s #$%02X", mnemonicTable[opcode], mmu.read8(cpu.PC()));
            case 0x83: return String.format("%s #$%02X", mnemonicTable[opcode], mmu.read8(cpu.PC()));
            case 0x84: return String.format("%s #$%02X", mnemonicTable[opcode], mmu.read8(cpu.PC()));
            case 0x85: return String.format("%s #$%02X", mnemonicTable[opcode], mmu.read8(cpu.PC()));
            case 0x86: return String.format("%s #$%02X", mnemonicTable[opcode], mmu.read8(cpu.PC()));
            case 0x87: return String.format("%s #$%02X", mnemonicTable[opcode], mmu.read8(cpu.PC()));
            case 0xA1: return String.format("%s #$%02X", mnemonicTable[opcode], mmu.read8(cpu.PC()));
            case 0xA2: return String.format("%s #$%02X", mnemonicTable[opcode], mmu.read8(cpu.PC()));
            case 0xA3: return String.format("%s #$%02X", mnemonicTable[opcode], mmu.read8(cpu.PC()));
            case 0xA4: return String.format("%s #$%02X", mnemonicTable[opcode], mmu.read8(cpu.PC()));
            case 0xA5: return String.format("%s #$%02X", mnemonicTable[opcode], mmu.read8(cpu.PC()));
            case 0xA6: return String.format("%s #$%02X", mnemonicTable[opcode], mmu.read8(cpu.PC()));
            case 0xA7: return String.format("%s #$%02X", mnemonicTable[opcode], mmu.read8(cpu.PC()));
            case 0xC3: return String.format("%s $%04X", mnemonicTable[opcode], mmu.read16(cpu.PC()));
            case 0xC1: return mnemonicTable[opcode];
            case 0xC2: return String.format("%s $%04X", mnemonicTable[opcode], mmu.read16(cpu.PC()));
            case 0xC5: return mnemonicTable[opcode];
            case 0xC6: return String.format("%s #$%02X", mnemonicTable[opcode], mmu.read8(cpu.PC()));
            case 0xC9: return mnemonicTable[opcode];
            case 0xCD: return String.format("%s $%04X", mnemonicTable[opcode], mmu.read16(cpu.PC()));
            case 0xD1: return mnemonicTable[opcode];
            case 0xD3: return String.format("%s #$%02X", mnemonicTable[opcode], mmu.read8(cpu.PC()));
            case 0xD5: return mnemonicTable[opcode];
            case 0xE1: return mnemonicTable[opcode];
            case 0xE5: return mnemonicTable[opcode];
            case 0xE6: return String.format("%s #$%02X", mnemonicTable[opcode], mmu.read8(cpu.PC()));
            case 0xEB: return mnemonicTable[opcode];
            case 0xF1: return mnemonicTable[opcode];
            case 0xFE: return String.format("%s #$%02X", mnemonicTable[opcode], mmu.read8(cpu.PC()));
            case 0xF5: return mnemonicTable[opcode];
            default: return mnemonicTable[opcode];
        }
    }

    private String[] mnemonicTable =
            {
                    "NOP",
                    "LXI B,",
                    "STAX B",
                    "INX B",
                    "INR B",
                    "DCR B",
                    "MVI B,",
                    "RLC",
                    "NOP",
                    "DAD",
                    "LDAX B",
                    "DCX B",
                    "INR C",
                    "DCR C",
                    "MVI C,",
                    "RRC",
                    "NOP",
                    "LXI D,",
                    "STAX D",
                    "INX D",
                    "INR D",
                    "DCR D",
                    "MVI D,",
                    "RAL",
                    "NOP",
                    "DAD D",
                    "LDAX D",
                    "DCX D",
                    "INR E",
                    "DCR E",
                    "MVI E,",
                    "RAR",
                    "NOP",
                    "LXI H,",
                    "SHLD ",
                    "INX H",
                    "INR H",
                    "DCR H",
                    "MVI H,",
                    "DAA",
                    "NOP",
                    "DAD H",
                    "LHLD",
                    "DCX H",
                    "INR L",
                    "DCR L",
                    "MVI L,",
                    "CMA",
                    "NOP",
                    "LXI SP,",
                    "STA",
                    "INX SP",
                    "INR M",
                    "DCR M",
                    "MVI M,",
                    "STC",
                    "NOP",
                    "DAD SP",
                    "LDA",
                    "DCX SP",
                    "INR A",
                    "DCR A",
                    "MVI A,",
                    "CMC",
                    "MOV B, B",
                    "MOV B, C",
                    "MOV B, D",
                    "MOV B, E",
                    "MOV B, H",
                    "MOV B, L",
                    "MOV B, M",
                    "MOV B, A",
                    "MOV C, B",
                    "MOV C, C",
                    "MOV C, D",
                    "MOV C, E",
                    "MOV C, H",
                    "MOV C, L",
                    "MOV C, M",
                    "MOV C, A",
                    "MOV D, B",
                    "MOV D, C",
                    "MOV D, D",
                    "MOV D, E",
                    "MOV D, H",
                    "MOV D, L",
                    "MOV D, M",
                    "MOV D, A",
                    "MOV E, B",
                    "MOV E, C",
                    "MOV E, D",
                    "MOV E, E",
                    "MOV E, H",
                    "MOV E, L",
                    "MOV E, M",
                    "MOV E, A",
                    "MOV H, B",
                    "MOV H, C",
                    "MOV H, D",
                    "MOV H, E",
                    "MOV H, H",
                    "MOV H, L",
                    "MOV H, M",
                    "MOV H, A",
                    "MOV L, B",
                    "MOV L, C",
                    "MOV L, D",
                    "MOV L, E",
                    "MOV L, H",
                    "MOV L, L",
                    "MOV L, M",
                    "MOV L, A",
                    "MOV M, B",
                    "MOV M, C",
                    "MOV M, D",
                    "MOV M, E",
                    "MOV M, H",
                    "MOV M, L",
                    "HLT",
                    "MOV M, A",
                    "MOV A, B",
                    "MOV A, C",
                    "MOV A, D",
                    "MOV A, E",
                    "MOV A, H",
                    "MOV A, L",
                    "MOV A, M",
                    "MOV A, A",
                    "ADD B",
                    "ADD C",
                    "ADD D",
                    "ADD E",
                    "ADD H",
                    "ADD L",
                    "ADD M",
                    "ADD A",
                    "ADC B",
                    "ADC C",
                    "ADC D",
                    "ADC E",
                    "ADC H",
                    "ADC L",
                    "ADC M",
                    "ADC A",
                    "SUB B",
                    "SUB C",
                    "SUB D",
                    "SUB E",
                    "SUB H",
                    "SUB L",
                    "SUB M",
                    "SUB A",
                    "SBB B",
                    "SBB C",
                    "SBB D",
                    "SBB E",
                    "SBB H",
                    "SBB L",
                    "SBB M",
                    "SBB A",
                    "ANA B",
                    "ANA C",
                    "ANA D",
                    "ANA E",
                    "ANA H",
                    "ANA L",
                    "ANA M",
                    "ANA A",
                    "XRA B",
                    "XRA C",
                    "XRA D",
                    "XRA E",
                    "XRA H",
                    "XRA L",
                    "XRA M",
                    "XRA A",
                    "ORA B",
                    "ORA C",
                    "ORA D",
                    "ORA E",
                    "ORA H",
                    "ORA L",
                    "ORA M",
                    "ORA A",
                    "CMP B",
                    "CMP C",
                    "CMP D",
                    "CMP E",
                    "CMP H",
                    "CMP L",
                    "CMP M",
                    "CMP A",
                    "RNZ",
                    "POP B",
                    "JNZ",
                    "JMP",
                    "CNZ",
                    "PUSH B",
                    "ADI",
                    "RST 0",
                    "RZ",
                    "RET",
                    "JZ",
                    "JMP",
                    "CZ",
                    "CALL",
                    "ACI",
                    "RST 1",
                    "RNC",
                    "POP D",
                    "JNC",
                    "OUT",
                    "CNC",
                    "PUSH D",
                    "SUI",
                    "RST 2",
                    "RC",
                    "RET",
                    "JC",
                    "IN",
                    "CC",
                    "CALL",
                    "SBI",
                    "RST 3",
                    "RPO",
                    "POP H",
                    "JPO",
                    "XTHL",
                    "CPO",
                    "PUSH H",
                    "ANI",
                    "RST 4",
                    "RPE",
                    "PCHL",
                    "JPE",
                    "XCHG",
                    "CPE",
                    "CALL",
                    "XRI",
                    "RST 5",
                    "RP",
                    "POP PSW",
                    "JP",
                    "DI",
                    "CP",
                    "PUSH PSW",
                    "ORI",
                    "RST 6",
                    "RM",
                    "SPHL",
                    "JM",
                    "EI",
                    "CM",
                    "CALL",
                    "CPI",
                    "RST 7"
            };
}
