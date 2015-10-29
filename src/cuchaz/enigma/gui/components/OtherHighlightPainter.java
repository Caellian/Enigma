/*******************************************************************************
 * Copyright (c) 2015 Jeff Martin.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public
 * License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 * 
 * Contributors:
 *     Jeff Martin - initial API and implementation
 ******************************************************************************/
package cuchaz.enigma.gui.components;

import cuchaz.enigma.gui.components.BoxHighlightPainter;

import java.awt.Color;

public class OtherHighlightPainter extends BoxHighlightPainter {
	
	public OtherHighlightPainter() {
		// grey
		super(null, new Color(180, 180, 180));
	}
}
