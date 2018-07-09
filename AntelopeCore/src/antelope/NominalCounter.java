/*  **************************************************************************
 *
 *  A BigInteger replacement for iterating through nominals.
 *
 *  Author: Pedro A. Góngora <pedro.gongora@gmail.com>
 *
 *  Copyright (C) 2010 Pedro A. Góngora
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 *  *************************************************************************/

package antelope;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NominalCounter {
    
    private static Logger log = Logger.getLogger(NominalCounter.class.getName());
    
    private boolean usingLongImpl = false;
    private LongCounter longCounter;

    private BigInteger maxValue;
    private byte value[];
    private byte max[];
    private String name;
    // least upper bound (big-endian, lower is more significant)
    // invariant: value[lub]>0 and value[i]=0 for i = 0,...,lub-1
    private int lub = 0;
    private int currLub;
    
    private int lubBit = -1; // for logging
    private int bitsIncreased = 0;

    public NominalCounter(BigInteger maxValue, String name) {
        this.maxValue = maxValue.add(BigInteger.ONE);
        this.name = "ByteArrayCounter(" + name + ")";
        initCounter();
    }

    public NominalCounter(BigInteger maxValue) {
        this.maxValue = maxValue.add(BigInteger.ONE);
        this.name = "ByteArrayCounter(" + maxValue + ")";
        initCounter();
    }
    
    private void initCounter() {
        // use native long data type if possible
        //if (false) {
        if (maxValue.bitLength() < 64) {
            usingLongImpl = true;
            longCounter = new LongCounter(maxValue.longValue(), name);
        } else {
            max = maxValue.toByteArray();
            value = new byte[max.length];
            currLub = max.length - 1;
            // verify that lub is initially correct (ignore the sign bit)
            // and initialize value to 0's
            for (int ii=max.length-1; ii>=0; ii--) {
                if (max[ii] != 0) lub=ii;
                value[ii] = 0;
            }
        }
    }
    
    public void increment() {
        if (usingLongImpl) {
            longCounter.increment();
        } else {
            boolean carry = false;
            int ii = max.length - 1;
            
            do {
                carry = value[ii] == -1;
                value[ii]++;
                ii--;
            } while (carry && ii>=0);
            
            int tmp = 8;
            for (int shift=0; shift<8; shift++) {
                if ( (value[currLub] & (1<<shift)) != 0 ) tmp = shift;
            }
            
            if (tmp > lubBit) {
                lubBit++;
                bitsIncreased++;
                if (log.isLoggable(Level.FINEST)) {
                    log.finest(name+" bitsize is now "+bitsIncreased+" bits");
                }
            }
            
            if (lubBit == 8) {
                currLub--;
                lubBit = 0;
            }
        }
    }
    
    public boolean testBit(int n) {
        if (usingLongImpl) {
            return longCounter.testBit(n);
        } else {
            if (n > maxValue.bitLength()) return false;
            int arrayPosition = value.length - n/8 - 1;
            int bytePosition = n % 8;
            
            return (value[arrayPosition] & (1 << bytePosition)) != 0;
        }
    }
    
    public boolean leqMaxValue() {
        if (usingLongImpl) {
            return longCounter.leqMaxValue();
        } else {
            return !testBit(maxValue.bitLength()); // nominals + 1
        }
    }
        
    //public boolean isMaxValue() {
    //    if (usingLongImpl) {
    //        return longCounter.isMaxValue();
    //    } else {
    //        if (currLub == lub) {
    //            for (int ii=0; ii<max.length; ii++) {
    //                if (max[ii] != value[ii]) return false;
    //            }
    //            return true;
    //        }
    //        return false;
    //    }
    //}
    
    
    private class LongCounter {
    
        //private static Logger log = Logger.getLogger(LongCounter.class.getName());
    
        private String name;
        private long maxValue;
        private long value = 0;
        private int shift = 7;
    
        public LongCounter(long maxValue, String name) {
            this.maxValue = maxValue;
            this.name = name;
        }
        
        public void increment() {
            value++;
            if ( (value & (1<<shift)) != 0 ) {
                shift++;
                if (log.isLoggable(Level.FINEST)) {
                    log.finest(name+" bitsize is now "+(shift)+" bits");
                }
            }
        }
        
        public boolean testBit(int n) {
            return (value & (1<<n)) != 0;
        }
        
        public boolean leqMaxValue() {
            return value < maxValue; // maxValue == nominals + 1
        }
        
        //public boolean isMaxValue() {
        //    return value == maxValue;
        //}
    
    }
}
