package dk.tbsalling.aismessages.ais.messages.asm;

import dk.tbsalling.aismessages.ais.BitDecoder;
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

    protected AreaNotice(int designatedAreaCode, int functionalId, String binaryData) {
        super(designatedAreaCode, functionalId, binaryData);

        // Eagerly decode all fields
        this.linkageId = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(0, 10));
        this.noticeType = NoticeType.valueOf(BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(10, 17)));
        this.month = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(17, 21));
        this.day = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(21, 26));
        this.hour = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(26, 31));
        this.minute = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(31, 37));
        this.durationMinutes = BitDecoder.INSTANCE.decodeUnsignedInt(getBinaryData().substring(37, 55));
        
        // Sub-areas are variable length - we'll just store the remaining data
        // Actual parsing would require iterating through sub-areas
        if (getBinaryData().length() > 55) {
            this.subAreasData = getBinaryData().substring(55);
        } else {
            this.subAreasData = "";
        }
    }

    int linkageId;
    NoticeType noticeType;
    int month;
    int day;
    int hour;
    int minute;
    int durationMinutes;
    String subAreasData;

    public enum NoticeType {
        CAUTION_AREA(0),
        CAUTION_AREA_MARINE_MAMMALS_HABITAT(1),
        CAUTION_AREA_DERELICTS(2),
        CAUTION_AREA_TRAFFIC(3),
        CAUTION_AREA_FISHING(4),
        CAUTION_AREA_TRAWLING(5),
        CAUTION_AREA_UNDERWATER_OPERATION(6),
        CAUTION_AREA_DIVING(7),
        CAUTION_AREA_SEAPLANE_OPERATIONS(8),
        CAUTION_AREA_MARINE_MAMMALS_OBSERVE(9),
        RESTRICTED_AREA(10),
        ANCHORAGE_AREA(11),
        OIL_BARRAGE(12),
        REPORTING_AREA(13),
        FISH_TRAP_AREA(14),
        NATURE_RESERVE(15),
        TRAFFIC_SEPARATION_SCHEME(16),
        SURVEY_AREA(17),
        LOCK(18),
        SHALLOW_WATER(19),
        CONSTRUCTION(20),
        WORK_IN_PROGRESS(21),
        DREDGING(22),
        DIVERS(23),
        BIRDS(24),
        AQUACULTURE(25),
        SEALS_SEABIRDS_WATCH(26),
        DOLPHINS_PORPOISES_WATCH(27),
        FAIRWAY_CLOSED(28),
        BRIDGE_CLOSED(29),
        CABLE_OR_PIPELINE(30),
        REEF(31),
        WRECK(32),
        OBSTRUCTION(33),
        UNDERWATER_STRUCTURE(34),
        CABLE_OR_PIPELINE_WORK(35),
        MARINE_EVENT(36),
        NATURE_SANCTUARY(37),
        MILITARY_OPERATIONS(38),
        DREDGING_OPERATIONS(39),
        ICE_AREA(40),
        WILDLIFE_SANCTUARY(41),
        SEAPLANE_ANCHORAGE(42),
        MILITARY_EXCLUSION_ZONE(43),
        DEEP_WATER_TRAFFIC_LANE(44),
        PARTICULARLY_SENSITIVE_SEA_AREA(45),
        CABLE(46),
        PIPELINE(47),
        FISH_HAVEN(48),
        DUMPING_GROUND(49),
        OCEANOGRAPHIC_DATA_COLLECTION(50),
        MILITARY_PRACTICE_AREA(51),
        VOLCANIC_EXCLUSION_AREA(52),
        PIPELINE_AREA(53),
        SUBMERGED_STRUCTURES(54),
        BERTHS(55),
        ANCHORAGE_QUARANTINE(56),
        ANCHORAGE_AWAITING_ORDERS(57),
        ANCHORAGE_FOR_NON_DANGEROUS_CARGO(58),
        ANCHORAGE_EXPLOSIVES(59),
        ANCHORAGE_RESERVED(60),
        ANCHORAGE_OFFSHORE(61),
        ANCHORAGE_TANKER(62),
        ANCHORAGE_RESERVED_LARGE_VESSELS(63),
        ANCHORAGE_DEEP_DRAUGHT_VESSELS(64),
        ANCHORAGE_GENERAL(65),
        HELICOPTER_OPERATING_AREA(66),
        MINE_COUNTERMEASURES_OPERATING_AREA(67),
        SMALL_CRAFT_ANCHORAGE(68),
        SPOIL_GROUND(69),
        FIRING_DANGER_AREA(70),
        ANCHORAGE_PROHIBITED(71),
        ANCHORAGE_AWAITING_BERTH(72),
        AREA_TO_BE_AVOIDED(73),
        HISTORIC_WRECK_AREA(74),
        RESTRICTED_FISHING_AREA(75),
        HISTORIC_MARINE_SANCTUARY(76),
        UNDERWATER_TRAINING_AREA(77),
        INSHORE_TRAFFIC_ZONE(78),
        PRECAUTIONARY_AREA(79),
        PRODUCTION_AREA(80),
        RESEARCH_RESERVE(81),
        SEASONAL_NO_ENTRY_AREA(82),
        LOG_STORAGE_AREA(83),
        CARGO_TRANSHIPMENT_AREA(84),
        RESTRICTED_AREA_HISTORIC_OR_ARCHEOLOGICAL_SIGNIFICANCE(85),
        FAIRWAY_MARKER(86),
        SEAPLANE_LANDING_AREA(87),
        ENTRY_PROHIBITED(88),
        ANCHORING_PROHIBITED(89),
        NAVIGATION_PROHIBITED(90),
        OVERTAKING_PROHIBITED(91),
        OVERTAKING_PROHIBITED_FOR_CONVOYS(92),
        TWO_WAY_TRAFFIC_PROHIBITED(93),
        REDUCED_WAKE(94),
        SPEED_LIMIT(95),
        STOP_ON_REQUEST(96),
        SOUND_SIGNAL_MANDATORY(97),
        VHF_WORKING_CHANNEL(98),
        SOUND_SIGNAL_PROHIBITION(99),
        CROSSING_PROHIBITION(100),
        NO_BERTHING_LATERAL_LIMIT(101),
        BERTHING_PERMITTED(102),
        BERTHING_PERMITTED_LATERAL_LIMIT(103),
        SPEED_LIMIT_KMPH(104),
        PASSING_PROHIBITION_GENERAL(105),
        PASSING_PROHIBITION_CONVOY(106),
        INFORMATION_RADIO_CHANNEL(107),
        ICE_SPAR(108),
        PROHIBITED_AREA_ICESPAR(109),
        NATURE_RESERVE_NO_TRAFFIC(110),
        NATURE_RESERVE_AWACS(111),
        BIRD_SANCTUARY_NO_ENTRY(112),
        PROHIBITED_AREA_SENSITIVE_AREA(113),
        PROHIBITED_AREA_SECURITY_REASON(114),
        PROHIBITED_AREA_SEWAGE(115),
        INDUSTRIAL_AREA(116),
        UNDERWATER_CABLES_AND_PIPELINES(117),
        FAIRWAY(118),
        WAITING_AREA(119),
        PROHIBITED_AREA_NO_EXPLANATION(120),
        NATURE_RESERVE_ENTRY_PERMITTED_NOT_ANCHORING(121),
        FISH_SANCTUARY_NO_TRAWLING_NO_ENTRY(122),
        MILITARY_AREA_ENTRY_PROHIBITED(123),
        HISTORIC_WRECK_AREA_NO_ENTRY(124),
        NAVIGATIONAL_AID(125),
        CAUTIONARY_AREA_UNDERWATER_FEATURE(126),
        UNDERWATER_DANGER_AREA(127);

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
