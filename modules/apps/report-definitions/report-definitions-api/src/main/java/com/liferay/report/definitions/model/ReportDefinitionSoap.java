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

package com.liferay.report.definitions.model;

import aQute.bnd.annotation.ProviderType;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is used by SOAP remote services.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@ProviderType
public class ReportDefinitionSoap implements Serializable {

	public static ReportDefinitionSoap toSoapModel(ReportDefinition model) {
		ReportDefinitionSoap soapModel = new ReportDefinitionSoap();

		soapModel.setUuid(model.getUuid());
		soapModel.setReportDefinitionId(model.getReportDefinitionId());
		soapModel.setGroupId(model.getGroupId());
		soapModel.setCompanyId(model.getCompanyId());
		soapModel.setUserId(model.getUserId());
		soapModel.setUserName(model.getUserName());
		soapModel.setCreateDate(model.getCreateDate());
		soapModel.setModifiedDate(model.getModifiedDate());
		soapModel.setDataDefinitionId(model.getDataDefinitionId());
		soapModel.setName(model.getName());
		soapModel.setDescription(model.getDescription());
		soapModel.setAvailableColumns(model.getAvailableColumns());
		soapModel.setSortColumns(model.getSortColumns());

		return soapModel;
	}

	public static ReportDefinitionSoap[] toSoapModels(
		ReportDefinition[] models) {

		ReportDefinitionSoap[] soapModels =
			new ReportDefinitionSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static ReportDefinitionSoap[][] toSoapModels(
		ReportDefinition[][] models) {

		ReportDefinitionSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels =
				new ReportDefinitionSoap[models.length][models[0].length];
		}
		else {
			soapModels = new ReportDefinitionSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static ReportDefinitionSoap[] toSoapModels(
		List<ReportDefinition> models) {

		List<ReportDefinitionSoap> soapModels =
			new ArrayList<ReportDefinitionSoap>(models.size());

		for (ReportDefinition model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new ReportDefinitionSoap[soapModels.size()]);
	}

	public ReportDefinitionSoap() {
	}

	public long getPrimaryKey() {
		return _reportDefinitionId;
	}

	public void setPrimaryKey(long pk) {
		setReportDefinitionId(pk);
	}

	public String getUuid() {
		return _uuid;
	}

	public void setUuid(String uuid) {
		_uuid = uuid;
	}

	public long getReportDefinitionId() {
		return _reportDefinitionId;
	}

	public void setReportDefinitionId(long reportDefinitionId) {
		_reportDefinitionId = reportDefinitionId;
	}

	public long getGroupId() {
		return _groupId;
	}

	public void setGroupId(long groupId) {
		_groupId = groupId;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	public long getUserId() {
		return _userId;
	}

	public void setUserId(long userId) {
		_userId = userId;
	}

	public String getUserName() {
		return _userName;
	}

	public void setUserName(String userName) {
		_userName = userName;
	}

	public Date getCreateDate() {
		return _createDate;
	}

	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		_modifiedDate = modifiedDate;
	}

	public long getDataDefinitionId() {
		return _dataDefinitionId;
	}

	public void setDataDefinitionId(long dataDefinitionId) {
		_dataDefinitionId = dataDefinitionId;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public String getAvailableColumns() {
		return _availableColumns;
	}

	public void setAvailableColumns(String availableColumns) {
		_availableColumns = availableColumns;
	}

	public String getSortColumns() {
		return _sortColumns;
	}

	public void setSortColumns(String sortColumns) {
		_sortColumns = sortColumns;
	}

	private String _uuid;
	private long _reportDefinitionId;
	private long _groupId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private long _dataDefinitionId;
	private String _name;
	private String _description;
	private String _availableColumns;
	private String _sortColumns;

}