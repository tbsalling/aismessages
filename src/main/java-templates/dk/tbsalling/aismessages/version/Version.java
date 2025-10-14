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

package dk.tbsalling.aismessages.version;

/**
 * Build version information for the AISMessages library.
 * This class is automatically generated during the Maven build process.
 *
 * @author tbsalling
 */
public final class Version {

    private Version() {
        // Utility class - prevent instantiation
    }

    /**
     * The version of the AISMessages library.
     */
    public static final String VERSION = "${project.version}";

    /**
     * The artifact ID of the AISMessages library.
     */
    public static final String ARTIFACT_ID = "${project.artifactId}";

    /**
     * The group ID of the AISMessages library.
     */
    public static final String GROUP_ID = "${project.groupId}";

    /**
     * The build timestamp (format: yyyy-MM-dd'T'HH:mm:ss'Z').
     */
    public static final String BUILD_TIMESTAMP = "${build.timestamp}";

    /**
     * The name of the library.
     */
    public static final String NAME = "${project.name}";

    /**
     * The Java version this library was compiled for.
     */
    public static final String JAVA_VERSION = "${maven.compiler.release}";

    /**
     * Returns a formatted string with complete version information.
     *
     * @return formatted version string
     */
    public static String getVersionInfo() {
        return String.format("%s v%s (built: %s, Java %s)", NAME, VERSION, BUILD_TIMESTAMP, JAVA_VERSION);
    }

    /**
     * Returns the Maven coordinates for this artifact.
     *
     * @return Maven coordinates in the format groupId:artifactId:version
     */
    public static String getMavenCoordinates() {
        return String.format("%s:%s:%s", GROUP_ID, ARTIFACT_ID, VERSION);
    }
}
