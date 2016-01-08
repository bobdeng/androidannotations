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
package org.androidannotations.test.ebean;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class BeanInjectedActivityTest {

	private BeanInjectedActivity_ activity;

	@Before
	public void setUp() {
		activity = Robolectric.buildActivity(BeanInjectedActivity_.class).create().get();
	}

	@Test
	public void dependencyIsInjected() {
		assertThat(activity.dependency).isNotNull();
	}

	@Test
	public void methodInjectedDependencyIsInjected() {
		assertThat(activity.methodInjectedDependency).isNotNull();
	}

	@Test
	public void methodAnnotatedParamsDependencyIsInjected() {
		assertThat(activity.annotatedParamDependency).isNotNull();
	}

	@Test
	public void dependencyWithAnnotationValueIsInjected() {
		assertThat(activity.interfaceDependency).isNotNull();
	}

	@Test
	public void methodInjectedDependencyWithAnnotationValueIsInjected() {
		assertThat(activity.methodInjectedInterface).isNotNull();
	}

	@Test
	public void methodAnnotatedParamsDependencyWithAnnotationValueIsInjected() {
		assertThat(activity.annotatedParamInterface).isNotNull();
	}

	@Test
	public void dependencyWithAnnotationValueIsOfAnnotationValueType() {
		assertThat(activity.interfaceDependency).isInstanceOf(SomeImplementation.class);
	}

	@Test
	public void methodInjectedDependencyWithAnnotationValueIsOfAnnotationValueType() {
		assertThat(activity.methodInjectedInterface).isInstanceOf(SomeImplementation.class);
	}

	@Test
	public void methodAnnotatedParamsDependencyWithAnnotationValueIsOfAnnotationValueType() {
		assertThat(activity.annotatedParamInterface).isInstanceOf(SomeImplementation.class);
	}

	@Test
	public void singletonDependencyIsSameReference() {
		SomeSingleton initialDependency = activity.singletonDependency;

		BeanInjectedActivity_ newActivity = Robolectric.buildActivity(BeanInjectedActivity_.class).create().get();

		assertThat(newActivity.singletonDependency).isSameAs(initialDependency);
	}

	@Test
	public void methodInjectedSingletonDependencyIsSameReference() {
		SomeSingleton initialDependency = activity.methodInjectedSingleton;

		BeanInjectedActivity_ newActivity = Robolectric.buildActivity(BeanInjectedActivity_.class).create().get();

		assertThat(newActivity.methodInjectedSingleton).isSameAs(initialDependency);
	}

	@Test
	public void methodAnnotatedParamsSingletonDependencyIsSameReference() {
		SomeSingleton initialDependency = activity.annotatedParamSingleton;

		BeanInjectedActivity_ newActivity = Robolectric.buildActivity(BeanInjectedActivity_.class).create().get();

		assertThat(newActivity.annotatedParamSingleton).isSameAs(initialDependency);
	}

	@Test
	public void multipleDependenciesInjected() {
		assertThat(activity.multiDependency).isNotNull();
		assertThat(activity.multiDependencyInterface).isNotNull();
		assertThat(activity.multiDependencySingleton).isNotNull();

		assertThat(activity.multiDependencyInterface).isInstanceOf(SomeImplementation.class);

		SomeSingleton initialDependency = activity.multiDependencySingleton;
		BeanInjectedActivity_ newActivity = Robolectric.buildActivity(BeanInjectedActivity_.class).create().get();
		assertThat(newActivity.multiDependencySingleton).isSameAs(initialDependency);
	}
}
