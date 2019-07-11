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

import 'clay-modal';
import {Config} from 'metal-state';
import Component from 'metal-component';
import dom from 'metal-dom';
import Soy from 'metal-soy';
import templates from './ReportDefinitions.soy.js';

class ReportDefinitions extends Component {
	static STATE = {
		spritemap: Config.string().required(),
		strings: Config.object().value({
			cancel: Liferay.Language.get('cancel'),
			ok: Liferay.Language.get('ok'),
			reportDefinitionParameters: Liferay.Language.get(
				'report-definition-parameters'
			)
		})
	};

	attached() {
		const {formBuilderButtonNode} = this;

		this._eventListeners = [
			dom.on(
				formBuilderButtonNode,
				'click',
				this._handleFormBuilderButtonClicked.bind(this)
			)
		];
	}

	_handleFormBuilderButtonClicked() {
		const {formBuilderAppNode} = this;

		formBuilderAppNode.classList.remove('hide');

		this.refs.modal.show();
	}

	_handleModalButtonClicked({currentTarget}) {
		if (currentTarget.classList.contains('btn-primary')) {
			this.refs.modal.emit('hide');
		}
	}

	_handleModalRendered() {
		const {formBuilderAppNode} = this;
		const body = this.refs.modal.element.querySelector('.modal-body');

		formBuilderAppNode.classList.remove('hide');

		dom.append(body, formBuilderAppNode);
	}
}

Soy.register(ReportDefinitions, templates);

export default ReportDefinitions;
