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

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * This class is a wrapper for {@link ReportDefinition}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ReportDefinition
 * @generated
 */
@ProviderType
public class ReportDefinitionWrapper
	implements ReportDefinition, ModelWrapper<ReportDefinition> {

	public ReportDefinitionWrapper(ReportDefinition reportDefinition) {
		_reportDefinition = reportDefinition;
	}

	@Override
	public Class<?> getModelClass() {
		return ReportDefinition.class;
	}

	@Override
	public String getModelClassName() {
		return ReportDefinition.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("uuid", getUuid());
		attributes.put("reportDefinitionId", getReportDefinitionId());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("dataDefinitionId", getDataDefinitionId());
		attributes.put("name", getName());
		attributes.put("description", getDescription());
		attributes.put("availableColumns", getAvailableColumns());
		attributes.put("sortColumns", getSortColumns());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		String uuid = (String)attributes.get("uuid");

		if (uuid != null) {
			setUuid(uuid);
		}

		Long reportDefinitionId = (Long)attributes.get("reportDefinitionId");

		if (reportDefinitionId != null) {
			setReportDefinitionId(reportDefinitionId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		String userName = (String)attributes.get("userName");

		if (userName != null) {
			setUserName(userName);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		Long dataDefinitionId = (Long)attributes.get("dataDefinitionId");

		if (dataDefinitionId != null) {
			setDataDefinitionId(dataDefinitionId);
		}

		String name = (String)attributes.get("name");

		if (name != null) {
			setName(name);
		}

		String description = (String)attributes.get("description");

		if (description != null) {
			setDescription(description);
		}

		String availableColumns = (String)attributes.get("availableColumns");

		if (availableColumns != null) {
			setAvailableColumns(availableColumns);
		}

		String sortColumns = (String)attributes.get("sortColumns");

		if (sortColumns != null) {
			setSortColumns(sortColumns);
		}
	}

	@Override
	public Object clone() {
		return new ReportDefinitionWrapper(
			(ReportDefinition)_reportDefinition.clone());
	}

	@Override
	public int compareTo(ReportDefinition reportDefinition) {
		return _reportDefinition.compareTo(reportDefinition);
	}

	/**
	 * Returns the available columns of this report definition.
	 *
	 * @return the available columns of this report definition
	 */
	@Override
	public String getAvailableColumns() {
		return _reportDefinition.getAvailableColumns();
	}

	/**
	 * Returns the company ID of this report definition.
	 *
	 * @return the company ID of this report definition
	 */
	@Override
	public long getCompanyId() {
		return _reportDefinition.getCompanyId();
	}

	/**
	 * Returns the create date of this report definition.
	 *
	 * @return the create date of this report definition
	 */
	@Override
	public Date getCreateDate() {
		return _reportDefinition.getCreateDate();
	}

	/**
	 * Returns the data definition ID of this report definition.
	 *
	 * @return the data definition ID of this report definition
	 */
	@Override
	public long getDataDefinitionId() {
		return _reportDefinition.getDataDefinitionId();
	}

	/**
	 * Returns the description of this report definition.
	 *
	 * @return the description of this report definition
	 */
	@Override
	public String getDescription() {
		return _reportDefinition.getDescription();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _reportDefinition.getExpandoBridge();
	}

	/**
	 * Returns the group ID of this report definition.
	 *
	 * @return the group ID of this report definition
	 */
	@Override
	public long getGroupId() {
		return _reportDefinition.getGroupId();
	}

	/**
	 * Returns the modified date of this report definition.
	 *
	 * @return the modified date of this report definition
	 */
	@Override
	public Date getModifiedDate() {
		return _reportDefinition.getModifiedDate();
	}

	/**
	 * Returns the name of this report definition.
	 *
	 * @return the name of this report definition
	 */
	@Override
	public String getName() {
		return _reportDefinition.getName();
	}

	/**
	 * Returns the primary key of this report definition.
	 *
	 * @return the primary key of this report definition
	 */
	@Override
	public long getPrimaryKey() {
		return _reportDefinition.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _reportDefinition.getPrimaryKeyObj();
	}

	/**
	 * Returns the report definition ID of this report definition.
	 *
	 * @return the report definition ID of this report definition
	 */
	@Override
	public long getReportDefinitionId() {
		return _reportDefinition.getReportDefinitionId();
	}

	/**
	 * Returns the sort columns of this report definition.
	 *
	 * @return the sort columns of this report definition
	 */
	@Override
	public String getSortColumns() {
		return _reportDefinition.getSortColumns();
	}

	/**
	 * Returns the user ID of this report definition.
	 *
	 * @return the user ID of this report definition
	 */
	@Override
	public long getUserId() {
		return _reportDefinition.getUserId();
	}

	/**
	 * Returns the user name of this report definition.
	 *
	 * @return the user name of this report definition
	 */
	@Override
	public String getUserName() {
		return _reportDefinition.getUserName();
	}

	/**
	 * Returns the user uuid of this report definition.
	 *
	 * @return the user uuid of this report definition
	 */
	@Override
	public String getUserUuid() {
		return _reportDefinition.getUserUuid();
	}

	/**
	 * Returns the uuid of this report definition.
	 *
	 * @return the uuid of this report definition
	 */
	@Override
	public String getUuid() {
		return _reportDefinition.getUuid();
	}

	@Override
	public int hashCode() {
		return _reportDefinition.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _reportDefinition.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _reportDefinition.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _reportDefinition.isNew();
	}

	@Override
	public void persist() {
		_reportDefinition.persist();
	}

	/**
	 * Sets the available columns of this report definition.
	 *
	 * @param availableColumns the available columns of this report definition
	 */
	@Override
	public void setAvailableColumns(String availableColumns) {
		_reportDefinition.setAvailableColumns(availableColumns);
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_reportDefinition.setCachedModel(cachedModel);
	}

	/**
	 * Sets the company ID of this report definition.
	 *
	 * @param companyId the company ID of this report definition
	 */
	@Override
	public void setCompanyId(long companyId) {
		_reportDefinition.setCompanyId(companyId);
	}

	/**
	 * Sets the create date of this report definition.
	 *
	 * @param createDate the create date of this report definition
	 */
	@Override
	public void setCreateDate(Date createDate) {
		_reportDefinition.setCreateDate(createDate);
	}

	/**
	 * Sets the data definition ID of this report definition.
	 *
	 * @param dataDefinitionId the data definition ID of this report definition
	 */
	@Override
	public void setDataDefinitionId(long dataDefinitionId) {
		_reportDefinition.setDataDefinitionId(dataDefinitionId);
	}

	/**
	 * Sets the description of this report definition.
	 *
	 * @param description the description of this report definition
	 */
	@Override
	public void setDescription(String description) {
		_reportDefinition.setDescription(description);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {

		_reportDefinition.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_reportDefinition.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_reportDefinition.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	 * Sets the group ID of this report definition.
	 *
	 * @param groupId the group ID of this report definition
	 */
	@Override
	public void setGroupId(long groupId) {
		_reportDefinition.setGroupId(groupId);
	}

	/**
	 * Sets the modified date of this report definition.
	 *
	 * @param modifiedDate the modified date of this report definition
	 */
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_reportDefinition.setModifiedDate(modifiedDate);
	}

	/**
	 * Sets the name of this report definition.
	 *
	 * @param name the name of this report definition
	 */
	@Override
	public void setName(String name) {
		_reportDefinition.setName(name);
	}

	@Override
	public void setNew(boolean n) {
		_reportDefinition.setNew(n);
	}

	/**
	 * Sets the primary key of this report definition.
	 *
	 * @param primaryKey the primary key of this report definition
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		_reportDefinition.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_reportDefinition.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	 * Sets the report definition ID of this report definition.
	 *
	 * @param reportDefinitionId the report definition ID of this report definition
	 */
	@Override
	public void setReportDefinitionId(long reportDefinitionId) {
		_reportDefinition.setReportDefinitionId(reportDefinitionId);
	}

	/**
	 * Sets the sort columns of this report definition.
	 *
	 * @param sortColumns the sort columns of this report definition
	 */
	@Override
	public void setSortColumns(String sortColumns) {
		_reportDefinition.setSortColumns(sortColumns);
	}

	/**
	 * Sets the user ID of this report definition.
	 *
	 * @param userId the user ID of this report definition
	 */
	@Override
	public void setUserId(long userId) {
		_reportDefinition.setUserId(userId);
	}

	/**
	 * Sets the user name of this report definition.
	 *
	 * @param userName the user name of this report definition
	 */
	@Override
	public void setUserName(String userName) {
		_reportDefinition.setUserName(userName);
	}

	/**
	 * Sets the user uuid of this report definition.
	 *
	 * @param userUuid the user uuid of this report definition
	 */
	@Override
	public void setUserUuid(String userUuid) {
		_reportDefinition.setUserUuid(userUuid);
	}

	/**
	 * Sets the uuid of this report definition.
	 *
	 * @param uuid the uuid of this report definition
	 */
	@Override
	public void setUuid(String uuid) {
		_reportDefinition.setUuid(uuid);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<ReportDefinition>
		toCacheModel() {

		return _reportDefinition.toCacheModel();
	}

	@Override
	public ReportDefinition toEscapedModel() {
		return new ReportDefinitionWrapper(_reportDefinition.toEscapedModel());
	}

	@Override
	public String toString() {
		return _reportDefinition.toString();
	}

	@Override
	public ReportDefinition toUnescapedModel() {
		return new ReportDefinitionWrapper(
			_reportDefinition.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _reportDefinition.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof ReportDefinitionWrapper)) {
			return false;
		}

		ReportDefinitionWrapper reportDefinitionWrapper =
			(ReportDefinitionWrapper)obj;

		if (Objects.equals(
				_reportDefinition, reportDefinitionWrapper._reportDefinition)) {

			return true;
		}

		return false;
	}

	@Override
	public StagedModelType getStagedModelType() {
		return _reportDefinition.getStagedModelType();
	}

	@Override
	public ReportDefinition getWrappedModel() {
		return _reportDefinition;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _reportDefinition.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _reportDefinition.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_reportDefinition.resetOriginalValues();
	}

	private final ReportDefinition _reportDefinition;

}