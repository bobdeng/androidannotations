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
package org.androidannotations.helper;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

@EActivity
public class BundleSparseArrayCompileSuccessActivity extends Activity {

	@InstanceState
	SparseArray<Parcelable> sparseArrayWithParcelable;

	@InstanceState
	SparseArray<ExtendParcelable> sparseArrayWithExtendParcelable;

	static class ExtendParcelable implements Parcelable {

		public static final Creator<ExtendParcelable> CREATOR = new Creator<ExtendParcelable>() {
			@Override
			public ExtendParcelable createFromParcel(Parcel in) {
				return new ExtendParcelable(in);
			}

			@Override
			public ExtendParcelable[] newArray(int size) {
				return new ExtendParcelable[size];
			}
		};

		protected ExtendParcelable(Parcel in) {
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel parcel, int i) {
		}
	}
}
