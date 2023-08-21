package com.openclassrooms.realestatemanager;

import org.junit.Test;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilsUnitTest {

    @Test
    public void convert_dollarToEuro() throws Exception {
        int dollars = 20;
        int result = Utils.convertDollarToEuro(dollars);
        assertEquals(Math.round(dollars * 0.92), result);
    }

    @Test
    public void convert_EuroToDollar() throws Exception {
        int euros = 20;
        int result = Utils.convertEuroToDollar(euros);
        assertEquals(Math.round(euros / 0.92), result);
    }

    @Test
    public void modify_DateFormat() throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String result = Utils.getTodayDate();
        assertEquals(dateFormat.format(new Date()), result);
    }
}