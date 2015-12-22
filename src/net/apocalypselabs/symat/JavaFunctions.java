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

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Skylar
 */
public class JavaFunctions extends Functions {

    public JavaFunctions() {
        super();
    }

    public double add(double a, double b) {
        return a + b;
    }

    public int add(int a, int b) {
        return a + b;
    }

    @Override
    public double add(double[] a) {
        return super.add(a);
    }

    public int add(int[] a) {
        return (int) super.add(intArrayToDouble(a));
    }

    public double subtract(double a, double b) {
        return a - b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }

    @Override
    public double subtract(double[] a) {
        return super.subtract(a);
    }

    public int subtract(int[] a) {
        return (int) super.subtract(intArrayToDouble(a));
    }

    public double times(double a, double b) {
        return a * b;
    }

    public int times(int a, int b) {
        return a * b;
    }

    @Override
    public double times(double[] a) {
        return super.times(a);
    }

    public int times(int[] a) {
        return (int) super.times(intArrayToDouble(a));
    }

    public double divide(double a, double b) {
        return a / b;
    }

    public int divide(int a, int b) {
        return a / b;
    }

    @Override
    public double divide(double[] a) {
        return super.divide(a);
    }

    public int divide(int[] a) {
        return (int) super.divide(intArrayToDouble(a));
    }

    public void drawdot(int x, int y) {
        super.drawdot(x, y);
    }

    public Double[][] perms(Double... objs) {
        Permutations<Double> perm = new Permutations<>(objs);

        Set perms = new HashSet<>();

        while (perm.hasNext()) {
            perms.add(perm.next());
        }

        Double[][] a = new Double[perms.size()][objs.length];
        return (Double[][]) perms.toArray(a);
    }

    public Integer[][] perms(Integer... objs) {
        Permutations<Integer> perm = new Permutations<>(objs);

        Set perms = new HashSet<>();

        while (perm.hasNext()) {
            perms.add(perm.next());
        }

        Integer[][] a = new Integer[perms.size()][objs.length];
        return (Integer[][]) perms.toArray(a);
    }

    public double[][] perms(double[] p) {
        Double[] o = new Double[p.length];
        for (int i = 0; i < p.length; i++) {
            o[i] = p[i];
        }
        Double[][] o2 = perms(o);
        double[][] out = new double[o2.length][o2[0].length];
        for (int i = 0; i < o2.length; i++) {
            for (int j = 0; j < o2[0].length; j++) {
                out[i][j] = o2[i][j];
            }
        }
        return out;
    }

    public int[][] perms(int[] p) {
        Integer[] o = new Integer[p.length];
        for (int i = 0; i < p.length; i++) {
            o[i] = p[i];
        }
        Integer[][] o2 = perms(o);
        int[][] out = new int[o2.length][o2[0].length];
        for (int i = 0; i < o2.length; i++) {
            for (int j = 0; j < o2[0].length; j++) {
                out[i][j] = o2[i][j];
            }
        }
        return out;
    }

    public int mod(int a, int b) {
        return a % b;
    }

    public double mod(double a, double b) {
        return a % b;
    }
    
    public int mod(int... a) {
        return (int) super.mod(intArrayToDouble(a));
    }
    
    public String printa(double[][] a) {
        return super.printa(a);
    }
    
    public String printa(int[][] a) {
        return super.printa(a);
    }

}
