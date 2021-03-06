/* 
 * Copyright (c) 2015, Netsyms Technologies
 * All rights reserved.
 * 
 * 
 * CODE LICENSE ==========
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this 
 * list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 * this list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 * 
 * 3. Neither the name of the copyright holder nor the names of its contributors 
 * may be used to endorse or promote products derived from this software without 
 * specific prior written permission.
 * 
 * 4. You adhere to the Media License detailed below.  If you do not, this license
 * is automatically revoked and you must purge all copies of the software you
 * possess, in source or binary form.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * 
 * MEDIA LICENSE ==========
 * All images and other graphical files ("media") included with this
 * software are copyright (c) 2015 Netsyms Technologies.  You may not distribute
 * the graphics or any program, source code repository, or other digital storage 
 * media containing them without permission from Netsyms Technologies.
 * This ban on distribution only applies to publicly available systems.
 * A password-protected network file share, USB drive, or other storage scheme that
 * cannot be easily accessed by the public is generally allowed.  If in doubt,
 * contact Netsyms Technologies.  If Netsyms Technologies allows or denies
 * you permission, that decision is considered final and binding.
 * 
 * You may only use the media for personal, 
 * non-commercial, non-educational use unless: 
 * 1, You have paid for the software and media via the SyMAT website,
 * or 2, you are using it as part of the 15-day trial period.  
 * Other uses are prohibited without permission.
 * If any part of this license is deemed unenforcable, the remainder 
 * of the license remains in full effect.
 */
package net.apocalypselabs.symat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import static java.lang.Math.*;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import static net.apocalypselabs.symat.Main.API_URL;
import net.apocalypselabs.symat.components.TextBox;
import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.math.MathException;
import org.mozilla.javascript.NativeArray;
import org.python.core.PyList;

/**
 * These functions are accessible from JavaScript.
 *
 * There are a lot of aliases in here too.
 *
 * @author Skylar
 */
public class Functions {

    /**
     *
     */
    public static final HashMap<String, Object> GLOBALS = new HashMap<>();

    private final EvalUtilities util = new EvalUtilities(true, true);
    Graph graphwin = new Graph(true);

    private String lang = "py";

    private final SecureRandom rng = new SecureRandom();

    /*
     Useful interactions
     */
    /**
     * Display a message dialog box.
     *
     * @param message The message to give.
     */
    public void notify(Object message) {
        JOptionPane.showInternalMessageDialog(Main.mainPane, message.toString());
    }

    /**
     * Display message dialog.
     *
     * This is an alias to help JavaScript programmers.
     *
     * @param message The message
     */
    public void alert(Object message) {
        notify(message);
    }

    /**
     * Display an input dialog box with a text field.
     *
     * @param question Text to label dialog
     * @return The inputted text
     */
    public String ask(String question) {
        return JOptionPane.showInternalInputDialog(Main.mainPane, question);
    }

    /**
     * Pause execution for the specified number of milliseconds.
     *
     * @param millis
     */
    public void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // Nothing to do.
        }
    }

    /**
     * @see pause(long millis)
     * @param millis
     */
    public void sleep(long millis) {
        pause(millis);
    }

    /**
     * Quit SyMAT.
     */
    public void quitApplication() {
        new ExitControl().windowClosing(null);
    }

    /*
     Math commands
     */
    /**
     * Find the factorial (n!) of a number.
     *
     * @param n positive integer
     * @return n! as String
     * @throws net.apocalypselabs.symat.BadInputException
     */
    public String factorial(long n) throws BadInputException {
        if (n <= 0) {
            throw new BadInputException("Input must be greater than zero!");
        }

        BigInteger inc = new BigInteger("1");
        BigInteger fact = new BigInteger("1");

        for (long c = 1; c <= n; c++) {
            fact = fact.multiply(inc);
            inc = inc.add(BigInteger.ONE);
        }
        return fact.toString();
    }

    /**
     * Take (a^b) mod m. If b is -1, finds the modular inverse of a mod m.
     *
     * @param a Number
     * @param b Exponent
     * @param m Modulus
     * @return pow(a,b)%m.
     */
    public double powermod(double a, double b, double m) {
        if (b == -1) {
            BigInteger in = new BigInteger(String.valueOf((int) a));
            return in.modInverse(new BigInteger(String.valueOf((int) m))).doubleValue();
        }
        return (pow(a, b) % m + m) % m;
    }

    /**
     * Take (a^b) mod m. If b is -1, finds the modular inverse of a mod m.
     *
     * @param a Number
     * @param b Exponent
     * @param m Modulus
     * @return pow(a,b)%m.
     */
    public String powermod(String a, String b, String m) {
        BigInteger in = new BigInteger(a);
        if (b.equals("-1")) {
            return in.modInverse(new BigInteger(m)).toString();
        }
        return in.modPow(new BigInteger(b), new BigInteger(m)).toString();
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public long gcd(long a, long b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    public String gcd(String a, String b) {
        return (new BigInteger(a).gcd(new BigInteger(b)).toString());
    }

    /**
     * Differentiate the function with respect to idv.
     *
     * @param function Function
     * @param idv independent variable
     * @return Differentiated function
     */
    public String diff(String function, String idv) {
        return util.evaluate("diff(" + function + "," + idv + ")").toString();
    }

    /**
     * Differentiate the function with respect to x.
     *
     * @param function Function
     * @return Differentiated function
     */
    public String diff(String function) {
        // Assume "x" as var
        return diff(function, "x");
    }

    /**
     *
     * @param function
     * @param idv
     * @return
     * @deprecated
     */
    @Deprecated
    public String D(String function, String idv) {
        return diff(function, idv);
    }

    /**
     *
     * @param function
     * @param idv
     * @param eq
     * @return
     */
    public double[] solve(String function, String idv, int eq) {
        String res = $("Solve[" + function + "==" + eq + ", " + idv + "]");
        res = res.substring(1, res.length() - 1);
        String[] cmp = res.split(",");
        for (int i = 0; i < cmp.length; i++) {
            cmp[i] = cmp[i].replace("{" + idv + "->", "");
            cmp[i] = cmp[i].replace("}", "");
        }
        double[] out = new double[cmp.length];
        for (int i = 0; i < cmp.length; i++) {
            try {
                if (cmp[i].contains("I")) {
                    if (cmp[i].contains("-I")) {
                        out[i] = Double.NEGATIVE_INFINITY;
                    } else {
                        out[i] = Double.POSITIVE_INFINITY;
                    }
                } else {
                    out[i] = Double.parseDouble(cmp[i]);
                }
            } catch (Exception ex) {
                return new double[]{Double.NaN};
            }
        }
        return out;
    }

    /**
     *
     * @param function
     * @param idv
     * @return
     */
    public double[] solve(String function, String idv) {
        return solve(function, idv, 0);
    }

    /**
     *
     * @param function
     * @return
     */
    public double[] solve(String function) {
        return solve(function, "x");
    }

    private String printa(double[] o) {
        String out = "[";
        for (int i = 0; i < o.length; i++) {
            out += o[i] + (i == o.length - 1 ? "" : ", ");
        }
        out += "]";
        return out;
    }

    private String printa(int[] o) {
        String out = "[";
        for (int i = 0; i < o.length; i++) {
            out += o[i] + (i == o.length - 1 ? "" : ", ");
        }
        out += "]";
        return out;
    }

    private String printa(boolean[] o) {
        String out = "[";
        for (int i = 0; i < o.length; i++) {
            out += (o[i] ? "true" : "false") + (i == o.length - 1 ? "" : ", ");
        }
        out += "]";
        return out;
    }

    /**
     *
     * @param o
     * @return
     */
    public String printa(Object o) {
        String out = "[";
        if (o instanceof int[]) {
            int[] arr = (int[]) o;
            for (int i = 0; i < arr.length; i++) {
                out += arr[i] + (i == arr.length - 1 ? "" : ", ");
            }
        } else if (o instanceof double[]) {
            double[] arr = (double[]) o;
            for (int i = 0; i < arr.length; i++) {
                out += arr[i] + (i == arr.length - 1 ? "" : ", ");
            }
        } else if (o instanceof boolean[]) {
            double[] arr = (double[]) o;
            for (int i = 0; i < arr.length; i++) {
                out += arr[i] + (i == arr.length - 1 ? "" : ", ");
            }
        } else if (o instanceof int[][]) {
            int[][] arr = (int[][]) o;
            for (int i = 0; i < arr.length; i++) {
                out += printa(arr[i]) + (i == arr.length - 1 ? "" : ", ");
            }
        } else if (o instanceof double[][]) {
            double[][] arr = (double[][]) o;
            for (int i = 0; i < arr.length; i++) {
                out += printa(arr[i]) + (i == arr.length - 1 ? "" : ", ");
            }
        } else if (o instanceof boolean[][]) {
            boolean[][] arr = (boolean[][]) o;
            for (int i = 0; i < arr.length; i++) {
                out += printa(arr[i]) + (i == arr.length - 1 ? "" : ", ");
            }
        } else if (o instanceof NativeArray) {
            NativeArray arr = (NativeArray) o;
            for (long i = 0; i < arr.getLength(); i++) {

                out += (arr.get(i) instanceof NativeArray ? printa(arr.get(i))
                        : arr.get(i).toString())
                        + (i == arr.getLength() - 1 ? "" : ", ");
            }
        } else if (o instanceof PyList) {
            PyList arr = (PyList) o;
            Object[] oo = arr.toArray();
            for (int i = 0; i < oo.length; i++) {
                out += (oo[i] instanceof Object[] ? printa(oo[i]) : oo[i].toString())
                        + (i == oo.length - 1 ? "" : ", ");
            }
        } else {
            Object[] arr = (Object[]) o;
            for (int i = 0; i < arr.length; i++) {
                out += (arr[i] instanceof Object[] ? printa(arr[i]) : arr[i].toString())
                        + (i == arr.length - 1 ? "" : ", ");
            }
        }
        out += "]";
        return out;
    }

    /**
     * Integrate the function with respect to idv.
     *
     * @param function Function
     * @param idv independent variable
     * @return Integrated function
     */
    public String integrate(String function, String idv) {
        return util.evaluate("integrate(" + function + "," + idv + ")").toString();
    }

    /**
     * Integrate the function with respect to x.
     *
     * @param function Function
     * @return Integrated function
     */
    public String integrate(String function) {
        return integrate(function, "x");
    }

    /**
     *
     * @param function
     * @return
     */
    public String factor(String function) {
        return sym("Factor(" + function + ")");
    }

    /**
     * Simplify the given expression.
     *
     * @param expr expression
     * @return simplified expression
     */
    public String simplify(String expr) {
        return sym("Simplify(" + expr + ")");
    }

    /**
     *
     * @param expr
     * @return
     */
    public Object vpa(String expr) {
        IExpr ans = util.evaluate("N(" + expr + ")");
        if (ans.isNumber()) {
            return Double.parseDouble(ans.toString());
        }
        return ans.toString();
    }

    /**
     *
     * @param in
     * @return
     */
    public int[] primes(int in) {
        boolean[] prime = new boolean[in];
        for (int i = 0; i < prime.length; i++) {
            prime[i] = true;
        }
        for (int i = 2; i < sqrt(in); i++) {
            if (prime[i]) {
                int x = 0;
                for (int j = i * i; j < in; j = i * i + x * i) {
                    prime[j] = false;
                    x++;
                }
            }
        }
        List<Integer> arr = new ArrayList<>();
        for (int i = 2; i < prime.length; i++) {
            if (prime[i]) {
                arr.add(i);
            }
        }
        int[] out = new int[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            out[i] = arr.get(i);
        }
        return out;
    }

    /**
     *
     * @param d
     * @return
     */
    public double sec(double d) {
        return 1.0 / Math.cos(d);
    }

    /**
     *
     * @param d
     * @return
     */
    public double csc(double d) {
        return 1.0 / Math.sin(d);
    }

    /**
     *
     * @param d
     * @return
     */
    public double cot(double d) {
        return 1.0 / Math.tan(d);
    }

    /**
     *
     * @param d
     * @return
     */
    public double asec(double d) {
        return Math.acos(1.0 / d);
    }

    /**
     *
     * @param d
     * @return
     */
    public double acsc(double d) {
        return Math.asin(1.0 / d);
    }

    /**
     *
     * @param d
     * @return
     */
    public double acot(double d) {
        return Math.atan(1.0 / d);
    }

    /**
     *
     * @param d
     * @return
     */
    public double sinh(double d) {
        return Math.sinh(d);
    }

    /**
     *
     * @param d
     * @return
     */
    public double cosh(double d) {
        return Math.cosh(d);
    }

    /**
     *
     * @param d
     * @return
     */
    public double tanh(double d) {
        return Math.tanh(d);
    }

    /**
     *
     * @param d
     * @return
     */
    public double sech(double d) {
        return pow(Math.cosh(d), -1);
    }

    /**
     *
     * @param d
     * @return
     */
    public double csch(double d) {
        return pow(Math.sinh(d), -1);
    }

    /**
     *
     * @param d
     * @return
     */
    public double coth(double d) {
        return Math.cosh(d) / Math.sinh(d);
    }

    /**
     * Multiplies the given numbers together.
     *
     * @param a numbers. Calculates first * second * third, etc.
     * @return The product of the numbers or the value of input if there is only
     * one input.
     */
    public double times(double... a) {
        double ans = 0;
        for (int i = 0; i < a.length; i++) {
            if (i == 0) {
                ans = a[i];
            } else {
                ans *= a[i];
            }
        }
        return ans;
    }

    /**
     * Multiplies the given numbers together.
     *
     * @param a
     * @param b
     * @return The product of the numbers or the value of input if there is only
     * one input.
     */
    public String times(String a, String b) {
        return (new BigInteger(a).multiply(new BigInteger(b))).toString();
    }

    /**
     * Divide the given numbers.
     *
     * @param a numbers. Calculates (first / second) / third, etc.
     * @return The quotient of the numbers or the value of input if there is
     * only one input.
     */
    public double divide(double... a) {
        double ans = 0;
        for (int i = 0; i < a.length; i++) {
            if (i == 0) {
                ans = a[i];
            } else {
                ans /= a[i];
            }
        }
        return ans;
    }

    /**
     * Divide the given numbers.
     *
     * @param a
     * @param b
     * @return The quotient of the numbers or the value of input if there is
     * only one input.
     */
    public String divide(String a, String b) {
        return (new BigInteger(a).divide(new BigInteger(b))).toString();
    }

    /**
     * Divide the first number by the second and return the remainder.
     *
     * @param a numbers. Calculates (first mod second) mod third, etc.
     * @return The modulus of the numbers or the value of input if there is only
     * one input.
     */
    public double mod(double... a) {
        double ans = 0;
        for (int i = 0; i < a.length; i++) {
            if (i == 0) {
                ans = a[i];
            } else {
                ans %= a[i];
            }
        }
        return ans;
    }

    /**
     * Divide the first number by the second and return the remainder.
     *
     * @param a
     * @param b
     * @return The modulus of the numbers or the value of input if there is only
     * one input.
     */
    public String mod(String a, String b) {
        return (new BigInteger(a).mod(new BigInteger(b))).toString();
    }

    public double[] intArrayToDouble(int[] a) {
        double out[] = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            out[i] = a[i];
        }
        return out;
    }
    
    public int[] doubleArrayToInt(double[] a) {
        int out[] = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            out[i] = Math.round((float) a[i]);
        }
        return out;
    }

    
    /**
     * Add the given numbers together.
     *
     * @param a numbers. Calculates first + second + third, etc.
     * @return The sum of the numbers or the value of input if there is only one
     * input.
     */
    public double add(double... a) {
        double ans = 0;
        for (double d : a) {
            ans += d;
        }
        return ans;
    }

    /**
     * Add the given numbers together.
     *
     * @param a numbers. Calculates first + second + third, etc.
     * @param b
     * @return The sum of the numbers or the value of input if there is only one
     * input.
     */
    public String add(String a, String b) {
        return (new BigInteger(a).subtract(new BigInteger(b))).toString();
    }

    /**
     * Subtract the given numbers.
     *
     * @param a numbers. Calculates (first - second) - third, etc.
     * @return The difference of the numbers or the value of input if there is
     * only one input.
     */
    public double subtract(double... a) {
        double ans = 0;
        for (int i = 0; i < a.length; i++) {
            if (i == 0) {
                ans = a[i];
            } else {
                ans -= a[i];
            }
        }
        return ans;
    }

    /**
     * Subtract the given numbers.
     *
     * @param a numbers. Calculates (first - second) - third, etc.
     * @param b
     * @return The difference of the numbers or the value of input if there is
     * only one input.
     */
    public String subtract(String a, String b) {
        BigInteger ans = new BigInteger(a);
        ans = ans.subtract(new BigInteger(b));
        return ans.toString();
    }

    /**
     *
     * @param a
     * @return
     */
    public double[][] minvert(double a[][]) {
        int n = a.length;
        double x[][] = new double[n][n];
        double b[][] = new double[n][n];
        int index[] = new int[n];
        for (int i = 0; i < n; ++i) {
            b[i][i] = 1;
        }
        // Transform the matrix into an upper triangle
        gaussian(a, index);
        // Update the matrix b[i][j] with the ratios stored
        for (int i = 0; i < n - 1; ++i) {
            for (int j = i + 1; j < n; ++j) {
                for (int k = 0; k < n; ++k) {
                    b[index[j]][k]
                            -= a[index[j]][i] * b[index[i]][k];
                }
            }
        }
        // Perform backward substitutions
        for (int i = 0; i < n; ++i) {
            x[n - 1][i] = b[index[n - 1]][i] / a[index[n - 1]][n - 1];
            for (int j = n - 2; j >= 0; --j) {
                x[j][i] = b[index[j]][i];
                for (int k = j + 1; k < n; ++k) {
                    x[j][i] -= a[index[j]][k] * x[k][i];
                }
                x[j][i] /= a[index[j]][j];
            }
        }
        return x;
    }

    // Method to carry out the partial-pivoting Gaussian
    // elimination.  Here index[] stores pivoting order.
    private void gaussian(double a[][], int index[]) {
        int n = index.length;
        double c[] = new double[n];
        // Initialize the index
        for (int i = 0; i < n; ++i) {
            index[i] = i;
        }
        // Find the rescaling factors, one from each row
        for (int i = 0; i < n; ++i) {
            double c1 = 0;
            for (int j = 0; j < n; ++j) {
                double c0 = Math.abs(a[i][j]);
                if (c0 > c1) {
                    c1 = c0;
                }
            }
            c[i] = c1;
        }

        // Search the pivoting element from each column
        int k = 0;
        for (int j = 0; j < n - 1; ++j) {
            double pi1 = 0;
            for (int i = j; i < n; ++i) {
                double pi0 = Math.abs(a[index[i]][j]);
                pi0 /= c[index[i]];
                if (pi0 > pi1) {
                    pi1 = pi0;
                    k = i;
                }
            }

            // Interchange rows according to the pivoting order
            int itmp = index[j];
            index[j] = index[k];
            index[k] = itmp;
            for (int i = j + 1; i < n; ++i) {
                double pj = a[index[i]][j] / a[index[j]][j];
                // Record pivoting ratios below the diagonal
                a[index[i]][j] = pj;
                // Modify other elements accordingly
                for (int l = j + 1; l < n; ++l) {
                    a[index[i]][l] -= pj * a[index[j]][l];
                }
            }
        }
    }

    /**
     * Get all prime factors of input number.
     *
     * @param n An integer number.
     * @return Array of primes.
     *
     * Thanks to
     * http://www.javacodegeeks.com/2014/05/how-to-find-prime-factors-of-integer-numbers-in-java-factorization.html
     * and http://stackoverflow.com/a/2451219/2534036
     */
    public long[] factor(long n) {
        long i;
        Set primes = new HashSet<>();
        long copyOfInput = n;

        for (i = 2; i <= copyOfInput; i++) {
            if (copyOfInput % i == 0) {
                primes.add(i); // prime factor
                copyOfInput /= i;
                i--;
            }
        }
        long[] a = new long[primes.size()];
        int j = 0;
        for (Object val : primes) {
            a[j++] = (long) val;
        }
        return a;
    }

    /**
     *
     * @param n
     * @return
     */
    public boolean isprime(long n) {
        int i = 2;
        while (i <= sqrt(n)) {
            if (n % i == 0) {
                return false;
            }
            i++;
        }
        return true;
    }

    /**
     *
     * @param nn
     * @return
     */
    public boolean isprime(String nn) {
        BigInteger n = new BigInteger(nn);
        BigInteger i = new BigInteger("2");
        BigInteger ns = bigIntSqRootCeil(n);
        while (i.compareTo(ns) <= 0) {
            if (n.mod(i).toString().equals("0")) {
                return false;
            }
            i = i.add(BigInteger.ONE);
        }
        return true;
    }

    /**
     * Thanks to http://stackoverflow.com/a/11962756/2534036
     *
     * @param x
     * @return
     * @throws IllegalArgumentException
     */
    private BigInteger bigIntSqRootCeil(BigInteger x) throws IllegalArgumentException {
        if (x.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("Negative argument.");
        }
        // square roots of 0 and 1 are trivial and
        // y == 0 will cause a divide-by-zero exception
        if (x == BigInteger.ZERO || x == BigInteger.ONE) {
            return x;
        } // end if
        BigInteger two = BigInteger.valueOf(2L);
        BigInteger y;
        // starting with y = x / 2 avoids magnitude issues with x squared
        for (y = x.divide(two); y.compareTo(x.divide(y)) > 0; y = ((x.divide(y)).add(y)).divide(two)) {
        }
        if (x.compareTo(y.multiply(y)) == 0) {
            return y;
        } else {
            return y.add(BigInteger.ONE);
        }

    } // end bigIntSqRootCeil

    /**
     * Get all unique permutations of the given array.
     *
     * @param objs Array of items.
     * @return Matrix
     */
    public Object[] perms(Object... objs) {
        Permutations<Object> perm = new Permutations<>(objs);

        Set perms = new HashSet<>();

        while (perm.hasNext()) {
            perms.add(perm.next());
        }

        Object[][] a = new Object[perms.size()][objs.length];
        return perms.toArray(a);
    }

    /**
     * Get a uniform random fraction between 0.0 (inclusive) and 1.0
     * (exclusive).
     *
     * @return random fraction
     */
    public double rand() {
        return rng.nextDouble();
    }

    /**
     * Get a random boolean value.
     *
     * @return true or false
     */
    public boolean randb() {
        return rng.nextBoolean();
    }

    /**
     * Get a uniform random integer.
     *
     * @param min Minimum value, inclusive
     * @param max Maximum value, inclusive
     * @return random integer
     */
    public double rand(int min, int max) {
        if (min == 0 && max == 0) {
            return rand();
        }
        return rng.nextInt((max - min) + 1) + min;
    }

    /**
     * Multiply two matrices.
     *
     * @param a First matrix
     * @param b Second matrix
     * @return The multiplied matrices
     * @throws net.apocalypselabs.symat.BadInputException When the matrices are
     * wrong.
     */
    public double[][] mtimes(double[][] a, double[][] b) throws BadInputException {
        double[][] ans = new double[a.length][b[0].length];
        double sum = 0;
        int c, d, k, m = a.length, q = b[0].length, p = b.length;

        if (a[0].length != b.length) {
            throw new BadInputException("First matrix column count must match "
                    + "second matrix row count.");
        }

        for (c = 0; c < m; c++) {
            for (d = 0; d < q; d++) {
                for (k = 0; k < p; k++) {
                    sum = sum + a[c][k] * b[k][d];
                }
                ans[c][d] = sum;
                sum = 0;
            }
        }
        return ans;
    }

    /**
     * Multiply the given matrix by itself b times.
     *
     * @param a Square matrix
     * @param b Power ( &gt;= 0 )
     * @return The matrix result
     * @throws BadInputException if the matrix is not square or power is less
     * than 0
     */
    public double[][] mpower(double[][] a, int b) throws BadInputException {
        if (a.length != a[0].length) {
            throw new BadInputException("Matrix needs to be square.");
        }
        if (b < 0) {
            throw new BadInputException("Power cannot be negative.");
        }

        double[][] ans = a;

        for (int i = 0; i < b; i++) {
            if (i == 0) {
                ans = a;
            } else {
                ans = mtimes(a, ans);
            }
        }
        return ans;
    }

    /**
     * Convert the given number of degrees to radians.
     *
     * @param degrees Number in degrees
     * @return Radians (degrees * (pi/180))
     */
    public double rad(double degrees) {
        return degrees * (PI / 180);
    }

    /**
     * Convert the given number of radians to degrees.
     *
     * @param radians Number in radians
     * @return Degrees (radians * (180/pi))
     */
    public double deg(double radians) {
        return radians * (180 / PI);
    }

    /**
     *
     * @param input
     * @return
     */
    public String sym(String input) {
        return util.evaluate(input).toString();
    }

    /**
     * Evaluate the given text with the builtin Symja parser.
     *
     * @param input
     * @return
     */
    public String $(String input) {
        return sym(input);
    }

    /**
     * Replace all occurrences of variable with newvar in function.
     *
     * @param function Text input
     * @param variable Text to replace
     * @param newvar Text to replace with
     * @return function with text replaced.
     */
    public String replace(String function, String variable, String newvar) {
        return function.replaceAll(variable, newvar);
    }

    /**
     * Substitute newvar for variable in function and attempt to calculate a
     * numerical answer.
     * <br>Example: subs('32*x','x',2) = 64.0
     *
     * @param function Function
     * @param variable Variable to substitute
     * @param newvar Value to replace with
     * @return The numerical answer or zero if there is no numerical answer.
     */
    public double subs(String function, String variable, String newvar) {
        return numof(function.replaceAll(variable, newvar));
    }

    /**
     * Attempt to find numerical value of input.
     *
     * @param f Function
     * @return answer or zero if it doesn't exist
     */
    public double numof(String f) {
        try {
            return Double.parseDouble(util.evaluate("N(" + f + ")").toString());
        } catch (MathException | NumberFormatException ex) {
            return 0.0;
        }
    }

    /*
     Graphing interfaces
     */
    /**
     *
     * @param xmin
     * @param xmax
     */
    public void xlim(double xmin, double xmax) {
        graphwin.setRange(xmin, xmax);
    }

    /**
     *
     * @param function
     */
    public void plot(String function) {
        showGraph();
        if (!function.equals("")) {
            graphwin.graphFunction(function);
        }
    }

    /**
     *
     * @param x
     * @param y
     */
    public void plot(double[] x, double[] y) {
        graphwin.plotPoints(x, y);
    }

    /**
     *
     * @param x
     * @param y
     * @param name
     */
    public void plot(double[] x, double[] y, String name) {
        graphwin.plotPoints(x, y, name);
    }

    /**
     *
     * @param function
     * @param xmin
     * @param xmax
     */
    public void plot(String function, double xmin, double xmax) {
        graphwin.setRange(xmin, xmax);
        plot(function);
    }

    /**
     *
     * @param f
     */
    public void ezplot(String f) {
        plot(f);
    }

    /**
     *
     * @param function
     * @param xmin
     * @param xmax
     */
    public void ezplot(String function, double xmin, double xmax) {
        plot(function, xmin, xmax);
    }

    /**
     *
     * @param f
     */
    public void graph(String f) {
        plot(f);
    }

    /**
     *
     * @return
     */
    public String plotname() {
        return graphwin.getTitle();
    }

    /**
     *
     * @param t
     * @return
     */
    public String plotname(String t) {
        if (t.equals("symatpythonnullplotname")) {
            return graphwin.getTitle();
        } else {
            graphwin.setWindowTitle(t);
            graphwin.setLabel(t);
            return "";
        }
    }

    /**
     *
     */
    public void plot() {
        showGraph();
    }

    /**
     *
     */
    public void plotclr() {
        graphwin.clearDraw();
    }

    /**
     *
     */
    public void clearplot() {
        plotclr();
    }

    /**
     *
     */
    public void plotclear() {
        plotclr();
    }

    /**
     *
     * @param x
     * @param y
     */
    public void drawdot(double x, double y) {
        showGraph();
        graphwin.drawDot(x, y);
    }

    /**
     *
     * @param path
     * @return
     * @throws IOException
     */
    public String readfile(String path) throws IOException {
        return FileUtils.readFile(path);
    }

    /**
     *
     * @param content
     * @param path
     * @throws IOException
     */
    public void savefile(String content, String path) throws IOException {
        FileUtils.saveFile(content, path, false);
    }

    /**
     * Show a file dialog and return the path of the chosen file (or "" if
     * canceled).
     *
     * @return
     */
    public String filedialog() {
        JFileChooser fc = new JFileChooser();
        int result = fc.showDialog(Main.maingui, "Choose");
        if (result == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile().getPath();
        }
        return "";
    }

    /**
     *
     * @param data
     * @return
     */
    public String md5sum(String data) {
        return FileUtils.MD5(data);
    }

    /**
     *
     * @param key
     * @param val
     */
    public void save(String key, String val) {
        Preferences prefs = Preferences.userNodeForPackage(Functions.class);
        prefs.put(key, val);
        try {
            prefs.flush();
        } catch (Exception ex) {

        }
    }

    /**
     *
     * @param key
     * @return
     */
    public String load(String key) {
        Preferences prefs = Preferences.userNodeForPackage(Functions.class);
        return prefs.get(key, "");
    }

    /*
     Other
     */
    /**
     *
     * @return
     */
    public String sysinfo() {
        String info = "==Java Information==\n";
        info += "Java version: " + System.getProperty("java.version");
        info += "\nJava vendor: " + System.getProperty("java.vendor");
        info += "\nJava home: " + System.getProperty("java.home");

        return info;
    }

    /**
     * Reset the license, quit the application.
     */
    public void resetlicense() {
        int result = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to reset your license?\n"
                + "This will close SyMAT and all open files!",
                "Reset license",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            PrefStorage.unset("license");
            System.exit(0);
        }
    }

    /**
     *
     * @return
     */
    public String license() {
        String expires = "Error";
        if (PrefStorage.getSetting("licensetype").equals("demo")) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            try {
                long expire = Long.parseLong(PrefStorage.getSetting("license"));
                long days = (((expire - c.getTimeInMillis()) / (60 * 60 * 24)) - 999) / 1000;
                if (days < 0) {
                    if (days == -1) {
                        expires = "Today";
                    } else {
                        expires = abs(days) + " days ago";
                    }
                } else {
                    expires = "In " + days + " days";
                }
            } catch (NumberFormatException e) {
            }
        } else {
            try {
                URL url = new URL(API_URL + "expire.php?email=" + PrefStorage.getSetting("license"));
                try (InputStream is = url.openStream();
                        BufferedReader br
                        = new BufferedReader(new InputStreamReader(is))) {
                    expires = br.readLine();
                } catch (IOException ex) {
                }
            } catch (Exception ex) {
            }
        }
        String lic = "==License Information==\n"
                + "License: " + PrefStorage.getSetting("license")
                + "\nType: " + PrefStorage.getSetting("licensetype")
                + "\nExpires: " + expires;
        return lic;
    }

    /**
     * Make sure the graph window shows.
     */
    private void showGraph() {
        graphwin.setVisible(true);
        graphwin.toFront();
    }

    /**
     *
     * @return
     */
    public TextBox textbox() {
        return new TextBox();
    }

    /**
     *
     * @return
     */
    public WebBrowser browser() {
        return new WebBrowser();
    }
    /*
     Constructor.
     */

    /**
     *
     */
    public Functions() {
        Main.loadFrame(graphwin, false);
    }

    /**
     *
     * @param l
     */
    public void setLang(String l) {
        lang = l;
    }

    /**
     * This class finds permutations of an array.
     *
     * Credit to http://stackoverflow.com/a/14444037/2534036
     *
     * License: CC-BY-SA 3.0
     *
     * @param <E>
     */
    class Permutations<E> implements Iterator<E[]> {

        private final E[] arr;
        private final int[] ind;
        private boolean has_next;

        public E[] output;//next() returns this array, make it public

        Permutations(E[] arr) {
            this.arr = arr.clone();
            ind = new int[arr.length];
            //convert an array of any elements into array of integers - first occurrence is used to enumerate
            Map<E, Integer> hm = new HashMap<>();
            for (int i = 0; i < arr.length; i++) {
                Integer n = hm.get(arr[i]);
                if (n == null) {
                    hm.put(arr[i], i);
                    n = i;
                }
                ind[i] = n;
            }
            Arrays.sort(ind);//start with ascending sequence of integers

            //output = new E[arr.length]; <-- cannot do in Java with generics, so use reflection
            output = (E[]) Array.newInstance(arr.getClass().getComponentType(), arr.length);
            has_next = true;
        }

        @Override
        public boolean hasNext() {
            return has_next;
        }

        /**
         * Computes next permutations. Same array instance is returned every
         * time!
         *
         * @return
         */
        @Override
        public E[] next() {
            if (!has_next) {
                throw new NoSuchElementException();
            }

            for (int i = 0; i < ind.length; i++) {
                output[i] = arr[ind[i]];
            }

            //get next permutation
            has_next = false;
            for (int tail = ind.length - 1; tail > 0; tail--) {
                if (ind[tail - 1] < ind[tail]) {//still increasing

                    //find last element which does not exceed ind[tail-1]
                    int s = ind.length - 1;
                    while (ind[tail - 1] >= ind[s]) {
                        s--;
                    }

                    swap(ind, tail - 1, s);

                    //reverse order of elements in the tail
                    for (int i = tail, j = ind.length - 1; i < j; i++, j--) {
                        swap(ind, i, j);
                    }
                    has_next = true;
                    break;
                }

            }
            return output;
        }

        private void swap(int[] arr, int i, int j) {
            int t = arr[i];
            arr[i] = arr[j];
            arr[j] = t;
        }

        @Override
        public void remove() {

        }
    }
}
