package com.openclassrooms.realestatemanager;

import org.junit.Test;

import static org.junit.Assert.*;

import com.openclassrooms.realestatemanager.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilsUnitTest {

    @Test
    public void convert_dollarToEuro() {
        int dollars = 20;
        int result = Utils.convertDollarToEuro(dollars);
        assertEquals(Math.round(dollars * 0.93), result);
    }

    @Test
    public void convert_EuroToDollar() {
        int euros = 20;
        int result = Utils.convertEuroToDollar(euros);
        assertEquals(Math.round(euros / 0.93), result);
    }

    @Test
    public void modify_DateFormat() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String result = Utils.getTodayDate();
        assertEquals(dateFormat.format(new Date()), result);
    }
}