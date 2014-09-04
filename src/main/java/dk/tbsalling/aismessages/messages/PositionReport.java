/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 * 
 * (C) Copyright 2011-2013 by S-Consult ApS, DK31327490, http://s-consult.dk, Denmark.
 * 
 * Released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * For details of this license see the nearby LICENCE-full file, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 * 
 * NOT FOR COMMERCIAL USE!
 * Contact sales@s-consult.dk to obtain a commercially licensed version of this software.
 * 
 */

/**
 * 
 */
package dk.tbsalling.aismessages.messages;

import dk.tbsalling.aismessages.messages.types.AISMessageType;
import dk.tbsalling.aismessages.messages.types.MMSI;
import dk.tbsalling.aismessages.messages.types.ManeuverIndicator;
import dk.tbsalling.aismessages.messages.types.NavigationStatus;


/**
 * @author tbsalling
 *
 */
@SuppressWarnings("serial")
public abstract class PositionReport extends DecodedAISMessage {

	protected PositionReport(AISMessageType messageType,
			Integer repeatIndicator, MMSI sourceMmsi,
			NavigationStatus navigationStatus, Integer rateOfTurn,
			Float speedOverGround, Boolean positionAccurate, Float latitude,
			Float longitude, Float courseOverGround, Integer trueHeading,
			Integer second, ManeuverIndicator maneuverIndicator,
			Boolean raimFlag) {
		super(messageType, repeatIndicator, sourceMmsi);
		this.navigationStatus = navigationStatus;
		this.rateOfTurn = rateOfTurn;
		this.speedOverGround = speedOverGround;
		this.positionAccurate = positionAccurate;
		this.latitude = latitude;
		this.longitude = longitude;
		this.courseOverGround = courseOverGround;
		this.trueHeading = trueHeading;
		this.second = second;
		this.maneuverIndicator = maneuverIndicator;
		this.raimFlag = raimFlag;
	}

    @SuppressWarnings("unused")
	public final NavigationStatus getNavigationStatus() {
		return navigationStatus;
	}

    @SuppressWarnings("unused")
	public final Integer getRateOfTurn() {
		return rateOfTurn;
	}

    @SuppressWarnings("unused")
	public final Float getSpeedOverGround() {
		return speedOverGround;
	}

    @SuppressWarnings("unused")
	public final Boolean getPositionAccurate() {
		return positionAccurate;
	}

    @SuppressWarnings("unused")
	public final Float getLatitude() {
		return latitude;
	}

    @SuppressWarnings("unused")
	public final Float getLongitude() {
		return longitude;
	}

    @SuppressWarnings("unused")
	public final Float getCourseOverGround() {
		return courseOverGround;
	}

    @SuppressWarnings("unused")
	public final Integer getTrueHeading() {
		return trueHeading;
	}

    @SuppressWarnings("unused")
	public final Integer getSecond() {
		return second;
	}

    @SuppressWarnings("unused")
	public final ManeuverIndicator getManeuverIndicator() {
		return maneuverIndicator;
	}

    @SuppressWarnings("unused")
	public final Boolean getRaimFlag() {
		return raimFlag;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PositionReport [navigationStatus=")
				.append(navigationStatus).append(", rateOfTurn=")
				.append(rateOfTurn).append(", speedOverGround=")
				.append(speedOverGround).append(", positionAccurate=")
				.append(positionAccurate).append(", latitude=")
				.append(latitude).append(", longitude=").append(longitude)
				.append(", courseOverGround=").append(courseOverGround)
				.append(", trueHeading=").append(trueHeading)
				.append(", second=").append(second)
				.append(", maneuverIndicator=").append(maneuverIndicator)
				.append(", raimFlag=").append(raimFlag)
				.append(", getMessageType()=").append(getMessageType())
				.append(", getRepeatIndicator()=").append(getRepeatIndicator())
				.append(", getSourceMmsi()=").append(getSourceMmsi())
				.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((courseOverGround == null) ? 0 : courseOverGround.hashCode());
		result = prime * result
				+ ((latitude == null) ? 0 : latitude.hashCode());
		result = prime * result
				+ ((longitude == null) ? 0 : longitude.hashCode());
		result = prime
				* result
				+ ((maneuverIndicator == null) ? 0 : maneuverIndicator
						.hashCode());
		result = prime
				* result
				+ ((navigationStatus == null) ? 0 : navigationStatus.hashCode());
		result = prime
				* result
				+ ((positionAccurate == null) ? 0 : positionAccurate.hashCode());
		result = prime * result
				+ ((raimFlag == null) ? 0 : raimFlag.hashCode());
		result = prime * result
				+ ((rateOfTurn == null) ? 0 : rateOfTurn.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		result = prime * result
				+ ((speedOverGround == null) ? 0 : speedOverGround.hashCode());
		result = prime * result
				+ ((trueHeading == null) ? 0 : trueHeading.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PositionReport))
			return false;
		PositionReport other = (PositionReport) obj;
		if (courseOverGround == null) {
			if (other.courseOverGround != null)
				return false;
		} else if (!courseOverGround.equals(other.courseOverGround))
			return false;
		if (latitude == null) {
			if (other.latitude != null)
				return false;
		} else if (!latitude.equals(other.latitude))
			return false;
		if (longitude == null) {
			if (other.longitude != null)
				return false;
		} else if (!longitude.equals(other.longitude))
			return false;
		if (maneuverIndicator != other.maneuverIndicator)
			return false;
		if (navigationStatus != other.navigationStatus)
			return false;
		if (positionAccurate == null) {
			if (other.positionAccurate != null)
				return false;
		} else if (!positionAccurate.equals(other.positionAccurate))
			return false;
		if (raimFlag == null) {
			if (other.raimFlag != null)
				return false;
		} else if (!raimFlag.equals(other.raimFlag))
			return false;
		if (rateOfTurn == null) {
			if (other.rateOfTurn != null)
				return false;
		} else if (!rateOfTurn.equals(other.rateOfTurn))
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		if (speedOverGround == null) {
			if (other.speedOverGround != null)
				return false;
		} else if (!speedOverGround.equals(other.speedOverGround))
			return false;
		if (trueHeading == null) {
			if (other.trueHeading != null)
				return false;
		} else if (!trueHeading.equals(other.trueHeading))
			return false;
		return true;
	}

	private final NavigationStatus navigationStatus;
	private final Integer rateOfTurn;
	private final Float speedOverGround;
	private final Boolean positionAccurate;
	private final Float latitude;
	private final Float longitude;
	private final Float courseOverGround;
	private final Integer trueHeading;
	private final Integer second;
	private final ManeuverIndicator maneuverIndicator;
	private final Boolean raimFlag;
}
