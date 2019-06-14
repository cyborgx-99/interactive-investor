package com.digital.equalexperts.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.digital.equalexperts.pages.BasePage.randBetween;

/**
 * Created by adnan on 14/06/2019.
 */
public class Guest {

    private String firstName;
    private String lastName;
    private double price;
    private boolean depositPaid;
    private Date checkInDate;
    private Date checkOutDate;
    private String bookingId;


    public Guest() {
        this.firstName = "ANewTestUser FirstName!@£$%^&*()_+}{|\":<>?"; //all valid chars
        this.lastName = "ANewTestUser LastName!@£$%^&*()_+}{|\":<>?";
        this.price = 99.99;
        this.depositPaid = true;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);

        this.checkInDate = c.getTime();

        c.add(Calendar.MONTH, 4);
        int rndm = randBetween(1,28);
        c.add(Calendar.DATE, rndm);

        this.checkOutDate = c.getTime();
        this.bookingId = "";
    }

    public Guest clearGuestValues(){
        this.firstName = "";
        this.lastName = "";
        this.price = 0;
        this.depositPaid = true;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date(Long.MIN_VALUE);

        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        this.checkInDate = c.getTime();
        this.checkOutDate = c.getTime();
        this.bookingId = "";

        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPrice() {
        return Double.toString(price);
    }

    public String getDepositPaid() {
        return Boolean.toString(depositPaid);
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setDepositPaid(Boolean depositPaid) {
        this.depositPaid = depositPaid;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public Boolean confirmBookingMatchesFor(Guest guest) {
        Boolean matchFound = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd");

        if (this.firstName.equals(guest.getFirstName())) {
            if (this.lastName.equals(guest.getLastName())) {
                if (this.getPrice().equals(guest.getPrice())) {
                    if (this.getDepositPaid().equals(guest.getDepositPaid())) {
                        if (sdf.format(this.getCheckInDate()).equals(sdf.format(guest.getCheckInDate()))) {
                            if (sdf.format(this.getCheckOutDate()).equals(sdf.format(guest.getCheckOutDate()))) {
                                matchFound = true;
                            }
                        }
                    }
                }
            }
        }

        return matchFound;
    }

    public String getBookingId() {
        return bookingId;
    }
}
