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

package com.liferay.report.definitions.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.util.HashUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.report.definitions.model.ReportDefinition;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing ReportDefinition in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@ProviderType
public class ReportDefinitionCacheModel
	implements CacheModel<ReportDefinition>, Externalizable {

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof ReportDefinitionCacheModel)) {
			return false;
		}

		ReportDefinitionCacheModel reportDefinitionCacheModel =
			(ReportDefinitionCacheModel)obj;

		if (reportDefinitionId ==
				reportDefinitionCacheModel.reportDefinitionId) {

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, reportDefinitionId);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(27);

		sb.append("{uuid=");
		sb.append(uuid);
		sb.append(", reportDefinitionId=");
		sb.append(reportDefinitionId);
		sb.append(", groupId=");
		sb.append(groupId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", userName=");
		sb.append(userName);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", dataDefinitionId=");
		sb.append(dataDefinitionId);
		sb.append(", name=");
		sb.append(name);
		sb.append(", description=");
		sb.append(description);
		sb.append(", availableColumns=");
		sb.append(availableColumns);
		sb.append(", sortColumns=");
		sb.append(sortColumns);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public ReportDefinition toEntityModel() {
		ReportDefinitionImpl reportDefinitionImpl = new ReportDefinitionImpl();

		if (uuid == null) {
			reportDefinitionImpl.setUuid("");
		}
		else {
			reportDefinitionImpl.setUuid(uuid);
		}

		reportDefinitionImpl.setReportDefinitionId(reportDefinitionId);
		reportDefinitionImpl.setGroupId(groupId);
		reportDefinitionImpl.setCompanyId(companyId);
		reportDefinitionImpl.setUserId(userId);

		if (userName == null) {
			reportDefinitionImpl.setUserName("");
		}
		else {
			reportDefinitionImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			reportDefinitionImpl.setCreateDate(null);
		}
		else {
			reportDefinitionImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			reportDefinitionImpl.setModifiedDate(null);
		}
		else {
			reportDefinitionImpl.setModifiedDate(new Date(modifiedDate));
		}

		reportDefinitionImpl.setDataDefinitionId(dataDefinitionId);

		if (name == null) {
			reportDefinitionImpl.setName("");
		}
		else {
			reportDefinitionImpl.setName(name);
		}

		if (description == null) {
			reportDefinitionImpl.setDescription("");
		}
		else {
			reportDefinitionImpl.setDescription(description);
		}

		if (availableColumns == null) {
			reportDefinitionImpl.setAvailableColumns("");
		}
		else {
			reportDefinitionImpl.setAvailableColumns(availableColumns);
		}

		if (sortColumns == null) {
			reportDefinitionImpl.setSortColumns("");
		}
		else {
			reportDefinitionImpl.setSortColumns(sortColumns);
		}

		reportDefinitionImpl.resetOriginalValues();

		return reportDefinitionImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		uuid = objectInput.readUTF();

		reportDefinitionId = objectInput.readLong();

		groupId = objectInput.readLong();

		companyId = objectInput.readLong();

		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();

		dataDefinitionId = objectInput.readLong();
		name = objectInput.readUTF();
		description = objectInput.readUTF();
		availableColumns = objectInput.readUTF();
		sortColumns = objectInput.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		if (uuid == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(uuid);
		}

		objectOutput.writeLong(reportDefinitionId);

		objectOutput.writeLong(groupId);

		objectOutput.writeLong(companyId);

		objectOutput.writeLong(userId);

		if (userName == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(userName);
		}

		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		objectOutput.writeLong(dataDefinitionId);

		if (name == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(name);
		}

		if (description == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(description);
		}

		if (availableColumns == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(availableColumns);
		}

		if (sortColumns == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(sortColumns);
		}
	}

	public String uuid;
	public long reportDefinitionId;
	public long groupId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public long dataDefinitionId;
	public String name;
	public String description;
	public String availableColumns;
	public String sortColumns;

}