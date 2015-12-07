/****************************************************************************
Copyright 2003, Landmark Graphics and others.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 ****************************************************************************/
package edu.mines.jtk.util;

import java.io.Serializable;
import java.util.Comparator;

import static edu.mines.jtk.util.MathPlus.DBL_EPSILON;
import static edu.mines.jtk.util.MathPlus.FLT_EPSILON;

/**
 * This class allows safe comparisons of floating point numbers
 * with limited precision.
 * The Comparator interface should be used for instances of
 * java.lang.Number.
 *
 * @author W.S. Harlan
 */
public class Almost implements Serializable, Comparator<Number> {
    static final long serialVersionUID = 1;
    //private static final Logger LOG = Logger.getLogger(Almost.class.getName());

    /**
     * Default epsilon assumes math has destroyed one digit of accuracy.
     */
    private double _epsilon = 10.0 * FLT_EPSILON;

    /**
     * This default minValue may be too small.
     * If subtraction is involved, to same value as epsilon
     */
    private double _minValue = 100.0 * Float.MIN_VALUE;

    /**
     * This instance uses default float precision
     */
    public static final Almost FLOAT = new Almost();

    /**
     * This instance uses default double precision
     */
    public static final Almost DOUBLE = new Almost(true);

    /**
     * Accept default precision, appropriate for arithmetic on floats.
     */
    public Almost() {
    }

    /**
     * Specify precision to be used for operations.
     *
     * @param epsilon  The smallest positive number that can be added to 1
     *                 before the number is considered different from 1.
     *                 For double precision, use a multiple of MathPlus.DBL_EPSILON.
     *                 For float precision use a multiple of MathPlus.FLT_EPSILON.
     *                 I recommend multiplying these values by at least 10
     *                 to allow for errors introduced by arithmetic.
     * @param minValue The smallest positive value that should be
     *                 distinguished from zero. Use a multiple (say 100) of
     *                 Double.MIN_VALUE or Float.MIN_VALUE.
     */
    public Almost(final double epsilon, final double minValue) {
        _epsilon = Math.abs(epsilon);
        _minValue = Math.abs(minValue);
    }

    /**
     * Specify precision to be used for operations.
     * MinValue defaults to 100.*Float.MIN_VALUE;
     *
     * @param epsilon The smallest positive number that can be added to 1
     *                before the number is considered different from 1.
     *                For float precision use a multiple of 1.192092896e-07.
     *                For double precision, use a multiple of 2.2204460492503131e-016;
     *                I recommend multiplying these values by at least 10
     *                to allow for errors introduced by arithmetic.
     *                Should be much less than 1.
     */
    public Almost(final double epsilon) {
        if (epsilon > 0.1) {
            throw new IllegalArgumentException
                    ("Epsilon should be a small number: " + epsilon);
        }
        _epsilon = Math.abs(epsilon);
    }

    /**
     * Specify precision to be used for operations.
     * Epsilon will be 0.1 to this power.
     * MinValue defaults to 100.*Float.MIN_VALUE;
     *
     * @param significantDigits the number of significant digits, should be
     *                          positive.
     */
    public Almost(int significantDigits) {
        if (significantDigits < 1) {
            throw new IllegalArgumentException
                    ("The number of significant digits should be positive.");
        }
        significantDigits = Math.abs(significantDigits);
        _epsilon = 1.0;
        while (significantDigits > 0) {
            _epsilon *= 0.1;
            --significantDigits;
        }
    }

    /**
     * Constructor that allows either double or float precision.
     *
     * @param isDouble If true, the precision will be appropriate
     *                 for a double; if false, a float.
     */
    public Almost(final boolean isDouble) {
        if (isDouble) {
            _epsilon = 10.0 * DBL_EPSILON;
            _minValue = 100.0 * Double.MIN_VALUE;
        } // else already have correct defaults
    }

    /**
     * Get the smallest positive number that can be added to 1
     * before the number is considered different from 1.
     *
     * @return epsilon
     */
    public double getEpsilon() {
        return _epsilon;
    }

    /**
     * Get the smallest positive value that should be distinguished from
     * zero.
     *
     * @return minValue
     */
    public double getMinValue() {
        return _minValue;
    }

    /**
     * See if value is between two other values, including almost equality
     *
     * @param x  Value to check
     * @param x1 Value at one end of interval.
     * @param x2 Value at other end of interval.
     * @return true if x is between x1 and x2.
     */
    public boolean between(final double x, final double x1, final double x2) {
        return ((cmp(x, x1) * cmp(x, x2)) <= 0);
    }

    /**
     * See if value is outside two other values.
     *
     * @param x  Value to check
     * @param x1 Value at one end of interval.
     * @param x2 Value at other end of interval.
     * @return Return 0 if x is between x1 and x2,
     *         -1 if outside and closer to x1, 1 if outside and closer to x2.
     */
    public int outside(final double x, final double x1, final double x2) {
        int i = 0;
        if (between(x, x1, x2)) {
            i = 0;
        } else if (between(x1, x, x2)) {
            i = -1;
        } else if (between(x2, x, x1)) {
            i = 1;
        }
        return i;
    }

    /**
     * See if the number is almost zero
     *
     * @param r A number to check
     * @return true if the r is almost zero.
     */
    public boolean zero(double r) {
        if (r < 0.0) {
            r = -r;
        }
        return (r < _minValue);
    }

    /**
     * See if two numbers are almost equal.
     *
     * @param r1 First number to check
     * @param r2 Second number to check
     * @return True if r1 and r2 are almost equal
     */
    public boolean equal(final double r1, final double r2) {
        return (cmp(r1, r2) == 0);
    }

    /**
     * Check whether first number is strictly less than second.
     *
     * @param r1 First number to check
     * @param r2 Second number to check
     * @return true if r1&lt;r2
     */
    public boolean lt(final double r1, final double r2) {
        return cmp(r1, r2) < 0;
    }

    /**
     * Check whether first number is less than or equal to second.
     *
     * @param r1 First number to check
     * @param r2 Second number to check
     * @return true if r1&lt;=r2
     */
    public boolean le(final double r1, final double r2) {
        return cmp(r1, r2) <= 0;
    }

    /**
     * Check whether first number is strictly greater than second.
     *
     * @param r1 First number to check
     * @param r2 Second number to check
     * @return true if r1&gt;r2
     */
    public boolean gt(final double r1, final double r2) {
        return cmp(r1, r2) > 0;
    }

    /**
     * Check whether first number is greater than or equal to second.
     *
     * @param r1 First number to check
     * @param r2 Second number to check
     * @return true if r1&gt;=r2
     */
    public boolean ge(final double r1, final double r2) {
        return cmp(r1, r2) >= 0;
    }

    /**
     * Check order of two numbers, within precision.
     *
     * @param r1 First number to check
     * @param r2 Second number to check
     * @return 1 if r1 &gt; r2; -1 if r1 &lt; r2; 0 if r1==r2, within precision.
     */
    public int cmp(final double r1, final double r2) {
        if (r1 == r2) {
            return 0; // no need for more careful test
        }

        // if (zero (r1) && zero (r2)) return 0; // optimized by following
        double ar1 = r1;
        double ar2 = r2;
        if (ar1 < 0.0) {
            ar1 = -ar1;
        }
        if (ar2 < 0.0) {
            ar2 = -ar2;
        }
        if (ar1 < _minValue && ar2 < _minValue) {
            return 0;
        }

        if (0 * r1 * r2 != 0) {
            throw new IllegalArgumentException("Comparing a NaN");
        }

        double er1 = _epsilon * ar1;
        if (_minValue > er1) {
            er1 = _minValue;
        }
        double er2 = _epsilon * ar2;
        if (_minValue > er2) {
            er2 = _minValue;
        }
        if (r1 - er1 > r2 + er2) {
            return 1;
        } else if (r1 + er1 < r2 - er2) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * Return a hashcode consistent with imprecise floating-point equality.
     * Integers and Longs require exact equality.
     *
     * @param number            Number to get hashcode of
     * @param significantDigits Number of significant digits to honor in hashCode
     * @return new hash code.
     */
    public int hashCodeOf(final Number number, final int significantDigits) {
        // Integers must be exactly equal
        if (number instanceof Long || number instanceof Integer) {
            return number.intValue();
        }
        double value = number.doubleValue();
        // zero is special
        if (zero(value)) {
            return 0;
        }
        // make value positive definite, and remember sign
        final boolean flipSign = value < 0.0;
        if (flipSign) {
            value = -value;
        }
        int pow = 0;
        // Put value in range [0.1,1.0), and remember base 10 exponent
        while (value < 0.1) {
            value *= 10.0;
            ++pow;
        }
        while (value > 1.0) {
            value *= 0.1;
            --pow;
        }
        // If mantissa is 1, it is too easy to get wrong exponent.
        if (equal(value, 1.0) || equal(value, 0.1)) {
            return 1;
        }
        // Now round to an integer with the correct number of significant digits
        for (int i = 0; i < Math.abs(significantDigits); ++i) {
            value *= 10.0;
        }
        int result = (Long.valueOf((long) (value + 0.5))).intValue();
        // Combine singificant info into a single hash code
        result = 100 * result + pow;
        if (flipSign) {
            result = -result;
        }
        return result;
    }

    /**
     * Return a hashcode consistent with imprecise floating-point equality.
     * Integers and Longs require exact equality.
     * The precision is consistent with the value of epsilon.
     *
     * @param number number to get hashcode from
     * @return hashcode
     */
    public int hashCodeOf(final Number number) {
        double epsilon = _epsilon;
        int digits = 0;
        while (epsilon < 0.99) {
            epsilon *= 10.0;
            ++digits;
        }
        return hashCodeOf(number, digits);
    }

    /**
     * Safely divide one number by another.  Handle zeros and infinity.
     * Never throws floating point exceptions.
     *
     * @param top        Numerator
     * @param bottom     Denominator
     * @param limitIsOne If true, 0/0 returns 1.  If false, 0/0 returns 0.
     * @return Regular value if arguments are okay.
     *         Return +/- 0.01*Float.MAX_VALUE, rather than infinity.
     */
    public double divide(final double top, final double bottom, final boolean limitIsOne) {
        return divide(top, bottom, (limitIsOne) ? 1.0 : 0.0);
    }

    /**
     * Safely take the reciprocal of a number.
     *
     * @param value Value to take reciprocal of.
     * @return reciprocal of value
     */
    public double reciprocal(final double value) {
        return divide(1.0, value, 0.0);
    }

    /**
     * Safely divide one number by another.  Handle zeros and infinity.
     * Never throws floating point exceptions.
     *
     * @param top    Numerator
     * @param bottom Denominator
     * @param limit  This is the returned value for 0/0.
     * @return Regular value if arguments are okay.
     *         Return +/- 0.01*Float.MAX_VALUE, rather than infinity.
     */
    public double divide(double top, double bottom, final double limit) {
        // remember sign of result
        double sign = 1.0;
        if ((top > 0.0 && bottom < 0.0) || (top < 0.0 && bottom > 0.0)) {
            sign = -1.0;
        }
        // remove signs to simplify if's
        if (top < 0) {
            top = -top;
        }
        if (bottom < 0) {
            bottom = -bottom;
        }

        if (bottom >= 1.0                      // Might underflow, but don't care.
                || top < bottom * 0.1 * Float.MAX_VALUE) { // bottom<1 won't overflow
            return sign * top / bottom;                 // safe
        } else if (equal(top, bottom)) {
            if (zero(top)) {                           // define 0/0 = limit
                return limit;
            }
            return sign;                  // ratio is within precision of unity
        } else {                // clip overflow
            return sign * 0.01 * Float.MAX_VALUE;
        }
    }

    // Comparator
    @Override
    public int compare(final Number n1, final Number n2) {
        return cmp(n1.doubleValue(), n2.doubleValue());
    }

    // Object
    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        } else if (object == null || getClass() != object.getClass() || hashCode() != object.hashCode()) {
            return false;
        }
        final Almost other = (Almost) object;
        return _epsilon == other._epsilon && _minValue == other._minValue;
    }

    // Object
    @Override
    public int hashCode() {
        return Long.valueOf(Double.doubleToLongBits(_epsilon) ^
                Double.doubleToLongBits(_minValue)).intValue();
    }

    // Object
    @Override
    public String toString() {
        return "Almost<eps=" + _epsilon + ",min=" + _minValue + ">";
    }
}

