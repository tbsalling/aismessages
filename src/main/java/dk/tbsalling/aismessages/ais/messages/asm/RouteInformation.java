package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitString;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * IMO SN.1/Circ.289 - Route Information (DAC=1, FI=27 broadcast or FI=28 addressed)
 * Provides information about a planned route
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RouteInformation extends ApplicationSpecificMessage {

    protected RouteInformation(int designatedAreaCode, int functionalId, BitString binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.linkageId = getBinaryData().getUnsignedInt(0, 10);
        this.senderType = getBinaryData().getUnsignedInt(10, 15);
        this.routeType = getBinaryData().getUnsignedInt(15, 20);
        this.month = getBinaryData().getUnsignedInt(20, 24);
        this.day = getBinaryData().getUnsignedInt(24, 29);
        this.hour = getBinaryData().getUnsignedInt(29, 34);
        this.minute = getBinaryData().getUnsignedInt(34, 40);
        this.duration = getBinaryData().getUnsignedInt(40, 58);
        
        // Waypoints are variable length - store remaining data
        if (getBinaryData().length() > 58) {
            this.waypointsData = getBinaryData().getBits(58, getBinaryData().length());
        } else {
            this.waypointsData = "";
        }
    }

    int linkageId;
    int senderType;
    int routeType;
    int month;
    int day;
    int hour;
    int minute;
    int duration;
    String waypointsData;

}
