package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.AISText;
import dk.tbsalling.aismessages.ais.BitString;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * Inland AIS - Inland Ship Static and Voyage Related Data (DAC=200, FI=10)
 * Regional application specific message of the European Inland AIS standard, carried in Message 8, supplementing
 * Message 5 for inland waterway vessels with the ENI, dimensions in decimetres, ERI ship type, hazardous cargo,
 * draught, loading status and quality of speed, course and heading information.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InlandShipStaticAndVoyageRelatedData extends ApplicationSpecificMessage {

    protected InlandShipStaticAndVoyageRelatedData(int designatedAreaCode, int functionalId, BitString binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.uniqueEuropeanVesselIdentificationNumber = AISText.decode(getBinaryData(), 0, 48);
        this.lengthOfShip = getBinaryData().getUnsignedInt(48, 61) / 10f;
        this.beamOfShip = getBinaryData().getUnsignedInt(61, 71) / 10f;
        this.shipOrCombinationType = getBinaryData().getUnsignedInt(71, 85);
        this.hazardousCargo = getBinaryData().getUnsignedInt(85, 88);
        this.draught = getBinaryData().getUnsignedInt(88, 99) / 100f;
        this.loaded = getBinaryData().getUnsignedInt(99, 101);
        this.qualityOfSpeedInformation = getBinaryData().getUnsignedInt(101, 102);
        this.qualityOfCourseInformation = getBinaryData().getUnsignedInt(102, 103);
        this.qualityOfHeadingInformation = getBinaryData().getUnsignedInt(103, 104);
    }

    /** Bits 0-48 of the application data (Inland AIS, DAC=200 FI=10): unique European vessel identification number (ENI), up to 8 six-bit ASCII characters (ITU-R M.1371-6 Table 45); padding removed. */
    String uniqueEuropeanVesselIdentificationNumber;
    /** Bits 48-61 of the application data (Inland AIS, DAC=200 FI=10): length of ship (transmitted value ÷ 10). */
    float lengthOfShip;
    /** Bits 61-71 of the application data (Inland AIS, DAC=200 FI=10): beam of ship (transmitted value ÷ 10). */
    float beamOfShip;
    /** Bits 71-85 of the application data (Inland AIS, DAC=200 FI=10): ship or combination type. */
    int shipOrCombinationType;
    /** Bits 85-88 of the application data (Inland AIS, DAC=200 FI=10): hazardous cargo. */
    int hazardousCargo;
    /** Bits 88-99 of the application data (Inland AIS, DAC=200 FI=10): draught (transmitted value ÷ 100). */
    float draught;
    /** Bits 99-101 of the application data (Inland AIS, DAC=200 FI=10): loaded. */
    int loaded;
    /** Bits 101-102 of the application data (Inland AIS, DAC=200 FI=10): quality of speed information. */
    int qualityOfSpeedInformation;
    /** Bits 102-103 of the application data (Inland AIS, DAC=200 FI=10): quality of course information. */
    int qualityOfCourseInformation;
    /** Bits 103-104 of the application data (Inland AIS, DAC=200 FI=10): quality of heading information. */
    int qualityOfHeadingInformation;
}
