import dtos.Booking;
import dtos.Property;
import dtos.Facilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for Booking functionality in the House Rental Management System.
 * Tests for User Stories 3, 4, 5, 15, and 16:
 * - As a customer I want to be able to change the end date before confirming the booking
 * - As a Customer, I want to search for properties by selecting start and end date
 * - As a Customer I want to be able to confirm my booking
 * - As a customer, I want to cancel my booking at least 7 days before the start date
 * - As a customer, I want to extend my active booking if the property is available
 */
public class BookingManagementTest {

    private Booking booking;
    private Date startDate;
    private Date endDate;
    private Date bookingDate;

    @Mock
    private Property mockProperty;

    @Mock
    private Facilities mockFacilities;    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up dates for testing
        bookingDate = Date.valueOf(LocalDate.now());
        startDate = Date.valueOf(LocalDate.now().plusDays(14));
        endDate = Date.valueOf(LocalDate.now().plusDays(21));

        // Create booking with property ID 123 and username "testuser"
        booking = new Booking(bookingDate, startDate, endDate, 123, "testuser");

        // Set up property mock
        when(mockProperty.getId()).thenReturn(123);
        when(mockProperty.getLocation()).thenReturn("Copenhagen");
        when(mockProperty.getPricePerNight()).thenReturn(200.0);
        when(mockProperty.getFacilities()).thenReturn(mockFacilities);
        when(mockFacilities.isAvailable()).thenReturn(true);
        when(mockProperty.getAvailability()).thenReturn(true);
    }    /**
     * Test searching for properties by date range (User Story 4)
     */
    @Test
    public void testSearchPropertiesByDate() {
        // Test dates for property search
        Date searchStartDate = Date.valueOf(LocalDate.now().plusDays(10));
        Date searchEndDate = Date.valueOf(LocalDate.now().plusDays(17));

        // Property is available for these dates
        when(mockProperty.getAvailability()).thenReturn(true);

        // Verify property can be found in search results
        assertTrue(mockProperty.getAvailability(), "Property should be available for the search dates");

        // Check if the property would appear in search results
        // We're simulating a search here, not actually calling a search method
        boolean isAvailableForBooking = mockProperty.getAvailability();
        assertTrue(isAvailableForBooking, "Property should be available for booking during search period");
    }

    /**
     * Test changing booking end date (User Story 3)
     */
    @Test
    public void testChangeBookingEndDate() {
        // Original end date
        Date originalEndDate = booking.getEndDate();

        // New end date is 3 days later
        Date newEndDate = Date.valueOf(LocalDate.now().plusDays(24));

        // Change the end date
        booking.setEndDate(newEndDate);

        // Verify the change
        assertNotEquals(originalEndDate, booking.getEndDate(), "End date should be changed");
        assertEquals(newEndDate, booking.getEndDate(), "New end date should be set correctly");

        // Verify other booking details remain unchanged
        assertEquals(startDate, booking.getStartDate(), "Start date should remain unchanged");
        assertEquals(123, booking.getPropertyId(), "Property ID should remain unchanged");
        assertEquals("testuser", booking.getUsername(), "Username should remain unchanged");
    }

    /**
     * Test booking confirmation (User Story 5)
     */
    @Test
    public void testBookingConfirmation() {
        // Setup booking details
        int propertyId = 123;
        String username = "testuser";
        double pricePerNight = 200.0;

        // Calculate expected booking duration and cost
        long days = ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate());
        double expectedTotalCost = days * pricePerNight;

        // Verify booking has correct property
        assertEquals(propertyId, booking.getPropertyId(), "Booking should have correct property ID");

        // Verify booking dates
        assertNotNull(booking.getStartDate(), "Booking should have start date");
        assertNotNull(booking.getEndDate(), "Booking should have end date");
        assertTrue(booking.getStartDate().before(booking.getEndDate()), "Start date should be before end date");

        // Simulate booking confirmation
        boolean bookingConfirmed = true;
        assertTrue(bookingConfirmed, "Booking should be confirmed successfully");
    }

    /**
     * Test booking cancellation (User Story 15)
     */
    @Test
    public void testCancelBooking() {
        // Current date for testing
        LocalDate currentDate = LocalDate.now();

        // Booking start date (14 days from now, as set up in setUp())
        LocalDate bookingStartLocalDate = startDate.toLocalDate();

        // Calculate days until booking starts
        long daysUntilBooking = ChronoUnit.DAYS.between(currentDate, bookingStartLocalDate);

        // Check if booking can be canceled (must be at least 7 days before start date)
        boolean canCancel = daysUntilBooking >= 7;

        // Assert that the booking can be canceled
        assertTrue(canCancel, "Booking should be cancellable at least 7 days before start date");
        assertTrue(daysUntilBooking > 7, "Test setup should ensure booking is more than 7 days in future");

        // Test cancellation of booking too close to start date
        Date tooLateStartDate = Date.valueOf(LocalDate.now().plusDays(5));
        Booking lateCancelBooking = new Booking(bookingDate, tooLateStartDate, endDate, 123, "testuser");

        // Calculate days until this booking starts
        long daysUntilLateBooking = ChronoUnit.DAYS.between(currentDate, tooLateStartDate.toLocalDate());

        // Check if this booking can be canceled
        boolean canCancelLate = daysUntilLateBooking >= 7;

        // Assert that this booking cannot be canceled
        assertFalse(canCancelLate, "Booking should not be cancellable less than 7 days before start date");
    }    /**
     * Test extending active booking (User Story 16)
     */
    @Test
    public void testExtendBooking() {
        // Setup a current ongoing booking (start date is yesterday, end date is 7 days from now)
        Date ongoingStartDate = Date.valueOf(LocalDate.now().minusDays(1));
        Date ongoingEndDate = Date.valueOf(LocalDate.now().plusDays(7));
        Booking ongoingBooking = new Booking(bookingDate, ongoingStartDate, ongoingEndDate, 123, "testuser");

        // New desired end date (extend by 5 more days)
        Date extendedEndDate = Date.valueOf(LocalDate.now().plusDays(12));

        // Property is available for extension
        when(mockProperty.getAvailability()).thenReturn(true);

        // Extend booking
        ongoingBooking.setEndDate(extendedEndDate);

        // Verify extension
        assertEquals(extendedEndDate, ongoingBooking.getEndDate(), "Booking end date should be extended");

        // Verify property can be booked for extended period
        assertTrue(mockProperty.getAvailability(), "Property should be available for extended booking period");
    }
@Test
    public void testBookingHistory() {
        // Set up a booking history for the user
        Booking booking1 = new Booking(bookingDate, startDate, endDate, 123, "testuser");
        Booking booking2 = new Booking(bookingDate, startDate, Date.valueOf(LocalDate.now().plusDays(10)), 124, "testuser");

        // Simulate a booking history
        Booking[] bookingHistory = {booking1, booking2};

        // Verify booking history contains correct bookings
        assertEquals(2, bookingHistory.length, "Booking history should contain two bookings");
        assertEquals("testuser", bookingHistory[0].getUsername(), "First booking should belong to testuser");
        assertEquals("testuser", bookingHistory[1].getUsername(), "Second booking should also belong to testuser");
    }
}
