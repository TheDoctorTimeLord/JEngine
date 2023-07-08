package ru.jengine.utils.serviceclasses;

import ru.jengine.beancontainer.exceptions.UtilsException;

import com.google.common.base.Objects;

public class Fraction {
    private final int numerator;
    private final int denominator;

    public Fraction(int numerator) {
        this(numerator, 1);
    }

    public Fraction(int numerator, int denominator) {
        checkDenominator(denominator);
        int gcd = getGreatestCommonDivisor(numerator, denominator);

        this.numerator = numerator / gcd;
        this.denominator = denominator / gcd;
    }

    public int getDenominator() {
        return denominator;
    }

    public int getNumerator() {
        return numerator;
    }

    public Fraction max(Fraction other) {
        return greaterThen(other) ? this : other;
    }

    public Fraction multiple(int multiplier) {
        return new Fraction(getNumerator() * multiplier, getDenominator());
    }

    public boolean greaterThen(int then) {
        return numerator > then * denominator;
    }

    public boolean greaterThen(Fraction then) {
        int gcd = getGreatestCommonDivisor(getDenominator(), then.getDenominator());
        int firstNumerator = getNumerator() * (then.getDenominator() / gcd);
        int secondNumerator = then.getNumerator() * (getDenominator() / gcd);
        return firstNumerator > secondNumerator;
    }

    public boolean lessThen(int then) {
        return numerator < then * denominator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Fraction fraction = (Fraction)o;
        return numerator == fraction.numerator && denominator == fraction.denominator;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(numerator, denominator);
    }

    @Override
    public String toString() {
        return numerator + " / " + denominator;
    }

    public static int getGreatestCommonDivisor(int numerator, int denominator) {
        checkDenominator(denominator);

        numerator = Math.abs(numerator);

        while (numerator != 0 && denominator != 0) {
            if (numerator > denominator) {
                numerator %= denominator;
            } else {
                denominator %= numerator;
            }
        }

        return numerator + denominator;
    }

    private static void checkDenominator(int denominator) {
        if (denominator <= 0) {
            throw new UtilsException("Denominator must be greater then 0");
        }
    }
}
