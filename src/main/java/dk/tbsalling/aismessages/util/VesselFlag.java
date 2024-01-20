package dk.tbsalling.aismessages.util;

import java.util.HashMap;

public class VesselFlag {

    private final static HashMap<Integer, VesselFlag> FLAGS_MAP = new HashMap<>();

    static {
        FLAGS_MAP.put(201, new VesselFlag("Albania", "AL"));
        FLAGS_MAP.put(202, new VesselFlag("Andorra", "AD"));
        FLAGS_MAP.put(203, new VesselFlag("Austria", "AT"));
        FLAGS_MAP.put(204, new VesselFlag("Portugal", "PT"));
        FLAGS_MAP.put(205, new VesselFlag("Belgium", "BE"));
        FLAGS_MAP.put(206, new VesselFlag("Belarus", "BY"));
        FLAGS_MAP.put(207, new VesselFlag("Bulgaria", "BG"));
        FLAGS_MAP.put(208, new VesselFlag("Vatican", "VA"));
        FLAGS_MAP.put(209, new VesselFlag("Cyprus", "CY"));
        FLAGS_MAP.put(210, new VesselFlag("Cyprus", "CY"));
        FLAGS_MAP.put(211, new VesselFlag("Germany", "DE"));
        FLAGS_MAP.put(212, new VesselFlag("Cyprus", "CY"));
        FLAGS_MAP.put(213, new VesselFlag("Georgia", "GE"));
        FLAGS_MAP.put(214, new VesselFlag("Moldova", "MD"));
        FLAGS_MAP.put(215, new VesselFlag("Malta", "MT"));
        FLAGS_MAP.put(216, new VesselFlag("Armenia", "AM"));
        FLAGS_MAP.put(218, new VesselFlag("Germany", "DE"));
        FLAGS_MAP.put(219, new VesselFlag("Denmark", "DK"));
        FLAGS_MAP.put(220, new VesselFlag("Denmark", "DK"));
        FLAGS_MAP.put(224, new VesselFlag("Spain", "ES"));
        FLAGS_MAP.put(225, new VesselFlag("Spain", "ES"));
        FLAGS_MAP.put(226, new VesselFlag("France", "FR"));
        FLAGS_MAP.put(227, new VesselFlag("France", "FR"));
        FLAGS_MAP.put(228, new VesselFlag("France", "FR"));
        FLAGS_MAP.put(229, new VesselFlag("Malta", "MT"));
        FLAGS_MAP.put(230, new VesselFlag("Finland", "FI"));
        FLAGS_MAP.put(231, new VesselFlag("Faroe Is", "FO"));
        FLAGS_MAP.put(232, new VesselFlag("United Kingdom", "GB"));
        FLAGS_MAP.put(233, new VesselFlag("United Kingdom", "GB"));
        FLAGS_MAP.put(234, new VesselFlag("United Kingdom", "GB"));
        FLAGS_MAP.put(235, new VesselFlag("United Kingdom", "GB"));
        FLAGS_MAP.put(236, new VesselFlag("Gibraltar", "GI"));
        FLAGS_MAP.put(237, new VesselFlag("Greece", "GR"));
        FLAGS_MAP.put(238, new VesselFlag("Croatia", "HR"));
        FLAGS_MAP.put(239, new VesselFlag("Greece", "GR"));
        FLAGS_MAP.put(240, new VesselFlag("Greece", "GR"));
        FLAGS_MAP.put(241, new VesselFlag("Greece", "GR"));
        FLAGS_MAP.put(242, new VesselFlag("Morocco", "MA"));
        FLAGS_MAP.put(243, new VesselFlag("Hungary", "HU"));
        FLAGS_MAP.put(244, new VesselFlag("Netherlands", "NL"));
        FLAGS_MAP.put(245, new VesselFlag("Netherlands", "NL"));
        FLAGS_MAP.put(246, new VesselFlag("Netherlands", "NL"));
        FLAGS_MAP.put(247, new VesselFlag("Italy", "IT"));
        FLAGS_MAP.put(248, new VesselFlag("Malta", "MT"));
        FLAGS_MAP.put(249, new VesselFlag("Malta", "MT"));
        FLAGS_MAP.put(250, new VesselFlag("Ireland", "IE"));
        FLAGS_MAP.put(251, new VesselFlag("Iceland", "IS"));
        FLAGS_MAP.put(252, new VesselFlag("Liechtenstein", "LI"));
        FLAGS_MAP.put(253, new VesselFlag("Luxembourg", "LU"));
        FLAGS_MAP.put(254, new VesselFlag("Monaco", "MC"));
        FLAGS_MAP.put(255, new VesselFlag("Portugal", "PT"));
        FLAGS_MAP.put(256, new VesselFlag("Malta", "MT"));
        FLAGS_MAP.put(257, new VesselFlag("Norway", "NO"));
        FLAGS_MAP.put(258, new VesselFlag("Norway", "NO"));
        FLAGS_MAP.put(259, new VesselFlag("Norway", "NO"));
        FLAGS_MAP.put(261, new VesselFlag("Poland", "PL"));
        FLAGS_MAP.put(262, new VesselFlag("Montenegro", "ME"));
        FLAGS_MAP.put(263, new VesselFlag("Portugal", "PT"));
        FLAGS_MAP.put(264, new VesselFlag("Romania", "RO"));
        FLAGS_MAP.put(265, new VesselFlag("Sweden", "SE"));
        FLAGS_MAP.put(266, new VesselFlag("Sweden", "SE"));
        FLAGS_MAP.put(267, new VesselFlag("Slovakia", "SK"));
        FLAGS_MAP.put(268, new VesselFlag("San Marino", "SM"));
        FLAGS_MAP.put(269, new VesselFlag("Switzerland", "CH"));
        FLAGS_MAP.put(270, new VesselFlag("Czech Republic", "CZ"));
        FLAGS_MAP.put(271, new VesselFlag("Turkey", "TR"));
        FLAGS_MAP.put(272, new VesselFlag("Ukraine", "UA"));
        FLAGS_MAP.put(273, new VesselFlag("Russia", "RU"));
        FLAGS_MAP.put(274, new VesselFlag("FYR Macedonia", "MK"));
        FLAGS_MAP.put(275, new VesselFlag("Latvia", "LV"));
        FLAGS_MAP.put(276, new VesselFlag("Estonia", "EE"));
        FLAGS_MAP.put(277, new VesselFlag("Lithuania", "LT"));
        FLAGS_MAP.put(278, new VesselFlag("Slovenia", "SI"));
        FLAGS_MAP.put(279, new VesselFlag("Serbia", "RS"));
        FLAGS_MAP.put(301, new VesselFlag("Anguilla", "AI"));
        FLAGS_MAP.put(303, new VesselFlag("USA", "US"));
        FLAGS_MAP.put(304, new VesselFlag("Antigua Barbuda", "AG"));
        FLAGS_MAP.put(305, new VesselFlag("Antigua Barbuda", "AG"));
        FLAGS_MAP.put(306, new VesselFlag("Curacao", "CW"));
        FLAGS_MAP.put(307, new VesselFlag("Aruba", "AW"));
        FLAGS_MAP.put(308, new VesselFlag("Bahamas", "BS"));
        FLAGS_MAP.put(309, new VesselFlag("Bahamas", "BS"));
        FLAGS_MAP.put(310, new VesselFlag("Bermuda", "BM"));
        FLAGS_MAP.put(311, new VesselFlag("Bahamas", "BS"));
        FLAGS_MAP.put(312, new VesselFlag("Belize", "BZ"));
        FLAGS_MAP.put(314, new VesselFlag("Barbados", "BB"));
        FLAGS_MAP.put(316, new VesselFlag("Canada", "CA"));
        FLAGS_MAP.put(319, new VesselFlag("Cayman Is", "KY"));
        FLAGS_MAP.put(321, new VesselFlag("Costa Rica", "CR"));
        FLAGS_MAP.put(323, new VesselFlag("Cuba", "CU"));
        FLAGS_MAP.put(325, new VesselFlag("Dominica", "DM"));
        FLAGS_MAP.put(327, new VesselFlag("Dominican Rep", "DO"));
        FLAGS_MAP.put(329, new VesselFlag("Guadeloupe", "GP"));
        FLAGS_MAP.put(330, new VesselFlag("Grenada", "GD"));
        FLAGS_MAP.put(331, new VesselFlag("Greenland", "GL"));
        FLAGS_MAP.put(332, new VesselFlag("Guatemala", "GT"));
        FLAGS_MAP.put(334, new VesselFlag("Honduras", "HN"));
        FLAGS_MAP.put(336, new VesselFlag("Haiti", "HT"));
        FLAGS_MAP.put(338, new VesselFlag("USA", "US"));
        FLAGS_MAP.put(339, new VesselFlag("Jamaica", "JM"));
        FLAGS_MAP.put(341, new VesselFlag("St Kitts Nevis", "KN"));
        FLAGS_MAP.put(343, new VesselFlag("St Lucia", "LC"));
        FLAGS_MAP.put(345, new VesselFlag("Mexico", "MX"));
        FLAGS_MAP.put(347, new VesselFlag("Martinique", "MQ"));
        FLAGS_MAP.put(348, new VesselFlag("Montserrat", "MS"));
        FLAGS_MAP.put(350, new VesselFlag("Nicaragua", "NI"));
        FLAGS_MAP.put(351, new VesselFlag("Panama", "PA"));
        FLAGS_MAP.put(352, new VesselFlag("Panama", "PA"));
        FLAGS_MAP.put(353, new VesselFlag("Panama", "PA"));
        FLAGS_MAP.put(354, new VesselFlag("Panama", "PA"));
        FLAGS_MAP.put(355, new VesselFlag("Panama", "PA"));
        FLAGS_MAP.put(356, new VesselFlag("Panama", "PA"));
        FLAGS_MAP.put(357, new VesselFlag("Panama", "PA"));
        FLAGS_MAP.put(358, new VesselFlag("Puerto Rico", "PR"));
        FLAGS_MAP.put(359, new VesselFlag("El Salvador", "SV"));
        FLAGS_MAP.put(361, new VesselFlag("St Pierre Miquelon", "PM"));
        FLAGS_MAP.put(362, new VesselFlag("Trinidad Tobago", "TT"));
        FLAGS_MAP.put(364, new VesselFlag("Turks Caicos Is", "TC"));
        FLAGS_MAP.put(366, new VesselFlag("USA", "US"));
        FLAGS_MAP.put(367, new VesselFlag("USA", "US"));
        FLAGS_MAP.put(368, new VesselFlag("USA", "US"));
        FLAGS_MAP.put(369, new VesselFlag("USA", "US"));
        FLAGS_MAP.put(370, new VesselFlag("Panama", "PA"));
        FLAGS_MAP.put(371, new VesselFlag("Panama", "PA"));
        FLAGS_MAP.put(372, new VesselFlag("Panama", "PA"));
        FLAGS_MAP.put(373, new VesselFlag("Panama", "PA"));
        FLAGS_MAP.put(374, new VesselFlag("Panama", "PA"));
        FLAGS_MAP.put(375, new VesselFlag("St Vincent Grenadines", "VC"));
        FLAGS_MAP.put(376, new VesselFlag("St Vincent Grenadines", "VC"));
        FLAGS_MAP.put(377, new VesselFlag("St Vincent Grenadines", "VC"));
        FLAGS_MAP.put(378, new VesselFlag("British Virgin Is", "VG"));
        FLAGS_MAP.put(379, new VesselFlag("US Virgin Is", "VI"));
        FLAGS_MAP.put(401, new VesselFlag("Afghanistan", "AF"));
        FLAGS_MAP.put(403, new VesselFlag("Saudi Arabia", "SA"));
        FLAGS_MAP.put(405, new VesselFlag("Bangladesh", "BD"));
        FLAGS_MAP.put(408, new VesselFlag("Bahrain", "BH"));
        FLAGS_MAP.put(410, new VesselFlag("Bhutan", "BT"));
        FLAGS_MAP.put(412, new VesselFlag("China", "CN"));
        FLAGS_MAP.put(413, new VesselFlag("China", "CN"));
        FLAGS_MAP.put(414, new VesselFlag("China", "CN"));
        FLAGS_MAP.put(416, new VesselFlag("Taiwan", "TW"));
        FLAGS_MAP.put(417, new VesselFlag("Sri Lanka", "LK"));
        FLAGS_MAP.put(419, new VesselFlag("India", "IN"));
        FLAGS_MAP.put(422, new VesselFlag("Iran", "IR"));
        FLAGS_MAP.put(423, new VesselFlag("Azerbaijan", "AZ"));
        FLAGS_MAP.put(425, new VesselFlag("Iraq", "IQ"));
        FLAGS_MAP.put(428, new VesselFlag("Israel", "IL"));
        FLAGS_MAP.put(431, new VesselFlag("Japan", "JP"));
        FLAGS_MAP.put(432, new VesselFlag("Japan", "JP"));
        FLAGS_MAP.put(434, new VesselFlag("Turkmenistan", "TM"));
        FLAGS_MAP.put(436, new VesselFlag("Kazakhstan", "KZ"));
        FLAGS_MAP.put(437, new VesselFlag("Uzbekistan", "UZ"));
        FLAGS_MAP.put(438, new VesselFlag("Jordan", "JO"));
        FLAGS_MAP.put(440, new VesselFlag("Korea", "KR"));
        FLAGS_MAP.put(441, new VesselFlag("Korea", "KR"));
        FLAGS_MAP.put(443, new VesselFlag("Palestine", "PS"));
        FLAGS_MAP.put(445, new VesselFlag("DPR Korea", "KP"));
        FLAGS_MAP.put(447, new VesselFlag("Kuwait", "KW"));
        FLAGS_MAP.put(450, new VesselFlag("Lebanon", "LB"));
        FLAGS_MAP.put(451, new VesselFlag("Kyrgyz Republic", "KG"));
        FLAGS_MAP.put(453, new VesselFlag("Macao", "MO"));
        FLAGS_MAP.put(455, new VesselFlag("Maldives", "MV"));
        FLAGS_MAP.put(457, new VesselFlag("Mongolia", "MN"));
        FLAGS_MAP.put(459, new VesselFlag("Nepal", "NP"));
        FLAGS_MAP.put(461, new VesselFlag("Oman", "OM"));
        FLAGS_MAP.put(463, new VesselFlag("Pakistan", "PK"));
        FLAGS_MAP.put(466, new VesselFlag("Qatar", "QA"));
        FLAGS_MAP.put(468, new VesselFlag("Syria", "SY"));
        FLAGS_MAP.put(470, new VesselFlag("UAE", "AE"));
        FLAGS_MAP.put(471, new VesselFlag("UAE", "AE"));
        FLAGS_MAP.put(472, new VesselFlag("Tajikistan", "TJ"));
        FLAGS_MAP.put(473, new VesselFlag("Yemen", "YE"));
        FLAGS_MAP.put(475, new VesselFlag("Yemen", "YE"));
        FLAGS_MAP.put(477, new VesselFlag("Hong Kong", "HK"));
        FLAGS_MAP.put(478, new VesselFlag("Bosnia and Herzegovina", "BA"));
        FLAGS_MAP.put(501, new VesselFlag("Antarctica", "AQ"));
        FLAGS_MAP.put(503, new VesselFlag("Australia", "AU"));
        FLAGS_MAP.put(506, new VesselFlag("Myanmar", "MM"));
        FLAGS_MAP.put(508, new VesselFlag("Brunei", "BN"));
        FLAGS_MAP.put(510, new VesselFlag("Micronesia", "FM"));
        FLAGS_MAP.put(511, new VesselFlag("Palau", "PW"));
        FLAGS_MAP.put(512, new VesselFlag("New Zealand", "NZ"));
        FLAGS_MAP.put(514, new VesselFlag("Cambodia", "KH"));
        FLAGS_MAP.put(515, new VesselFlag("Cambodia", "KH"));
        FLAGS_MAP.put(516, new VesselFlag("Christmas Is", "CX"));
        FLAGS_MAP.put(518, new VesselFlag("Cook Is", "CK"));
        FLAGS_MAP.put(520, new VesselFlag("Fiji", "FJ"));
        FLAGS_MAP.put(523, new VesselFlag("Cocos Is", "CC"));
        FLAGS_MAP.put(525, new VesselFlag("Indonesia", "ID"));
        FLAGS_MAP.put(529, new VesselFlag("Kiribati", "KI"));
        FLAGS_MAP.put(531, new VesselFlag("Laos", "LA"));
        FLAGS_MAP.put(533, new VesselFlag("Malaysia", "MY"));
        FLAGS_MAP.put(536, new VesselFlag("N Mariana Is", "MP"));
        FLAGS_MAP.put(538, new VesselFlag("Marshall Is", "MH"));
        FLAGS_MAP.put(540, new VesselFlag("New Caledonia", "NC"));
        FLAGS_MAP.put(542, new VesselFlag("Niue", "NU"));
        FLAGS_MAP.put(544, new VesselFlag("Nauru", "NR"));
        FLAGS_MAP.put(546, new VesselFlag("French Polynesia", "PF"));
        FLAGS_MAP.put(548, new VesselFlag("Philippines", "PH"));
        FLAGS_MAP.put(553, new VesselFlag("Papua New Guinea", "PG"));
        FLAGS_MAP.put(555, new VesselFlag("Pitcairn Is", "PN"));
        FLAGS_MAP.put(557, new VesselFlag("Solomon Is", "SB"));
        FLAGS_MAP.put(559, new VesselFlag("American Samoa", "AS"));
        FLAGS_MAP.put(561, new VesselFlag("Samoa", "WS"));
        FLAGS_MAP.put(563, new VesselFlag("Singapore", "SG"));
        FLAGS_MAP.put(564, new VesselFlag("Singapore", "SG"));
        FLAGS_MAP.put(565, new VesselFlag("Singapore", "SG"));
        FLAGS_MAP.put(566, new VesselFlag("Singapore", "SG"));
        FLAGS_MAP.put(567, new VesselFlag("Thailand", "TH"));
        FLAGS_MAP.put(570, new VesselFlag("Tonga", "TO"));
        FLAGS_MAP.put(572, new VesselFlag("Tuvalu", "TV"));
        FLAGS_MAP.put(574, new VesselFlag("Vietnam", "VN"));
        FLAGS_MAP.put(576, new VesselFlag("Vanuatu", "VU"));
        FLAGS_MAP.put(577, new VesselFlag("Vanuatu", "VU"));
        FLAGS_MAP.put(578, new VesselFlag("Wallis Futuna Is", "WF"));
        FLAGS_MAP.put(601, new VesselFlag("South Africa", "ZA"));
        FLAGS_MAP.put(603, new VesselFlag("Angola", "AO"));
        FLAGS_MAP.put(605, new VesselFlag("Algeria", "DZ"));
        FLAGS_MAP.put(607, new VesselFlag("St Paul Amsterdam Is", "TF"));
        FLAGS_MAP.put(608, new VesselFlag("Ascension Is", "IO"));
        FLAGS_MAP.put(609, new VesselFlag("Burundi", "BI"));
        FLAGS_MAP.put(610, new VesselFlag("Benin", "BJ"));
        FLAGS_MAP.put(611, new VesselFlag("Botswana", "BW"));
        FLAGS_MAP.put(612, new VesselFlag("Cen Afr Rep", "CF"));
        FLAGS_MAP.put(613, new VesselFlag("Cameroon", "CM"));
        FLAGS_MAP.put(615, new VesselFlag("Congo", "CG"));
        FLAGS_MAP.put(616, new VesselFlag("Comoros", "KM"));
        FLAGS_MAP.put(617, new VesselFlag("Cape Verde", "CV"));
        FLAGS_MAP.put(618, new VesselFlag("Antarctica", "AQ"));
        FLAGS_MAP.put(619, new VesselFlag("Ivory Coast", "CI"));
        FLAGS_MAP.put(620, new VesselFlag("Comoros", "KM"));
        FLAGS_MAP.put(621, new VesselFlag("Djibouti", "DJ"));
        FLAGS_MAP.put(622, new VesselFlag("Egypt", "EG"));
        FLAGS_MAP.put(624, new VesselFlag("Ethiopia", "ET"));
        FLAGS_MAP.put(625, new VesselFlag("Eritrea", "ER"));
        FLAGS_MAP.put(626, new VesselFlag("Gabon", "GA"));
        FLAGS_MAP.put(627, new VesselFlag("Ghana", "GH"));
        FLAGS_MAP.put(629, new VesselFlag("Gambia", "GM"));
        FLAGS_MAP.put(630, new VesselFlag("Guinea-Bissau", "GW"));
        FLAGS_MAP.put(631, new VesselFlag("Equ. Guinea", "GQ"));
        FLAGS_MAP.put(632, new VesselFlag("Guinea", "GN"));
        FLAGS_MAP.put(633, new VesselFlag("Burkina Faso", "BF"));
        FLAGS_MAP.put(634, new VesselFlag("Kenya", "KE"));
        FLAGS_MAP.put(635, new VesselFlag("Antarctica", "AQ"));
        FLAGS_MAP.put(636, new VesselFlag("Liberia", "LR"));
        FLAGS_MAP.put(637, new VesselFlag("Liberia", "LR"));
        FLAGS_MAP.put(642, new VesselFlag("Libya", "LY"));
        FLAGS_MAP.put(644, new VesselFlag("Lesotho", "LS"));
        FLAGS_MAP.put(645, new VesselFlag("Mauritius", "MU"));
        FLAGS_MAP.put(647, new VesselFlag("Madagascar", "MG"));
        FLAGS_MAP.put(649, new VesselFlag("Mali", "ML"));
        FLAGS_MAP.put(650, new VesselFlag("Mozambique", "MZ"));
        FLAGS_MAP.put(654, new VesselFlag("Mauritania", "MR"));
        FLAGS_MAP.put(655, new VesselFlag("Malawi", "MW"));
        FLAGS_MAP.put(656, new VesselFlag("Niger", "NE"));
        FLAGS_MAP.put(657, new VesselFlag("Nigeria", "NG"));
        FLAGS_MAP.put(659, new VesselFlag("Namibia", "NA"));
        FLAGS_MAP.put(660, new VesselFlag("Reunion", "RE"));
        FLAGS_MAP.put(661, new VesselFlag("Rwanda", "RW"));
        FLAGS_MAP.put(662, new VesselFlag("Sudan", "SD"));
        FLAGS_MAP.put(663, new VesselFlag("Senegal", "SN"));
        FLAGS_MAP.put(664, new VesselFlag("Seychelles", "SC"));
        FLAGS_MAP.put(665, new VesselFlag("St Helena", "SH"));
        FLAGS_MAP.put(666, new VesselFlag("Somalia", "SO"));
        FLAGS_MAP.put(667, new VesselFlag("Sierra Leone", "SL"));
        FLAGS_MAP.put(668, new VesselFlag("Sao Tome Principe", "ST"));
        FLAGS_MAP.put(669, new VesselFlag("Swaziland", "SZ"));
        FLAGS_MAP.put(670, new VesselFlag("Chad", "TD"));
        FLAGS_MAP.put(671, new VesselFlag("Togo", "TG"));
        FLAGS_MAP.put(672, new VesselFlag("Tunisia", "TN"));
        FLAGS_MAP.put(674, new VesselFlag("Tanzania", "TZ"));
        FLAGS_MAP.put(675, new VesselFlag("Uganda", "UG"));
        FLAGS_MAP.put(676, new VesselFlag("DR Congo", "CD"));
        FLAGS_MAP.put(677, new VesselFlag("Tanzania", "TZ"));
        FLAGS_MAP.put(678, new VesselFlag("Zambia", "ZM"));
        FLAGS_MAP.put(679, new VesselFlag("Zimbabwe", "ZW"));
        FLAGS_MAP.put(701, new VesselFlag("Argentina", "AR"));
        FLAGS_MAP.put(710, new VesselFlag("Brazil", "BR"));
        FLAGS_MAP.put(720, new VesselFlag("Bolivia", "BO"));
        FLAGS_MAP.put(725, new VesselFlag("Chile", "CL"));
        FLAGS_MAP.put(730, new VesselFlag("Colombia", "CO"));
        FLAGS_MAP.put(735, new VesselFlag("Ecuador", "EC"));
        FLAGS_MAP.put(740, new VesselFlag("UK", "UK"));
        FLAGS_MAP.put(745, new VesselFlag("Guiana", "GF"));
        FLAGS_MAP.put(750, new VesselFlag("Guyana", "GY"));
        FLAGS_MAP.put(755, new VesselFlag("Paraguay", "PY"));
        FLAGS_MAP.put(760, new VesselFlag("Peru", "PE"));
        FLAGS_MAP.put(765, new VesselFlag("Suriname", "SR"));
        FLAGS_MAP.put(770, new VesselFlag("Uruguay", "UY"));
        FLAGS_MAP.put(775, new VesselFlag("Venezuela", "VE"));
    }

    public static HashMap getVesselFlags() {
        return FLAGS_MAP;
    }

    private String country, shortCode;

    protected VesselFlag(String country, String shortCode) {
        this.country = country;
        this.shortCode = shortCode;
    }

    public String getCountry() {
        return country;
    }

    public String getShortCode() {
        return shortCode;
    }

    public static VesselFlag getFlagFromMmsi(String mmsi) throws NumberFormatException {
        int mid;
        if (mmsi.startsWith("111") || mmsi.startsWith("970")) {
            mid = Integer.parseInt(mmsi.substring(3, 6));
        } else if (mmsi.startsWith("00") || mmsi.startsWith("98") || mmsi.startsWith("99")) {
            mid = Integer.parseInt(mmsi.substring(2, 5));
        } else if (mmsi.startsWith("8") || mmsi.startsWith("0")) {
            mid = Integer.parseInt(mmsi.substring(1, 4));
        } else {
            mid = Integer.parseInt(mmsi.substring(0, 3));
        }

        return FLAGS_MAP.get(mid);
    }
}