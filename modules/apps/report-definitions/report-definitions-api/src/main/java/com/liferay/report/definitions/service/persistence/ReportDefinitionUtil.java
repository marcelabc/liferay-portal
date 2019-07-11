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

package com.liferay.report.definitions.service.persistence;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.report.definitions.model.ReportDefinition;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The persistence utility for the report definition service. This utility wraps <code>com.liferay.report.definitions.service.persistence.impl.ReportDefinitionPersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ReportDefinitionPersistence
 * @generated
 */
@ProviderType
public class ReportDefinitionUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache()
	 */
	public static void clearCache() {
		getPersistence().clearCache();
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static void clearCache(ReportDefinition reportDefinition) {
		getPersistence().clearCache(reportDefinition);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#countWithDynamicQuery(DynamicQuery)
	 */
	public static long countWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#fetchByPrimaryKeys(Set)
	 */
	public static Map<Serializable, ReportDefinition> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<ReportDefinition> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {

		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<ReportDefinition> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<ReportDefinition> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<ReportDefinition> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static ReportDefinition update(ReportDefinition reportDefinition) {
		return getPersistence().update(reportDefinition);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static ReportDefinition update(
		ReportDefinition reportDefinition, ServiceContext serviceContext) {

		return getPersistence().update(reportDefinition, serviceContext);
	}

	/**
	 * Returns all the report definitions where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching report definitions
	 */
	public static List<ReportDefinition> findByUuid(String uuid) {
		return getPersistence().findByUuid(uuid);
	}

	/**
	 * Returns a range of all the report definitions where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>ReportDefinitionModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of report definitions
	 * @param end the upper bound of the range of report definitions (not inclusive)
	 * @return the range of matching report definitions
	 */
	public static List<ReportDefinition> findByUuid(
		String uuid, int start, int end) {

		return getPersistence().findByUuid(uuid, start, end);
	}

	/**
	 * Returns an ordered range of all the report definitions where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>ReportDefinitionModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of report definitions
	 * @param end the upper bound of the range of report definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching report definitions
	 */
	public static List<ReportDefinition> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<ReportDefinition> orderByComparator) {

		return getPersistence().findByUuid(uuid, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the report definitions where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>ReportDefinitionModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of report definitions
	 * @param end the upper bound of the range of report definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of matching report definitions
	 */
	public static List<ReportDefinition> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<ReportDefinition> orderByComparator,
		boolean retrieveFromCache) {

		return getPersistence().findByUuid(
			uuid, start, end, orderByComparator, retrieveFromCache);
	}

	/**
	 * Returns the first report definition in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching report definition
	 * @throws NoSuchReportDefinitionException if a matching report definition could not be found
	 */
	public static ReportDefinition findByUuid_First(
			String uuid, OrderByComparator<ReportDefinition> orderByComparator)
		throws com.liferay.report.definitions.exception.
			NoSuchReportDefinitionException {

		return getPersistence().findByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Returns the first report definition in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching report definition, or <code>null</code> if a matching report definition could not be found
	 */
	public static ReportDefinition fetchByUuid_First(
		String uuid, OrderByComparator<ReportDefinition> orderByComparator) {

		return getPersistence().fetchByUuid_First(uuid, orderByComparator);
	}

	/**
	 * Returns the last report definition in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching report definition
	 * @throws NoSuchReportDefinitionException if a matching report definition could not be found
	 */
	public static ReportDefinition findByUuid_Last(
			String uuid, OrderByComparator<ReportDefinition> orderByComparator)
		throws com.liferay.report.definitions.exception.
			NoSuchReportDefinitionException {

		return getPersistence().findByUuid_Last(uuid, orderByComparator);
	}

	/**
	 * Returns the last report definition in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching report definition, or <code>null</code> if a matching report definition could not be found
	 */
	public static ReportDefinition fetchByUuid_Last(
		String uuid, OrderByComparator<ReportDefinition> orderByComparator) {

		return getPersistence().fetchByUuid_Last(uuid, orderByComparator);
	}

	/**
	 * Returns the report definitions before and after the current report definition in the ordered set where uuid = &#63;.
	 *
	 * @param reportDefinitionId the primary key of the current report definition
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next report definition
	 * @throws NoSuchReportDefinitionException if a report definition with the primary key could not be found
	 */
	public static ReportDefinition[] findByUuid_PrevAndNext(
			long reportDefinitionId, String uuid,
			OrderByComparator<ReportDefinition> orderByComparator)
		throws com.liferay.report.definitions.exception.
			NoSuchReportDefinitionException {

		return getPersistence().findByUuid_PrevAndNext(
			reportDefinitionId, uuid, orderByComparator);
	}

	/**
	 * Removes all the report definitions where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	public static void removeByUuid(String uuid) {
		getPersistence().removeByUuid(uuid);
	}

	/**
	 * Returns the number of report definitions where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching report definitions
	 */
	public static int countByUuid(String uuid) {
		return getPersistence().countByUuid(uuid);
	}

	/**
	 * Returns the report definition where uuid = &#63; and groupId = &#63; or throws a <code>NoSuchReportDefinitionException</code> if it could not be found.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching report definition
	 * @throws NoSuchReportDefinitionException if a matching report definition could not be found
	 */
	public static ReportDefinition findByUUID_G(String uuid, long groupId)
		throws com.liferay.report.definitions.exception.
			NoSuchReportDefinitionException {

		return getPersistence().findByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the report definition where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching report definition, or <code>null</code> if a matching report definition could not be found
	 */
	public static ReportDefinition fetchByUUID_G(String uuid, long groupId) {
		return getPersistence().fetchByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the report definition where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the matching report definition, or <code>null</code> if a matching report definition could not be found
	 */
	public static ReportDefinition fetchByUUID_G(
		String uuid, long groupId, boolean retrieveFromCache) {

		return getPersistence().fetchByUUID_G(uuid, groupId, retrieveFromCache);
	}

	/**
	 * Removes the report definition where uuid = &#63; and groupId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the report definition that was removed
	 */
	public static ReportDefinition removeByUUID_G(String uuid, long groupId)
		throws com.liferay.report.definitions.exception.
			NoSuchReportDefinitionException {

		return getPersistence().removeByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the number of report definitions where uuid = &#63; and groupId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the number of matching report definitions
	 */
	public static int countByUUID_G(String uuid, long groupId) {
		return getPersistence().countByUUID_G(uuid, groupId);
	}

	/**
	 * Returns all the report definitions where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching report definitions
	 */
	public static List<ReportDefinition> findByUuid_C(
		String uuid, long companyId) {

		return getPersistence().findByUuid_C(uuid, companyId);
	}

	/**
	 * Returns a range of all the report definitions where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>ReportDefinitionModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of report definitions
	 * @param end the upper bound of the range of report definitions (not inclusive)
	 * @return the range of matching report definitions
	 */
	public static List<ReportDefinition> findByUuid_C(
		String uuid, long companyId, int start, int end) {

		return getPersistence().findByUuid_C(uuid, companyId, start, end);
	}

	/**
	 * Returns an ordered range of all the report definitions where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>ReportDefinitionModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of report definitions
	 * @param end the upper bound of the range of report definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching report definitions
	 */
	public static List<ReportDefinition> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<ReportDefinition> orderByComparator) {

		return getPersistence().findByUuid_C(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the report definitions where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>ReportDefinitionModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of report definitions
	 * @param end the upper bound of the range of report definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of matching report definitions
	 */
	public static List<ReportDefinition> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<ReportDefinition> orderByComparator,
		boolean retrieveFromCache) {

		return getPersistence().findByUuid_C(
			uuid, companyId, start, end, orderByComparator, retrieveFromCache);
	}

	/**
	 * Returns the first report definition in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching report definition
	 * @throws NoSuchReportDefinitionException if a matching report definition could not be found
	 */
	public static ReportDefinition findByUuid_C_First(
			String uuid, long companyId,
			OrderByComparator<ReportDefinition> orderByComparator)
		throws com.liferay.report.definitions.exception.
			NoSuchReportDefinitionException {

		return getPersistence().findByUuid_C_First(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the first report definition in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching report definition, or <code>null</code> if a matching report definition could not be found
	 */
	public static ReportDefinition fetchByUuid_C_First(
		String uuid, long companyId,
		OrderByComparator<ReportDefinition> orderByComparator) {

		return getPersistence().fetchByUuid_C_First(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the last report definition in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching report definition
	 * @throws NoSuchReportDefinitionException if a matching report definition could not be found
	 */
	public static ReportDefinition findByUuid_C_Last(
			String uuid, long companyId,
			OrderByComparator<ReportDefinition> orderByComparator)
		throws com.liferay.report.definitions.exception.
			NoSuchReportDefinitionException {

		return getPersistence().findByUuid_C_Last(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the last report definition in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching report definition, or <code>null</code> if a matching report definition could not be found
	 */
	public static ReportDefinition fetchByUuid_C_Last(
		String uuid, long companyId,
		OrderByComparator<ReportDefinition> orderByComparator) {

		return getPersistence().fetchByUuid_C_Last(
			uuid, companyId, orderByComparator);
	}

	/**
	 * Returns the report definitions before and after the current report definition in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param reportDefinitionId the primary key of the current report definition
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next report definition
	 * @throws NoSuchReportDefinitionException if a report definition with the primary key could not be found
	 */
	public static ReportDefinition[] findByUuid_C_PrevAndNext(
			long reportDefinitionId, String uuid, long companyId,
			OrderByComparator<ReportDefinition> orderByComparator)
		throws com.liferay.report.definitions.exception.
			NoSuchReportDefinitionException {

		return getPersistence().findByUuid_C_PrevAndNext(
			reportDefinitionId, uuid, companyId, orderByComparator);
	}

	/**
	 * Removes all the report definitions where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	public static void removeByUuid_C(String uuid, long companyId) {
		getPersistence().removeByUuid_C(uuid, companyId);
	}

	/**
	 * Returns the number of report definitions where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching report definitions
	 */
	public static int countByUuid_C(String uuid, long companyId) {
		return getPersistence().countByUuid_C(uuid, companyId);
	}

	/**
	 * Returns all the report definitions where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the matching report definitions
	 */
	public static List<ReportDefinition> findByGroupId(long groupId) {
		return getPersistence().findByGroupId(groupId);
	}

	/**
	 * Returns a range of all the report definitions where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>ReportDefinitionModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of report definitions
	 * @param end the upper bound of the range of report definitions (not inclusive)
	 * @return the range of matching report definitions
	 */
	public static List<ReportDefinition> findByGroupId(
		long groupId, int start, int end) {

		return getPersistence().findByGroupId(groupId, start, end);
	}

	/**
	 * Returns an ordered range of all the report definitions where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>ReportDefinitionModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of report definitions
	 * @param end the upper bound of the range of report definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching report definitions
	 */
	public static List<ReportDefinition> findByGroupId(
		long groupId, int start, int end,
		OrderByComparator<ReportDefinition> orderByComparator) {

		return getPersistence().findByGroupId(
			groupId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the report definitions where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>ReportDefinitionModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of report definitions
	 * @param end the upper bound of the range of report definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of matching report definitions
	 */
	public static List<ReportDefinition> findByGroupId(
		long groupId, int start, int end,
		OrderByComparator<ReportDefinition> orderByComparator,
		boolean retrieveFromCache) {

		return getPersistence().findByGroupId(
			groupId, start, end, orderByComparator, retrieveFromCache);
	}

	/**
	 * Returns the first report definition in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching report definition
	 * @throws NoSuchReportDefinitionException if a matching report definition could not be found
	 */
	public static ReportDefinition findByGroupId_First(
			long groupId, OrderByComparator<ReportDefinition> orderByComparator)
		throws com.liferay.report.definitions.exception.
			NoSuchReportDefinitionException {

		return getPersistence().findByGroupId_First(groupId, orderByComparator);
	}

	/**
	 * Returns the first report definition in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching report definition, or <code>null</code> if a matching report definition could not be found
	 */
	public static ReportDefinition fetchByGroupId_First(
		long groupId, OrderByComparator<ReportDefinition> orderByComparator) {

		return getPersistence().fetchByGroupId_First(
			groupId, orderByComparator);
	}

	/**
	 * Returns the last report definition in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching report definition
	 * @throws NoSuchReportDefinitionException if a matching report definition could not be found
	 */
	public static ReportDefinition findByGroupId_Last(
			long groupId, OrderByComparator<ReportDefinition> orderByComparator)
		throws com.liferay.report.definitions.exception.
			NoSuchReportDefinitionException {

		return getPersistence().findByGroupId_Last(groupId, orderByComparator);
	}

	/**
	 * Returns the last report definition in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching report definition, or <code>null</code> if a matching report definition could not be found
	 */
	public static ReportDefinition fetchByGroupId_Last(
		long groupId, OrderByComparator<ReportDefinition> orderByComparator) {

		return getPersistence().fetchByGroupId_Last(groupId, orderByComparator);
	}

	/**
	 * Returns the report definitions before and after the current report definition in the ordered set where groupId = &#63;.
	 *
	 * @param reportDefinitionId the primary key of the current report definition
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next report definition
	 * @throws NoSuchReportDefinitionException if a report definition with the primary key could not be found
	 */
	public static ReportDefinition[] findByGroupId_PrevAndNext(
			long reportDefinitionId, long groupId,
			OrderByComparator<ReportDefinition> orderByComparator)
		throws com.liferay.report.definitions.exception.
			NoSuchReportDefinitionException {

		return getPersistence().findByGroupId_PrevAndNext(
			reportDefinitionId, groupId, orderByComparator);
	}

	/**
	 * Removes all the report definitions where groupId = &#63; from the database.
	 *
	 * @param groupId the group ID
	 */
	public static void removeByGroupId(long groupId) {
		getPersistence().removeByGroupId(groupId);
	}

	/**
	 * Returns the number of report definitions where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the number of matching report definitions
	 */
	public static int countByGroupId(long groupId) {
		return getPersistence().countByGroupId(groupId);
	}

	/**
	 * Caches the report definition in the entity cache if it is enabled.
	 *
	 * @param reportDefinition the report definition
	 */
	public static void cacheResult(ReportDefinition reportDefinition) {
		getPersistence().cacheResult(reportDefinition);
	}

	/**
	 * Caches the report definitions in the entity cache if it is enabled.
	 *
	 * @param reportDefinitions the report definitions
	 */
	public static void cacheResult(List<ReportDefinition> reportDefinitions) {
		getPersistence().cacheResult(reportDefinitions);
	}

	/**
	 * Creates a new report definition with the primary key. Does not add the report definition to the database.
	 *
	 * @param reportDefinitionId the primary key for the new report definition
	 * @return the new report definition
	 */
	public static ReportDefinition create(long reportDefinitionId) {
		return getPersistence().create(reportDefinitionId);
	}

	/**
	 * Removes the report definition with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param reportDefinitionId the primary key of the report definition
	 * @return the report definition that was removed
	 * @throws NoSuchReportDefinitionException if a report definition with the primary key could not be found
	 */
	public static ReportDefinition remove(long reportDefinitionId)
		throws com.liferay.report.definitions.exception.
			NoSuchReportDefinitionException {

		return getPersistence().remove(reportDefinitionId);
	}

	public static ReportDefinition updateImpl(
		ReportDefinition reportDefinition) {

		return getPersistence().updateImpl(reportDefinition);
	}

	/**
	 * Returns the report definition with the primary key or throws a <code>NoSuchReportDefinitionException</code> if it could not be found.
	 *
	 * @param reportDefinitionId the primary key of the report definition
	 * @return the report definition
	 * @throws NoSuchReportDefinitionException if a report definition with the primary key could not be found
	 */
	public static ReportDefinition findByPrimaryKey(long reportDefinitionId)
		throws com.liferay.report.definitions.exception.
			NoSuchReportDefinitionException {

		return getPersistence().findByPrimaryKey(reportDefinitionId);
	}

	/**
	 * Returns the report definition with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param reportDefinitionId the primary key of the report definition
	 * @return the report definition, or <code>null</code> if a report definition with the primary key could not be found
	 */
	public static ReportDefinition fetchByPrimaryKey(long reportDefinitionId) {
		return getPersistence().fetchByPrimaryKey(reportDefinitionId);
	}

	/**
	 * Returns all the report definitions.
	 *
	 * @return the report definitions
	 */
	public static List<ReportDefinition> findAll() {
		return getPersistence().findAll();
	}

	/**
	 * Returns a range of all the report definitions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>ReportDefinitionModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of report definitions
	 * @param end the upper bound of the range of report definitions (not inclusive)
	 * @return the range of report definitions
	 */
	public static List<ReportDefinition> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	 * Returns an ordered range of all the report definitions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>ReportDefinitionModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of report definitions
	 * @param end the upper bound of the range of report definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of report definitions
	 */
	public static List<ReportDefinition> findAll(
		int start, int end,
		OrderByComparator<ReportDefinition> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the report definitions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>ReportDefinitionModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of report definitions
	 * @param end the upper bound of the range of report definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param retrieveFromCache whether to retrieve from the finder cache
	 * @return the ordered range of report definitions
	 */
	public static List<ReportDefinition> findAll(
		int start, int end,
		OrderByComparator<ReportDefinition> orderByComparator,
		boolean retrieveFromCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, retrieveFromCache);
	}

	/**
	 * Removes all the report definitions from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of report definitions.
	 *
	 * @return the number of report definitions
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static Set<String> getBadColumnNames() {
		return getPersistence().getBadColumnNames();
	}

	public static ReportDefinitionPersistence getPersistence() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<ReportDefinitionPersistence, ReportDefinitionPersistence>
			_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			ReportDefinitionPersistence.class);

		ServiceTracker<ReportDefinitionPersistence, ReportDefinitionPersistence>
			serviceTracker =
				new ServiceTracker
					<ReportDefinitionPersistence, ReportDefinitionPersistence>(
						bundle.getBundleContext(),
						ReportDefinitionPersistence.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}