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

package com.liferay.data.engine.internal.executor;

import com.liferay.data.engine.exception.DEDataRecordCollectionException;
import com.liferay.data.engine.internal.storage.DEDataStorageTracker;
import com.liferay.data.engine.model.DEDataDefinition;
import com.liferay.data.engine.model.DEDataRecord;
import com.liferay.data.engine.service.DEDataRecordCollectionSaveRecordRequest;
import com.liferay.data.engine.service.DEDataRecordCollectionSaveRecordResponse;
import com.liferay.data.engine.storage.DEDataStorage;
import com.liferay.data.engine.storage.DEDataStorageRequestBuilder;
import com.liferay.data.engine.storage.DEDataStorageSaveRequest;
import com.liferay.data.engine.storage.DEDataStorageSaveResponse;
import com.liferay.dynamic.data.lists.model.DDLRecord;
import com.liferay.dynamic.data.lists.service.DDLRecordLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;

/**
 * @author Leonardo Barros
 */
public class DEDataRecordCollectionSaveRecordRequestExecutor {

	public DEDataRecordCollectionSaveRecordRequestExecutor(
		DEDataStorageTracker deDataStorageTracker,
		DDLRecordLocalService ddlRecordLocalService) {

		_deDataStorageTracker = deDataStorageTracker;
		_ddlRecordLocalService = ddlRecordLocalService;
	}

	public DEDataRecordCollectionSaveRecordResponse execute(
			DEDataRecordCollectionSaveRecordRequest
				deDataRecordCollectionSaveRecordRequest)
		throws Exception {

		DEDataRecord deDataRecord =
			deDataRecordCollectionSaveRecordRequest.getDEDataRecord();

		DEDataDefinition deDataDefinition = deDataRecord.getDEDataDefinition();

		if (deDataDefinition == null) {
			return DEDataRecordCollectionSaveRecordResponse.Builder.of(
				null
			);
		}

		String storageType = deDataDefinition.getStorageType();

		DEDataStorage deDataStorage = _deDataStorageTracker.getDEDataStorage(
			storageType);

		if (deDataStorage == null) {
			throw new DEDataRecordCollectionException.NoSuchDataStorage(
				storageType);
		}

		DEDataStorageSaveRequest deDataStorageSaveRequest =
			DEDataStorageRequestBuilder.saveBuilder(
				deDataRecord
			).build();

		DEDataStorageSaveResponse deDataStorageSaveResponse =
			deDataStorage.save(deDataStorageSaveRequest);

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		long deDataRecordId = deDataRecord.getDEDataRecordId();

		DDLRecord ddlRecord = null;

		if (deDataRecordId == 0) {
			ddlRecord = _ddlRecordLocalService.addRecord(
				deDataRecordCollectionSaveRecordRequest.getUserId(),
				deDataRecordCollectionSaveRecordRequest.getGroupId(),
				deDataStorageSaveResponse.getDEDataStorageId(),
				deDataRecord.getDEDataRecordCollectionId(), serviceContext);
		}
		else {
			ddlRecord = _ddlRecordLocalService.updateRecord(
				deDataRecordCollectionSaveRecordRequest.getUserId(),
				deDataRecordCollectionSaveRecordRequest.getGroupId(),
				deDataStorageSaveResponse.getDEDataStorageId(), serviceContext);
		}

		deDataRecord.setDEDataRecordId(ddlRecord.getRecordId());

		return DEDataRecordCollectionSaveRecordResponse.Builder.of(
			deDataRecord
		);
	}

	private final DDLRecordLocalService _ddlRecordLocalService;
	private final DEDataStorageTracker _deDataStorageTracker;

}