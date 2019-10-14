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
public class UpgradeJournalStructuresToDataEngine extends UpgradeProcess {

	public UpgradeJournalStructuresToDataEngine(
		CounterLocalService counterLocalService) {

		_counterLocalService = counterLocalService;
	}

	protected String adaptDefinition(String definition) throws Exception {
		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(definition);

		jsonObject.put("rules", JSONFactoryUtil.createJSONArray());

		jsonObject.remove("successPage");

		return jsonObject.toString();
	}

	protected void addDDMStructure(
			long newDDMStructureId, long newParentStructureId,
			ResultSet resultSet)
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

	protected void addDDMStructureChildren(
			long newParentStructureId, long parentStructureId)
		throws Exception {

		try (PreparedStatement ps = connection.prepareStatement(
				"select * from DDMStructure where parentStructureId = ?")) {

			ps.setLong(1, parentStructureId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					long ddmStructureId = rs.getLong("structureId");

					long newDDMStructureId = _counterLocalService.increment();

					addDDMStructure(
						newDDMStructureId, newParentStructureId, rs);

					addDDMStructureChildren(newDDMStructureId, ddmStructureId);
				}
			}
		}
	}

	protected void addDDMStructureLayout(
			long ddmStructureVersionId, long newDDMStructureVersionId)
		throws Exception {

		String sql1 =
			"select * from DDMStructureLayout where structureVersionId = ?";

		StringBundler sb = new StringBundler(5);

		sb.append("insert into DDMStructureLayout (uuid_, structureLayoutId, ");
		sb.append("groupId, companyId, userId, userName, createDate, ");
		sb.append("modifiedDate, classNameId, structureLayoutKey, ");
		sb.append("structureVersionId, name, description, definition) values ");
		sb.append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		String sql2 = sb.toString();

		try (PreparedStatement ps1 = connection.prepareStatement(sql1);
			PreparedStatement ps2 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection, sql2)) {

			ps1.setLong(1, ddmStructureVersionId);

			try (ResultSet rs = ps1.executeQuery()) {
				while (rs.next()) {
					ps2.setString(1, PortalUUIDUtil.generate());
					ps2.setLong(2, _counterLocalService.increment());
					ps2.setLong(3, rs.getLong("groupId"));
					ps2.setLong(4, rs.getLong("companyId"));
					ps2.setLong(5, rs.getLong("userId"));
					ps2.setString(6, rs.getString("userName"));
					ps2.setTimestamp(7, rs.getTimestamp("createDate"));
					ps2.setTimestamp(8, rs.getTimestamp("modifiedDate"));
					ps2.setLong(9, rs.getLong("classNameId"));
					ps2.setString(
						10, String.valueOf(_counterLocalService.increment()));
					ps2.setLong(11, newDDMStructureVersionId);
					ps2.setString(12, rs.getString("name"));
					ps2.setString(13, rs.getString("description"));
					ps2.setString(14, rs.getString("definition"));

					ps2.addBatch();
				}

				ps2.executeBatch();
			}
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

	protected void addDDMStructureVersion() throws Exception {
		StringBundler sb = new StringBundler(6);

		sb.append("insert into DDMStructureVersion (structureVersionId, ");
		sb.append("groupId, companyId, userId, userName, createDate, ");
		sb.append("structureId, version, parentStructureId, name, ");
		sb.append("description, definition, storageType, type_, status, ");
		sb.append("statusByUserId, statusByUserName, statusDate) values (?, ");
		sb.append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		String sql = sb.toString();

		for (Long ddmStructureId : _ddmStructureIds.keySet()) {
			try (PreparedStatement ps1 = connection.prepareStatement(
					"select * from DDMStructureVersion where structureId = ?");
				PreparedStatement ps2 =
					AutoBatchPreparedStatementUtil.concurrentAutoBatch(
						connection, sql)) {

				ps1.setLong(1, ddmStructureId);

				try (ResultSet rs = ps1.executeQuery()) {
					while (rs.next()) {
						long newDDMStructureVersionId =
							_counterLocalService.increment();

						ps2.setLong(1, newDDMStructureVersionId);

						ps2.setLong(2, rs.getLong("groupId"));
						ps2.setLong(3, rs.getLong("companyId"));
						ps2.setLong(4, rs.getLong("userId"));
						ps2.setString(5, rs.getString("userName"));
						ps2.setTimestamp(6, rs.getTimestamp("createDate"));
						ps2.setLong(
							7,
							_ddmStructureIds.getOrDefault(
								ddmStructureId, ddmStructureId));
						ps2.setString(8, rs.getString("version"));
						ps2.setLong(
							9,
							_ddmStructureIds.getOrDefault(
								rs.getLong("parentStructureId"),
								rs.getLong("parentStructureId")));
						ps2.setString(10, rs.getString("name"));
						ps2.setString(11, rs.getString("description"));
						ps2.setString(
							12, adaptDefinition(rs.getString("definition")));
						ps2.setString(13, rs.getString("storageType"));
						ps2.setInt(14, rs.getInt("type_"));
						ps2.setInt(15, rs.getInt("status"));
						ps2.setLong(16, rs.getLong("statusByUserId"));
						ps2.setString(17, rs.getString("statusByUserName"));
						ps2.setTimestamp(18, rs.getTimestamp("statusDate"));

						ps2.addBatch();

						addDDMStructureLayout(
							rs.getLong("structureVersionId"),
							newDDMStructureVersionId);
					}

					ps2.executeBatch();
				}
			}
		}
	}

	@Override
	protected void doUpgrade() throws Exception {
		StringBundler sb = new StringBundler(2);

		sb.append("select * from DDMStructure where classNameId = ? and ");
		sb.append("parentStructureId = 0");

		String sql = sb.toString();

		try (PreparedStatement ps1 = connection.prepareStatement(sql)) {
			ps1.setLong(1, _JOURNAL_ARTICLE_NAME_ID);

			try (ResultSet rs = ps1.executeQuery()) {
				while (rs.next()) {
					long ddmStructureId = rs.getLong("structureId");

					long newDDMStructureId = _counterLocalService.increment();

					addDDMStructure(newDDMStructureId, 0, rs);

					addDDMStructureChildren(newDDMStructureId, ddmStructureId);
				}
			}
		}

		addDDMStructureVersion();
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