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
package org.androidannotations.annotations.res;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.androidannotations.annotations.ResId;

/**
 * <p>
 * Use on {@link java.lang.Integer} or <code>int</code> fields in any enhanced
 * classes that should be injected with this specific color resource.
 * </p>
 * <p>
 * The annotation value must be one of R.color.* fields. If the value is not
 * set, the field or method name will be used as the R.color.* field name.
 * </p>
 */
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
public @interface ColorRes {

	/**
	 * The R.color.* field which refers to the color resource.
	 * 
	 * @return the id of the resource
	 */
	int value() default ResId.DEFAULT_VALUE;

	/**
	 * The resource name as a string which refers to the color resource.
	 * 
	 * @return the resource name of the resource
	 */
	String resName() default "";
}
