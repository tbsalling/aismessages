![Build status](https://travis-ci.org/tbsalling/aismessages.svg?branch=master)
[![License](http://img.shields.io/badge/license-CCANS3-green.svg)](https://github.com/tbsalling/aismessages/blob/master/LICENSE)

AISmessages is a Java-based light-weight, zero-dependency, and ultra-efficient message decoder for maritime
navigation and safety messages compliant with ITU 1371 (NMEA armoured AIS messages).

It exhibits lazy decoding and fully decodes tens of thousands of NMEA armoured AIS messages per second per CPU
core on an Intel i7-based laptop.

See simple demo applications in the source code in the `dk.tbsalling.aismessages.demo` package (view via 
[Github](https://github.com/tbsalling/aismessages/tree/master/src/main/java/dk/tbsalling/aismessages/demo)). 
A live demo of AISmessages is available on http://ais.tbsalling.dk. 

Programmatically the starting point is the AISMessageInputStreamReader. It takes an InputStream (feeding
NMEA data), and a consumer of AISMessages which as called back every time an AIS message is decoded.

So an application capable of decoding AIS messages from an NMEA input stream would simply look like this:

``` java

    public class DemoApp {

        public static void main(String[] args) throws IOException {

           InputStream inputStream = ...

           AISMessageInputStreamReader streamReader = new AISMessageInputStreamReader(inputStream, aisMessage ->
               System.out.println("Received AIS message from MMSI " + aisMessage.getSourceMmsi().getMMSI() + ": " + aisMessage)
           );

           streamReader.run();
	    }

    }
```

Read more in the wiki - [https://github.com/tbsalling/aismessages/wiki](https://github.com/tbsalling/aismessages/wiki).
