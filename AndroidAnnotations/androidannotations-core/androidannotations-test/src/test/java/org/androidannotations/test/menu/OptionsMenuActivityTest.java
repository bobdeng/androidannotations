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
import static org.mockito.Mockito.when;

import org.androidannotations.test.R;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.view.Menu;
import android.view.MenuItem;

@RunWith(RobolectricTestRunner.class)
public class OptionsMenuActivityTest {

	private OptionsMenuActivity_ activity;

	@Before
	public void setUp() {
		activity = Robolectric.buildActivity(OptionsMenuActivity_.class).create().get();
	}

	@Test
	public void defaultIdSelected() {
		MenuItem item = mock(MenuItem.class);

		when(item.getItemId()).thenReturn(R.id.menu_refresh);

		activity.onOptionsItemSelected(item);

		assertThat(activity.menuRefreshSelected).isTrue();
	}

	@Test
	public void multipleIdsSelected() {
		MenuItem item = mock(MenuItem.class);

		when(item.getItemId()).thenReturn(R.id.menu_search);

		boolean result = activity.onOptionsItemSelected(item);

		assertThat(activity.multipleMenuItems).isTrue();
		assertThat(result).isFalse();
		activity.multipleMenuItems = false;

		when(item.getItemId()).thenReturn(R.id.menu_share);

		result = activity.onOptionsItemSelected(item);
		assertThat(activity.multipleMenuItems).isTrue();
		assertThat(result).isFalse();
	}

	@Test
	public void defaultIdUnderscore() {
		MenuItem item = mock(MenuItem.class);

		when(item.getItemId()).thenReturn(R.id.menu_add);

		activity.onOptionsItemSelected(item);

		assertThat(activity.menuAdd).isTrue();
	}

	@Test
	public void subclassTakesPrecedenceInMenuItemHandling() {
		MenuItem item = mock(MenuItem.class);

		when(item.getItemId()).thenReturn(R.id.menu_refresh);

		activity.onOptionsItemSelected(item);

		assertThat(activity.menuRefreshSelected).isTrue();
		assertThat(activity.menuRefreshSelectedFromAnnotatedClass).isFalse();
	}

	@Test
	@Ignore
	// TODO robolectric does not support menu inflation
	public void methodInjectedMenuItem() {
		Menu menu = mock(Menu.class);
		activity.onCreateOptionsMenu(menu);
		assertThat(activity.methodInjectedMenuItem).isNotNull();
	}

	@Test
	@Ignore
	// TODO robolectric does not support menu inflation
	public void multiInjectedMenuItem() {
		Menu menu = mock(Menu.class);
		activity.onCreateOptionsMenu(menu);
		assertThat(activity.multiInjectedMenuItem).isNotNull();
	}
}
