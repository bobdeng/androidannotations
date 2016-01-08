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
package org.androidannotations.ormlite.test;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class OrmLiteActivityTest {
	
	private MyActivity activity;

	@Before
	public void setup() {
		activity = Robolectric.buildActivity(MyActivity_.class).create().get();
	}

	@Test
	public void customDaoIsInjected() {
		assertThat((Object) activity.userDao).isNotNull();
	}

	@Test
	public void daoIsInjected() {
		assertThat((Object) activity.carDao).isNotNull();
	}

	@Test
	public void beanIsInjected() {
		assertThat((Object) activity.ormLiteBean).isNotNull();
	}

	@Test
	public void daoInBeanIsInjected() {
		assertThat((Object) activity.ormLiteBean.userDao).isNotNull();
	}

	@Test
	public void methodInjectedOrmLiteDao() {
		assertThat((Object) activity.ormLiteBean.methodInjectedOrmLiteDao).isNotNull();
	}

	@Test
	public void multiInjectedOrmLiteDao() {
		assertThat((Object) activity.ormLiteBean.multiInjectedOrmLiteDao).isNotNull();
	}
}
