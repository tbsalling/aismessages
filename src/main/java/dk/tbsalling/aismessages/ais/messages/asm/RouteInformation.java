package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitDecoder;
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

    protected RouteInformation(int designatedAreaCode, int functionalId, String binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.linkageId = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(0, 10));
        this.senderType = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(10, 15));
        this.routeType = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(15, 20));
        this.month = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(20, 24));
        this.day = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(24, 29));
        this.hour = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(29, 34));
        this.minute = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(34, 40));
        this.duration = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(40, 58));
        
        // Waypoints are variable length - store remaining data
        if (getBinaryData().length() > 58) {
            this.waypointsData = getBinaryData().substring(58);
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
