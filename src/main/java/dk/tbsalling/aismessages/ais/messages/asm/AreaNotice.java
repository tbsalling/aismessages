package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitString;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import static java.util.Arrays.stream;

/**
 * IMO SN.1/Circ.289 - Area Notice (DAC=1, FI=22 broadcast or FI=23 addressed)
 * Used for broadcasting navigational warnings and area notices
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AreaNotice extends ApplicationSpecificMessage {

    protected AreaNotice(int designatedAreaCode, int functionalId, BitString binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.linkageId = getBinaryData().getUnsignedInt(0, 10);
        this.noticeType = NoticeType.valueOf(getBinaryData().getUnsignedInt(10, 17));
        this.month = getBinaryData().getUnsignedInt(17, 21);
        this.day = getBinaryData().getUnsignedInt(21, 26);
        this.hour = getBinaryData().getUnsignedInt(26, 31);
        this.minute = getBinaryData().getUnsignedInt(31, 37);
        this.durationMinutes = getBinaryData().getUnsignedInt(37, 55);
        
        // Sub-areas are variable length - we'll just store the remaining data
        // Actual parsing would require iterating through sub-areas
        if (getBinaryData().length() > 55) {
            this.subAreasData = getBinaryData().getBits(55, getBinaryData().length());
        } else {
            this.subAreasData = "";
        }
    }

    /** Bits 0-10 of the application data (IMO SN.1/Circ.289, DAC=1 FI=22 or FI=23): linkage ID. */
    int linkageId;
    /** Bits 10-17 of the application data (IMO SN.1/Circ.289, DAC=1 FI=22 or FI=23): notice type code, decoded as {@link NoticeType}. */
    NoticeType noticeType;
    /** Bits 17-21 of the application data (IMO SN.1/Circ.289, DAC=1 FI=22 or FI=23): month. */
    int month;
    /** Bits 21-26 of the application data (IMO SN.1/Circ.289, DAC=1 FI=22 or FI=23): day. */
    int day;
    /** Bits 26-31 of the application data (IMO SN.1/Circ.289, DAC=1 FI=22 or FI=23): hour. */
    int hour;
    /** Bits 31-37 of the application data (IMO SN.1/Circ.289, DAC=1 FI=22 or FI=23): minute. */
    int minute;
    /** Bits 37-55 of the application data (IMO SN.1/Circ.289, DAC=1 FI=22 or FI=23): duration minutes. */
    int durationMinutes;
    /** Bits 55 to end of the application data (IMO SN.1/Circ.289, DAC=1 FI=22 or FI=23): sub areas data as a string of '0'/'1', not decoded further. */
    String subAreasData;

    /** Codes of the notice type field, bits 10-17 of the application data (IMO SN.1/Circ.289, DAC=1 FI=22 or FI=23). */
    public enum NoticeType {
        /** 0 = caution area. */
        CAUTION_AREA(0),
        /** 1 = caution area marine mammals habitat. */
        CAUTION_AREA_MARINE_MAMMALS_HABITAT(1),
        /** 2 = caution area derelicts. */
        CAUTION_AREA_DERELICTS(2),
        /** 3 = caution area traffic. */
        CAUTION_AREA_TRAFFIC(3),
        /** 4 = caution area fishing. */
        CAUTION_AREA_FISHING(4),
        /** 5 = caution area trawling. */
        CAUTION_AREA_TRAWLING(5),
        /** 6 = caution area underwater operation. */
        CAUTION_AREA_UNDERWATER_OPERATION(6),
        /** 7 = caution area diving. */
        CAUTION_AREA_DIVING(7),
        /** 8 = caution area seaplane operations. */
        CAUTION_AREA_SEAPLANE_OPERATIONS(8),
        /** 9 = caution area marine mammals observe. */
        CAUTION_AREA_MARINE_MAMMALS_OBSERVE(9),
        /** 10 = restricted area. */
        RESTRICTED_AREA(10),
        /** 11 = anchorage area. */
        ANCHORAGE_AREA(11),
        /** 12 = oil barrage. */
        OIL_BARRAGE(12),
        /** 13 = reporting area. */
        REPORTING_AREA(13),
        /** 14 = fish trap area. */
        FISH_TRAP_AREA(14),
        /** 15 = nature reserve. */
        NATURE_RESERVE(15),
        /** 16 = traffic separation scheme. */
        TRAFFIC_SEPARATION_SCHEME(16),
        /** 17 = survey area. */
        SURVEY_AREA(17),
        /** 18 = lock. */
        LOCK(18),
        /** 19 = shallow water. */
        SHALLOW_WATER(19),
        /** 20 = construction. */
        CONSTRUCTION(20),
        /** 21 = work in progress. */
        WORK_IN_PROGRESS(21),
        /** 22 = dredging. */
        DREDGING(22),
        /** 23 = divers. */
        DIVERS(23),
        /** 24 = birds. */
        BIRDS(24),
        /** 25 = aquaculture. */
        AQUACULTURE(25),
        /** 26 = seals seabirds watch. */
        SEALS_SEABIRDS_WATCH(26),
        /** 27 = dolphins porpoises watch. */
        DOLPHINS_PORPOISES_WATCH(27),
        /** 28 = fairway closed. */
        FAIRWAY_CLOSED(28),
        /** 29 = bridge closed. */
        BRIDGE_CLOSED(29),
        /** 30 = cable or pipeline. */
        CABLE_OR_PIPELINE(30),
        /** 31 = reef. */
        REEF(31),
        /** 32 = wreck. */
        WRECK(32),
        /** 33 = obstruction. */
        OBSTRUCTION(33),
        /** 34 = underwater structure. */
        UNDERWATER_STRUCTURE(34),
        /** 35 = cable or pipeline work. */
        CABLE_OR_PIPELINE_WORK(35),
        /** 36 = marine event. */
        MARINE_EVENT(36),
        /** 37 = nature sanctuary. */
        NATURE_SANCTUARY(37),
        /** 38 = military operations. */
        MILITARY_OPERATIONS(38),
        /** 39 = dredging operations. */
        DREDGING_OPERATIONS(39),
        /** 40 = ice area. */
        ICE_AREA(40),
        /** 41 = wildlife sanctuary. */
        WILDLIFE_SANCTUARY(41),
        /** 42 = seaplane anchorage. */
        SEAPLANE_ANCHORAGE(42),
        /** 43 = military exclusion zone. */
        MILITARY_EXCLUSION_ZONE(43),
        /** 44 = deep water traffic lane. */
        DEEP_WATER_TRAFFIC_LANE(44),
        /** 45 = particularly sensitive sea area. */
        PARTICULARLY_SENSITIVE_SEA_AREA(45),
        /** 46 = cable. */
        CABLE(46),
        /** 47 = pipeline. */
        PIPELINE(47),
        /** 48 = fish haven. */
        FISH_HAVEN(48),
        /** 49 = dumping ground. */
        DUMPING_GROUND(49),
        /** 50 = oceanographic data collection. */
        OCEANOGRAPHIC_DATA_COLLECTION(50),
        /** 51 = military practice area. */
        MILITARY_PRACTICE_AREA(51),
        /** 52 = volcanic exclusion area. */
        VOLCANIC_EXCLUSION_AREA(52),
        /** 53 = pipeline area. */
        PIPELINE_AREA(53),
        /** 54 = submerged structures. */
        SUBMERGED_STRUCTURES(54),
        /** 55 = berths. */
        BERTHS(55),
        /** 56 = anchorage quarantine. */
        ANCHORAGE_QUARANTINE(56),
        /** 57 = anchorage awaiting orders. */
        ANCHORAGE_AWAITING_ORDERS(57),
        /** 58 = anchorage for non dangerous cargo. */
        ANCHORAGE_FOR_NON_DANGEROUS_CARGO(58),
        /** 59 = anchorage explosives. */
        ANCHORAGE_EXPLOSIVES(59),
        /** 60 = anchorage reserved. */
        ANCHORAGE_RESERVED(60),
        /** 61 = anchorage offshore. */
        ANCHORAGE_OFFSHORE(61),
        /** 62 = anchorage tanker. */
        ANCHORAGE_TANKER(62),
        /** 63 = anchorage reserved large vessels. */
        ANCHORAGE_RESERVED_LARGE_VESSELS(63),
        /** 64 = anchorage deep draught vessels. */
        ANCHORAGE_DEEP_DRAUGHT_VESSELS(64),
        /** 65 = anchorage general. */
        ANCHORAGE_GENERAL(65),
        /** 66 = helicopter operating area. */
        HELICOPTER_OPERATING_AREA(66),
        /** 67 = mine countermeasures operating area. */
        MINE_COUNTERMEASURES_OPERATING_AREA(67),
        /** 68 = small craft anchorage. */
        SMALL_CRAFT_ANCHORAGE(68),
        /** 69 = spoil ground. */
        SPOIL_GROUND(69),
        /** 70 = firing danger area. */
        FIRING_DANGER_AREA(70),
        /** 71 = anchorage prohibited. */
        ANCHORAGE_PROHIBITED(71),
        /** 72 = anchorage awaiting berth. */
        ANCHORAGE_AWAITING_BERTH(72),
        /** 73 = area to be avoided. */
        AREA_TO_BE_AVOIDED(73),
        /** 74 = historic wreck area. */
        HISTORIC_WRECK_AREA(74),
        /** 75 = restricted fishing area. */
        RESTRICTED_FISHING_AREA(75),
        /** 76 = historic marine sanctuary. */
        HISTORIC_MARINE_SANCTUARY(76),
        /** 77 = underwater training area. */
        UNDERWATER_TRAINING_AREA(77),
        /** 78 = inshore traffic zone. */
        INSHORE_TRAFFIC_ZONE(78),
        /** 79 = precautionary area. */
        PRECAUTIONARY_AREA(79),
        /** 80 = production area. */
        PRODUCTION_AREA(80),
        /** 81 = research reserve. */
        RESEARCH_RESERVE(81),
        /** 82 = seasonal no entry area. */
        SEASONAL_NO_ENTRY_AREA(82),
        /** 83 = log storage area. */
        LOG_STORAGE_AREA(83),
        /** 84 = cargo transhipment area. */
        CARGO_TRANSHIPMENT_AREA(84),
        /** 85 = restricted area historic or archeological significance. */
        RESTRICTED_AREA_HISTORIC_OR_ARCHEOLOGICAL_SIGNIFICANCE(85),
        /** 86 = fairway marker. */
        FAIRWAY_MARKER(86),
        /** 87 = seaplane landing area. */
        SEAPLANE_LANDING_AREA(87),
        /** 88 = entry prohibited. */
        ENTRY_PROHIBITED(88),
        /** 89 = anchoring prohibited. */
        ANCHORING_PROHIBITED(89),
        /** 90 = navigation prohibited. */
        NAVIGATION_PROHIBITED(90),
        /** 91 = overtaking prohibited. */
        OVERTAKING_PROHIBITED(91),
        /** 92 = overtaking prohibited for convoys. */
        OVERTAKING_PROHIBITED_FOR_CONVOYS(92),
        /** 93 = two way traffic prohibited. */
        TWO_WAY_TRAFFIC_PROHIBITED(93),
        /** 94 = reduced wake. */
        REDUCED_WAKE(94),
        /** 95 = speed limit. */
        SPEED_LIMIT(95),
        /** 96 = stop on request. */
        STOP_ON_REQUEST(96),
        /** 97 = sound signal mandatory. */
        SOUND_SIGNAL_MANDATORY(97),
        /** 98 = VHF working channel. */
        VHF_WORKING_CHANNEL(98),
        /** 99 = sound signal prohibition. */
        SOUND_SIGNAL_PROHIBITION(99),
        /** 100 = crossing prohibition. */
        CROSSING_PROHIBITION(100),
        /** 101 = no berthing lateral limit. */
        NO_BERTHING_LATERAL_LIMIT(101),
        /** 102 = berthing permitted. */
        BERTHING_PERMITTED(102),
        /** 103 = berthing permitted lateral limit. */
        BERTHING_PERMITTED_LATERAL_LIMIT(103),
        /** 104 = speed limit kmph. */
        SPEED_LIMIT_KMPH(104),
        /** 105 = passing prohibition general. */
        PASSING_PROHIBITION_GENERAL(105),
        /** 106 = passing prohibition convoy. */
        PASSING_PROHIBITION_CONVOY(106),
        /** 107 = information radio channel. */
        INFORMATION_RADIO_CHANNEL(107),
        /** 108 = ice spar. */
        ICE_SPAR(108),
        /** 109 = prohibited area icespar. */
        PROHIBITED_AREA_ICESPAR(109),
        /** 110 = nature reserve no traffic. */
        NATURE_RESERVE_NO_TRAFFIC(110),
        /** 111 = nature reserve awacs. */
        NATURE_RESERVE_AWACS(111),
        /** 112 = bird sanctuary no entry. */
        BIRD_SANCTUARY_NO_ENTRY(112),
        /** 113 = prohibited area sensitive area. */
        PROHIBITED_AREA_SENSITIVE_AREA(113),
        /** 114 = prohibited area security reason. */
        PROHIBITED_AREA_SECURITY_REASON(114),
        /** 115 = prohibited area sewage. */
        PROHIBITED_AREA_SEWAGE(115),
        /** 116 = industrial area. */
        INDUSTRIAL_AREA(116),
        /** 117 = underwater cables and pipelines. */
        UNDERWATER_CABLES_AND_PIPELINES(117),
        /** 118 = fairway. */
        FAIRWAY(118),
        /** 119 = waiting area. */
        WAITING_AREA(119),
        /** 120 = prohibited area no explanation. */
        PROHIBITED_AREA_NO_EXPLANATION(120),
        /** 121 = nature reserve entry permitted not anchoring. */
        NATURE_RESERVE_ENTRY_PERMITTED_NOT_ANCHORING(121),
        /** 122 = fish sanctuary no trawling no entry. */
        FISH_SANCTUARY_NO_TRAWLING_NO_ENTRY(122),
        /** 123 = military area entry prohibited. */
        MILITARY_AREA_ENTRY_PROHIBITED(123),
        /** 124 = historic wreck area no entry. */
        HISTORIC_WRECK_AREA_NO_ENTRY(124),
        /** 125 = navigational aid. */
        NAVIGATIONAL_AID(125),
        /** 126 = cautionary area underwater feature. */
        CAUTIONARY_AREA_UNDERWATER_FEATURE(126),
        /** 127 = underwater danger area. */
        UNDERWATER_DANGER_AREA(127);

        /** The code as transmitted. */
        int value;

        NoticeType(int value) {
            this.value = value;
        }

        public static NoticeType valueOf(int value) {
            return stream(values())
                    .filter(e -> e.value == value)
                    .findFirst()
                    .orElse(CAUTION_AREA);
        }
    }

}
