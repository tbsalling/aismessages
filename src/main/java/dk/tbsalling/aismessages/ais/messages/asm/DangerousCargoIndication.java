package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitString;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import static java.util.Arrays.stream;

/**
 * IMO SN.1/Circ.289 - Dangerous Cargo Indication (DAC=1, FI=25)
 * Information about dangerous cargo being transported
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DangerousCargoIndication extends ApplicationSpecificMessage {

    protected DangerousCargoIndication(int designatedAreaCode, int functionalId, BitString binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.unit = getBinaryData().getUnsignedInt(0, 2);
        this.amount = getBinaryData().getUnsignedInt(2, 12);
        this.cargoCode = getBinaryData().getUnsignedInt(12, 19);
        this.imoClass = ImoClass.valueOf(getBinaryData().getUnsignedInt(19, 23));
        this.unNumber = getBinaryData().getUnsignedInt(23, 36);
        this.bc = getBinaryData().getBoolean(36, 37);
        this.marpol = getBinaryData().getBoolean(37, 38);
        this.category = getBinaryData().getUnsignedInt(38, 42);
    }

    /** Bits 0-2 of the application data (IMO SN.1/Circ.289, DAC=1 FI=25): unit. */
    int unit;
    /** Bits 2-12 of the application data (IMO SN.1/Circ.289, DAC=1 FI=25): amount. */
    int amount;
    /** Bits 12-19 of the application data (IMO SN.1/Circ.289, DAC=1 FI=25): cargo code. */
    int cargoCode;
    /** Bits 19-23 of the application data (IMO SN.1/Circ.289, DAC=1 FI=25): IMO class code, decoded as {@link ImoClass}. */
    ImoClass imoClass;
    /** Bits 23-36 of the application data (IMO SN.1/Circ.289, DAC=1 FI=25): UN number. */
    int unNumber;
    /** Bit 36 of the application data (IMO SN.1/Circ.289, DAC=1 FI=25): BC flag. */
    boolean bc;
    /** Bit 37 of the application data (IMO SN.1/Circ.289, DAC=1 FI=25): MARPOL flag. */
    boolean marpol;
    /** Bits 38-42 of the application data (IMO SN.1/Circ.289, DAC=1 FI=25): category. */
    int category;

    /** Codes of the IMO class field, bits 19-23 of the application data (IMO SN.1/Circ.289, DAC=1 FI=25). */
    public enum ImoClass {
        /** 0 = not available. */
        NOT_AVAILABLE(0),
        /** 1 = class 1. */
        CLASS_1(1),
        /** 2 = class 1 1. */
        CLASS_1_1(2),
        /** 3 = class 1 2. */
        CLASS_1_2(3),
        /** 4 = class 1 3. */
        CLASS_1_3(4),
        /** 5 = class 1 4. */
        CLASS_1_4(5),
        /** 6 = class 1 5. */
        CLASS_1_5(6),
        /** 7 = class 1 6. */
        CLASS_1_6(7),
        /** 8 = class 2. */
        CLASS_2(8),
        /** 9 = class 2 1. */
        CLASS_2_1(9),
        /** 10 = class 2 2. */
        CLASS_2_2(10),
        /** 11 = class 2 3. */
        CLASS_2_3(11),
        /** 12 = class 3. */
        CLASS_3(12),
        /** 13 = class 4. */
        CLASS_4(13),
        /** 14 = class 4 1. */
        CLASS_4_1(14),
        /** 15 = class 4 2. */
        CLASS_4_2(15);

        /** The code as transmitted. */
        int value;

        ImoClass(int value) {
            this.value = value;
        }

        public static ImoClass valueOf(int value) {
            return stream(values())
                    .filter(e -> e.value == value)
                    .findFirst()
                    .orElse(NOT_AVAILABLE);
        }
    }

}
