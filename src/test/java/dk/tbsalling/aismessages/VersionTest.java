package dk.tbsalling.aismessages;

import dk.tbsalling.aismessages.version.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VersionTest {

    @Test
    public void testVersionIsNotNull() {
        assertNotNull(Version.VERSION);
        assertFalse(Version.VERSION.isEmpty());
    }

    @Test
    public void testArtifactId() {
        assertEquals("aismessages", Version.ARTIFACT_ID);
    }

    @Test
    public void testGroupId() {
        assertEquals("dk.tbsalling", Version.GROUP_ID);
    }

    @Test
    public void testBuildTimestampFormat() {
        // Check that timestamp follows ISO 8601 format: yyyy-MM-dd'T'HH:mm:ss'Z'
        assertNotNull(Version.BUILD_TIMESTAMP);
        assertTrue(Version.BUILD_TIMESTAMP.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z"),
                "Build timestamp should follow format: yyyy-MM-dd'T'HH:mm:ss'Z', got: " + Version.BUILD_TIMESTAMP);
    }

    @Test
    public void testName() {
        assertEquals("aismessages", Version.NAME);
    }

    @Test
    public void testGetVersionInfo() {
        String versionInfo = Version.getVersionInfo();
        assertNotNull(versionInfo);
        assertTrue(versionInfo.contains(Version.NAME));
        assertTrue(versionInfo.contains(Version.VERSION));
        assertTrue(versionInfo.contains(Version.BUILD_TIMESTAMP));
    }

    @Test
    public void testGetMavenCoordinates() {
        String coordinates = Version.getMavenCoordinates();
        assertNotNull(coordinates);
        assertEquals("dk.tbsalling:aismessages:" + Version.VERSION, coordinates);
    }

    @Test
    public void testVersionInfoFormat() {
        // Should be formatted as: "aismessages v3.5.1 (built: 2025-10-14T15:39:01Z)"
        String versionInfo = Version.getVersionInfo();
        assertTrue(versionInfo.startsWith(Version.NAME + " v" + Version.VERSION));
        assertTrue(versionInfo.contains("built:"));
    }
}
