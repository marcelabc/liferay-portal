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
public final class DEDataRecordCollectionDeleteRecordRequest {

	public long getDEDataRecordId() {
		return _deDataRecordId;
	}

	public static final class Builder {

		public Builder(long deDataRecordId) {
			_deDataRecordCollectionDeleteRecordRequest._deDataRecordId =
				deDataRecordId;
		}

		public DEDataRecordCollectionDeleteRecordRequest build() {
			return _deDataRecordCollectionDeleteRecordRequest;
		}

		private final DEDataRecordCollectionDeleteRecordRequest
			_deDataRecordCollectionDeleteRecordRequest =
				new DEDataRecordCollectionDeleteRecordRequest();

	}

	private DEDataRecordCollectionDeleteRecordRequest() {
	}

	private long _deDataRecordId;

}