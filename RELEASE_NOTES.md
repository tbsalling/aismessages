# AISmessages Release Notes

Application-facing release notes for AISmessages - a Java library for decoding AIS messages from digital VHF radio
traffic.

---

## Version 6.0.0-SNAPSHOT

**Development Version**

**Major Version Update - ITU-R M.1371-6 compliance, binary incompatible with 5.x**

Alignment with Recommendation ITU-R M.1371-6 (02/2026).

### Breaking API Changes

**Not binary compatible with 5.x: applications must be recompiled against 6.0.0.** Several public getters
changed their return types, so code compiled against 5.x fails with `NoSuchMethodError`; review the list below
for source changes.

- **New message type in sealed hierarchies.** `AidToNavigationReportSingleSlot` is a new permitted subclass of
  the sealed `AISMessage`, and `AidToNavigationReportSingleSlot` is a new `AISMessageType` constant. Code that
  switches exhaustively over either without a `default` branch (pattern-matching `switch` on `AISMessage`, or
  a `switch` expression on `AISMessageType`) must add a case for it; code compiled against 5.x throws
  `MatchException` when such a message arrives.
- **Coordinates are `double`.** Every `getLatitude()` and `getLongitude()` (including `DynamicDataReport`, the
  corner coordinates of Message 23 and the application specific messages) returns `double` instead of `float`.
  A float cannot distinguish adjacent positions at the 1/10 000-minute AIS resolution beyond about 32°.
  `ChannelManagement`'s four corner getters return `Double`, which is `null` for addressed messages.
- **Absent optional parts are `null`.** Fields that used -1 for "not present" are now nullable `Integer`:
  `SafetyRelatedAcknowledge.getSequence2()`-`getSequence4()`; `Interrogation.getType1_2()`,
  `getOffset1_2()`, `getType2_1()` and `getOffset2_1()`; `ClassBCSStaticDataReport.getUnitModelCode()` and
  `getUnitSerialNumber()` for Part A; and the application identifier, `getDesignatedAreaCode()` and
  `getFunctionalId()` of unstructured Messages 25 and 26. Message 24 Part A reports the
  ship dimensions as 0, the spec's "not available" value (Fig. 38), instead of -1; they stay `int`.
- **Coded fields are enums.** `ShipAndVoyageData.getAisVersionIndicator()` returns `AisVersionIndicator`,
  `ClassBCSStaticDataReport.getVdesCapabilities()` returns `VdesCapabilities` (`null` for Part A) and
  `LongRangeBroadcastMessage.getPositionLatency()` returns `PositionLatency`.
- **Regional and spare bits are `int`.** `getRegionalReserved1()`-`getRegionalReserved3()` (Messages 18 and 19),
  `getRegionalReserved()` and `getRadioStatus()` (Message 9) and `getRegionalUse()` (Message 21) return the bits
  as an unsigned `int`, first bit most significant, instead of a string of '0'/'1'.
- **No magic numbers: not-available values are `NaN` or `null`.** Decoded fields no longer return the
  transmitted "not available" code; see the table under "Upgrade Notes". Primitive decimals (coordinates, speed and
  course over ground, rate of turn, draught) are `NaN`; integers become nullable `Integer`: `getTrueHeading()`
  and `getSecond()` (also on `ExtendedDynamicDataReport`), Message 9 `getAltitude()`, the year to second of
  Messages 4 and 11, the ETA parts of Message 5, and `getImo()`. Names, call signs, destinations and vendor IDs
  transmitted as all "@" are `null` instead of "". `ChannelManagement`'s north-east corner is `null` when not
  available.
- **Indicator enums for fields with several special values.** `getSpeedOverGroundIndicator()` (Messages 1-3, 9,
  18, 19, 27, also on `DynamicDataReport`), `getTimeStampIndicator()` (Messages 1-3, 9, 18, 19, 21, 28),
  `getAltitudeIndicator()` (Message 9), `getDraughtIndicator()` (Message 5) and `getRateOfTurnIndicator()`
  (Messages 1-3) tell measured values, "maximum or higher" and the special codes apart. `getRateOfTurn()` is a
  `float` in degrees per minute, `NaN` unless measured.
- **Raw values removed.** `getRawLatitude()`, `getRawLongitude()`, `getRawSpeedOverGround()`,
  `getRawCourseOverGround()` (also from `DynamicDataReport`), `getRawRateOfTurn()` and `getRawDraught()` are
  gone; use the decoded getters. Spare, regional-reserved, radio-status and AtoN-status bits stay, as ITU-R
  M.1371 defines them.
- **More coded values are enums.** `ClassBCSStaticDataReport.getPartNumber()` is replaced by `getPart()`
  returning `Message24Part`. `Interrogation.getType1_1()`, `getType1_2()` and `getType2_1()` return
  `AISMessageType`. Message 28's `getDimensionA()` and `getDimensionB()` are replaced by decoded getters that
  depend on the dimension type (Table 84): `getRadiusMetres()`, `getHeightMetres()`, `getMobileCourseOverGround()`
  with `getMobileCourseIndicator()`, `getMobileSpeedOverGround()`, `getPolygonVertexNumber()`,
  `getPolygonLineCount()`, `getOrientation()`, `getLineLengthMetres()`, and `getDimensionAMetres()`/
  `getDimensionBMetres()` for type 0; the ones that do not apply are `null`.
- **Renamed.** `AidToNavigationReport.isNameExtensionIsLabel()` is `isNameExtensionLabel()`.
  `GroupAssignmentCommand.getSpare1()` and `getSpare2()` return `int`.
- **Removed.** The placeholder constants `ShipType.PassengerFuture1-3`, `CargoFuture1-4` and `TankerFuture1-2`,
  `ReportingInterval.FutureUse1` and `AidType.FutureUse1` (their codes now resolve to the named constants
  under "Completed enumerations"); `BinaryMessageSingleSlot.getBinaryData()`; and `NMEAMessageSocketClient`
  and `SocketDemoApp`, deprecated since 4.0.
- **Corrected decodings change values.** Messages 15, 17, 20, 22, 23, 25 and 26 and the IFM 0 text telegram now
  decode per M.1371-6; see "Corrected decodings".
- **Stricter validation.** Messages 12, 14 and 25 shorter than their header, and Messages 26 shorter than 64 or
  longer than 1064 bits, are now rejected with `InvalidMessage`.
- **Constructors.** The `protected` constructors of several message classes changed. They are not part of the
  supported API; construct messages through `AISMessageFactory`.

### Upgrade Notes

- Recompile against 6.0.0. The build is otherwise unchanged: Java 25 and no runtime dependencies.
- Add a case for `AidToNavigationReportSingleSlot` to exhaustive `switch` statements or expressions over
  `AISMessage` or `AISMessageType`, or handle it through a `default` branch.
- Change `float` variables that hold latitudes or longitudes to `double`.
- Replace `-1` checks on the optional fields listed above with `null` checks, and unbox
  `ChannelManagement`'s corner coordinates after a null check or after testing `isAddressed()`.
- Replace int comparisons of the AIS version indicator, VDES capabilities and position latency with the new
  enum constants, or use their `getCode()`.
- Replace `ShipType.PassengerFuture1-3` with `PassengerCruiseShip`, `PassengerFerry` and `PassengerExcursionShip`;
  `CargoFuture1-4` with `CargoBulkCarrier`, `CargoContainerShip`, `CargoRollOnRollOffCarrier` and
  `CargoLandingCraft`; `TankerFuture1-2` with `TankerNonHazardous` and `IntegratedTugAndTankBarge`;
  `ReportingInterval.FutureUse1` with `TwoSeconds`; and `AidType.FutureUse1` with `EmergencyWreckMarkingBuoy`.
- Replace checks for transmitted "not available" codes with `NaN`, `null` or indicator checks:

  | Field | 5.x value when not available | 6.0.0 |
  |---|---|---|
  | Latitude, longitude | 91, 181 | `Double.isNaN(...)` |
  | Speed over ground | 102.3 (Message 9: 1023, Message 27: 63) | `NaN`; `getSpeedOverGroundIndicator()` |
  | Speed over ground, maximum | 102.2 (1022, 62) | same value; indicator `AtLeastMaximum` |
  | Course over ground | 360 (Message 27: 511) | `Float.isNaN(...)` |
  | True heading | 511 | `null` |
  | Time stamp second | 60-63 | `null`; `getTimeStampIndicator()` |
  | Rate of turn | converted -128/±127 | `NaN`; `getRateOfTurnIndicator()` |
  | Altitude (Message 9) | 4095 (4094 = or higher) | `null`; `getAltitudeIndicator()` |
  | Draught (Message 5) | 0 (25.5 = or greater) | `NaN`; `getDraughtIndicator()` |
  | Date and time (Messages 4, 11), ETA (Message 5) | 0, 24, 60 | `null` |
  | IMO number | `IMO` of 0 | `null` |
  | Name, call sign, destination | "" | `null` |

- Use `getLength()`, `getBeam()` and `isReferencePointAvailable()` on `StaticDataReport` instead of interpreting
  zero dimensions.
- Parse regional and spare bits with bit operations instead of string handling; `Integer.toBinaryString` gives
  the old representation without leading zeros.
- For Message 25 data use `getData()`, `getDesignatedAreaCode()`, `getFunctionalId()` and
  `getApplicationSpecificMessage()` instead of `getBinaryData()`.
- Use `NMEAMessageUDPSocket`, or an `AISInputStreamReader` over a socket's input stream, instead of
  `NMEAMessageSocketClient`.
- Stream readers (`AISInputStreamReader`, `NMEAMessageInputStreamReader`, `NMEAMessageUDPSocket`) no longer
  stop on malformed sentences. Code that restarted a reader after such a failure can drop that workaround.

### Corrected decodings (behaviour changes)

- **Message 15 (Interrogation):** the second interrogated station was read from the wrong bit offsets
  (MMSI at 120-150, message id at 150-156 and a 6-bit slot offset at 156-162). It is now decoded per
  Table 64: MMSI at bits 110-140, message id at 140-146, 12-bit slot offset at 146-158.
- **Message 20 (Data link management):** reservation blocks 2 to 4 were read from the wrong offsets and
  their increments as 7 bits. All four blocks are now 12 + 4 + 3 + 11 bits starting at bits 40, 70, 100
  and 130 (Table 70); blocks 2 to 4 are present when the message is at least 100, 130 and 160 bits long.
- **Message 23 (Group assignment command):** Tx/Rx mode, reporting interval and quiet time were read from
  bits 166-176, beyond the end of the 160-bit message, and therefore always decoded as 0. They are now
  read from bits 144-146, 146-150 and 150-154 (Table 74).
- **Message 25 (Single slot binary message):** the layout now follows Table 79. A destination MMSI is only
  decoded when the destination indicator is set (`getDestinationMMSI()` returns `null` for broadcasts; it
  used to return a value made of data bits). When the binary data flag is set, the 16-bit application
  identifier is split into `getDesignatedAreaCode()` and `getFunctionalId()` (both `null` when unstructured)
  and resolved to an `ApplicationSpecificMessage` via `getApplicationSpecificMessage()`. The application
  data proper is available as `BitString getData()`, starting at bit 40, 56, 72 or 88 depending on the two
  flags.
- **Message 26 (Multiple slot binary message):** the layout now follows Table 81. Destination MMSI and
  application identifier are only decoded when their flags are set (`getDestinationMmsi()` is `null` for
  broadcasts, `getApplicationId()`, `getDesignatedAreaCode()` and `getFunctionalId()` are `null` when
  unstructured). The trailing 4 spare bits, communication state selector and 19-bit communication state
  are decoded into `isCommStateSelectorFlag()` and `getCommunicationState()` (SOTDMA or ITDMA).
  `getData()` now holds exactly the application data between header and trailer and is no longer
  zero-padded to 1005 bits. Messages shorter than 64 bits or longer than 1064 bits are rejected as
  `InvalidMessage`.
- **Message 22 (Channel management), addressed variant:** decoding threw a `NullPointerException` because
  the four area coordinate fields were primitive `float` while the factory passes `null` for addressed
  messages (whose area fields hold two station IDs instead). The getters `getNorthEastLongitude()`,
  `getNorthEastLatitude()`, `getSouthWestLongitude()` and `getSouthWestLatitude()` now return `Double` and
  are `null` when `isAddressed()` is true.
- **Truncated or malformed payloads:** a payload shorter than its message type requires (for example a
  163-bit position report) or an NMEA sentence with more than 5 fill bits made `AISMessageFactory.create`
  leak an `IllegalArgumentException`, which `AISInputStreamReader` does not catch. Both cases now raise
  `InvalidMessage` like every other undecodable input, so stream readers log and continue.
- **Non-numeric NMEA fields stopped stream readers:** a sentence whose fragment count, fragment number,
  sequential message identifier or fill bits field was not a number, or a tag block whose `c`, `n` or `r`
  parameter was not a number, threw `NumberFormatException`. `NMEAMessageInputStreamReader`,
  `AISInputStreamReader` and `NMEAMessageUDPSocket` did not catch it, so one such sentence ended the
  reader. `NMEAMessage` and `NMEATagBlock` now throw `NMEAParseException`, which the readers log and skip.
- **Tag blocks with a wrong checksum stopped stream readers:** `InvalidTagBlock` was not among the
  exceptions `NMEAMessageInputStreamReader` and `NMEAMessageUDPSocket` catch. They now log and skip it.
- **A failed decode lost the next multi-fragment message:** when a sentence or a complete set of
  fragments failed to decode, `NMEAMessageHandler` kept the fragments buffered, so the first fragment of
  the next multi-fragment message was discarded as out of sequence. The buffer is now cleared before
  decoding.
- **Payloads shorter than their header:** a Message 12 shorter than 72 bits made the six-bit text decoder
  throw `NegativeArraySizeException`, and Messages 14 and 25 accepted payloads shorter than their 40-bit
  header. Messages 12, 14 and 25 now enforce their minimum lengths (72, 40 and 40 bits; ITU-R M.1371-6
  Tables 59, 61 and 79) and reject shorter payloads with `InvalidMessage`; `AISText.decode` returns an empty
  string for an empty or negative bit range. The `InvalidMessage` for a wrong-length Message 5 now names the
  assumed source MMSI like the other message types.
- **IFM 0 text telegram (`TextDescription`, DAC 1 FI 0):** the text was decoded from bit 0 of the
  application data, ignoring the 1-bit "acknowledge required" flag and 11-bit text sequence number that
  Tables 25 to 28 place in front of it, so every text came out two characters out of phase. The text now
  starts at bit 12 and the two header fields are exposed as `isAcknowledgeRequired()` and
  `getTextSequenceNumber()`. FI 1 is marked discontinued in M.1371-6 but is still decoded as text.
- **Messages 17, 22 and 23, coordinates in 1/10 min:** the DGNSS reference station position (Table 66) and
  the area corners (Tables 73 and 74) were divided by 10, which yields minutes instead of degrees (59.99° was
  reported as 3599.2). They are now divided by 600 and returned in degrees.
- **Messages 1-3, rate of turn special codes:** +127, -127 and -128 were converted like measured values into
  rates such as -720° per minute. `getRateOfTurn()` is now `NaN` for them and `getRateOfTurnIndicator()` (new
  `RateOfTurnIndicator`: `Measured`, `TurningRightWithoutTurnIndicator`, `TurningLeftWithoutTurnIndicator`,
  `NotAvailable`) classifies the transmitted value (Table 46).

### New message type: Message 28, aid-to-navigation report (single slot)

- New `AidToNavigationReportSingleSlot` (`AISMessageType.AidToNavigationReportSingleSlot`, code 28) decodes
  the 168-bit report of Table 84: time stamp, position, `RestrictedUseIndicator`, `AtoNStationType`, a 7-bit
  `AidType` (Table 72 codes 0-31 plus the Table 85 mobile AtoN codes 32-50), IALA MRN,
  `AtoNDimensionsType` with raw dimensions A and B, multiple-dimension-types flag, charted flag,
  `AtoNOnStationStatus`, AtoN status bits and authentication flag.
- `AISMessageType.MAXIMUM_CODE` is now 28. Message IDs 29 and above are still rejected.

### New application specific messages (ITU-R M.1371-6 Annex 4, DAC 1)

- `InterrogationForSpecificFunctionalMessage` (IFM 2), `CapabilityInterrogation` (IFM 3),
  `CapabilityReply` (IFM 4, with `isFunctionalIdAvailable(int)`) and `ApplicationAcknowledgement` (IFM 5)
  replace `UnknownApplicationSpecificMessage` for these function identifiers.

### Fields added in ITU-R M.1371-6 now decoded

- Messages 1, 2, 3 (`PositionReport`) and 18 (`StandardClassBCSPositionReport`): `isTransmitPowerLow()`
  (Tables 46 and 68; false = high power, true = low power).
- Messages 4 (`BaseStationReport`) and 11 (`UTCAndDateResponse`):
  `isSatelliteBroadcastTransmissionControl()` (Table 49, control of Message 27 transmission within the
  base station coverage area). `UTCAndDateResponse` additionally exposes `getCommunicationState()`.
- Message 5 (`ShipAndVoyageData`): `getAisVersionIndicator()` (new `AisVersionIndicator`, Table 50).
- Message 9 (`StandardSARAircraftPositionReport`): `isBarometricAltitude()` (Table 57 altitude sensor),
  `isCommStateSelectorFlag()` and a typed `getCommunicationState()`.
- Message 21 (`AidToNavigationReport`): `isNameExtensionLabel()`. When the name extension starts with
  "@@@" (Table 71: portray as label, not as name), the marker is stripped from `getNameExtension()` and
  this flag is true.
- Message 24 Part B (`ClassBCSStaticDataReport`): `getPositionFixingDevice()` and `getVdesCapabilities()`
  (new `VdesCapabilities`, Table 77), plus the Table 78 split of the manufacturer ID into `getManufacturerMnemonic()`,
  `getUnitModelCode()` and `getUnitSerialNumber()`. `getVendorId()` is unchanged; `getMothershipMmsi()`
  is kept but documented as not being part of M.1371-6.

### Completed enumerations (ITU-R M.1371-6 Tables 46, 49, 51, 72, 75, 85)

- `PositionFixingDevice`: added `BeiDou` (9), `ReservedForFutureUse10`, `ReservedForFutureUse11`,
  `IntegratedPNT` (12), `InertialNavigationSystem` (13), `TerrestrialRadioNavigation` (14) and
  `InternalGNSS` (15). Code 15 is what Class B "CS" stations transmit; `fromInteger(15)` previously
  returned `null`.
- `ManeuverIndicator`: added `ReservedForRegionalUse` (3).
- `ReportingInterval`: added `TwoSeconds` (11), replacing `FutureUse1`.
- `AidType`: added `EmergencyWreckMarkingBuoy` (4, replacing `FutureUse1`) and the Table 85 mobile
  aid-to-navigation types 32-50 used by Message 28.
- `ShipType`: added the special purpose and support vessel codes 1-19, the reserved codes 25-28, 47 and
  48, `WingInGroundNoAdditionalInfo` (29), `Trawler` (38), `PatrolVessel` (39),
  `HighSpeedCraftPassenger` (45), `HighSpeedCraftRoRo` (46), `HighSpeedCraftNoAdditionalInfo` (49),
  `PassengerCruiseShip` (65), `PassengerFerry` (66), `PassengerExcursionShip` (67), `CargoBulkCarrier`
  (75), `CargoContainerShip` (76), `CargoRollOnRollOffCarrier` (77), `CargoLandingCraft` (78),
  `TankerNonHazardous` (85) and `IntegratedTugAndTankBarge` (86). These replace the former `PassengerFuture1-3`,
  `CargoFuture1-4`, `TankerFuture1` and `TankerFuture2` constants.

### New public APIs

- `StaticDataReport` (Messages 5 and 24): `getLength()`, `getBeam()` and `isReferencePointAvailable()`, applying
  the dimension rules of Fig. 38.
- New enums `SpeedOverGroundIndicator`, `TimeStampIndicator`, `AltitudeIndicator`, `DraughtIndicator`,
  `MobileAtoNCourseIndicator` and `Message24Part`, each citing its ITU-R M.1371-6 table.

- `BinaryMessageSingleSlot`: `getDesignatedAreaCode()`, `getFunctionalId()`, `getData()`,
  `getApplicationSpecificMessage()`.
- `BinaryMessageMultipleSlot`: `getDesignatedAreaCode()`, `getFunctionalId()`,
  `getApplicationSpecificMessage()`, `isCommStateSelectorFlag()`, `getCommunicationState()`.
- `PositionReport`: `getRateOfTurnIndicator()`.
- New enums `AisVersionIndicator`, `VdesCapabilities`, `PositionLatency` and `RateOfTurnIndicator`.

### Documentation

- Every message class, field and enum constant of the message model now has Javadoc referencing the defining
  ITU-R M.1371-6 section, table and bit range (or IMO SN.1/Circ.289 for application specific messages), including
  units, scaling and not-available values.

**Full Changelog:** https://github.com/tbsalling/aismessages/compare/aismessages-5.0.0...master

---

## Version 5.0.0

**Release Date:** 2026-10-08

**Major Version Update - Java 25 Required**

### Breaking API Changes

- **Minimum Java version increased from 21 to 25.** The library is compiled with
  `--release 25`, so consuming applications must run on a Java 25 or later JVM.

- **`AISMessageDecoder.decode(String)` now reports all undecodable input as
  `IllegalArgumentException`.** It previously let `NumberFormatException`, `InvalidTagBlock`,
  `IllegalArgumentException` and other unchecked exceptions escape from deeper in the decoding
  pipeline. It now consistently throws `IllegalArgumentException`, as its JavaDoc always
  specified.

**Packed `BitString` replaces `String`-of-`'0'`/`'1'` payload representation:**

- Introduced `dk.tbsalling.aismessages.ais.BitString`, a packed immutable bit vector with typed accessors such as
  `getUnsignedInt`, `getSignedInt`, `getUnsignedLong`, `getBoolean`, `getSignedFloat`, `getUnsignedFloat`, `slice`, and
  `withLengthPaddedTo`.
- Removed `dk.tbsalling.aismessages.ais.BitStringParser` and `dk.tbsalling.aismessages.ais.BitDecoder`.
- `Metadata.bitString()` now returns `BitString` instead of `String`. Call `.toString()` on the result to get the legacy
  `'0'`/`'1'` representation.
- Constructor parameter types changed from `String bitString` to `BitString bitString` on `AISMessage` and all permitted
  subclasses.
- `ApplicationSpecificMessage.binaryData` is now `BitString` instead of `String`, and
  `ApplicationSpecificMessage.create(int, int, BitString)` was updated accordingly.
- `SOTDMACommunicationState.fromBitString(...)` and `ITDMACommunicationState.fromBitString(...)` now take `BitString`.
- `AISMessageFactory.toBitString(String, int)` was removed in favor of `BitString.fromNmeaPayload(String, int)`.

### New Public APIs

- Added `dk.tbsalling.aismessages.ais.SixBitAsciiCodec` for working with custom six-bit ASCII alphabets on top of
  `BitString`.
- Added `dk.tbsalling.aismessages.ais.AISText` for AIS text decoding with AIS filler handling (`'@'` becomes space and
  surrounding whitespace is trimmed).
- Added `dk.tbsalling.aismessages.nmea.NMEAArmouring` for explicit NMEA armouring encode/decode operations.
- Added non-throwing `tryDecode` counterparts to every `dk.tbsalling.aismessages.AISMessageDecoder.decode`
  overload: `tryDecode(String)` returns `Optional<AISMessage>`, and `tryDecode(String...)`,
  `tryDecode(List<String>)` and `tryDecode(InputStream)` return `Optional<List<AISMessage>>`. They never
  throw - malformed, unsupported and `null` input (argument or element) all yield an empty `Optional`
  rather than an exception, so `tryDecode` suits untrusted or lossy feeds.
- An empty `Optional` from `tryDecode` uniformly means "no AIS message could be decoded", whatever the
  cause: bad input, nothing supplied, or an unreadable stream. A present result always holds a non-empty,
  immutable list. `tryDecode` does not report *why* nothing was decoded; use `decode` and catch when the
  reason matters.
- Added `dk.tbsalling.aismessages.AISMessageRouter`, a `Consumer<AISMessage>` built with a fluent builder
  (`on(Class, Consumer)`, `otherwise(...)`, `onError(...)`) that routes each message to the handler for its
  type. Handlers may target concrete classes, abstract classes or interfaces; the most specific match wins,
  and ambiguous or unmatchable registrations are rejected by `build()`. `onError` receives exceptions thrown
  by handlers (by default they propagate); decode failures never reach the router.

### Performance

- `AISMessageFactory.create(...)` is now about **11x faster** end-to-end.
- Retained heap per decoded message is reduced by roughly **3x to 6x**, depending on payload size.

### Fixes

- **`AISMessageDecoder.decode(String...)` and `decode(List<String>)` now really do ignore every
  malformed or unsupported sentence.** They previously caught only three NMEA-layer exception
  types, so a single bad sentence could still abort the whole batch with `NumberFormatException`,
  `InvalidTagBlock`, `IllegalArgumentException`, `ArrayIndexOutOfBoundsException` or
  `ais.exceptions.UnsupportedMessageType`. Undecodable sentences are now skipped and logged:
  expected parse failures at `FINE`, unexpected ones at `WARNING` with a stack trace.

### Upgrade Notes

- Set `<release>25</release>` (or the equivalent `--release 25`) in your own build, and run on a Java 25 JVM.
- The README now reflects the `BitString`-based API.
- NMEA terminology was standardized from "fill bits" to "padding bits" in documentation and related APIs.
- See [`docs/articles/performance-analysis.md`](docs/articles/performance-analysis.md) for benchmarks and migration
  context.

**Full Changelog:** https://github.com/tbsalling/aismessages/compare/aismessages-4.1.2...aismessages-5.0.0

---

## Version 4.1.2

**Release Date:** 2025-11-19

No application-facing changes.

**Full Changelog:** https://github.com/tbsalling/aismessages/compare/aismessages-4.1.1...aismessages-4.1.2

---

## Version 4.1.1

**Release Date:** 2025-11-19

### Performance Improvements

- Replaced the character-to-six-bit lookup with an array-based implementation.
- Delivers about **3.97x faster** character-to-six-bit conversion during AIS decoding.

### Upgrade Notes

- Added upgrade guidance for moving from AISmessages 3.3.1 to 4.1.0 (`UPGRADE_GUIDE.md`, removed before 5.0.0;
  still available at
  [tag aismessages-4.1.2](https://github.com/tbsalling/aismessages/blob/aismessages-4.1.2/UPGRADE_GUIDE.md)).
- Added Java modules examples and refreshed README usage examples to match the current API.

### Fixes

- Fixed resource leak issues.

**Full Changelog:** https://github.com/tbsalling/aismessages/compare/aismessages-4.1.0...aismessages-4.1.1

---

## Version 4.1.0

**Release Date:** 2025-10-19

### New Features & Improvements

**Expanded Application Specific Messages (ASM) support - IMO SN.1/Circ.289:**

- Added 12 new ASM decoders for IMO standardized messages (DAC=001):
  - `TextDescription` (FI=0, 1)
  - `UtcDateInquiry` (FI=10)
  - `UtcDateResponse` (FI=11)
  - `TidalWindow` (FI=14)
  - `VtsGeneratedSyntheticTargets` (FI=17)
  - `MarineTrafficSignal` (FI=18, 19)
  - `WeatherObservation` (FI=21)
  - `AreaNotice` (FI=22, 23)
  - `DangerousCargoIndication` (FI=25)
  - `Environmental` (FI=26)
  - `RouteInformation` (FI=27, 28)
  - `MeteorologicalAndHydrographicalData` (FI=31)
- Total ASM support increased from 6 to 18 message types.

**UDP receiver support:**

- Added `NMEAMessageUDPSocket` for receiving AIS messages via UDP.
- Supports binding to a host/port and processing incoming messages through a handler callback.

**NMEA handling improvements:**

- Added `NMEAMessageHandlerStrict` for strict checksum validation and stricter NMEA message handling.

### Fixes

- Fixed the validation error message for binary broadcast messages exceeding 1008 bits.

**Full Changelog:** https://github.com/tbsalling/aismessages/compare/aismessages-4.0.0...aismessages-4.1.0

---

## Version 4.0.0

**Major Version Update - Java 21 Required**

### Breaking Changes

- **Minimum Java version increased from 11 to 21.**
- AIS message classes are now immutable value objects.
- Message decoding is now eager rather than lazy, so fields are parsed at construction time.

### New Features & Improvements

**Immutable message objects:**

- Message objects are pure data carriers with no parsing responsibilities.
- Improved thread safety and fewer mutation-related bugs.
- Added stable `equals()` / `hashCode()` behavior across message types.

**Performance and memory improvements:**

- Eliminates the old `WeakReference`-based lazy-decoding overhead.
- Decodes messages in a single pass during construction.
- Moves allocation cost to construction time and avoids follow-up allocations during field access.
- Reduces GC pressure in high-throughput and memory-constrained workloads.

**Full Changelog:** https://github.com/tbsalling/aismessages/compare/aismessages-3.5.1...aismessages-4.0.0

---

## Version 3.5.1

**Release Date:** 2025-07-22

No significant application-facing API changes.

**Full Changelog:** https://github.com/tbsalling/aismessages/compare/aismessages-3.5.0...aismessages-3.5.1

---

## Version 3.5.0

**Release Date:** 2025-07-xx

No specific release notes available. See version 3.4.2 for previous changes.

---

## Version 3.4.2

**Release Date:** 2024-01-20

Maintenance release with no significant application-facing changes.

---

## Version 3.2.3

**Release Date:** 2022-01-11

**Major Version Update - Java 11 Required**

### Breaking Changes

- **Minimum Java version increased from 8 to 11.**
- Removed deprecated `TIME_DECODER`; use the individual ETA field getters instead.
- Updated message validation and error handling APIs.

### New Features

**NMEA Tag Block support:**

- Added support for NMEA 0183 Tag Blocks with parameters `c`, `d`, `g`, `n`, `r`, `s`, and `t`.
- Added `NMEATagBlock`, `NMEATagBlockParameterCode`, and `TAGBlockParameterCodeType`.
- Tag blocks are accessible through `AISMessage.getNmeaTagBlock()`.

**Application Specific Messages (ASM):**

- Added a framework for decoding binary application-specific messages.
- Implemented ASM decoders for:
  - DAC 1 FI 20 (`BerthingData`)
  - DAC 1 FI 24 (`ExtendedShipStaticAndVoyageRelatedData`)
  - DAC 1 FI 40 (`NumberOfPersonsOnBoard`)
  - DAC 200 FI 10 (`InlandShipStaticAndVoyageRelatedData`)
- ASM payloads are accessible through `getApplicationSpecificMessage()`.

**Enhanced ETA handling:**

- Added `getEtaAfterReceived()` returning `Optional<ZonedDateTime>`.
- Added individual ETA component getters: `getEtaMonth()`, `getEtaDay()`, `getEtaHour()`, `getEtaMinute()`.

**Raw data access:**

- Added raw-value accessors such as `getRawLatitude()`, `getRawLongitude()`, `getRawSpeedOverGround()`,
  `getRawCourseOverGround()`, and `getRawDraught()`.

### Platform Support

- Added `module-info.java` for Java Platform Module System (JPMS) support.

---

## Version 2.2.3

**Release Date:** 2018-06-15

Maintenance release for Java 8.

---

## Version 2.2.1

**Release Date:** 2016-02-25

Bug fixes and stability improvements. JDK 7 backport available.

---

## Version 2.1.0

**Release Date:** 2015-02-03

### Major Feature: `AISInputStreamReader`

- Introduced `AISInputStreamReader` for simplified stream processing.
- Added functional interface support with `Consumer<AISMessage>`.
- Added communication-state decoding for Class A position reports.

---

## Version 2.0.2

**Release Date:** 2015-01-26

### First production-ready v2 release

- Java 8 required.
- Zero runtime dependencies maintained.
- Introduced lazy decoding.
- Improved ITU 1371-5 compliance.

---

## Maven Coordinates

### Development Version (5.0.0-SNAPSHOT)
```xml
<dependency>
    <groupId>dk.tbsalling</groupId>
    <artifactId>aismessages</artifactId>
    <version>5.0.0-SNAPSHOT</version>
</dependency>
```

### Latest Stable Version (4.1.2)
```xml
<dependency>
    <groupId>dk.tbsalling</groupId>
    <artifactId>aismessages</artifactId>
    <version>4.1.2</version>
</dependency>
```

### Previous Stable Version (4.1.1)
```xml
<dependency>
    <groupId>dk.tbsalling</groupId>
    <artifactId>aismessages</artifactId>
    <version>4.1.1</version>
</dependency>
```

### Previous Stable Version (4.1.0)
```xml
<dependency>
    <groupId>dk.tbsalling</groupId>
    <artifactId>aismessages</artifactId>
    <version>4.1.0</version>
</dependency>
```

### Previous Major Version (4.0.0)
```xml
<dependency>
    <groupId>dk.tbsalling</groupId>
    <artifactId>aismessages</artifactId>
    <version>4.0.0</version>
</dependency>
```

### Previous Stable Version (3.5.1)
```xml
<dependency>
    <groupId>dk.tbsalling</groupId>
    <artifactId>aismessages</artifactId>
    <version>3.5.1</version>
</dependency>
```

---

## License

AISmessages is licensed under Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported (CC BY-NC-SA 3.0).

**Commercial licenses available:** Contact Thomas Borg Salling <tbsalling@tbsalling.dk>
