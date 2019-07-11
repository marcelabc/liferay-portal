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

package com.liferay.report.definitions.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.report.definitions.model.ReportDefinition;
import com.liferay.report.definitions.service.base.ReportDefinitionLocalServiceBaseImpl;

import java.util.List;

/**
 * The implementation of the report definition local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>com.liferay.report.definitions.service.ReportDefinitionLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ReportDefinitionLocalServiceBaseImpl
 */
public class ReportDefinitionLocalServiceImpl
	extends ReportDefinitionLocalServiceBaseImpl {

	@Override
	public ReportDefinition addReportDefinition(
			long userId, long groupId, long dataDefinitionId, String name,
			String description, String availableColumns, String sortColumns,
			ServiceContext serviceContext)
		throws PortalException {

		User user = userLocalService.getUser(userId);

		long reportDefinitionId = counterLocalService.increment();

		ReportDefinition reportDefinition = reportDefinitionPersistence.create(
			reportDefinitionId);

		reportDefinition.setUuid(serviceContext.getUuid());
		reportDefinition.setGroupId(groupId);
		reportDefinition.setCompanyId(user.getCompanyId());
		reportDefinition.setUserId(user.getUserId());
		reportDefinition.setUserName(user.getFullName());
		reportDefinition.setDataDefinitionId(dataDefinitionId);
		reportDefinition.setName(name);
		reportDefinition.setDescription(description);
		reportDefinition.setAvailableColumns(availableColumns);
		reportDefinition.setSortColumns(sortColumns);

		reportDefinitionPersistence.update(reportDefinition);

		return reportDefinition;
	}

	@Override
	public List<ReportDefinition> search(
		long companyId, long groupId, String keywords, int start, int end,
		OrderByComparator<ReportDefinition> orderByComparator) {

		return reportDefinitionFinder.findByKeywords(
			companyId, groupId, keywords, start, end, orderByComparator);
	}

	@Override
	public List<ReportDefinition> search(
		long companyId, long groupId, String[] names, String[] descriptions,
		boolean andOperator, int start, int end,
		OrderByComparator<ReportDefinition> orderByComparator) {

		return reportDefinitionFinder.findByC_G_N_D(
			companyId, groupId, names, descriptions, andOperator, start, end,
			orderByComparator);
	}

	@Override
	public int searchCount(long companyId, long groupId, String keywords) {
		return reportDefinitionFinder.countByKeywords(
			companyId, groupId, keywords);
	}

	@Override
	public int searchCount(
		long companyId, long groupId, String[] names, String[] descriptions,
		boolean andOperator) {

		return reportDefinitionFinder.countByC_G_N_D(
			companyId, groupId, names, descriptions, andOperator);
	}

	public ReportDefinition updateReportDefinition(
			long userId, long groupId, long reportDefinitionId,
			long dataDefinitionId, String name, String description,
			String availableColumns, String sortColumns,
			ServiceContext serviceContext)
		throws PortalException {

		User user = userLocalService.getUser(userId);

		ReportDefinition reportDefinition =
			reportDefinitionPersistence.fetchByPrimaryKey(reportDefinitionId);

		reportDefinition.setUuid(serviceContext.getUuid());
		reportDefinition.setGroupId(groupId);
		reportDefinition.setCompanyId(user.getCompanyId());
		reportDefinition.setDataDefinitionId(dataDefinitionId);
		reportDefinition.setName(name);
		reportDefinition.setDescription(description);
		reportDefinition.setAvailableColumns(availableColumns);
		reportDefinition.setSortColumns(sortColumns);

		reportDefinitionPersistence.update(reportDefinition);

		return reportDefinition;
	}

}