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

package com.liferay.data.engine.internal.upgrade.v1_1_1;

import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.jdbc.AutoBatchPreparedStatementUtil;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Marcela Cunha
 */
public class UpgradeDDMStructure extends UpgradeProcess {

	public UpgradeDDMStructure(CounterLocalService counterLocalService) {
		_counterLocalService = counterLocalService;
	}

	protected String adaptDefinition(String definition) throws Exception {
		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(definition);

		jsonObject.remove("successPage");

		jsonObject.put("rules", JSONFactoryUtil.createJSONArray());

		return jsonObject.toString();
	}

	protected void addDDMStructure(
			ResultSet resultSet, long newDDMStructureId,
			long newParentStructureId)
		throws Exception {

		StringBundler sb = new StringBundler(6);

		sb.append("insert into DDMStructure (uuid_, structureId, groupId, ");
		sb.append("companyId, userId, userName, versionUserId, ");
		sb.append("versionUserName, createDate, modifiedDate, ");
		sb.append("parentStructureId, classNameId, structureKey, version, ");
		sb.append("name, description, definition, storageType, type_) values ");
		sb.append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		String sql = sb.toString();

		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, PortalUUIDUtil.generate());
			ps.setLong(2, newDDMStructureId);
			ps.setLong(3, resultSet.getLong("groupId"));
			ps.setLong(4, resultSet.getLong("companyId"));
			ps.setLong(5, resultSet.getLong("userId"));
			ps.setString(6, resultSet.getString("userName"));
			ps.setLong(7, resultSet.getLong("versionUserId"));
			ps.setString(8, resultSet.getString("versionUserName"));
			ps.setTimestamp(9, resultSet.getTimestamp("createDate"));
			ps.setTimestamp(10, resultSet.getTimestamp("modifiedDate"));
			ps.setLong(11, newParentStructureId);
			ps.setLong(12, _DATA_DEFINITION_INTERNAL_NAME_ID);
			ps.setString(13, String.valueOf(_counterLocalService.increment()));
			ps.setString(14, resultSet.getString("version"));
			ps.setString(15, resultSet.getString("name"));
			ps.setString(16, resultSet.getString("description"));
			ps.setString(
				17, adaptDefinition(resultSet.getString("definition")));
			ps.setString(18, resultSet.getString("storageType"));
			ps.setInt(19, resultSet.getInt("type_"));

			ps.executeUpdate();

			_ddmStructureIds.put(
				resultSet.getLong("structureId"), newDDMStructureId);

			addDDMStructureLink(
				resultSet.getLong("companyId"), newDDMStructureId);
		}
	}

	protected void addDDMStructureLink(long companyId, long newDDMStructureId)
		throws Exception {

		StringBundler sb = new StringBundler(2);

		sb.append("insert into DDMStructureLink (structureLinkId, companyId, ");
		sb.append("classNameId, structureId) values (?, ?, ?, ?) ");

		String sql = sb.toString();

		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setLong(1, _counterLocalService.increment());
			ps.setLong(2, companyId);
			ps.setLong(3, _JOURNAL_ARTICLE_NAME_ID);
			ps.setLong(4, newDDMStructureId);

			ps.executeUpdate();
		}
	}

	@Override
	protected void doUpgrade() throws Exception {
		
	}

	private static final Long _DATA_DEFINITION_INTERNAL_NAME_ID =
		PortalUtil.getClassNameId(
			"com.liferay.data.engine.rest.internal.model." +
				"InternalDataDefinition");

	private static final Long _JOURNAL_ARTICLE_NAME_ID =
		PortalUtil.getClassNameId("com.liferay.journal.model.JournalArticle");

	private final CounterLocalService _counterLocalService;
	private final Map<Long, Long> _ddmStructureIds = new HashMap<>();

}