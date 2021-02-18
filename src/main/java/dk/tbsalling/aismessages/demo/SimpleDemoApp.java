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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SimpleDemoApp {

    public void runDemo() throws IOException {

        InputStream inputStream = new ByteArrayInputStream(demoNmeaStrings.getBytes());

		System.out.println("AISMessages Demo App");
		System.out.println("--------------------");

        AISInputStreamReader streamReader = new AISInputStreamReader(inputStream, aisMessage ->
            System.out.println("Received AIS message from MMSI " + aisMessage.getSourceMmsi().getMMSI() + ": " + aisMessage)
        );

        streamReader.run();
	}

    public static void main(String[] args) throws IOException {
		new SimpleDemoApp().runDemo();
	}

    private final String demoNmeaStrings = new String(
        "!AIVDM,1,1,,A,18UG;P0012G?Uq4EdHa=c;7@051@,0*53\n" +
        "!AIVDM,2,1,1,,539L8BT29ked@90F220I8TE<h4pB22222222220o1p?4400Ht00000000000,0*49\n" +
        "!AIVDM,2,2,1,,00000000008,2*6C\n" +
        "!AIVDM,1,1,,A,15NIrB0001G?endE`CpIgQSN08K6,0*02\n" +
        "!AIVDM,1,1,,B,152Hn;?P00G@K34EWE0d>?wN28KB,0*12\n" +
        "!AIVDM,1,1,,B,138Ngv0OinG>DFnDekIF6lkN00Rk,0*2E\n" +
        "!AIVDM,1,1,,B,15N06LPP00G?Sf6Egkh0TwwL0HKO,0*2B\n" +
        "!AIVDM,1,1,,A,15N:Ie0P00G@6VpEa4n68?wL0HKf,0*2C\n" +
        "!AIVDM,1,1,,B,15MqdBP000G@qoLEi69PVGaN0D0=,0*3A\n" +
        "!AIVDM,1,1,,B,B5NJ;PP005l4onUIsc@03woUoP06,0*3A\n" +
        "!AIVDM,1,1,,B,15Mv4a0P00G?<pHEeU59nwwN08L3,0*7D\n" +
        "!AIVDM,1,1,,A,35Ml=50Oh@o>Lf2EVPJI>nqP017A,0*51\n" +
        "!AIVDM,1,1,,A,15Mw0J0P01G?aLVE`VfaM?wN00RV,0*3B\n" +
        "!AIVDM,1,1,,B,16:252002lo=Gn8E7k?=0bGN0@LH,0*04\n" +
        "!AIVDM,1,1,,A,19NWsbP000o@58pE`8pHhSGP00SE,0*0B\n" +
        "!AIVDM,1,1,,B,35AjiT5000G@4vhE`ok8a6sR0Dbb,0*06\n" +
        "!AIVDM,1,1,,B,15MwksP000G@6TDEa501Uc5P08Cq,0*3B\n" +
        "!AIVDM,1,1,,A,15N59@PP00G?iGhEW<9P0?wL0HLg,0*3E\n" +
        "!AIVDM,1,1,,B,15N:`e0000G@6IlEa5O`V93L0@Lt,0*22\n" +
        "!AIVDM,1,1,,B,15Ms0FPP00o?arNEdfdUw?wR08M3,0*09\n" +
        "!AIVDM,1,1,,B,13U8W:002;o>lC`EWMwaaWiR8D10,0*09\n" +
        "!AIVDM,1,1,,B,35MA9T0Oino<fFPE1=cG75iR0000,0*4D\n" +
        "!AIVDM,1,1,,B,4h3Ovk1udq`Dio>jPHEdjdW008MI,0*63\n" +
        "!AIVDM,1,1,,B,4h3Ovl1udq`DioCkldEpGh70051@,0*25\n" +
        "!AIVDM,1,1,,B,4h3OvkQudq`Djo?UhFEf=Ko00<18,0*43\n" +
        "!AIVDM,1,1,,B,4h3Ovl1udq`DjoCkllEpGh70051@,0*2E\n" +
        "!AIVDM,1,1,,B,35OqO05vh0G@8GREWEmVVwwT0000,0*3D\n" +
        "!AIVDM,1,1,,B,35Ml=5000=o>LeVEVPH96ns`0000,0*50\n" +
        "!AIVDM,1,1,,B,15>gpr0PAuG=AglDjcc68Ts200S2,0*4A\n" +
        "!AIVDM,1,1,,B,18UG;P000pG?UgdEdOeeec6t08DW,0*0A\n" +
        "!AIVDM,1,1,,B,85MwpKiKf0wLgSt5BlHF<3FMlaSRCjf1?Nq;4TAA7Mj:oOH5bs=8,0*7D\n" +
        "!AIVDM,1,1,,A,152Hn;?P00G@K3HEWDot<gw82HDi,0*5B\n" +
        "!AIVDM,1,1,,B,152SGj001so?U5fEg5j8?VU808Dm,0*19\n" +
        "!AIVDM,1,1,,B,15NIrB0001G?envE`Cp9gQG80D18,0*09\n" +
        "!AIVDM,1,1,,B,15MwpWhP1so?KpFEaiOL<Ow60HE>,0*14\n" +
        "!AIVDM,1,1,,B,16:252002uo=FHHE86H=8:G600S?,0*5F\n" +
        "!AIVDM,1,1,,A,138Ngv001uG>EINDeV;654k:0@EJ,0*69\n" +
        "!AIVDM,1,1,,B,15N:Ie0P00G@6W>Ea4ollOw600S0,0*53\n" +
        "!AIVDM,1,1,,A,15N06LPP00G?SdvEgki0Tww80@ET,0*02\n" +
        "!AIVDM,1,1,,B,13U8W:002@o>ipDEWH19d7k88@El,0*5A\n" +
        "!AIVDM,1,1,,B,18UG7V0019G?ithE`a;m;D;600SB,0*2B\n" +
        "!AIVDM,1,1,,B,15MA9T001no<fEpE0wno25i:0@F7,0*49\n" +
        "!AIVDM,1,1,,B,33TWed1001G?tg@EUg3cBV?80000,0*38\n" +
        "!AIVDM,1,1,,A,15MwksP000G@6T`Ea501Ms5:0D0w,0*77\n" +
        "!AIVDM,1,1,,A,15MiuGg000o?<b6EeVq8;aW:0HF=,0*50\n" +
        "!AIVDM,1,1,,B,19NWsbP000o@59BE`8qFJ3G<0HFK,0*79\n" +
        "!AIVDM,1,1,,B,15Ml=50P@Do>LR`EVNsHQFc>00RJ,0*77\n" +
        "!AIVDM,1,1,,B,15Mw0J0P02G?aLRE`Vf`mOw<08Fd,0*32\n" +
        "!AIVDM,1,1,,A,15Mv4a0P00G?<plEeU3anww<0HFi,0*56\n" +
        "!AIVDM,1,1,,A,15N:`e0000G@6InEa5OTDq160<11,0*71\n" +
        "!AIVDM,1,1,,B,15N59@PP00G?iGhEW<9P0?w:0<16,0*13\n" +
        "!AIVDM,1,1,,A,35MA9T001no<fF6E0wVG25k>0000,0*1A\n" +
        "!AIVDM,1,1,,A,Dh3Ovk0nIN>4,0*38\n" +
        "!AIVDM,1,1,,B,15ND4kP001G@6I@Ea5AM;I3>0<0w,0*04\n" +
        "!AIVDM,1,1,,B,Dh3Ovl0sqN>4,0*19\n" +
        "!AIVDM,1,1,,A,Dh3Ovl0mUN>4,0*20\n" +
        "!AIVDM,1,1,,B,Dh3Ovk0tMN>4,0*25\n" +
        "!AIVDM,1,1,,A,Dh3Ovl0mMN>4,0*38\n" +
        "!AIVDM,1,1,,A,13:112002?o@FRnDS<bdu:E:08GQ,0*77\n" +
        "!AIVDM,1,1,,A,4h3Ovk1udq`FWo>jPHEdjdW0051H,0*2C\n" +
        "!AIVDM,1,1,,A,15N6r>P000G<dG0Esaod<:U@08GM,0*53\n" +
        "!AIVDM,1,1,,A,4h3OvkQudq`F`o?UhFEf=Ko00D1;,0*33\n" +
        "!AIVDM,1,1,,B,15Ph;00Oi@o@V?PDmKanwUaB08Gs,0*02\n" +
        "!AIVDM,1,1,,A,15Mva0P00no?Ui>EdS;MobMB08Gt,0*19\n" +
        "!AIVDM,1,1,,B,15NGH8POi8G?ii4E`bPE74?p0U1H,0*58\n" +
        "!AIVDM,1,1,,A,15MwDf0P00G?<k4EeSU@Ugw@00Sm,0*1C\n" +
        "!AIVDM,1,1,,B,15MvlfP000G?lwrEd9aJIicD0D1;,0*2B\n" +
        "!AIVDM,1,1,,A,16:252002io=FE@E87S=3:IB0<09,0*47\n" +
        "!AIVDM,1,1,,B,15MwlV0P00G@6N8Ea5FujwwD08I0,0*7B\n" +
        "!AIVDM,1,1,,A,15NGdT?001G?eWRE`E9r8QoF2D11,0*10\n" +
        "!AIVDM,1,1,,A,15ND4kP000G@6I@Ea5AGhI3D0HI6,0*69\n" +
        "!AIVDM,1,1,,B,15M67FO000G@7EHEa28cvRsF251H,0*4B\n" +
        "!AIVDM,1,1,,B,15NH7?PP00G@>aTEWwd<<wwJ0@It,0*25\n" +
        "!AIVDM,1,1,,A,15MQqQ0P00G?iH>EW<<@0?wD08J4,0*01\n" +
        "!AIVDM,1,1,,B,15NHHAP000G@rn<Ei:<5c1eJ00Ss,0*2B\n" +
        "!AIVDM,1,1,,A,15?ECL001=G<wHPEON52>QeH08JK,0*47\n" +
        "!AIVDM,1,1,,B,13:112002?o@FNbDS=Ntu:EF00ST,0*3C\n" +
        "!AIVDM,1,1,,A,15>gpr001sG=AnHDjb>V3TwF08Jd,0*72\n" +
        "!AIVDM,1,1,,A,152SGj001to?TvlEg4`H?6UL08Jo,0*36\n" +
        "\\c:1609841515,s:r3669961*78\\!AIVDM,1,1,,A,13ukmN7@0<0pRcHPTkn4P33f0000,0*58\n" +
        "\\c:1609841515,s:r3669961,g:1-2-1234*0E\\!AIVDM,1,1,,A,13ukmN7@0<0pRcHPTkn4P33f0000,0*58\n"
    );

}
