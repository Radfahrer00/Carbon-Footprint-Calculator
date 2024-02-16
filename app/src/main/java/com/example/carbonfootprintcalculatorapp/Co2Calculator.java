package com.example.carbonfootprintcalculatorapp;

/**
 * The Co2Calculator class provides a method to calculate the total CO2 emissions
 * based on the consumption of various products and activities.
 * It utilizes predefined CO2 emission factors for each category of product or activity
 * to compute the total CO2 footprint.
 */
public class Co2Calculator {

    // Constants for CO2 factors per unit of consumption
    private static final float BEEF_CO2_PER_UNIT = 16.88f;
    private static final float PORK_CO2_PER_UNIT = 6.92f;
    private static final float CHICKEN_CO2_PER_UNIT = 2.79f;
    private static final float FISH_CO2_PER_UNIT = 5.14f;
    private static final float BUTTER_CO2_PER_UNIT = 12.11f;
    private static final float DAIRY_PRODUCTS_CO2_PER_UNIT = 5.89f;
    private static final float CAR_CO2_PER_UNIT = 171f;
    private static final float PUBLIC_TRANSPORT_CO2_PER_UNIT = 67f;
    private static final float PLANE_CO2_PER_UNIT = 365f;
    private static final float APPLIANCES_CO2_PER_UNIT = 750f;


    /**
     * Calculates the total CO2 emissions based on the specified consumption values for various categories.
     *
     * @param values An array of integers where each element represents the consumption quantity
     *               for a specific category. The order is expected to be beef, pork, chicken, fish,
     *               butter, dairy products, car usage, public transport usage, plane usage, and appliance usage.
     * @return The total CO2 emissions as a float.
     * @throws IllegalArgumentException If the length of the input array is not equal to 10.
     */
    public float calculateTotalCo2(int[] values) {
        if (values.length != 10) {
            throw new IllegalArgumentException("Expected 10 input values for CO2 calculation.");
        }

        return values[0] * BEEF_CO2_PER_UNIT +
                values[1] * PORK_CO2_PER_UNIT +
                values[2] * CHICKEN_CO2_PER_UNIT +
                values[3] * FISH_CO2_PER_UNIT +
                values[4] * BUTTER_CO2_PER_UNIT +
                values[5] * DAIRY_PRODUCTS_CO2_PER_UNIT +
                values[6] * CAR_CO2_PER_UNIT +
                values[7] * PUBLIC_TRANSPORT_CO2_PER_UNIT +
                values[8] * PLANE_CO2_PER_UNIT +
                values[9] * APPLIANCES_CO2_PER_UNIT;
    }
}


