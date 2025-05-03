package main.dk.via.pro2.sprint1;

import main.dk.via.pro2.sprint1.model.Facilities;
import main.dk.via.pro2.sprint1.model.Property;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class PropertyTest {

    private Property property;

    @Mock
    private Facilities mockFacilities;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        property = new Property(6, "Test Location", 100.0, mockFacilities, true);
    }

    @Test
    public void testGetLocation() {
        assertEquals("Test Location", property.getLocation());
    }

    @Test
    public void testSetLocation() {
        property.setLocation("New Location");
        assertEquals("New Location", property.getLocation());
    }

    @Test
    public void testGetPricePerNight() {
        assertEquals(100.0, property.getPricePerNight(), 0.001);
    }

    @Test
    public void testSetPricePerNight() {
        property.setPricePerNight(150.0);
        assertEquals(150.0, property.getPricePerNight(), 0.001);
    }

    @Test
    public void testGetAvailability() {
        assertTrue(property.getAvailability());
    }

    @Test
    public void testSetAvailability() {
        property.setAvailability(false);
        assertFalse(property.getAvailability());
    }

    @Test
    public void testGetFacilities() {
        when(mockFacilities.toString()).thenReturn("Mock Facilities");
        assertEquals(mockFacilities, property.getFacilities());
    }

    @Test
    public void testChangeEndDate() {

    }
}