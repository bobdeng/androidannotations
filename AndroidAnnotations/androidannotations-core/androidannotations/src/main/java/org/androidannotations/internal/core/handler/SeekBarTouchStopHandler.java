/**
 * Copyright (C) 2010-2015 eBusiness Information, Excilys Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.androidannotations.internal.core.handler;

import org.androidannotations.AndroidAnnotationsEnvironment;
import org.androidannotations.annotations.SeekBarTouchStop;
import org.androidannotations.holder.OnSeekBarChangeListenerHolder;

import com.helger.jcodemodel.JBlock;
import com.helger.jcodemodel.JVar;

public class SeekBarTouchStopHandler extends AbstractSeekBarTouchHandler {

	public SeekBarTouchStopHandler(AndroidAnnotationsEnvironment environment) {
		super(SeekBarTouchStop.class, environment);
	}

	@Override
	protected JBlock getMethodBodyToCall(OnSeekBarChangeListenerHolder onSeekBarChangeListenerHolder) {
		return onSeekBarChangeListenerHolder.getOnStopTrackingTouchBody();
	}

	@Override
	protected JVar getMethodParamToPass(OnSeekBarChangeListenerHolder onSeekBarChangeListenerHolder) {
		return onSeekBarChangeListenerHolder.getOnStopTrackingTouchSeekBarParam();
	}
}
