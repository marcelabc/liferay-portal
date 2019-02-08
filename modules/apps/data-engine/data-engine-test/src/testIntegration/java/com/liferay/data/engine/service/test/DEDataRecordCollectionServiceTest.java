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

package com.liferay.data.engine.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.data.engine.constants.DEActionKeys;
import com.liferay.data.engine.exception.DEDataRecordCollectionException;
import com.liferay.data.engine.model.DEDataDefinition;
import com.liferay.data.engine.model.DEDataRecord;
import com.liferay.data.engine.model.DEDataRecordCollection;
import com.liferay.data.engine.service.DEDataDefinitionService;
import com.liferay.data.engine.service.DEDataRecordCollectionDeleteRequest;
import com.liferay.data.engine.service.DEDataRecordCollectionGetRecordRequest;
import com.liferay.data.engine.service.DEDataRecordCollectionRequestBuilder;
import com.liferay.data.engine.service.DEDataRecordCollectionSaveModelPermissionsRequest;
import com.liferay.data.engine.service.DEDataRecordCollectionSavePermissionsRequest;
import com.liferay.data.engine.service.DEDataRecordCollectionSaveRequest;
import com.liferay.data.engine.service.DEDataRecordCollectionSaveResponse;
import com.liferay.data.engine.service.DEDataRecordCollectionService;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.service.permission.ModelPermissions;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RoleTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Leonardo Barros
 */
@RunWith(Arquillian.class)
public class DEDataRecordCollectionServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		setUpPermissionThreadLocal();

		_group = GroupTestUtil.addGroup();

		_siteMember = UserTestUtil.addGroupUser(
			_group, RoleConstants.SITE_MEMBER);

		_adminUser = UserTestUtil.addOmniAdminUser();
	}

	@Test
	public void testAddDataRecordCollectionPermission() throws Exception {
		Role role = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);

		User user = UserTestUtil.addGroupUser(_group, role.getName());

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _adminUser.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollectionSavePermissionsRequest
				deDataRecordCollectionSavePermissionsRequest =
					DEDataRecordCollectionRequestBuilder.savePermissionsBuilder(
						TestPropsValues.getCompanyId(), _adminUser.getGroupId(),
						new String[] {role.getName()}
					).allowAddDataRecordCollection(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSavePermissionsRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}

		DEDataDefinition deDataDefinition =
			DEDataEngineTestUtil.insertDEDataDefinition(
				_adminUser, _group, _deDataDefinitionService);

		DEDataRecordCollection deDataRecordCollection =
			DEDataEngineTestUtil.insertDEDataRecordCollection(
				user, _group, deDataDefinition, _deDataRecordCollectionService);

		Assert.assertTrue(
			deDataRecordCollection.getDEDataRecordCollectionId() > 0);
	}

	@Test(expected = DEDataRecordCollectionException.MustHavePermission.class)
	public void testAddDataRecordCollectionPermissionByGuestUser()
		throws Exception {

		User user = UserTestUtil.addGroupUser(_group, RoleConstants.GUEST);

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(user));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), user.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollectionSavePermissionsRequest
				deDataRecordCollectionSavePermissionsRequest =
					DEDataRecordCollectionRequestBuilder.savePermissionsBuilder(
						TestPropsValues.getCompanyId(), _group.getGroupId(),
						new String[] {RoleConstants.ORGANIZATION_USER}
					).allowAddDataRecordCollection(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSavePermissionsRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}
	}

	@Test(expected = DEDataRecordCollectionException.MustHavePermission.class)
	public void testAddDataRecordCollectionPermissionBySiteMember()
		throws Exception {

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_siteMember));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _siteMember.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollectionSavePermissionsRequest
				deDataRecordCollectionSavePermissionsRequest =
					DEDataRecordCollectionRequestBuilder.savePermissionsBuilder(
						TestPropsValues.getCompanyId(), _group.getGroupId(),
						new String[] {RoleConstants.ORGANIZATION_USER}
					).allowAddDataRecordCollection(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSavePermissionsRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}
	}

	@Test(expected = DEDataRecordCollectionException.PrincipalException.class)
	public void testAddDataRecordCollectionPermissionToGuestUser()
		throws Exception {

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _adminUser.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollectionSavePermissionsRequest
				deDataRecordCollectionSavePermissionsRequest =
					DEDataRecordCollectionRequestBuilder.savePermissionsBuilder(
						TestPropsValues.getCompanyId(), _adminUser.getGroupId(),
						new String[] {RoleConstants.GUEST}
					).allowAddDataRecordCollection(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSavePermissionsRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}
	}

	@Test(expected = DEDataRecordCollectionException.MustHavePermission.class)
	public void testDefinePermissionsByGuestUser() throws Exception {
		User user = UserTestUtil.addGroupUser(_group, RoleConstants.GUEST);

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(user));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), user.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollectionSavePermissionsRequest
				deDataRecordCollectionSavePermissionsRequest =
					DEDataRecordCollectionRequestBuilder.savePermissionsBuilder(
						TestPropsValues.getCompanyId(), _group.getGroupId(),
						new String[] {RoleConstants.ORGANIZATION_USER}
					).allowDefinePermissions(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSavePermissionsRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}
	}

	@Test(expected = DEDataRecordCollectionException.MustHavePermission.class)
	public void testDefinePermissionsBySiteMember() throws Exception {
		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_siteMember));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _siteMember.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollectionSavePermissionsRequest
				deDataRecordCollectionSavePermissionsRequest =
					DEDataRecordCollectionRequestBuilder.savePermissionsBuilder(
						TestPropsValues.getCompanyId(), _group.getGroupId(),
						new String[] {RoleConstants.ORGANIZATION_USER}
					).allowDefinePermissions(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSavePermissionsRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}
	}

	@Test(expected = DEDataRecordCollectionException.PrincipalException.class)
	public void testDefinePermissionsToGuestUser() throws Exception {
		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _adminUser.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollectionSavePermissionsRequest
				deDataRecordCollectionSavePermissionsRequest =
					DEDataRecordCollectionRequestBuilder.savePermissionsBuilder(
						TestPropsValues.getCompanyId(), _group.getGroupId(),
						new String[] {RoleConstants.GUEST}
					).allowDefinePermissions(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSavePermissionsRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}
	}

	@Test
	public void testDefinePermissionToAllowAddDataRecordCollection()
		throws Exception {

		Role role1 = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);

		User user1 = UserTestUtil.addGroupUser(_group, role1.getName());

		Role role2 = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);

		User user2 = UserTestUtil.addGroupUser(_group, role2.getName());

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _adminUser.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollectionSavePermissionsRequest
				deDataRecordCollectionSavePermissionsRequest =
					DEDataRecordCollectionRequestBuilder.savePermissionsBuilder(
						TestPropsValues.getCompanyId(), _group.getGroupId(),
						new String[] {role1.getName()}
					).allowDefinePermissions(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSavePermissionsRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(user1));

		ServiceContext serviceContext2 =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), user1.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext2);

		try {
			DEDataRecordCollectionSavePermissionsRequest
				deDataRecordCollectionSavePermissionsRequest2 =
					DEDataRecordCollectionRequestBuilder.savePermissionsBuilder(
						TestPropsValues.getCompanyId(), _group.getGroupId(),
						new String[] {role2.getName()}
					).allowAddDataRecordCollection(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSavePermissionsRequest2);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}

		DEDataDefinition deDataDefinition =
			DEDataEngineTestUtil.insertDEDataDefinition(
				_adminUser, _group, _deDataDefinitionService);

		DEDataRecordCollection deDataRecordCollection =
			DEDataEngineTestUtil.insertDEDataRecordCollection(
				user2, _group, deDataDefinition,
				_deDataRecordCollectionService);

		Assert.assertTrue(
			deDataRecordCollection.getDEDataRecordCollectionId() > 0);
	}

	@Test
	public void testDefinePermissionToAllowDeleteDataRecord() throws Exception {
		Role role1 = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);

		User user1 = UserTestUtil.addGroupUser(_group, role1.getName());

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _adminUser.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollectionSavePermissionsRequest
				deDataRecordCollectionSavePermissionsRequest =
					DEDataRecordCollectionRequestBuilder.savePermissionsBuilder(
						TestPropsValues.getCompanyId(), _group.getGroupId(),
						new String[] {role1.getName()}
					).allowDefinePermissions(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSavePermissionsRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}

		DEDataRecordCollection deDataRecordCollection =
			DEDataEngineTestUtil.insertDEDataRecordCollection(
				_adminUser, _group, _deDataDefinitionService,
				_deDataRecordCollectionService);

		User user2 = UserTestUtil.addGroupUser(
			_group, RoleConstants.ORGANIZATION_USER);

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(user1));

		ServiceContext serviceContext2 =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), user1.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext2);

		try {
			DEDataRecordCollectionSaveModelPermissionsRequest
				deDataRecordCollectionSaveModelPermissionsRequest2 =
					DEDataRecordCollectionRequestBuilder.
						saveModelPermissionsBuilder(
							TestPropsValues.getCompanyId(), _group.getGroupId(),
							user1.getUserId(), _group.getGroupId(),
							deDataRecordCollection.
								getDEDataRecordCollectionId(),
							new String[] {RoleConstants.ORGANIZATION_USER}
						).allowDeleteDataRecord(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSaveModelPermissionsRequest2);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}

		DEDataRecord deDataRecord = DEDataEngineTestUtil.insertDEDataRecord(
			_adminUser, _group, deDataRecordCollection,
			_deDataRecordCollectionService);

		DEDataEngineTestUtil.deleteDEDataRecord(
			user2, _group, deDataRecord.getDEDataRecordId(),
			_deDataRecordCollectionService);
	}

	@Test
	public void testDefinePermissionToAllowDeleteDataRecordCollection()
		throws Exception {

		Role role1 = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);

		User user1 = UserTestUtil.addGroupUser(_group, role1.getName());

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _adminUser.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollectionSavePermissionsRequest
				deDataRecordCollectionSavePermissionsRequest =
					DEDataRecordCollectionRequestBuilder.savePermissionsBuilder(
						TestPropsValues.getCompanyId(), _group.getGroupId(),
						new String[] {role1.getName()}
					).allowDefinePermissions(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSavePermissionsRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}

		DEDataRecordCollection deDataRecordCollection =
			DEDataEngineTestUtil.insertDEDataRecordCollection(
				_adminUser, _group, _deDataDefinitionService,
				_deDataRecordCollectionService);

		User user2 = UserTestUtil.addGroupUser(
			_group, RoleConstants.ORGANIZATION_USER);

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(user1));

		ServiceContext serviceContext2 =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), user1.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext2);

		try {
			DEDataRecordCollectionSaveModelPermissionsRequest
				deDataRecordCollectionSaveModelPermissionsRequest2 =
					DEDataRecordCollectionRequestBuilder.
						saveModelPermissionsBuilder(
							TestPropsValues.getCompanyId(), _group.getGroupId(),
							user1.getUserId(), _group.getGroupId(),
							deDataRecordCollection.
								getDEDataRecordCollectionId(),
							new String[] {RoleConstants.ORGANIZATION_USER}
						).allowDelete(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSaveModelPermissionsRequest2);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}

		DEDataEngineTestUtil.deleteDEDataRecordCollection(
			user2, _group.getGroupId(),
			deDataRecordCollection.getDEDataRecordCollectionId(),
			_deDataRecordCollectionService);
	}

	@Test
	public void testDefinePermissionToAllowUpdateDataRecordCollection()
		throws Exception {

		Role role1 = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);

		User user1 = UserTestUtil.addGroupUser(_group, role1.getName());

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _adminUser.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollectionSavePermissionsRequest
				deDataRecordCollectionSavePermissionsRequest =
					DEDataRecordCollectionRequestBuilder.savePermissionsBuilder(
						TestPropsValues.getCompanyId(), _group.getGroupId(),
						new String[] {role1.getName()}
					).allowDefinePermissions(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSavePermissionsRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}

		DEDataRecordCollection deDataRecordCollection =
			DEDataEngineTestUtil.insertDEDataRecordCollection(
				_adminUser, _group, _deDataDefinitionService,
				_deDataRecordCollectionService);

		User user2 = UserTestUtil.addGroupUser(
			_group, RoleConstants.ORGANIZATION_USER);

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(user1));

		ServiceContext serviceContext2 =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), user1.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext2);

		try {
			DEDataRecordCollectionSaveModelPermissionsRequest
				deDataRecordCollectionSaveModelPermissionsRequest2 =
					DEDataRecordCollectionRequestBuilder.
						saveModelPermissionsBuilder(
							TestPropsValues.getCompanyId(), _group.getGroupId(),
							user1.getUserId(), _group.getGroupId(),
							deDataRecordCollection.
								getDEDataRecordCollectionId(),
							new String[] {RoleConstants.ORGANIZATION_USER}
						).allowUpdate(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSaveModelPermissionsRequest2);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}

		DEDataRecordCollection deDataRecordCollectionAfterUpdate =
			DEDataEngineTestUtil.updateDEDataRecordCollection(
				user2, _group, deDataRecordCollection,
				_deDataRecordCollectionService);

		Assert.assertEquals(
			deDataRecordCollection.getDEDataRecordCollectionId(),
			deDataRecordCollectionAfterUpdate.getDEDataRecordCollectionId());
	}

	@Test
	public void testDefinePermissionToAllowViewDataRecord() throws Exception {
		Role role1 = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);

		User user1 = UserTestUtil.addGroupUser(_group, role1.getName());

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _adminUser.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollectionSavePermissionsRequest
				deDataRecordCollectionSavePermissionsRequest =
					DEDataRecordCollectionRequestBuilder.savePermissionsBuilder(
						TestPropsValues.getCompanyId(), _group.getGroupId(),
						new String[] {role1.getName()}
					).allowDefinePermissions(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSavePermissionsRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}

		DEDataRecordCollection deDataRecordCollection =
			DEDataEngineTestUtil.insertDEDataRecordCollection(
				_adminUser, _group, _deDataDefinitionService,
				_deDataRecordCollectionService);

		User user2 = UserTestUtil.addGroupUser(
			_group, RoleConstants.ORGANIZATION_USER);

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(user1));

		ServiceContext serviceContext2 =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), user1.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext2);

		try {
			DEDataRecordCollectionSaveModelPermissionsRequest
				deDataRecordCollectionSaveModelPermissionsRequest2 =
					DEDataRecordCollectionRequestBuilder.
						saveModelPermissionsBuilder(
							TestPropsValues.getCompanyId(), _group.getGroupId(),
							user1.getUserId(), _group.getGroupId(),
							deDataRecordCollection.
								getDEDataRecordCollectionId(),
							new String[] {RoleConstants.ORGANIZATION_USER}
						).allowViewDataRecord(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSaveModelPermissionsRequest2);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}

		DEDataRecord expectedDEDataRecord =
			DEDataEngineTestUtil.insertDEDataRecord(
				_adminUser, _group, deDataRecordCollection,
				_deDataRecordCollectionService);

		DEDataRecord deDataRecord = DEDataEngineTestUtil.getDEDataRecord(
			user2, _group, expectedDEDataRecord.getDEDataRecordId(),
			_deDataRecordCollectionService);

		Assert.assertEquals(expectedDEDataRecord, deDataRecord);
	}

	@Test(expected = DEDataRecordCollectionException.MustHavePermission.class)
	public void testDeleteByGuest() throws Exception {
		User user = UserTestUtil.addGroupUser(_group, RoleConstants.GUEST);

		DEDataDefinition deDataDefinition =
			DEDataEngineTestUtil.insertDEDataDefinition(
				_adminUser, _group, _deDataDefinitionService);

		DEDataRecordCollection deDataRecordCollection =
			DEDataEngineTestUtil.insertDEDataRecordCollection(
				_adminUser, _group, deDataDefinition,
				_deDataRecordCollectionService);

		DEDataEngineTestUtil.deleteDEDataRecordCollection(
			user, _group.getGroupId(),
			deDataRecordCollection.getDEDataRecordCollectionId(),
			_deDataRecordCollectionService);
	}

	@Test(expected = DEDataRecordCollectionException.MustHavePermission.class)
	public void testDeleteBySiteMember() throws Exception {
		DEDataDefinition deDataDefinition =
			DEDataEngineTestUtil.insertDEDataDefinition(
				_adminUser, _group, _deDataDefinitionService);

		DEDataRecordCollection deDataRecordCollection =
			DEDataEngineTestUtil.insertDEDataRecordCollection(
				_adminUser, _group, deDataDefinition,
				_deDataRecordCollectionService);

		DEDataEngineTestUtil.deleteDEDataRecordCollection(
			_siteMember, _group.getGroupId(),
			deDataRecordCollection.getDEDataRecordCollectionId(),
			_deDataRecordCollectionService);
	}

	@Test
	public void testDeleteDataRecord() throws Exception {
		DEDataRecord deDataRecord = DEDataEngineTestUtil.insertDEDataRecord(
			_adminUser, _group, _deDataDefinitionService,
			_deDataRecordCollectionService);

		long deDataRecordId = DEDataEngineTestUtil.deleteDEDataRecord(
			_adminUser, _group, deDataRecord.getDEDataRecordId(),
			_deDataRecordCollectionService);

		Assert.assertEquals(deDataRecord.getDEDataRecordId(), deDataRecordId);
	}

	@Test
	public void testDeleteDataRecordAndKeepTheOther() throws Exception {
		DEDataRecordCollection deDataRecordCollection =
			DEDataEngineTestUtil.insertDEDataRecordCollection(
				_adminUser, _group, _deDataDefinitionService,
				_deDataRecordCollectionService);

		DEDataRecord deDataRecord1 = DEDataEngineTestUtil.insertDEDataRecord(
			_adminUser, _group, deDataRecordCollection,
			_deDataRecordCollectionService);

		DEDataRecord deDataRecord2 = DEDataEngineTestUtil.insertDEDataRecord(
			_adminUser, _group, deDataRecordCollection,
			_deDataRecordCollectionService);

		DEDataEngineTestUtil.deleteDEDataRecord(
			_adminUser, _group, deDataRecord1.getDEDataRecordId(),
			_deDataRecordCollectionService);

		DEDataRecord deDataRecord2AfterDelete =
			DEDataEngineTestUtil.getDEDataRecord(
				_adminUser, _group, deDataRecord2.getDEDataRecordId(),
				_deDataRecordCollectionService);

		Assert.assertEquals(deDataRecord2, deDataRecord2AfterDelete);
	}

	@Test(expected = DEDataRecordCollectionException.MustHavePermission.class)
	public void testDeleteDataRecordByGuestUser() throws Exception {
		User user = UserTestUtil.addGroupUser(_group, RoleConstants.GUEST);

		DEDataRecord deDataRecord = DEDataEngineTestUtil.insertDEDataRecord(
			_adminUser, _group, _deDataDefinitionService,
			_deDataRecordCollectionService);

		DEDataEngineTestUtil.deleteDEDataRecord(
			user, _group, deDataRecord.getDEDataRecordId(),
			_deDataRecordCollectionService);
	}

	@Test(expected = DEDataRecordCollectionException.MustHavePermission.class)
	public void testDeleteDataRecordBySiteMember() throws Exception {
		DEDataRecord deDataRecord = DEDataEngineTestUtil.insertDEDataRecord(
			_adminUser, _group, _deDataDefinitionService,
			_deDataRecordCollectionService);

		DEDataEngineTestUtil.deleteDEDataRecord(
			_siteMember, _group, deDataRecord.getDEDataRecordId(),
			_deDataRecordCollectionService);
	}

	@Test(expected = DEDataRecordCollectionException.NoSuchDataRecord.class)
	public void testDeleteDataRecordCollectionAndCheckIfDataRecordsAreDeleted()
		throws Exception {

		DEDataRecordCollection deDataRecordCollection =
			DEDataEngineTestUtil.insertDEDataRecordCollection(
				_adminUser, _group, _deDataDefinitionService,
				_deDataRecordCollectionService);

		DEDataRecord deDataRecord = DEDataEngineTestUtil.insertDEDataRecord(
			_adminUser, _group, deDataRecordCollection,
			_deDataRecordCollectionService);

		DEDataEngineTestUtil.deleteDEDataRecordCollection(
			_adminUser, _group.getGroupId(),
			deDataRecordCollection.getDEDataRecordCollectionId(),
			_deDataRecordCollectionService);

		DEDataEngineTestUtil.getDEDataRecord(
			_adminUser, _group, deDataRecord.getDEDataRecordCollectionId(),
			_deDataRecordCollectionService);
	}

	@Test
	public void testDeleteDataRecordCollectionAndKeepTheOther()
		throws Exception {

		DEDataRecordCollection deDataRecordCollection1 =
			DEDataEngineTestUtil.insertDEDataRecordCollection(
				_adminUser, _group, _deDataDefinitionService,
				_deDataRecordCollectionService);

		DEDataRecordCollection deDataRecordCollection2 =
			DEDataEngineTestUtil.insertDEDataRecordCollection(
				_adminUser, _group, _deDataDefinitionService,
				_deDataRecordCollectionService);

		DEDataEngineTestUtil.deleteDEDataRecordCollection(
			_adminUser, _group.getGroupId(),
			deDataRecordCollection1.getDEDataRecordCollectionId(),
			_deDataRecordCollectionService);

		DEDataEngineTestUtil.deleteDEDataRecordCollection(
			_adminUser, _group.getGroupId(),
			deDataRecordCollection2.getDEDataRecordCollectionId(),
			_deDataRecordCollectionService);
	}

	@Test
	public void testDeleteDataRecordCollectionWithDataRecordAssociated()
		throws Exception {

		DEDataRecordCollection deDataRecordCollection =
			DEDataEngineTestUtil.insertDEDataRecordCollection(
				_adminUser, _group, _deDataDefinitionService,
				_deDataRecordCollectionService);

		DEDataEngineTestUtil.insertDEDataRecord(
			_adminUser, _group, deDataRecordCollection,
			_deDataRecordCollectionService);

		DEDataEngineTestUtil.deleteDEDataRecordCollection(
			_adminUser, _group.getGroupId(),
			deDataRecordCollection.getDEDataRecordCollectionId(),
			_deDataRecordCollectionService);
	}

	@Test(expected = DEDataRecordCollectionException.MustHavePermission.class)
	public void testDeleteDataRecordWithNoPermission() throws Exception {
		Role role = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);

		User user = UserTestUtil.addGroupUser(_group, role.getName());

		DEDataRecord deDataRecord = DEDataEngineTestUtil.insertDEDataRecord(
			_adminUser, _group, _deDataDefinitionService,
			_deDataRecordCollectionService);

		DEDataEngineTestUtil.deleteDEDataRecord(
			user, _group, deDataRecord.getDEDataRecordId(),
			_deDataRecordCollectionService);
	}

	@Test(expected = DEDataRecordCollectionException.NoSuchDataRecord.class)
	public void testDeleteNoSuchDataRecord() throws Exception {
		DEDataEngineTestUtil.deleteDEDataRecord(
			_siteMember, _group, 1, _deDataRecordCollectionService);
	}

	@Test(
		expected =
			DEDataRecordCollectionException.NoSuchDataRecordCollection.class
	)
	public void testDeleteNoSuchDataRecordCollection() throws Exception {
		DEDataEngineTestUtil.deleteDEDataRecordCollection(
			_adminUser, _group.getGroupId(), 1, _deDataRecordCollectionService);
	}

	@Test(
		expected =
			DEDataRecordCollectionException.NoSuchDataRecordCollection.class
	)
	public void testDeleteNoSuchDataRecordCollection2() throws Exception {
		DEDataEngineTestUtil.deleteDEDataRecordCollection(
			_adminUser, _group.getGroupId(), Long.MAX_VALUE,
			_deDataRecordCollectionService);
	}

	@Test(
		expected =
			DEDataRecordCollectionException.NoSuchDataRecordCollection.class
	)
	public void testDeleteWithNoIDParameter() throws Exception {
		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _adminUser.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollectionDeleteRequest
				deDataRecordCollectionDeleteRequest =
					DEDataRecordCollectionRequestBuilder.deleteBuilder(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionDeleteRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}
	}

	@Test(expected = DEDataRecordCollectionException.MustHavePermission.class)
	public void testDeleteWithNoPermission() throws Exception {
		Role role = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);

		User user = UserTestUtil.addGroupUser(_group, role.getName());

		DEDataRecordCollection deDataRecordCollection =
			DEDataEngineTestUtil.insertDEDataRecordCollection(
				_adminUser, _group, _deDataDefinitionService,
				_deDataRecordCollectionService);

		DEDataEngineTestUtil.deleteDEDataRecordCollection(
			user, _group.getGroupId(),
			deDataRecordCollection.getDEDataRecordCollectionId(),
			_deDataRecordCollectionService);
	}

	@Test
	public void testGetDataRecord() throws Exception {
		DEDataRecord deDataRecord = DEDataEngineTestUtil.insertDEDataRecord(
			_adminUser, _group, _deDataDefinitionService,
			_deDataRecordCollectionService);

		DEDataEngineTestUtil.getDEDataRecord(
			_adminUser, _group, deDataRecord.getDEDataRecordId(),
			_deDataRecordCollectionService);
	}

	@Test
	public void testGetDataRecordByGuestUser() throws Exception {
		DEDataDefinition deDataDefinition =
			DEDataEngineTestUtil.insertDEDataDefinition(
				_adminUser, _group, _deDataDefinitionService);

		DEDataRecordCollection deDataRecordCollection;

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group, _adminUser.getUserId());

		ModelPermissions modelPermissions = new ModelPermissions();

		modelPermissions.addRolePermissions(
			RoleConstants.GUEST, DEActionKeys.VIEW_DATA_RECORD);

		serviceContext.setModelPermissions(modelPermissions);

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		try {
			deDataRecordCollection = new DEDataRecordCollection();

			deDataRecordCollection.addName(LocaleUtil.US, "Data record list");
			deDataRecordCollection.addDescription(
				LocaleUtil.BRAZIL, "Lista de registro de dados");
			deDataRecordCollection.addDescription(
				LocaleUtil.US, "Data record list description");
			deDataRecordCollection.addDescription(
				LocaleUtil.BRAZIL, "Descrição da lista de registro de dados");
			deDataRecordCollection.setDEDataDefinition(deDataDefinition);

			DEDataRecordCollectionSaveRequest
				deDataRecordCollectionSaveRequest =
					DEDataRecordCollectionRequestBuilder.saveBuilder(
						deDataRecordCollection
					).onBehalfOf(
						_adminUser.getUserId()
					).inGroup(
						_group.getGroupId()
					).build();

			DEDataRecordCollectionSaveResponse
				deDataRecordCollectionSaveResponse =
					_deDataRecordCollectionService.execute(
						deDataRecordCollectionSaveRequest);

			deDataRecordCollection =
				deDataRecordCollectionSaveResponse.getDEDataRecordCollection();
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}

		DEDataRecord expectedDEDataRecord =
			DEDataEngineTestUtil.insertDEDataRecord(
				_adminUser, _group, deDataRecordCollection,
				_deDataRecordCollectionService);

		User user = UserTestUtil.addGroupUser(_group, RoleConstants.GUEST);

		DEDataRecord deDataRecord = DEDataEngineTestUtil.getDEDataRecord(
			user, _group, expectedDEDataRecord.getDEDataRecordId(),
			_deDataRecordCollectionService);

		Assert.assertEquals(expectedDEDataRecord, deDataRecord);
	}

	@Test
	public void testGetDataRecordBySiteMember() throws Exception {
		DEDataDefinition deDataDefinition =
			DEDataEngineTestUtil.insertDEDataDefinition(
				_adminUser, _group, _deDataDefinitionService);

		DEDataRecordCollection deDataRecordCollection;

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group, _adminUser.getUserId());

		ModelPermissions modelPermissions = new ModelPermissions();

		modelPermissions.addRolePermissions(
			RoleConstants.SITE_MEMBER, DEActionKeys.VIEW_DATA_RECORD);

		serviceContext.setModelPermissions(modelPermissions);

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		try {
			deDataRecordCollection = new DEDataRecordCollection();

			deDataRecordCollection.addName(LocaleUtil.US, "Data record list");
			deDataRecordCollection.addDescription(
				LocaleUtil.BRAZIL, "Lista de registro de dados");
			deDataRecordCollection.addDescription(
				LocaleUtil.US, "Data record list description");
			deDataRecordCollection.addDescription(
				LocaleUtil.BRAZIL, "Descrição da lista de registro de dados");
			deDataRecordCollection.setDEDataDefinition(deDataDefinition);

			DEDataRecordCollectionSaveRequest
				deDataRecordCollectionSaveRequest =
					DEDataRecordCollectionRequestBuilder.saveBuilder(
						deDataRecordCollection
					).onBehalfOf(
						_adminUser.getUserId()
					).inGroup(
						_group.getGroupId()
					).build();

			DEDataRecordCollectionSaveResponse
				deDataRecordCollectionSaveResponse =
					_deDataRecordCollectionService.execute(
						deDataRecordCollectionSaveRequest);

			deDataRecordCollection =
				deDataRecordCollectionSaveResponse.getDEDataRecordCollection();
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}

		DEDataRecord expectedDEDataRecord =
			DEDataEngineTestUtil.insertDEDataRecord(
				_adminUser, _group, deDataRecordCollection,
				_deDataRecordCollectionService);

		User user = UserTestUtil.addGroupUser(
			_group, RoleConstants.SITE_MEMBER);

		DEDataRecord deDataRecord = DEDataEngineTestUtil.getDEDataRecord(
			user, _group, expectedDEDataRecord.getDEDataRecordId(),
			_deDataRecordCollectionService);

		Assert.assertEquals(expectedDEDataRecord, deDataRecord);
	}

	@Test(expected = DEDataRecordCollectionException.MustHavePermission.class)
	public void testGetDataRecordWithNoPermission() throws Exception {
		Role role = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);

		User user = UserTestUtil.addGroupUser(_group, role.getName());

		DEDataRecord deDataRecord = DEDataEngineTestUtil.insertDEDataRecord(
			_adminUser, _group, _deDataDefinitionService,
			_deDataRecordCollectionService);

		DEDataEngineTestUtil.getDEDataRecord(
			user, _group, deDataRecord.getDEDataRecordId(),
			_deDataRecordCollectionService);
	}

	@Test(expected = DEDataRecordCollectionException.NoSuchDataRecord.class)
	public void testGetNoSuchDataRecord() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group, _adminUser.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		try {
			DEDataRecordCollectionGetRecordRequest
				deDataRecordCollectionGetRecordRequest =
					DEDataRecordCollectionRequestBuilder.getRecordBuilder(
						1
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionGetRecordRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}
	}

	@Test(expected = DEDataRecordCollectionException.PrincipalException.class)
	public void testGrantDeleteDataRecordPermissionToGuestUser()
		throws Exception {

		DEDataRecord deDataRecord = DEDataEngineTestUtil.insertDEDataRecord(
			_adminUser, _group, _deDataDefinitionService,
			_deDataRecordCollectionService);

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _adminUser.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollectionSaveModelPermissionsRequest
				deDataRecordCollectionSaveModelPermissionsRequest =
					DEDataRecordCollectionRequestBuilder.
						saveModelPermissionsBuilder(
							TestPropsValues.getCompanyId(), _group.getGroupId(),
							_adminUser.getUserId(), _group.getGroupId(),
							deDataRecord.getDEDataRecordCollectionId(),
							new String[] {RoleConstants.GUEST}
						).allowDeleteDataRecord(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSaveModelPermissionsRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}
	}

	@Test
	public void testGrantDeleteDataRecordPermissionToSiteMember()
		throws Exception {

		DEDataRecord deDataRecord = DEDataEngineTestUtil.insertDEDataRecord(
			_adminUser, _group, _deDataDefinitionService,
			_deDataRecordCollectionService);

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _adminUser.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollectionSaveModelPermissionsRequest
				deDataRecordCollectionSaveModelPermissionsRequest =
					DEDataRecordCollectionRequestBuilder.
						saveModelPermissionsBuilder(
							TestPropsValues.getCompanyId(), _group.getGroupId(),
							_adminUser.getUserId(), _group.getGroupId(),
							deDataRecord.getDEDataRecordCollectionId(),
							new String[] {RoleConstants.SITE_MEMBER}
						).allowDeleteDataRecord(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSaveModelPermissionsRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}

		long deDataRecordId = DEDataEngineTestUtil.deleteDEDataRecord(
			_siteMember, _group, deDataRecord.getDEDataRecordId(),
			_deDataRecordCollectionService);

		Assert.assertEquals(deDataRecord.getDEDataRecordId(), deDataRecordId);
	}

	@Test(expected = DEDataRecordCollectionException.PrincipalException.class)
	public void testGrantDeletePermissionToGuestUser() throws Exception {
		DEDataRecordCollection deDataRecordCollection =
			DEDataEngineTestUtil.insertDEDataRecordCollection(
				_adminUser, _group, _deDataDefinitionService,
				_deDataRecordCollectionService);

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _adminUser.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollectionSaveModelPermissionsRequest
				deDataRecordCollectionSaveModelPermissionsRequest =
					DEDataRecordCollectionRequestBuilder.
						saveModelPermissionsBuilder(
							TestPropsValues.getCompanyId(), _group.getGroupId(),
							_adminUser.getUserId(), _group.getGroupId(),
							deDataRecordCollection.
								getDEDataRecordCollectionId(),
							new String[] {RoleConstants.GUEST}
						).allowDelete(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSaveModelPermissionsRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}
	}

	@Test
	public void testGrantDeletePermissionToSiteMember() throws Exception {
		DEDataRecordCollection deDataRecordCollection =
			DEDataEngineTestUtil.insertDEDataRecordCollection(
				_adminUser, _group, _deDataDefinitionService,
				_deDataRecordCollectionService);

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _adminUser.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollectionSaveModelPermissionsRequest
				deDataRecordCollectionSaveModelPermissionsRequest =
					DEDataRecordCollectionRequestBuilder.
						saveModelPermissionsBuilder(
							TestPropsValues.getCompanyId(), _group.getGroupId(),
							_adminUser.getUserId(), _group.getGroupId(),
							deDataRecordCollection.
								getDEDataRecordCollectionId(),
							new String[] {RoleConstants.SITE_MEMBER}
						).allowDelete(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSaveModelPermissionsRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}

		DEDataEngineTestUtil.deleteDEDataRecordCollection(
			_siteMember, _group.getGroupId(),
			deDataRecordCollection.getDEDataRecordCollectionId(),
			_deDataRecordCollectionService);
	}

	@Test(expected = DEDataRecordCollectionException.PrincipalException.class)
	public void testGrantUpdatePermissionToGuestUser() throws Exception {
		DEDataRecordCollection deDataRecordCollection =
			DEDataEngineTestUtil.insertDEDataRecordCollection(
				_adminUser, _group, _deDataDefinitionService,
				_deDataRecordCollectionService);

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _adminUser.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollectionSaveModelPermissionsRequest
				deDataRecordCollectionSaveModelPermissionsRequest =
					DEDataRecordCollectionRequestBuilder.
						saveModelPermissionsBuilder(
							TestPropsValues.getCompanyId(), _group.getGroupId(),
							_adminUser.getUserId(), _group.getGroupId(),
							deDataRecordCollection.
								getDEDataRecordCollectionId(),
							new String[] {RoleConstants.GUEST}
						).allowUpdate(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSaveModelPermissionsRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}
	}

	@Test
	public void testGrantUpdatePermissionToSiteMember() throws Exception {
		DEDataRecordCollection deDataRecordCollection =
			DEDataEngineTestUtil.insertDEDataRecordCollection(
				_adminUser, _group, _deDataDefinitionService,
				_deDataRecordCollectionService);

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _adminUser.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollectionSaveModelPermissionsRequest
				deDataRecordCollectionSaveModelPermissionsRequest =
					DEDataRecordCollectionRequestBuilder.
						saveModelPermissionsBuilder(
							TestPropsValues.getCompanyId(), _group.getGroupId(),
							_adminUser.getUserId(), _group.getGroupId(),
							deDataRecordCollection.
								getDEDataRecordCollectionId(),
							new String[] {RoleConstants.SITE_MEMBER}
						).allowUpdate(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSaveModelPermissionsRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}

		DEDataRecordCollection deDataRecordCollectionAfterUpdate =
			DEDataEngineTestUtil.updateDEDataRecordCollection(
				_siteMember, _group, deDataRecordCollection,
				_deDataRecordCollectionService);

		Assert.assertEquals(
			deDataRecordCollection, deDataRecordCollectionAfterUpdate);
	}

	@Test
	public void testGrantViewDataRecordPermissionToGuestUser()
		throws Exception {

		DEDataRecord expectedDEDataRecord =
			DEDataEngineTestUtil.insertDEDataRecord(
				_adminUser, _group, _deDataDefinitionService,
				_deDataRecordCollectionService);

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _adminUser.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollectionSaveModelPermissionsRequest
				deDataRecordCollectionSaveModelPermissionsRequest =
					DEDataRecordCollectionRequestBuilder.
						saveModelPermissionsBuilder(
							TestPropsValues.getCompanyId(), _group.getGroupId(),
							_adminUser.getUserId(), _group.getGroupId(),
							expectedDEDataRecord.getDEDataRecordCollectionId(),
							new String[] {RoleConstants.GUEST}
						).allowViewDataRecord(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSaveModelPermissionsRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}

		User user = UserTestUtil.addGroupUser(_group, RoleConstants.GUEST);

		DEDataRecord deDataRecord = DEDataEngineTestUtil.getDEDataRecord(
			user, _group, expectedDEDataRecord.getDEDataRecordId(),
			_deDataRecordCollectionService);

		Assert.assertEquals(expectedDEDataRecord, deDataRecord);
	}

	@Test
	public void testGrantViewDataRecordPermissionToSiteMember()
		throws Exception {

		DEDataRecord expectedDEDataRecord =
			DEDataEngineTestUtil.insertDEDataRecord(
				_adminUser, _group, _deDataDefinitionService,
				_deDataRecordCollectionService);

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _adminUser.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollectionSaveModelPermissionsRequest
				deDataRecordCollectionSaveModelPermissionsRequest =
					DEDataRecordCollectionRequestBuilder.
						saveModelPermissionsBuilder(
							TestPropsValues.getCompanyId(), _group.getGroupId(),
							_adminUser.getUserId(), _group.getGroupId(),
							expectedDEDataRecord.getDEDataRecordCollectionId(),
							new String[] {RoleConstants.SITE_MEMBER}
						).allowViewDataRecord(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSaveModelPermissionsRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}

		DEDataRecord deDataRecord = DEDataEngineTestUtil.getDEDataRecord(
			_siteMember, _group, expectedDEDataRecord.getDEDataRecordId(),
			_deDataRecordCollectionService);

		Assert.assertEquals(expectedDEDataRecord, deDataRecord);
	}

	@Test(expected = DEDataRecordCollectionException.MustHavePermission.class)
	public void testInsertByGuestUser() throws Exception {
		User user = UserTestUtil.addGroupUser(_group, RoleConstants.GUEST);

		DEDataDefinition deDataDefinition =
			DEDataEngineTestUtil.insertDEDataDefinition(
				_adminUser, _group, _deDataDefinitionService);

		DEDataEngineTestUtil.insertDEDataRecordCollection(
			user, _group, deDataDefinition, _deDataRecordCollectionService);
	}

	@Test(expected = DEDataRecordCollectionException.MustHavePermission.class)
	public void testInsertBySiteMember() throws Exception {
		DEDataDefinition deDataDefinition =
			DEDataEngineTestUtil.insertDEDataDefinition(
				_adminUser, _group, _deDataDefinitionService);

		DEDataEngineTestUtil.insertDEDataRecordCollection(
			_siteMember, _group, deDataDefinition,
			_deDataRecordCollectionService);
	}

	@Test(expected = DEDataRecordCollectionException.class)
	public void testInsertWithBadRequest() throws Exception {
		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _adminUser.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollection deDataRecordCollection =
				new DEDataRecordCollection();

			deDataRecordCollection.addName(LocaleUtil.US, "Data record list");
			deDataRecordCollection.addDescription(
				LocaleUtil.BRAZIL, "Lista de registro de dados");

			DEDataRecordCollectionSaveRequest
				deDataRecordCollectionSaveRequest =
					DEDataRecordCollectionRequestBuilder.saveBuilder(
						deDataRecordCollection
					).onBehalfOf(
						_adminUser.getUserId()
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSaveRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}
	}

	@Test(expected = DEDataRecordCollectionException.class)
	public void testInsertWithBadRequest2() throws Exception {
		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _adminUser.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollection deDataRecordCollection =
				new DEDataRecordCollection();

			deDataRecordCollection.addName(LocaleUtil.US, "Data record list");
			deDataRecordCollection.addDescription(
				LocaleUtil.BRAZIL, "Lista de registro de dados");

			DEDataRecordCollectionSaveRequest
				deDataRecordCollectionSaveRequest =
					DEDataRecordCollectionRequestBuilder.saveBuilder(
						deDataRecordCollection
					).inGroup(
						_group.getGroupId()
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSaveRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}
	}

	@Test(expected = DEDataRecordCollectionException.class)
	public void testInsertWithNoDataDefinition() throws Exception {
		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _adminUser.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollection deDataRecordCollection =
				new DEDataRecordCollection();

			deDataRecordCollection.addName(LocaleUtil.US, "Data record list");
			deDataRecordCollection.addDescription(
				LocaleUtil.BRAZIL, "Lista de registro de dados");

			DEDataRecordCollectionSaveRequest
				deDataRecordCollectionSaveRequest =
					DEDataRecordCollectionRequestBuilder.saveBuilder(
						deDataRecordCollection
					).onBehalfOf(
						_adminUser.getUserId()
					).inGroup(
						_group.getGroupId()
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSaveRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}
	}

	@Test(expected = DEDataRecordCollectionException.class)
	public void testInsertWithNonexistentGroup() throws Exception {
		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _adminUser.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataDefinition deDataDefinition =
				DEDataEngineTestUtil.insertDEDataDefinition(
					_adminUser, _group, _deDataDefinitionService);

			DEDataRecordCollection deDataRecordCollection =
				new DEDataRecordCollection();

			deDataRecordCollection.addName(LocaleUtil.US, "Data record list");
			deDataRecordCollection.addDescription(
				LocaleUtil.BRAZIL, "Lista de registro de dados");
			deDataRecordCollection.setDEDataDefinition(deDataDefinition);

			DEDataRecordCollectionSaveRequest
				deDataRecordCollectionSaveRequest =
					DEDataRecordCollectionRequestBuilder.saveBuilder(
						deDataRecordCollection
					).onBehalfOf(
						_adminUser.getUserId()
					).inGroup(
						1
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSaveRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}
	}

	@Test(expected = DEDataRecordCollectionException.class)
	public void testInsertWithNonexistentUser() throws Exception {
		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _adminUser.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataDefinition deDataDefinition =
				DEDataEngineTestUtil.insertDEDataDefinition(
					_adminUser, _group, _deDataDefinitionService);

			DEDataRecordCollection deDataRecordCollection =
				new DEDataRecordCollection();

			deDataRecordCollection.addName(LocaleUtil.US, "Data record list");
			deDataRecordCollection.addDescription(
				LocaleUtil.BRAZIL, "Lista de registro de dados");
			deDataRecordCollection.setDEDataDefinition(deDataDefinition);

			DEDataRecordCollectionSaveRequest
				deDataRecordCollectionSaveRequest =
					DEDataRecordCollectionRequestBuilder.saveBuilder(
						deDataRecordCollection
					).onBehalfOf(
						1
					).inGroup(
						_group.getGroupId()
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSaveRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}
	}

	@Test(expected = DEDataRecordCollectionException.MustHavePermission.class)
	public void testInsertWithNoPermission() throws Exception {
		Role role = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);

		User user = UserTestUtil.addGroupUser(_group, role.getName());

		DEDataDefinition deDataDefinition =
			DEDataEngineTestUtil.insertDEDataDefinition(
				_adminUser, _group, _deDataDefinitionService);

		DEDataEngineTestUtil.insertDEDataRecordCollection(
			user, _group, deDataDefinition, _deDataRecordCollectionService);
	}

	@Test(expected = DEDataRecordCollectionException.MustHavePermission.class)
	public void testRevokeAddDataRecordCollectionPermission() throws Exception {
		Role role = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);

		User user = UserTestUtil.addGroupUser(_group, role.getName());

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _adminUser.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollectionSavePermissionsRequest
				deDataRecordCollectionSavePermissionsRequest =
					DEDataRecordCollectionRequestBuilder.savePermissionsBuilder(
						TestPropsValues.getCompanyId(), _group.getGroupId(),
						new String[] {role.getName()}
					).allowAddDataRecordCollection(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSavePermissionsRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}

		DEDataDefinition deDataDefinition =
			DEDataEngineTestUtil.insertDEDataDefinition(
				_adminUser, _group, _deDataDefinitionService);

		DEDataRecordCollection deDataRecordCollection =
			DEDataEngineTestUtil.insertDEDataRecordCollection(
				user, _group, deDataDefinition, _deDataRecordCollectionService);

		Assert.assertTrue(
			deDataRecordCollection.getDEDataRecordCollectionId() > 0);

		DEDataEngineTestUtil.deleteDEDataRecordCollectionPermissions(
			TestPropsValues.getCompanyId(), _adminUser, _group.getGroupId(),
			new String[] {role.getName()}, _deDataRecordCollectionService);

		DEDataEngineTestUtil.insertDEDataRecordCollection(
			user, _group, deDataDefinition, _deDataRecordCollectionService);
	}

	@Test(expected = DEDataRecordCollectionException.MustHavePermission.class)
	public void testRevokeDefinePermissions() throws Exception {
		DEDataRecordCollection deDataRecordCollection =
			DEDataEngineTestUtil.insertDEDataRecordCollection(
				_adminUser, _group, _deDataDefinitionService,
				_deDataRecordCollectionService);

		Role role1 = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);

		User user1 = UserTestUtil.addGroupUser(_group, role1.getName());

		Role role2 = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);

		User user2 = UserTestUtil.addGroupUser(_group, role2.getName());

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _adminUser.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollectionSavePermissionsRequest
				deDataRecordCollectionSavePermissionsRequest =
					DEDataRecordCollectionRequestBuilder.savePermissionsBuilder(
						TestPropsValues.getCompanyId(), _group.getGroupId(),
						new String[] {role1.getName()}
					).allowDefinePermissions(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSavePermissionsRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(user1));

		ServiceContext serviceContext2 =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), user1.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext2);

		try {
			DEDataRecordCollectionSaveModelPermissionsRequest
				deDataRecordCollectionSaveModelPermissionsRequest =
					DEDataRecordCollectionRequestBuilder.
						saveModelPermissionsBuilder(
							TestPropsValues.getCompanyId(), _group.getGroupId(),
							user1.getUserId(), _group.getGroupId(),
							deDataRecordCollection.
								getDEDataRecordCollectionId(),
							new String[] {role2.getName()}
						).allowUpdate(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSaveModelPermissionsRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}

		DEDataRecordCollection deDataRecordCollectionAfterUpdate =
			DEDataEngineTestUtil.updateDEDataRecordCollection(
				user2, _group, deDataRecordCollection,
				_deDataRecordCollectionService);

		Assert.assertEquals(
			deDataRecordCollection.getDEDataRecordCollectionId(),
			deDataRecordCollectionAfterUpdate.getDEDataRecordCollectionId());

		DEDataEngineTestUtil.deleteDEDataRecordCollectionPermissions(
			TestPropsValues.getCompanyId(), _adminUser, _group.getGroupId(),
			new String[] {role1.getName()}, _deDataRecordCollectionService);

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(user1));

		ServiceContext serviceContext3 =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), user1.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext3);

		try {
			DEDataRecordCollectionSaveModelPermissionsRequest
				deDataRecordCollectionSaveModelPermissionsRequest =
					DEDataRecordCollectionRequestBuilder.
						saveModelPermissionsBuilder(
							TestPropsValues.getCompanyId(), _group.getGroupId(),
							user1.getUserId(), _group.getGroupId(),
							deDataRecordCollection.
								getDEDataRecordCollectionId(),
							new String[] {role2.getName()}
						).allowUpdate(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSaveModelPermissionsRequest);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}
	}

	@Test(expected = DEDataRecordCollectionException.MustHavePermission.class)
	public void testRevokeDeleteDataRecordCollectionPermission()
		throws Exception {

		Role role = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);

		User user = UserTestUtil.addGroupUser(_group, role.getName());

		DEDataRecordCollection deDataRecordCollection =
			DEDataEngineTestUtil.insertDEDataRecordCollection(
				_adminUser, _group, _deDataDefinitionService,
				_deDataRecordCollectionService);

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _adminUser.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollectionSaveModelPermissionsRequest
				deDataRecordCollectionSaveModelPermissionsRequest2 =
					DEDataRecordCollectionRequestBuilder.
						saveModelPermissionsBuilder(
							TestPropsValues.getCompanyId(), _group.getGroupId(),
							_adminUser.getUserId(), _group.getGroupId(),
							deDataRecordCollection.
								getDEDataRecordCollectionId(),
							new String[] {role.getName()}
						).allowDelete(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSaveModelPermissionsRequest2);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}

		DEDataEngineTestUtil.deleteDEDataRecordCollectionModelPermissions(
			TestPropsValues.getCompanyId(), _adminUser, _group.getGroupId(),
			deDataRecordCollection.getDEDataRecordCollectionId(),
			new String[] {ActionKeys.DELETE}, new String[] {role.getName()},
			_deDataRecordCollectionService);

		DEDataEngineTestUtil.deleteDEDataRecordCollection(
			user, _group.getGroupId(),
			deDataRecordCollection.getDEDataRecordCollectionId(),
			_deDataRecordCollectionService);
	}

	@Test(expected = DEDataRecordCollectionException.MustHavePermission.class)
	public void testRevokeDeleteDataRecordPermission() throws Exception {
		Role role = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);

		User user = UserTestUtil.addGroupUser(_group, role.getName());

		DEDataRecordCollection deDataRecordCollection =
			DEDataEngineTestUtil.insertDEDataRecordCollection(
				_adminUser, _group, _deDataDefinitionService,
				_deDataRecordCollectionService);

		DEDataRecord deDataRecord = DEDataEngineTestUtil.insertDEDataRecord(
			_adminUser, _group, deDataRecordCollection,
			_deDataRecordCollectionService);

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _adminUser.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollectionSaveModelPermissionsRequest
				deDataRecordCollectionSaveModelPermissionsRequest2 =
					DEDataRecordCollectionRequestBuilder.
						saveModelPermissionsBuilder(
							TestPropsValues.getCompanyId(), _group.getGroupId(),
							_adminUser.getUserId(), _group.getGroupId(),
							deDataRecordCollection.
								getDEDataRecordCollectionId(),
							new String[] {role.getName()}
						).allowDeleteDataRecord(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSaveModelPermissionsRequest2);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}

		DEDataEngineTestUtil.deleteDEDataRecordCollectionModelPermissions(
			TestPropsValues.getCompanyId(), _adminUser, _group.getGroupId(),
			deDataRecordCollection.getDEDataRecordCollectionId(),
			new String[] {DEActionKeys.DELETE_DATA_RECORD},
			new String[] {role.getName()}, _deDataRecordCollectionService);

		DEDataEngineTestUtil.deleteDEDataRecord(
			user, _group, deDataRecord.getDEDataRecordId(),
			_deDataRecordCollectionService);
	}

	@Test(expected = DEDataRecordCollectionException.MustHavePermission.class)
	public void testRevokeModelPermissionsWithNoPermission() throws Exception {
		Role role = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);

		User user = UserTestUtil.addGroupUser(_group, role.getName());

		DEDataRecordCollection deDataRecordCollection =
			DEDataEngineTestUtil.insertDEDataRecordCollection(
				_adminUser, _group, _deDataDefinitionService,
				_deDataRecordCollectionService);

		DEDataEngineTestUtil.deleteDEDataRecordCollectionModelPermissions(
			TestPropsValues.getCompanyId(), user, _group.getGroupId(),
			deDataRecordCollection.getDEDataRecordCollectionId(),
			new String[] {ActionKeys.UPDATE},
			new String[] {RoleConstants.ORGANIZATION_USER},
			_deDataRecordCollectionService);
	}

	@Test(expected = DEDataRecordCollectionException.MustHavePermission.class)
	public void testRevokePermissionsWithNoPermission() throws Exception {
		Role role = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);

		User user = UserTestUtil.addGroupUser(_group, role.getName());

		DEDataEngineTestUtil.deleteDEDataRecordCollectionPermissions(
			TestPropsValues.getCompanyId(), user, _group.getGroupId(),
			new String[] {RoleConstants.ORGANIZATION_USER},
			_deDataRecordCollectionService);
	}

	@Test(expected = DEDataRecordCollectionException.MustHavePermission.class)
	public void testRevokeUpdateDataRecordCollectionPermission()
		throws Exception {

		Role role = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);

		User user = UserTestUtil.addGroupUser(_group, role.getName());

		DEDataRecordCollection deDataRecordCollection =
			DEDataEngineTestUtil.insertDEDataRecordCollection(
				_adminUser, _group, _deDataDefinitionService,
				_deDataRecordCollectionService);

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _adminUser.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollectionSaveModelPermissionsRequest
				deDataRecordCollectionSaveModelPermissionsRequest2 =
					DEDataRecordCollectionRequestBuilder.
						saveModelPermissionsBuilder(
							TestPropsValues.getCompanyId(), _group.getGroupId(),
							_adminUser.getUserId(), _group.getGroupId(),
							deDataRecordCollection.
								getDEDataRecordCollectionId(),
							new String[] {role.getName()}
						).allowUpdate(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSaveModelPermissionsRequest2);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}

		DEDataEngineTestUtil.updateDEDataRecordCollection(
			user, _group, deDataRecordCollection,
			_deDataRecordCollectionService);

		DEDataEngineTestUtil.deleteDEDataRecordCollectionModelPermissions(
			TestPropsValues.getCompanyId(), _adminUser, _group.getGroupId(),
			deDataRecordCollection.getDEDataRecordCollectionId(),
			new String[] {ActionKeys.UPDATE}, new String[] {role.getName()},
			_deDataRecordCollectionService);

		DEDataEngineTestUtil.updateDEDataRecordCollection(
			user, _group, deDataRecordCollection,
			_deDataRecordCollectionService);
	}

	@Test(expected = DEDataRecordCollectionException.MustHavePermission.class)
	public void testRevokeViewDataRecordPermission() throws Exception {
		Role role = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);

		User user = UserTestUtil.addGroupUser(_group, role.getName());

		DEDataRecordCollection deDataRecordCollection =
			DEDataEngineTestUtil.insertDEDataRecordCollection(
				_adminUser, _group, _deDataDefinitionService,
				_deDataRecordCollectionService);

		DEDataRecord deDataRecord = DEDataEngineTestUtil.insertDEDataRecord(
			_adminUser, _group, deDataRecordCollection,
			_deDataRecordCollectionService);

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_adminUser));

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _adminUser.getUserId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		try {
			DEDataRecordCollectionSaveModelPermissionsRequest
				deDataRecordCollectionSaveModelPermissionsRequest2 =
					DEDataRecordCollectionRequestBuilder.
						saveModelPermissionsBuilder(
							TestPropsValues.getCompanyId(), _group.getGroupId(),
							_adminUser.getUserId(), _group.getGroupId(),
							deDataRecordCollection.
								getDEDataRecordCollectionId(),
							new String[] {role.getName()}
						).allowViewDataRecord(
					).build();

			_deDataRecordCollectionService.execute(
				deDataRecordCollectionSaveModelPermissionsRequest2);
		}
		finally {
			ServiceContextThreadLocal.popServiceContext();
		}

		DEDataRecord deDataRecord2 = DEDataEngineTestUtil.getDEDataRecord(
			user, _group, deDataRecord.getDEDataRecordId(),
			_deDataRecordCollectionService);

		Assert.assertEquals(deDataRecord, deDataRecord2);

		DEDataEngineTestUtil.deleteDEDataRecordCollectionModelPermissions(
			TestPropsValues.getCompanyId(), _adminUser, _group.getGroupId(),
			deDataRecordCollection.getDEDataRecordCollectionId(),
			new String[] {DEActionKeys.VIEW_DATA_RECORD},
			new String[] {role.getName()}, _deDataRecordCollectionService);

		DEDataEngineTestUtil.getDEDataRecord(
			user, _group, deDataRecord.getDEDataRecordId(),
			_deDataRecordCollectionService);
	}

	@Test
	public void testUpdateAfterUpdate() throws Exception {
		DEDataRecordCollection deDataRecordCollection =
			DEDataEngineTestUtil.insertDEDataRecordCollection(
				_adminUser, _group, _deDataDefinitionService,
				_deDataRecordCollectionService);

		deDataRecordCollection.addDescription(
			LocaleUtil.US, "Data record list 2");

		DEDataEngineTestUtil.updateDEDataRecordCollection(
			_adminUser, _group, deDataRecordCollection,
			_deDataRecordCollectionService);

		deDataRecordCollection.addDescription(
			LocaleUtil.US, "Data record list 3");

		DEDataRecordCollection deDataRecordCollectionAfterUpdate =
			DEDataEngineTestUtil.updateDEDataRecordCollection(
				_adminUser, _group, deDataRecordCollection,
				_deDataRecordCollectionService);

		Assert.assertEquals(
			deDataRecordCollection.getDEDataRecordCollectionId(),
			deDataRecordCollectionAfterUpdate.getDEDataRecordCollectionId());
	}

	@Test(expected = DEDataRecordCollectionException.MustHavePermission.class)
	public void testUpdateByGuestUser() throws Exception {
		DEDataRecordCollection deDataRecordCollection =
			DEDataEngineTestUtil.insertDEDataRecordCollection(
				_adminUser, _group, _deDataDefinitionService,
				_deDataRecordCollectionService);

		deDataRecordCollection.addDescription(
			LocaleUtil.US, "Data record list 2");

		User user = UserTestUtil.addGroupUser(_group, RoleConstants.GUEST);

		DEDataEngineTestUtil.updateDEDataRecordCollection(
			user, _group, deDataRecordCollection,
			_deDataRecordCollectionService);
	}

	@Test(expected = DEDataRecordCollectionException.MustHavePermission.class)
	public void testUpdateBySiteMember() throws Exception {
		DEDataRecordCollection deDataRecordCollection =
			DEDataEngineTestUtil.insertDEDataRecordCollection(
				_adminUser, _group, _deDataDefinitionService,
				_deDataRecordCollectionService);

		deDataRecordCollection.addDescription(
			LocaleUtil.US, "Data record list 2");

		DEDataEngineTestUtil.updateDEDataRecordCollection(
			_siteMember, _group, deDataRecordCollection,
			_deDataRecordCollectionService);
	}

	protected void setUpPermissionThreadLocal() throws Exception {
		_originalPermissionChecker =
			PermissionThreadLocal.getPermissionChecker();
	}

	@DeleteAfterTestRun
	private User _adminUser;

	@Inject(type = DEDataDefinitionService.class)
	private DEDataDefinitionService _deDataDefinitionService;

	@Inject(type = DEDataRecordCollectionService.class)
	private DEDataRecordCollectionService _deDataRecordCollectionService;

	@DeleteAfterTestRun
	private Group _group;

	private PermissionChecker _originalPermissionChecker;

	@DeleteAfterTestRun
	private User _siteMember;

	@Inject(type = UserLocalService.class)
	private UserLocalService _userLocalService;

}