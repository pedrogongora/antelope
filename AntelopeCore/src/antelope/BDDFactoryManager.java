/*  **************************************************************************
 *
 *  Helper class to search for the best BDDFactory implementantion available.
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import net.sf.javabdd.BDDFactory;

public class BDDFactoryManager {
    
    private static Logger log = Logger.getLogger(BDDFactoryManager.class.getName());

    private static BDDFactory factory = null;
    
    // TODO: fetch nodenum & cachesize values from some configuration file
    //private static int nodenum   = 40000000;
    private static int nodenum   = 1000000;
    private static int cachesize = 100000;
    
    private static String factoryName;
    
    public static final String[] factoryNames = {
        "net.sf.javabdd.BuDDyFactory",
        //"net.sf.javabdd.CUDDFactory",
        //"net.sf.javabdd.CALFactory",
        "net.sf.javabdd.JFactory"//,
        //"net.sf.javabdd.MicroFactory"
    };
    
    public static BDDFactory initFactory() {
        if (factory != null) {
            log.info("Reseting BDDFactory");
            factory.done();
            factory = null;
        }
        for (String fname : factoryNames) {
            log.finest("trying BDD implementation:" + fname);
            try {
                //System.out.println("nodenum: " + nodenum);
                Class<?> c = Class.forName(fname);
                Method m = c.getMethod("init", new Class[] { int.class, int.class });
                factory = (BDDFactory) m.invoke(null, new Object[] { new Integer(nodenum), new Integer(cachesize) });
                if (factory != null) {
                    log.info("Using BDD implementation: " + fname);
                    factoryName = fname;
                    break;
                }
            }
            catch (Throwable _) {
                if (_ instanceof InvocationTargetException)
                    _ = ((InvocationTargetException)_).getTargetException();
                log.finest("Failed initializing "+fname+": "+_);
            }
        }
        if (factory == null) {
            log.severe("There is no BDD implementation available");
            throw new ProgramErrorException("There is no BDD implementation available");
        }
        return factory;
    }
    
    public static BDDFactory getFactory() {
        return factory;
    }
    
    public static String getFactoryName() {
        return factoryName;
    }
}
