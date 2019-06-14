package com.digital.equalexperts.pages;

import com.digital.equalexperts.helpers.Guest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.digital.ConfigProperty.TIMEOUT_DOM;

public class HomePage extends BasePage{

    @FindBy(css = "div.jumbotron h1")
    private WebElement formTitle;

    @FindBy(css = baseFormElement  + "#firstname")
    private WebElement firstName;

    @FindBy(css = baseFormElement  + "#lastname")
    private WebElement lastName;

    @FindBy(css = baseFormElement  + "#totalprice")
    private WebElement totalPrice;

    @FindBy(css = baseFormElement  + "#depositpaid")
    private WebElement depositPaid;

    @FindBy(css = baseFormElement  + "#checkin")
    private WebElement checkInDate;

    @FindBy(css = baseFormElement  + "#checkout")
    private WebElement checkOutDate;

    //unnecessary white space before and after the value
    @FindBy(css = baseFormElement  + "input[value=' Save ']")
    private WebElement save;

    @FindBy(css = "div.ui-datepicker-title")
    private WebElement calendarUI;

    @FindBy(css = ".ui-datepicker-month")
    private WebElement calendarMonth;

    @FindBy(css = ".ui-datepicker-year")
    private WebElement calendarYear;

    @FindBy(css = "table.ui-datepicker-calendar tbody tr")
    private List<WebElement> calendarRows;

    // this is a little weak : if the id changes and doesn't begin with 1, then the test will fail
    private By bookingTableRows = By.cssSelector(bookingTable  + " div[id^='1']");

    private static final String bookingTable = "div#bookings";

    private static final String baseFormElement = "div#form .row ";

    public HomePage(WebDriver webDriver) {
        super(webDriver);
    }

    public boolean isAt(){
        waitForElementToBeVisible(firstName);
        return formTitle.getText().equals("Hotel booking form");
    }

    public HomePage enterBookingFor(Guest guest) throws ParseException {
        firstName.sendKeys(guest.getFirstName());
        lastName.sendKeys(guest.getLastName());
        totalPrice.sendKeys(guest.getPrice());
        Select select = new Select(depositPaid);
        select.selectByVisibleText(guest.getDepositPaid());
        selectCheckInDate(guest.getCheckInDate());
        selectCheckOutDate(guest.getCheckOutDate());
        return this;
    }

    private void selectCheckOutDate(Date checkOut) throws ParseException {
        checkOutDate.click();
        waitForElementToBeVisible(calendarUI);

        selectDateInCalendar(checkOut);
    }

    private void selectDateInCalendar(Date dt) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMMM yyyy");

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();


        cal1.setTime(dt);
        int dayOfMonth = cal1.get(Calendar.DAY_OF_MONTH);
        String dayOfMonthStr = String.valueOf(dayOfMonth);

        String monthYear = calendarMonth.getText()  + " " + calendarYear.getText();
        Date date = dateFormat.parse(dayOfMonthStr + " " + monthYear);
        cal2.setTime(date);

        int totalMonths1 = cal1.get(Calendar.YEAR) * 12 + cal1.get(Calendar.MONTH);
        int totalMonths2 = cal2.get(Calendar.YEAR) * 12 + cal2.get(Calendar.MONTH);

        int monthDifference = totalMonths2 - totalMonths1;

        if(monthDifference != 0) {
            if(monthDifference > 0){
                for(int i=1; i <= monthDifference; i++)
                {
                    By by = By.cssSelector("a.ui-datepicker-prev");
                    WebElement prev = webDriver.findElement(by);
                    prev.click();
                    waitForElementTextToNotEqual(by, monthYear);
                    monthYear = calendarMonth.getText()  + " " + calendarYear.getText();
                }
            }
            else{
                for(int i=monthDifference; i<0; i++)
                {
                    By by = By.cssSelector("a.ui-datepicker-next");
                    WebElement prev = webDriver.findElement(by);
                    prev.click();
                    waitForElementTextToNotEqual(by, monthYear);
                    monthYear = calendarMonth.getText()  + " " + calendarYear.getText();
                }
            }
        }

        for (WebElement row:
             calendarRows) {
            boolean dateFound = false;

            List<WebElement> calendarDays = row.findElements(By.cssSelector("td a"));
            for (WebElement day :
                    calendarDays) {
                if (day.getText().equals(Integer.toString(cal2.get(Calendar.DATE)))){
                    day.click();
                    dateFound = true;
                    break;
                }
            }

            if(dateFound) {
                break;
            }
        }
    }

    private void selectCheckInDate(Date checkIn) throws ParseException {
        checkInDate.click();
        waitForElementToBeVisible(calendarUI);

        selectDateInCalendar(checkIn);
    }

    public void saveBooking() {
        int originalRowCount = getRowCount();
        save.click();
        WebDriverWait wait = new WebDriverWait(webDriver, TIMEOUT_DOM.getValueAsInt());
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(bookingTableRows, originalRowCount));
    }

    public int getRowCount() {
        return webDriver.findElements(bookingTableRows).size();
    }

    public List<WebElement> getBookingRows()
    {
        return webDriver.findElements(bookingTableRows);
    }

    public Guest findBookingFor(Guest guest, int rowNumber) throws ParseException {
        Guest newGuestBooking = new Guest();
        newGuestBooking.clearGuestValues();

        List<WebElement> bookingRows = getBookingRows();

        WebElement row = bookingRows.get(rowNumber);
        List<WebElement> fields = row.findElements(By.cssSelector("div p"));

        newGuestBooking.setFirstName(fields.get(0).getText());
        newGuestBooking.setLastName(fields.get(1).getText());
        newGuestBooking.setPrice(Double.valueOf(fields.get(2).getText()));
        newGuestBooking.setDepositPaid(Boolean.valueOf(fields.get(3).getText()));

        Date date1 = new SimpleDateFormat("yyy-MM-dd").parse(fields.get(4).getText());
        Date date2 =new SimpleDateFormat("yyy-MM-dd").parse(fields.get(5).getText());

        newGuestBooking.setCheckInDate(date1);
        newGuestBooking.setCheckOutDate(date2);
        newGuestBooking.setBookingId(row.getAttribute("id"));

        return newGuestBooking;
    }

    public void deleteBooking(String bookingId) {
        int originalRowCount = getRowCount();
        webDriver.findElement(By.cssSelector("input[onclick=\'deleteBooking(" + bookingId + ")\'")).click();
        WebDriverWait wait = new WebDriverWait(webDriver, TIMEOUT_DOM.getValueAsInt());
        wait.until(ExpectedConditions.numberOfElementsToBeLessThan(bookingTableRows, originalRowCount));
    }

    public List<String> getAllBookingIds() {
        List<String> bookingIds = new ArrayList<String>();
        List<WebElement> bookingRows = getBookingRows();
        for (WebElement row :
                bookingRows) {
            bookingIds.add(getBookingIdForRow(row));
        }

        return bookingIds;
    }

    public String getBookingIdForRow(WebElement row)
    {
        return row.getAttribute("id");
    }
}
