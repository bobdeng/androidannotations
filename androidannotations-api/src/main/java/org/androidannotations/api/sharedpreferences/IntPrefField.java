/**
 * Copyright (C) 2010-2014 eBusiness Information, Excilys Group
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
package org.androidannotations.api.sharedpreferences;

import android.content.SharedPreferences;

public class IntPrefField extends AbstractPrefField {

	private final int defaultValue;

	IntPrefField(SharedPreferences sharedPreferences, String key, int defaultValue) {
		super(sharedPreferences, key);
		this.defaultValue = defaultValue;
	}

	public int get() {
		return getOr(defaultValue);
	}

	public int getOr(int defaultValue) {
		try {
			return sharedPreferences.getInt(key, defaultValue);
		} catch (ClassCastException e) {
			// The pref could be a String, if that is the case try this
			// recovery bit
			try {
				String value = sharedPreferences.getString(key, "" + defaultValue);
				return Integer.parseInt(value);
			} catch (Exception e2) {
				// our  recovery bit failed. The problem is elsewhere. Send the original error
				throw e;
			}
		}

	}

	public void put(int value) {
		apply(edit().putInt(key, value));
	}

}
