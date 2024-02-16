package com.example.carbonfootprintcalculatorapp;

import static org.junit.Assert.*;

import org.junit.Test;

public class Co2CalculatorTest {

    @Test
    public void testCalculateTotalCo2() {
        Co2Calculator calculator = new Co2Calculator();
        // Assuming the order is beef, pork, chicken, fish, butter, dairy, car, public transport, plane, appliances
        int[] consumptionValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}; // Sample consumption values
        float expected = 1*16.88f + 2*6.92f + 3*2.79f + 4*5.14f + 5*12.11f + 6*5.89f + 7*171f + 8*67f + 9*365f + 10*750f;
        assertEquals("Total CO2 calculation should match expected value", expected, calculator.calculateTotalCo2(consumptionValues), 0.01);
    }

    @Test
    public void testCalculateTotalCo2WithZeroes() {
        Co2Calculator calculator = new Co2Calculator();
        int[] consumptionValues = {1, 0, 3, 0, 5, 0, 7, 0, 0, 0}; // Sample consumption values
        float expected = 1*16.88f + 0*6.92f + 3*2.79f + 0*5.14f + 5*12.11f + 0*5.89f + 7*171f + 0*67f + 0*365f + 0*750f;
        assertEquals("Total CO2 calculation should match expected value", expected, calculator.calculateTotalCo2(consumptionValues), 0.01);
    }

}