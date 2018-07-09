/*  **************************************************************************
 *
 *  Base class for PCTL state formulae.
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

package antelope.ctl;

public abstract class StateFormula {

    public final static int TYPE_BOOLEAN_CONST =  1;
    public final static int TYPE_VARIABLE      =  2;
    public final static int TYPE_NOT           =  3;
    public final static int TYPE_AND           =  4;
    public final static int TYPE_OR            =  5;
    public final static int TYPE_IMP           =  6;
    public final static int TYPE_IFF           =  7;
    public final static int TYPE_EX            =  8;
    public final static int TYPE_EY            =  9;
    public final static int TYPE_EF            = 10;
    public final static int TYPE_EG            = 11;
    public final static int TYPE_EU            = 12;
    public final static int TYPE_AX            = 13;
    public final static int TYPE_AY            = 14;
    public final static int TYPE_AF            = 15;
    public final static int TYPE_AG            = 16;
    public final static int TYPE_AU            = 17;
    public final static int TYPE_NOMINAL       = 18;
    public final static int TYPE_AT_STATE      = 19;
    public final static int TYPE_EXISTS_STATE  = 20;
    public final static int TYPE_STATE_BINDER  = 21;
    
    public abstract int type();

}
