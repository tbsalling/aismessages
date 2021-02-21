![Build status](https://travis-ci.org/tbsalling/aismessages.svg?branch=master)
[![License](http://img.shields.io/badge/license-CCANS3-green.svg)](https://github.com/tbsalling/aismessages/blob/master/LICENSE)

Introduction
---
AISmessages is a Java-based light-weight, zero-dependency, and ultra-efficient message decoder for maritime
navigation and safety messages compliant with ITU 1371 (NMEA armoured AIS messages).

It exhibits lazy decoding and fully decodes tens of thousands of NMEA armoured AIS messages per second per CPU
core on an Intel i7-based laptop.

For more than 10 years AISmessages has been used in production in many systems and solutions all over the world.

If you are new to AIS you can read a short introduction to it on [my blog](https://tbsalling.dk/2018/09/01/an-introduction-to-ais/). 

Other AIS projects
---
In addition to AISmessages, its sister project [AISutils](https://github.com/tbsalling/aisutils) offers higher level functionality such as Tracking and Filtering using AISmessages as a foundation.

There is also [AIScli](https://github.com/tbsalling/aiscli) which allows conversion, filtering, etc. of AIS messages from the command line. AIScli 
is built on top of both AISmessages and AISutils.

Applications, demos, and talks
---
There are several demos, intros, and public appearances of AISmessages; like for instance:

1. How AISmessages is utilized by OpenRemote, Inc. for the Safe Waterways project in the Beatrix Canal, NL, as explained in this [Youtube video](https://youtu.be/_pcH0KB5J2Q):<br>
[![IMAGE ALT TEXT HERE](http://img.youtube.com/vi/_pcH0KB5J2Q/0.jpg)](https://youtu.be/_pcH0KB5J2Q)

1. How AISmessages is used in Dakosy's PRISE system to optimise sequencing and arrival of mega-ships on the river Elbe and at the Port of Hamburg ([read reference page](https://www.dakosy.de/en/solutions/port-community-system/prise/))

1. How AISmessages can be used to "decode the air around you" as Bert Jan Schrijver ([@bjschrijver](https://twitter.com/bjschrijver)) of JPoint talks about 
	1. at DEVOXX BE 2015 ([watch conference video](https://www.youtube.com/watch?v=fOlz0OcZPjc))
	1. at DEVOXX UK 2015 ([watch conference video](https://www.parleys.com/tutorial/decoding-air-around-you-java-7-hardware)) 

1. A live demo of AISmessages is available on http://ais.tbsalling.dk. 

1. See a simple demo applications in the source code in the `dk.tbsalling.aismessages.demo` package (view via 
[Github](https://github.com/tbsalling/aismessages/tree/master/src/main/java/dk/tbsalling/aismessages/demo)). 

1. AISMessages is used by Pronto, which enables the Port of Rotterdam to optimize port calls and vessel opterations. Watch it on [Youtube](https://youtu.be/0JpowdbL8xM):<br>
[![IMAGE ALT TEXT HERE](https://i.ytimg.com/vi/0JpowdbL8xM/hqdefault.jpg?sqp=-oaymwEXCNACELwBSFryq4qpAwkIARUAAIhCGAE=&rs=AOn4CLDxh5uSoxSj-rIiO4ysun8Ir0iWag)](https://youtu.be/0JpowdbL8xM)

1. AISMessages is used by Shiptracker ([https://shiptracker.portofrotterdam.com](https://shiptracker.portofrotterdam.com)), which enables Port of Rotterdam to show its customers the current position and ETA of ships scheduled to arrive in the port.<br>
[![IMAGE ALT TEXT HERE](http://tbsalling.dk/wp-content/uploads/2018/11/PoR-Pronto.png)](https://shiptracker.portofrotterdam.com)

Programmatic usage
---
Programmatically the starting point is the AISStreamReader. It takes an InputStream (feeding
NMEA data), and a consumer of AISMessages which as called back every time an AIS message is decoded. So,
if you have an InputStream serving data like this:

```
    !AIVDM,1,1,,A,15Mv5v?P00IS0J`A86KTROvN0<5k,0*12
    !AIVDM,1,1,,A,15Mwd<PP00ISfGpA7jBr??vP0<3:,0*04
    !AIVDM,2,1,4,B,55MwW7P00001L@?;GS0<51B08Thj0TdpE800000P0hD556IE07RlSm6P0000,0*0B
    !AIVDM,2,2,4,B,00000000000,2*23
    !AIVDM,1,1,,A,15N7th0P00ISsi4A5I?:fgvP2<40,0*06
    !AIVDM,1,1,,A,15NIEcP000ISrjPA8tEIBq<P089=,0*63
    !AIVDM,1,1,,B,15MuS0PP00IS00HA8gEtSgvN0<3U,0*61
    \c:1609841515,s:r3669961*78\!AIVDM,1,1,,A,13ukmN7@0<0pRcHPTkn4P33f0000,0*58
    \c:1609841515,s:r3669961,g:1-2-1234*0E\!AIVDM,1,1,,A,13ukmN7@0<0pRcHPTkn4P33f0000,0*58
```

then you can decode it into Java POJO's of type AISMessage like this:

``` java

    public class DemoApp {

        public static void main(String[] args) throws IOException {

           InputStream inputStream = ...

           AISInputStreamReader streamReader
                = new AISInputStreamReader(
                    inputStream,
                    aisMessage -> System.out.println(aisMessage))
           );

           streamReader.run();
	    }

    }
```

The second argument to the `AISInputStreamReader` constructor (`aisMessage -> System.out.println(aisMessage)`) is a 
`Consumer<? super AISMessage>` which consumes decoded AIS messages in the form of `AISMessage` objects.

`AISMessage` has several subclasses - one for each type of AIS message, which can be decoded. For instance an AIS
message of type 5 "Static and voyage related data" are represented by objects of class `ShipAndVoyageData` as shown here
in fragments:

```java
public class ShipAndVoyageData extends AISMessage implements StaticDataReport {
   ...
   public IMO getImo() { ... }
   public String getCallsign() { ... }
   public String getShipName() { ... }
   public ShipType getShipType() { ... }
   public Integer getToBow() { ... }
   public Integer getToStern() { ... }
   public Integer getToStarboard() { ... }
   public Integer getToPort() { ... }
   ...
}
```

Obtaining AISmessages
---
You do not need to compile AISmessages yourself. It is available in Maven Central. So if you are using Maven, all you
need to do is add these lines to your pom.xml:

``` xml
...
<dependency>
    <groupId>dk.tbsalling</groupId>
    <artifactId>aismessages</artifactId>
    <version>3.4.0</version>
</dependency>
...
```

License
---
AISmessages is released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported (CC BY-NC-SA 3.0)
license - which means that it is free for non-commercial use. For full license details see the
[LICENSE-full](LICENSE-full) file.

To obtain a commercial license and/or commercial support contact
[Thomas Borg Salling](mailto:tbsalling@tbsalling.dk?subject=[GitHub]%20AISmessages%20license).
