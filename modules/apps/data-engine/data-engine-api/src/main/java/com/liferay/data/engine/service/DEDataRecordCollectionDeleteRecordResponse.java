/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.data.engine.service;

/**
 * @author Leonardo Barros
 */
public final class DEDataRecordCollectionDeleteRecordResponse {

	public long getDEDataRecordId() {
		return _deDataRecordId;
	}

	public static final class Builder {

		public static Builder newBuilder(long deDataRecordId) {
			return new Builder(deDataRecordId);
		}

		public static DEDataRecordCollectionDeleteRecordResponse of(
			long deDataRecordId) {

			return newBuilder(
				deDataRecordId
			).build();
		}

		public DEDataRecordCollectionDeleteRecordResponse build() {
			return _deDataRecordCollectionDeleteRecordResponse;
		}

		private Builder(long deDataRecordId) {
			_deDataRecordCollectionDeleteRecordResponse._deDataRecordId =
				deDataRecordId;
		}

		private final DEDataRecordCollectionDeleteRecordResponse
			_deDataRecordCollectionDeleteRecordResponse =
				new DEDataRecordCollectionDeleteRecordResponse();

	}

	private DEDataRecordCollectionDeleteRecordResponse() {
	}

	private long _deDataRecordId;

}