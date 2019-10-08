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

import {Component} from 'metal-component';
import {Config} from 'metal-state';
import React, {useState} from 'react';
import ReactDOM from 'react-dom';
import Soy from 'metal-soy';

function getConnectedReactComponentAdapter(ReactComponent, templates) {
	class ReactComponentAdapter extends Component {
		disposed() {
			ReactDOM.unmountComponentAtNode(this.refs.app);
		}

		rendered(firstRender) {
			if (firstRender) {
				this._mountApp();
			}
		}

		willReceiveState() {
			this._mountApp();
		}

		_mountApp() {

			if (this.refs.app.hasChildNodes()) {
				ReactDOM.unmountComponentAtNode(this.refs.app);
			}

			// eslint-disable-next-line liferay-portal/no-react-dom-render
			ReactDOM.render(
				<ReactComponent
					onChange={payload => this.emit('onchange', {payload})}
					{...this}
				/>,
				this.refs.app
			);
		}
	}

	ReactComponentAdapter.STATE = {
		value: Config.string()
	};

	Soy.register(ReactComponentAdapter, templates);

	return ReactComponentAdapter;
}

export {getConnectedReactComponentAdapter};
export default getConnectedReactComponentAdapter;
