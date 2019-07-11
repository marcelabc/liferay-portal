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

package com.liferay.report.definitions.web.internal.display.context;

import com.liferay.data.engine.renderer.DataLayoutRenderer;
import com.liferay.data.engine.rest.client.dto.v1_0.DataDefinition;
import com.liferay.data.engine.rest.client.dto.v1_0.DataLayout;
import com.liferay.data.engine.rest.client.resource.v1_0.DataDefinitionResource;
import com.liferay.data.engine.rest.client.resource.v1_0.DataLayoutResource;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.NavigationItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.NavigationItemList;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.ViewTypeItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.ViewTypeItemList;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PrefsParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.report.definitions.model.ReportDefinition;
import com.liferay.report.definitions.service.ReportDefinitionLocalService;
import com.liferay.report.definitions.util.comparator.ReportDefinitionModifiedDateComparator;
import com.liferay.report.definitions.util.comparator.ReportDefinitionNameComparator;
import com.liferay.report.definitions.web.internal.configuration.ReportDefinitionConfiguration;
import com.liferay.report.definitions.web.internal.display.context.util.ReportDefinitionsRequestHelper;
import com.liferay.report.definitions.web.internal.search.ReportDefinitionSearch;

import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Bruno Basto
 * @author Leonardo Barros
 */
public class ReportDefinitionsDisplayContext {

	public ReportDefinitionsDisplayContext(
		DataLayoutRenderer dataLayoutRenderer, Portal portal,
		RenderRequest renderRequest, RenderResponse renderResponse,
		ReportDefinitionConfiguration reportDefinitionConfiguration,
		ReportDefinitionLocalService reportDefinitionLocalService) {

		_dataLayoutRenderer = dataLayoutRenderer;
		_portal = portal;
		_renderRequest = renderRequest;
		_renderResponse = renderResponse;
		_reportDefinitionConfiguration = reportDefinitionConfiguration;
		_reportDefinitionsRequestHelper = new ReportDefinitionsRequestHelper(
			renderRequest);
		_reportDefinitionLocalService = reportDefinitionLocalService;
	}

	public List<DropdownItem> getActionItemsDropdownItems() {
		return new DropdownItemList() {
			{
				add(
					dropdownItem -> {
						dropdownItem.putData(
							"action", "deleteReportDefinitions");
						dropdownItem.setIcon("times-circle");
						dropdownItem.setLabel(
							LanguageUtil.get(
								_reportDefinitionsRequestHelper.getRequest(),
								"delete"));
						dropdownItem.setQuickAction(true);
					});
			}
		};
	}

	public String getClearResultsURL() throws PortletException {
		PortletURL portletURL = PortletURLUtil.clone(
			getPortletURL(), _renderResponse);

		portletURL.setParameter("keywords", StringPool.BLANK);

		return portletURL.toString();
	}

	public CreationMenu getCreationMenu() {
		return new CreationMenu() {
			{
				HttpServletRequest httpServletRequest =
					_reportDefinitionsRequestHelper.getRequest();

				ThemeDisplay themeDisplay =
					(ThemeDisplay)httpServletRequest.getAttribute(
						WebKeys.THEME_DISPLAY);

				addPrimaryDropdownItem(
					dropdownItem -> {
						dropdownItem.setHref(
							_renderResponse.createRenderURL(),
							"mvcRenderCommandName", "/edit_report_definition",
							"redirect",
							PortalUtil.getCurrentURL(httpServletRequest),
							"groupId",
							String.valueOf(themeDisplay.getScopeGroupId()));

						dropdownItem.setLabel(
							LanguageUtil.get(
								httpServletRequest, "new-report-definition"));
					});
			}
		};
	}

	public long getDataDefinitionId() {
		ReportDefinition reportDefinition = getReportDefinition();

		if (reportDefinition == null) {
			return 0L;
		}

		return reportDefinition.getDataDefinitionId();
	}

	public long getDataLayoutId() throws Exception {
		ReportDefinition reportDefinition = getReportDefinition();

		if (reportDefinition == null) {
			return 0L;
		}

		DataLayout dataLayout = _getDataLayout(
			_getDataDefinition(reportDefinition.getDataDefinitionId()));

		if (dataLayout == null) {
			return 0L;
		}

		return dataLayout.getId();
	}

	public String getDisplayStyle() {
		if (_displayStyle == null) {
			_displayStyle = getDisplayStyle(
				_renderRequest, _reportDefinitionConfiguration,
				getDisplayViews());
		}

		return _displayStyle;
	}

	public String[] getDisplayViews() {
		return _DISPLAY_VIEWS;
	}

	public List<DropdownItem> getFilterItemsDropdownItems() {
		HttpServletRequest httpServletRequest =
			_reportDefinitionsRequestHelper.getRequest();

		return new DropdownItemList() {
			{
				addGroup(
					dropdownGroupItem -> {
						dropdownGroupItem.setDropdownItems(
							getFilterNavigationDropdownItems());
						dropdownGroupItem.setLabel(
							LanguageUtil.get(
								httpServletRequest, "filter-by-navigation"));
					});

				addGroup(
					dropdownGroupItem -> {
						dropdownGroupItem.setDropdownItems(
							getOrderByDropdownItems());
						dropdownGroupItem.setLabel(
							LanguageUtil.get(httpServletRequest, "order-by"));
					});
			}
		};
	}

	public List<NavigationItem> getNavigationItems() {
		HttpServletRequest httpServletRequest =
			_reportDefinitionsRequestHelper.getRequest();

		return new NavigationItemList() {
			{
				add(
					navigationItem -> {
						navigationItem.setActive(true);
						navigationItem.setHref(getPortletURL());
						navigationItem.setLabel(
							LanguageUtil.get(
								httpServletRequest, "report-definitions"));
					});
			}
		};
	}

	public String getOrderByCol() {
		return ParamUtil.getString(
			_renderRequest, "orderByCol", "modified-date");
	}

	public String getOrderByType() {
		return ParamUtil.getString(_renderRequest, "orderByType", "desc");
	}

	public PortletURL getPortletURL() {
		PortletURL portletURL = _renderResponse.createRenderURL();

		portletURL.setParameter("mvcPath", "/view.jsp");
		portletURL.setParameter("groupId", String.valueOf(getScopeGroupId()));

		String delta = ParamUtil.getString(_renderRequest, "delta");

		if (Validator.isNotNull(delta)) {
			portletURL.setParameter("delta", delta);
		}

		String displayStyle = ParamUtil.getString(
			_renderRequest, "displayStyle");

		if (Validator.isNotNull(displayStyle)) {
			portletURL.setParameter("displayStyle", getDisplayStyle());
		}

		String keywords = getKeywords();

		if (Validator.isNotNull(keywords)) {
			portletURL.setParameter("keywords", keywords);
		}

		String orderByCol = getOrderByCol();

		if (Validator.isNotNull(orderByCol)) {
			portletURL.setParameter("orderByCol", orderByCol);
		}

		String orderByType = getOrderByType();

		if (Validator.isNotNull(orderByType)) {
			portletURL.setParameter("orderByType", orderByType);
		}

		return portletURL;
	}

	public ReportDefinition getReportDefinition() {
		if (_reportDefinition != null) {
			return _reportDefinition;
		}

		_reportDefinition = _reportDefinitionLocalService.fetchReportDefinition(
			getReportDefinitionId());

		return _reportDefinition;
	}

	public String getReportDefinitionAvailableColumns() throws PortalException {
		ReportDefinition reportDefinition = getReportDefinition();

		if (reportDefinition != null) {
			return reportDefinition.getAvailableColumns();
		}

		return ParamUtil.getString(
			_reportDefinitionsRequestHelper.getRequest(), "availableColumns");
	}

	public String getReportDefinitionDescription() throws PortalException {
		ReportDefinition reportDefinition = getReportDefinition();

		if (reportDefinition != null) {
			return reportDefinition.getDescription();
		}

		return ParamUtil.getString(
			_reportDefinitionsRequestHelper.getRequest(), "description");
	}

	public long getReportDefinitionId() {
		if (_reportDefinitionId != 0) {
			return _reportDefinitionId;
		}

		_reportDefinitionId = PrefsParamUtil.getLong(
			_renderRequest.getPreferences(), _renderRequest,
			"reportDefinitionId");

		if (_reportDefinitionId == 0) {
			_reportDefinitionId = getReportDefinitionIdFromSession();
		}

		return _reportDefinitionId;
	}

	public String getReportDefinitionName() throws PortalException {
		ReportDefinition reportDefinition = getReportDefinition();

		if (reportDefinition != null) {
			return reportDefinition.getName();
		}

		return ParamUtil.getString(
			_reportDefinitionsRequestHelper.getRequest(), "name");
	}

	public String getReportDefinitionSortColumns() throws PortalException {
		ReportDefinition reportDefinition = getReportDefinition();

		if (reportDefinition != null) {
			return reportDefinition.getSortColumns();
		}

		return ParamUtil.getString(
			_reportDefinitionsRequestHelper.getRequest(), "sortColumns");
	}

	public long getScopeGroupId() {
		return _reportDefinitionsRequestHelper.getScopeGroupId();
	}

	public SearchContainer<?> getSearch() {
		String displayStyle = getDisplayStyle();

		PortletURL portletURL = getPortletURL();

		portletURL.setParameter("displayStyle", displayStyle);

		ReportDefinitionSearch reportDefinitionSearch =
			new ReportDefinitionSearch(_renderRequest, portletURL);

		String orderByCol = getOrderByCol();
		String orderByType = getOrderByType();

		OrderByComparator<ReportDefinition> orderByComparator =
			getReportDefinitionOrderByComparator(orderByCol, orderByType);

		reportDefinitionSearch.setOrderByCol(orderByCol);
		reportDefinitionSearch.setOrderByComparator(orderByComparator);
		reportDefinitionSearch.setOrderByType(orderByType);

		if (reportDefinitionSearch.isSearch()) {
			reportDefinitionSearch.setEmptyResultsMessage(
				"no-report-definitions-were-found");
		}
		else {
			reportDefinitionSearch.setEmptyResultsMessage(
				"there-are-no-report-definitions");
		}

		setReportDefinitionSearchResults(reportDefinitionSearch);
		setReportDefinitionSearchTotal(reportDefinitionSearch);

		return reportDefinitionSearch;
	}

	public String getSearchActionURL() {
		PortletURL portletURL = _renderResponse.createRenderURL();

		portletURL.setParameter("mvcPath", "/view.jsp");
		portletURL.setParameter("groupId", String.valueOf(getScopeGroupId()));
		portletURL.setParameter("currentTab", "report_definitions");

		return portletURL.toString();
	}

	public String getSearchContainerId() {
		return "reportDefinitions";
	}

	public String getSortingURL() throws Exception {
		PortletURL portletURL = PortletURLUtil.clone(
			getPortletURL(), _renderResponse);

		String orderByType = ParamUtil.getString(_renderRequest, "orderByType");

		portletURL.setParameter(
			"orderByType", orderByType.equals("asc") ? "desc" : "asc");

		return portletURL.toString();
	}

	public int getTotalItems() {
		SearchContainer<?> searchContainer = getSearch();

		return searchContainer.getTotal();
	}

	public List<ViewTypeItem> getViewTypesItems() throws Exception {
		PortletURL portletURL = PortletURLUtil.clone(
			getPortletURL(), _renderResponse);

		return new ViewTypeItemList(portletURL, getDisplayStyle()) {
			{
				String[] viewTypes = getDisplayViews();

				for (String viewType : viewTypes) {
					if (viewType.equals("descriptive")) {
						addListViewTypeItem();
					}
					else {
						addTableViewTypeItem();
					}
				}
			}
		};
	}

	public boolean isDisabledManagementBar() {
		if (hasResults()) {
			return false;
		}

		if (isSearch()) {
			return false;
		}

		return true;
	}

	protected String getDisplayStyle(
		PortletRequest portletRequest,
		ReportDefinitionConfiguration reportDefinitionConfiguration,
		String[] displayViews) {

		String displayStyle = ParamUtil.getString(
			portletRequest, "displayStyle");

		if (!ArrayUtil.contains(displayViews, displayStyle)) {
			displayStyle = displayViews[0];
		}

		return GetterUtil.getString(
			displayStyle, reportDefinitionConfiguration.defaultDisplayView());
	}

	protected List<DropdownItem> getFilterNavigationDropdownItems() {
		return new DropdownItemList() {
			{
				add(
					dropdownItem -> {
						dropdownItem.setActive(true);
						dropdownItem.setHref(
							getPortletURL(), "navigation", "all");
						dropdownItem.setLabel(
							LanguageUtil.get(
								_reportDefinitionsRequestHelper.getRequest(),
								"all"));
					});
			}
		};
	}

	protected String getKeywords() {
		return ParamUtil.getString(_renderRequest, "keywords");
	}

	protected UnsafeConsumer<DropdownItem, Exception> getOrderByDropdownItem(
		String orderByCol) {

		return dropdownItem -> {
			dropdownItem.setActive(orderByCol.equals(getOrderByCol()));
			dropdownItem.setHref(getPortletURL(), "orderByCol", orderByCol);
			dropdownItem.setLabel(
				LanguageUtil.get(
					_reportDefinitionsRequestHelper.getRequest(), orderByCol));
		};
	}

	protected List<DropdownItem> getOrderByDropdownItems() {
		return new DropdownItemList() {
			{
				add(getOrderByDropdownItem("modified-date"));
				add(getOrderByDropdownItem("name"));
			}
		};
	}

	protected long getReportDefinitionIdFromSession() {
		PortletSession portletSession = _renderRequest.getPortletSession();

		return GetterUtil.getLong(
			portletSession.getAttribute("reportDefinitionId"));
	}

	protected OrderByComparator<ReportDefinition>
		getReportDefinitionOrderByComparator(
			String orderByCol, String orderByType) {

		boolean orderByAsc = false;

		if (orderByType.equals("asc")) {
			orderByAsc = true;
		}

		OrderByComparator<ReportDefinition> orderByComparator = null;

		if (orderByCol.equals("modified-date")) {
			orderByComparator = new ReportDefinitionModifiedDateComparator(
				orderByAsc);
		}
		else if (orderByCol.equals("name")) {
			orderByComparator = new ReportDefinitionNameComparator(orderByAsc);
		}

		return orderByComparator;
	}

	protected boolean hasResults() {
		if (getTotalItems() > 0) {
			return true;
		}

		return false;
	}

	protected boolean isSearch() {
		if (Validator.isNotNull(getKeywords())) {
			return true;
		}

		return false;
	}

	protected void setReportDefinitionSearchResults(
		ReportDefinitionSearch reportDefinitionSearch) {

		List<ReportDefinition> results = _reportDefinitionLocalService.search(
			_reportDefinitionsRequestHelper.getCompanyId(),
			_reportDefinitionsRequestHelper.getScopeGroupId(), getKeywords(),
			reportDefinitionSearch.getStart(), reportDefinitionSearch.getEnd(),
			reportDefinitionSearch.getOrderByComparator());

		reportDefinitionSearch.setResults(results);
	}

	protected void setReportDefinitionSearchTotal(
		ReportDefinitionSearch reportDefinitionSearch) {

		int total = _reportDefinitionLocalService.searchCount(
			_reportDefinitionsRequestHelper.getCompanyId(),
			_reportDefinitionsRequestHelper.getScopeGroupId(), getKeywords());

		reportDefinitionSearch.setTotal(total);
	}

	private DataDefinition _getDataDefinition(long dataDefinitionId)
		throws Exception {

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		User user = permissionChecker.getUser();

		HttpServletRequest httpServletRequest =
			_reportDefinitionsRequestHelper.getRequest();

		DataDefinitionResource dataDefinitionResource =
			DataDefinitionResource.builder(
			).authentication(
				user.getEmailAddress(), "test"
			).endpoint(
				_portal.getHost(httpServletRequest),
				httpServletRequest.getServerPort(),
				httpServletRequest.getScheme()
			).build();

		return dataDefinitionResource.getDataDefinition(dataDefinitionId);
	}

	private DataLayout _getDataLayout(DataDefinition dataDefinition)
		throws Exception {

		if (dataDefinition == null) {
			return null;
		}

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		User user = permissionChecker.getUser();

		HttpServletRequest httpServletRequest =
			_reportDefinitionsRequestHelper.getRequest();

		DataLayoutResource dataLayoutResource = DataLayoutResource.builder(
		).authentication(
			user.getEmailAddress(), "test"
		).endpoint(
			_portal.getHost(httpServletRequest),
			httpServletRequest.getServerPort(), httpServletRequest.getScheme()
		).build();

		return dataLayoutResource.getSiteDataLayout(
			dataDefinition.getSiteId(), dataDefinition.getDataDefinitionKey());
	}

	private static final String[] _DISPLAY_VIEWS = {"descriptive", "list"};

	private final DataLayoutRenderer _dataLayoutRenderer;
	private String _displayStyle;
	private final Portal _portal;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;
	private ReportDefinition _reportDefinition;
	private final ReportDefinitionConfiguration _reportDefinitionConfiguration;
	private long _reportDefinitionId;
	private final ReportDefinitionLocalService _reportDefinitionLocalService;
	private final ReportDefinitionsRequestHelper
		_reportDefinitionsRequestHelper;

}