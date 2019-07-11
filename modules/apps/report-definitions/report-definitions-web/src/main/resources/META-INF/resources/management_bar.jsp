<%--
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
--%>

<%@ include file="/init.jsp" %>

<clay:management-toolbar
	actionDropdownItems="<%= displayContext.getActionItemsDropdownItems() %>"
	clearResultsURL="<%= displayContext.getClearResultsURL() %>"
	componentId="reportDefinitionsManagementToolbar"
	creationMenu="<%= displayContext.getCreationMenu() %>"
	disabled="<%= displayContext.isDisabledManagementBar() %>"
	filterDropdownItems="<%= displayContext.getFilterItemsDropdownItems() %>"
	itemsTotal="<%= displayContext.getTotalItems() %>"
	namespace="<%= renderResponse.getNamespace() %>"
	searchActionURL="<%= displayContext.getSearchActionURL() %>"
	searchContainerId="<%= displayContext.getSearchContainerId() %>"
	searchFormName="fm1"
	selectable="<%= !user.isDefaultUser() %>"
	sortingOrder="<%= displayContext.getOrderByType() %>"
	sortingURL="<%= displayContext.getSortingURL() %>"
	viewTypeItems="<%= displayContext.getViewTypesItems() %>"
/>