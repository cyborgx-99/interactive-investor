package com.digital.equalexperts;

import com.digital.equalexperts.helpers.Guest;
import com.digital.equalexperts.helpers.Guests;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

public class SimpleTest extends Setup
{
    @Before
    public void before() throws IOException {
        super.init();
        navigateToStartPage();
        Assert.assertTrue(homePage.isAt());
    }

    @Test
    public void addBooking() throws ParseException {
        Guest guest = Guests.createValidGuest();
        Boolean bookingMatches = false;

        List<String> originalBookingIds = homePage.getAllBookingIds();

        homePage.enterBookingFor(guest)
                .saveBooking();

        // the booking is most likely to appear at the end so traverse the table in reverse
        int rowCount = homePage.getRowCount();
        for(int j=rowCount-1; j>0; j--) {

            Guest newBookingFound = homePage.findBookingFor(guest, j);
            bookingMatches = newBookingFound.confirmBookingMatchesFor(guest);

            //if all the data matches, then check the new booking id is not in the original list
            if(bookingMatches && !originalBookingIds.contains(newBookingFound.getBookingId())){
                break;
            }
        }

        Assert.assertTrue("Unable to find new booking", bookingMatches == true);
    }

    @Test
    public void deleteBooking() throws ParseException {
        Guest guest = Guests.createValidGuest();
        Boolean bookingMatches = false;

        List<String> bookingIds = new LinkedList<String>();

        homePage.enterBookingFor(guest)
                .saveBooking();

        int rowCount = homePage.getRowCount();
        for(int i=rowCount-1; i>0; i--) {

            Guest newBookingFound = homePage.findBookingFor(guest, i);
            bookingMatches = newBookingFound.confirmBookingMatchesFor(guest);

            if (bookingMatches) {
                String bookingId = newBookingFound.getBookingId();
                guest.setBookingId(bookingId);

                homePage.deleteBooking(newBookingFound.getBookingId());
                bookingIds = homePage.getAllBookingIds();

                bookingMatches = false;
                break;
            }
        }

        List<WebElement> rows = homePage.getBookingRows();
        for(int j=rows.size()-1; j>0; j--) {
            String bookingId = guest.getBookingId();
            if(bookingIds.contains(bookingId)) {
                Guest newBookingGuest = homePage.findBookingFor(guest, j);
                Boolean isMatch = newBookingGuest.confirmBookingMatchesFor(guest);

                if(isMatch)
                {
                    bookingMatches = true;
                    break;
                }
            }
        }

        Assert.assertFalse("Booking was found when it should have been deleted", bookingMatches);
    }

    @After
    public void close()
    {
        closeBrowser();
    }
}
