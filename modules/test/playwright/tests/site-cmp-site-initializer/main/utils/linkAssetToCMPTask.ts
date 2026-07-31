/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {DataApiHelpers} from '../../../../helpers/ApiHelpers';

const CMP_TASK_LINK = 'cmp/task-links';

/**
 * Links an asset to a CMP task by writing the "L_CMP_TASK_LINK" row the backend
 * matches on, so the asset surfaces under the task and under its project.
 *
 * The three identifying values mirror what "ObjectEntryLinkService" sends from
 * the product UI, because "CMPLinkedObjectEntryUtil.getLinkedObjectEntryIds"
 * filters the rows on all three and drops the asset when any one disagrees. The
 * class name has to be read at run time rather than spelled out in a spec, since
 * object definition class names carry a random "#XXXX" suffix that differs per
 * environment.
 */
export async function linkAssetToCMPTask({
	apiHelpers,
	asset,
	assetObjectDefinitionExternalReferenceCode,
	scopeKey,
	task,
}: {
	apiHelpers: DataApiHelpers;
	asset: ObjectEntry;
	assetObjectDefinitionExternalReferenceCode: string;
	scopeKey: string;
	task: ObjectEntry;
}): Promise<ObjectEntry> {
	const objectDefinition =
		await apiHelpers.objectAdmin.getObjectDefinitionByExternalReferenceCode(
			assetObjectDefinitionExternalReferenceCode
		);

	return apiHelpers.objectEntry.postObjectEntry(
		{
			classExternalReferenceCode: asset.externalReferenceCode,
			className: objectDefinition.className,
			groupExternalReferenceCode:
				asset.systemProperties?.scope?.externalReferenceCode ?? '',
			r_cmpTaskToCMPTaskLinks_c_cmpTaskId: task.id,
		},
		CMP_TASK_LINK,
		scopeKey
	);
}
