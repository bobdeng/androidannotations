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
package org.androidannotations.test.menu;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.view.Menu;

@RunWith(RobolectricTestRunner.class)
public class InjectMenuActivityTest {

	private InjectMenuActivity_ injectMenuActivity;

	@Before
	public void setUp() {
		injectMenuActivity = Robolectric.buildActivity(InjectMenuActivity_.class).create().get();
	}

	@Test
	public void menuIsNull() {
		assertThat(injectMenuActivity.menu).isNull();
	}

	@Test
	public void testMenuInjectedFromOnCreateOptionsMenu() {
		Menu menu = mock(Menu.class);
		injectMenuActivity.onCreateOptionsMenu(menu);
		assertThat(injectMenuActivity.menu).isSameAs(menu);
	}

	@Test
	public void methodInjectedMenu() {
		Menu menu = mock(Menu.class);
		injectMenuActivity.onCreateOptionsMenu(menu);
		assertThat(injectMenuActivity.methodInjectedMenu).isSameAs(menu);
	}

	@Test
	public void multiInjectedMenu() {
		Menu menu = mock(Menu.class);
		injectMenuActivity.onCreateOptionsMenu(menu);
		assertThat(injectMenuActivity.multiInjectedMenu).isSameAs(menu);
	}
}
