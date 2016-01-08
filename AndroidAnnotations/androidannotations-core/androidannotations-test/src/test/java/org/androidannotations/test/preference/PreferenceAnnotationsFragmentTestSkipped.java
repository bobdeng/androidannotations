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
package org.androidannotations.test.preference;

import static org.fest.assertions.api.Assertions.assertThat;

import org.androidannotations.test.R;
import org.robolectric.util.FragmentTestUtil;

import android.preference.Preference;

// TODO not yet implemented in Robolectric
// @RunWith(RobolectricTestRunner.class)
public class PreferenceAnnotationsFragmentTestSkipped {

	private PreferenceAnnotationsFragment_ fragment;

	// @Before
	public void setUp() {
		fragment = new PreferenceAnnotationsFragment_();
		FragmentTestUtil.startFragment(fragment);
	}

	// @Test
	public void testPreferenceChangeHandled() {
		assertThat(fragment.preferenceWithKeyChanged).isFalse();

		Preference preference = fragment.findPreference(fragment.getString(R.string.listPreferenceKey));
		preference.getOnPreferenceChangeListener().onPreferenceChange(preference, new Object());

		assertThat(fragment.preferenceWithKeyChanged).isTrue();
	}

	// @Test
	public void testAfterPreferencesCalled() {
		assertThat(fragment.afterPreferencesCalled).isTrue();
	}
}
