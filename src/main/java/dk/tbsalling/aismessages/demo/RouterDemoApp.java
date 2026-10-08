/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 *
 * (C) Copyright 2011- by S-Consult ApS, VAT no. DK31327490, Denmark.
 *
 * Released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * For details of this license see the nearby LICENCE-full file, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 *
 * NOT FOR COMMERCIAL USE!
 * Contact Thomas Borg Salling <tbsalling@tbsalling.dk> to obtain a commercially licensed version of this software.
 *
 */

package dk.tbsalling.aismessages.demo;

import dk.tbsalling.aismessages.AISInputStreamReader;
import dk.tbsalling.aismessages.AISMessageRouter;
import dk.tbsalling.aismessages.ais.messages.BaseStationReport;
import dk.tbsalling.aismessages.ais.messages.DynamicDataReport;
import dk.tbsalling.aismessages.ais.messages.PositionReport;
import dk.tbsalling.aismessages.ais.messages.ShipAndVoyageData;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class RouterDemoApp {

    public void runDemo() {

        InputStream inputStream = new ByteArrayInputStream(demoNmeaStrings.getBytes());

        System.out.println("AISMessages Router Demo App");
        System.out.println("---------------------------");

        // Route each decoded message to the handler for its type. When several handlers match,
        // the most specific one wins: PositionReport beats DynamicDataReport for types 1, 2 and 3.
        AISMessageRouter router = AISMessageRouter.builder()
                .on(PositionReport.class, positionReport -> System.out.println("Class A position from MMSI " + positionReport.getSourceMmsi().getMmsi() + ": " + positionReport.getLatitude() + ", " + positionReport.getLongitude() + " at " + positionReport.getSpeedOverGround() + " kn"))
                .on(ShipAndVoyageData.class, shipAndVoyageData -> System.out.println("Ship and voyage data from MMSI " + shipAndVoyageData.getSourceMmsi().getMmsi() + ": " + shipAndVoyageData.getShipName() + " (" + shipAndVoyageData.getCallsign() + ")"))
                .on(BaseStationReport.class, baseStationReport -> System.out.println("Base station MMSI " + baseStationReport.getSourceMmsi().getMmsi() + " at " + baseStationReport.getLatitude() + ", " + baseStationReport.getLongitude()))
                .on(DynamicDataReport.class, dynamicDataReport -> System.out.println("Other dynamic data report (" + dynamicDataReport.getClass().getSimpleName() + "): " + dynamicDataReport.getLatitude() + ", " + dynamicDataReport.getLongitude()))
                .otherwise(aisMessage -> System.out.println("Unrouted " + aisMessage.getMessageType() + " from MMSI " + aisMessage.getSourceMmsi().getMmsi()))
                .onError(e -> System.err.println("Message handler failed: " + e))
                .build();

        // The router is a plain Consumer<AISMessage>, so it plugs straight into AISInputStreamReader.
        AISInputStreamReader streamReader = new AISInputStreamReader(inputStream, router);
        streamReader.run();
    }

    public static void main(String[] args) {
        new RouterDemoApp().runDemo();
    }

    private final String demoNmeaStrings = """
            !AIVDM,1,1,,A,18UG;P0012G?Uq4EdHa=c;7@051@,0*53
            !AIVDM,2,1,1,,539L8BT29ked@90F220I8TE<h4pB22222222220o1p?4400Ht00000000000,0*49
            !AIVDM,2,2,1,,00000000008,2*6C
            !AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A
            !AIVDM,1,1,,B,B5NJ;PP005l4onUIsc@03woUoP06,0*3A
            !AIVDM,1,1,,A,35Ml=50Oh@o>Lf2EVPJI>nqP017A,0*51
            !AIVDM,1,1,,B,4h3Ovk1udq`Dio>jPHEdjdW008MI,0*63
            !AIVDM,1,1,,B,85MwpKiKf0wLgSt5BlHF<3FMlaSRCjf1?Nq;4TAA7Mj:oOH5bs=8,0*7D
            !AIVDM,1,1,,A,Dh3Ovk0nIN>4,0*38
            """;

}
