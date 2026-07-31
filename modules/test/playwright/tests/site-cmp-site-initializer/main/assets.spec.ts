/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {expect, mergeTests} from '@playwright/test';

import {dataApiHelpersTest} from '../../../fixtures/dataApiHelpersTest';
import {loginTest} from '../../../fixtures/loginTest';
import {clickAndExpectToBeVisible} from '../../../utils/clickAndExpectToBeVisible';
import getRandomString from '../../../utils/getRandomString';
import {cmsPagesTest} from '../../site-cms-site-initializer/main/fixtures/cmsPagesTest';
import {cmpPagesTest} from './fixtures/cmpPagesTest';
import {linkAssetToCMPTask} from './utils/linkAssetToCMPTask';

const test = mergeTests(
	cmpPagesTest,
	cmsPagesTest,
	dataApiHelpersTest,
	loginTest()
);

const CMP_PROJECT = 'cmp/projects';
const CMP_TASK = 'cmp/tasks';

test(
	'Info panel opens without crashing when showing details for a related asset',
	{tag: ['@LPD-97663']},
	async ({apiHelpers, page, projectPage, projectsPage}) => {
		const assetTitle = `Asset ${getRandomString()}`;
		const projectTitle = `Project ${getRandomString()}`;

		let project;
		let task;

		await test.step('Create a project and a task', async () => {
			project = await apiHelpers.objectEntry.postObjectEntry(
				{
					title: projectTitle,
				},
				CMP_PROJECT
			);

			task = await apiHelpers.objectEntry.postObjectEntry(
				{
					r_cmpProjectToCMPTasks_c_cmpProjectId: project.id,
					title: getRandomString(),
				},
				CMP_TASK,
				project.scopeKey
			);
		});

		await test.step('Associate an asset to the task', async () => {
			const space =
				await apiHelpers.headlessAssetLibrary.createAssetLibrary({
					name: getRandomString(),
					settings: {trashEnabled: true},
					type: 'Space',
				});

			const asset = await apiHelpers.objectEntry.postObjectEntry(
				{
					objectEntryFolderExternalReferenceCode: 'L_CONTENTS',
					title: assetTitle,
				},
				'cms/basic-web-contents',
				space.name
			);

			await linkAssetToCMPTask({
				apiHelpers,
				asset,
				assetObjectDefinitionExternalReferenceCode:
					'L_CMS_BASIC_WEB_CONTENT',
				scopeKey: project.scopeKey,
				task,
			});
		});

		await test.step('Open the project Assets tab', async () => {
			await projectsPage.goto();

			await projectsPage.getProject(projectTitle).click();

			await clickAndExpectToBeVisible({
				target: page.getByRole('button', {name: assetTitle}),
				trigger: projectPage.assetsTab,
			});
		});

		await test.step('Click the Asset details option and assert it renders', async () => {
			await page.getByRole('button', {name: assetTitle}).click();

			await page.getByRole('menuitem', {name: 'Show Details'}).click();

			await expect(
				page.getByRole('heading', {name: assetTitle})
			).toBeVisible();
		});
	}
);
