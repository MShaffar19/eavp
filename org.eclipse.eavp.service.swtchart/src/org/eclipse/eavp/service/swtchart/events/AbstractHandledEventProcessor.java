/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.eavp.service.swtchart.events;

import org.eclipse.eavp.service.swtchart.core.BaseChart;

public abstract class AbstractHandledEventProcessor implements IHandledEventProcessor {

	@Override
	public int getButton() {

		return BaseChart.BUTTON_NONE;
	}
}