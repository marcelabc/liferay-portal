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

package com.liferay.report.definitions.service;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link ReportDefinitionLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see ReportDefinitionLocalService
 * @generated
 */
@ProviderType
public class ReportDefinitionLocalServiceWrapper
	implements ReportDefinitionLocalService,
			   ServiceWrapper<ReportDefinitionLocalService> {

	public ReportDefinitionLocalServiceWrapper(
		ReportDefinitionLocalService reportDefinitionLocalService) {

		_reportDefinitionLocalService = reportDefinitionLocalService;
	}

	@Override
	public com.liferay.report.definitions.model.ReportDefinition
			addReportDefinition(
				long userId, long groupId, long dataDefinitionId, String name,
				String description, String availableColumns, String sortColumns,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _reportDefinitionLocalService.addReportDefinition(
			userId, groupId, dataDefinitionId, name, description,
			availableColumns, sortColumns, serviceContext);
	}

	/**
	 * Adds the report definition to the database. Also notifies the appropriate model listeners.
	 *
	 * @param reportDefinition the report definition
	 * @return the report definition that was added
	 */
	@Override
	public com.liferay.report.definitions.model.ReportDefinition
		addReportDefinition(
			com.liferay.report.definitions.model.ReportDefinition
				reportDefinition) {

		return _reportDefinitionLocalService.addReportDefinition(
			reportDefinition);
	}

	/**
	 * Creates a new report definition with the primary key. Does not add the report definition to the database.
	 *
	 * @param reportDefinitionId the primary key for the new report definition
	 * @return the new report definition
	 */
	@Override
	public com.liferay.report.definitions.model.ReportDefinition
		createReportDefinition(long reportDefinitionId) {

		return _reportDefinitionLocalService.createReportDefinition(
			reportDefinitionId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _reportDefinitionLocalService.deletePersistedModel(
			persistedModel);
	}

	/**
	 * Deletes the report definition with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param reportDefinitionId the primary key of the report definition
	 * @return the report definition that was removed
	 * @throws PortalException if a report definition with the primary key could not be found
	 */
	@Override
	public com.liferay.report.definitions.model.ReportDefinition
			deleteReportDefinition(long reportDefinitionId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _reportDefinitionLocalService.deleteReportDefinition(
			reportDefinitionId);
	}

	/**
	 * Deletes the report definition from the database. Also notifies the appropriate model listeners.
	 *
	 * @param reportDefinition the report definition
	 * @return the report definition that was removed
	 */
	@Override
	public com.liferay.report.definitions.model.ReportDefinition
		deleteReportDefinition(
			com.liferay.report.definitions.model.ReportDefinition
				reportDefinition) {

		return _reportDefinitionLocalService.deleteReportDefinition(
			reportDefinition);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _reportDefinitionLocalService.dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _reportDefinitionLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.report.definitions.model.impl.ReportDefinitionModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return _reportDefinitionLocalService.dynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.report.definitions.model.impl.ReportDefinitionModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return _reportDefinitionLocalService.dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _reportDefinitionLocalService.dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return _reportDefinitionLocalService.dynamicQueryCount(
			dynamicQuery, projection);
	}

	@Override
	public com.liferay.report.definitions.model.ReportDefinition
		fetchReportDefinition(long reportDefinitionId) {

		return _reportDefinitionLocalService.fetchReportDefinition(
			reportDefinitionId);
	}

	/**
	 * Returns the report definition matching the UUID and group.
	 *
	 * @param uuid the report definition's UUID
	 * @param groupId the primary key of the group
	 * @return the matching report definition, or <code>null</code> if a matching report definition could not be found
	 */
	@Override
	public com.liferay.report.definitions.model.ReportDefinition
		fetchReportDefinitionByUuidAndGroupId(String uuid, long groupId) {

		return _reportDefinitionLocalService.
			fetchReportDefinitionByUuidAndGroupId(uuid, groupId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _reportDefinitionLocalService.getActionableDynamicQuery();
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery
		getExportActionableDynamicQuery(
			com.liferay.exportimport.kernel.lar.PortletDataContext
				portletDataContext) {

		return _reportDefinitionLocalService.getExportActionableDynamicQuery(
			portletDataContext);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _reportDefinitionLocalService.
			getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _reportDefinitionLocalService.getOSGiServiceIdentifier();
	}

	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _reportDefinitionLocalService.getPersistedModel(primaryKeyObj);
	}

	/**
	 * Returns the report definition with the primary key.
	 *
	 * @param reportDefinitionId the primary key of the report definition
	 * @return the report definition
	 * @throws PortalException if a report definition with the primary key could not be found
	 */
	@Override
	public com.liferay.report.definitions.model.ReportDefinition
			getReportDefinition(long reportDefinitionId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _reportDefinitionLocalService.getReportDefinition(
			reportDefinitionId);
	}

	/**
	 * Returns the report definition matching the UUID and group.
	 *
	 * @param uuid the report definition's UUID
	 * @param groupId the primary key of the group
	 * @return the matching report definition
	 * @throws PortalException if a matching report definition could not be found
	 */
	@Override
	public com.liferay.report.definitions.model.ReportDefinition
			getReportDefinitionByUuidAndGroupId(String uuid, long groupId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _reportDefinitionLocalService.
			getReportDefinitionByUuidAndGroupId(uuid, groupId);
	}

	/**
	 * Returns a range of all the report definitions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.report.definitions.model.impl.ReportDefinitionModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of report definitions
	 * @param end the upper bound of the range of report definitions (not inclusive)
	 * @return the range of report definitions
	 */
	@Override
	public java.util.List<com.liferay.report.definitions.model.ReportDefinition>
		getReportDefinitions(int start, int end) {

		return _reportDefinitionLocalService.getReportDefinitions(start, end);
	}

	/**
	 * Returns all the report definitions matching the UUID and company.
	 *
	 * @param uuid the UUID of the report definitions
	 * @param companyId the primary key of the company
	 * @return the matching report definitions, or an empty list if no matches were found
	 */
	@Override
	public java.util.List<com.liferay.report.definitions.model.ReportDefinition>
		getReportDefinitionsByUuidAndCompanyId(String uuid, long companyId) {

		return _reportDefinitionLocalService.
			getReportDefinitionsByUuidAndCompanyId(uuid, companyId);
	}

	/**
	 * Returns a range of report definitions matching the UUID and company.
	 *
	 * @param uuid the UUID of the report definitions
	 * @param companyId the primary key of the company
	 * @param start the lower bound of the range of report definitions
	 * @param end the upper bound of the range of report definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the range of matching report definitions, or an empty list if no matches were found
	 */
	@Override
	public java.util.List<com.liferay.report.definitions.model.ReportDefinition>
		getReportDefinitionsByUuidAndCompanyId(
			String uuid, long companyId, int start, int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<com.liferay.report.definitions.model.ReportDefinition>
					orderByComparator) {

		return _reportDefinitionLocalService.
			getReportDefinitionsByUuidAndCompanyId(
				uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns the number of report definitions.
	 *
	 * @return the number of report definitions
	 */
	@Override
	public int getReportDefinitionsCount() {
		return _reportDefinitionLocalService.getReportDefinitionsCount();
	}

	@Override
	public java.util.List<com.liferay.report.definitions.model.ReportDefinition>
		search(
			long companyId, long groupId, String keywords, int start, int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<com.liferay.report.definitions.model.ReportDefinition>
					orderByComparator) {

		return _reportDefinitionLocalService.search(
			companyId, groupId, keywords, start, end, orderByComparator);
	}

	@Override
	public java.util.List<com.liferay.report.definitions.model.ReportDefinition>
		search(
			long companyId, long groupId, String[] names, String[] descriptions,
			boolean andOperator, int start, int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<com.liferay.report.definitions.model.ReportDefinition>
					orderByComparator) {

		return _reportDefinitionLocalService.search(
			companyId, groupId, names, descriptions, andOperator, start, end,
			orderByComparator);
	}

	@Override
	public int searchCount(long companyId, long groupId, String keywords) {
		return _reportDefinitionLocalService.searchCount(
			companyId, groupId, keywords);
	}

	@Override
	public int searchCount(
		long companyId, long groupId, String[] names, String[] descriptions,
		boolean andOperator) {

		return _reportDefinitionLocalService.searchCount(
			companyId, groupId, names, descriptions, andOperator);
	}

	@Override
	public com.liferay.report.definitions.model.ReportDefinition
			updateReportDefinition(
				long userId, long groupId, long reportDefinitionId,
				long dataDefinitionId, String name, String description,
				String availableColumns, String sortColumns,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _reportDefinitionLocalService.updateReportDefinition(
			userId, groupId, reportDefinitionId, dataDefinitionId, name,
			description, availableColumns, sortColumns, serviceContext);
	}

	/**
	 * Updates the report definition in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param reportDefinition the report definition
	 * @return the report definition that was updated
	 */
	@Override
	public com.liferay.report.definitions.model.ReportDefinition
		updateReportDefinition(
			com.liferay.report.definitions.model.ReportDefinition
				reportDefinition) {

		return _reportDefinitionLocalService.updateReportDefinition(
			reportDefinition);
	}

	@Override
	public ReportDefinitionLocalService getWrappedService() {
		return _reportDefinitionLocalService;
	}

	@Override
	public void setWrappedService(
		ReportDefinitionLocalService reportDefinitionLocalService) {

		_reportDefinitionLocalService = reportDefinitionLocalService;
	}

	private ReportDefinitionLocalService _reportDefinitionLocalService;

}