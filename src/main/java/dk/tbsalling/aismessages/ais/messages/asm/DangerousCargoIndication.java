package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitDecoder;
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

    protected DangerousCargoIndication(int designatedAreaCode, int functionalId, String binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.unit = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(0, 2));
        this.amount = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(2, 12));
        this.cargoCode = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(12, 19));
        this.imoClass = ImoClass.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(19, 23)));
        this.unNumber = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(23, 36));
        this.bc = BitDecoder.INSTANCE.decodeBoolean(getBinaryData().substring(36, 37));
        this.marpol = BitDecoder.INSTANCE.decodeBoolean(getBinaryData().substring(37, 38));
        this.category = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(38, 42));
    }

    int unit;
    int amount;
    int cargoCode;
    ImoClass imoClass;
    int unNumber;
    boolean bc;
    boolean marpol;
    int category;

    public enum ImoClass {
        NOT_AVAILABLE(0),
        CLASS_1(1),
        CLASS_1_1(2),
        CLASS_1_2(3),
        CLASS_1_3(4),
        CLASS_1_4(5),
        CLASS_1_5(6),
        CLASS_1_6(7),
        CLASS_2(8),
        CLASS_2_1(9),
        CLASS_2_2(10),
        CLASS_2_3(11),
        CLASS_3(12),
        CLASS_4(13),
        CLASS_4_1(14),
        CLASS_4_2(15);

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
