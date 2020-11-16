/* jshint ignore:start */
import Component from 'metal-component';
import Soy from 'metal-soy';

var templates;
goog.loadModule(function(exports) {
var soy = goog.require('soy');
var soydata = goog.require('soydata');
// This file was automatically generated from MockField.soy.
// Please don't edit this file by hand.

/**
 * @fileoverview Templates in namespace ReactFieldAdapter.
 * @public
 */

goog.module('ReactFieldAdapter.incrementaldom');

goog.require('goog.soy.data.SanitizedContent');
var incrementalDom = goog.require('incrementaldom');
goog.require('soy.asserts');
var soyIdom = goog.require('soy.idom');


/**
 * @param {{
 *  fieldType: (!goog.soy.data.SanitizedContent|null|string|undefined)
 * }} opt_data
 * @param {Object<string, *>=} opt_ijData
 * @param {Object<string, *>=} opt_ijData_deprecated
 * @return {void}
 * @suppress {checkTypes}
 */
function $render(opt_data, opt_ijData, opt_ijData_deprecated) {
  opt_ijData = opt_ijData_deprecated || opt_ijData;
  var $$temp;
  opt_data = opt_data || {};
  /** @type {!goog.soy.data.SanitizedContent|null|string|undefined} */
  var fieldType = soy.asserts.assertType(opt_data.fieldType == null || (goog.isString(opt_data.fieldType) || opt_data.fieldType instanceof goog.soy.data.SanitizedContent), 'fieldType', opt_data.fieldType, '!goog.soy.data.SanitizedContent|null|string|undefined');
  incrementalDom.elementOpenStart('div');
      incrementalDom.attr('id', 'fields-rule');
  incrementalDom.elementOpenEnd();
    var $tmp = fieldType;
    switch (goog.isObject($tmp) ? $tmp.toString() : $tmp) {
      case 'checkbox':
        $Checkbox(null, null, opt_ijData);
        break;
      case 'checkbox_multiple':
        $CheckboxMultiple(null, null, opt_ijData);
        break;
      case 'date':
        $Date(null, null, opt_ijData);
        break;
      case 'numeric':
        $Numeric(null, null, opt_ijData);
        break;
      case 'radio':
        $Radio(null, null, opt_ijData);
        break;
      case 'select':
        $Select(null, null, opt_ijData);
        break;
      case 'text':
        $Text(null, null, opt_ijData);
        break;
      case 'validation':
        $Validation(null, null, opt_ijData);
        break;
      default:
        incrementalDom.elementOpenStart('div');
          incrementalDom.attr('class', 'ddm-template-undefined');
      incrementalDom.elementOpenEnd();
        incrementalDom.text('Undefined template.');
      incrementalDom.elementClose('div');
    }
  incrementalDom.elementClose('div');
}
exports.render = $render;
/**
 * @typedef {{
 *  fieldType: (!goog.soy.data.SanitizedContent|null|string|undefined)
 * }}
 */
$render.Params;
if (goog.DEBUG) {
  $render.soyTemplateName = 'ReactFieldAdapter.render';
}


/**
 * @param {Object<string, *>=} opt_data
 * @param {Object<string, *>=} opt_ijData
 * @param {Object<string, *>=} opt_ijData_deprecated
 * @return {void}
 * @suppress {checkTypes}
 */
function $Checkbox(opt_data, opt_ijData, opt_ijData_deprecated) {
  opt_ijData = opt_ijData_deprecated || opt_ijData;
  incrementalDom.elementOpen('p');
    incrementalDom.text('Checkbox Field');
  incrementalDom.elementClose('p');
}
exports.Checkbox = $Checkbox;
if (goog.DEBUG) {
  $Checkbox.soyTemplateName = 'ReactFieldAdapter.Checkbox';
}


/**
 * @param {Object<string, *>=} opt_data
 * @param {Object<string, *>=} opt_ijData
 * @param {Object<string, *>=} opt_ijData_deprecated
 * @return {void}
 * @suppress {checkTypes}
 */
function $CheckboxMultiple(opt_data, opt_ijData, opt_ijData_deprecated) {
  opt_ijData = opt_ijData_deprecated || opt_ijData;
  incrementalDom.elementOpen('p');
    incrementalDom.text('Checkbox Multiple Field');
  incrementalDom.elementClose('p');
}
exports.CheckboxMultiple = $CheckboxMultiple;
if (goog.DEBUG) {
  $CheckboxMultiple.soyTemplateName = 'ReactFieldAdapter.CheckboxMultiple';
}


/**
 * @param {Object<string, *>=} opt_data
 * @param {Object<string, *>=} opt_ijData
 * @param {Object<string, *>=} opt_ijData_deprecated
 * @return {void}
 * @suppress {checkTypes}
 */
function $Date(opt_data, opt_ijData, opt_ijData_deprecated) {
  opt_ijData = opt_ijData_deprecated || opt_ijData;
  incrementalDom.elementOpen('p');
    incrementalDom.text('Date Field');
  incrementalDom.elementClose('p');
}
exports.Date = $Date;
if (goog.DEBUG) {
  $Date.soyTemplateName = 'ReactFieldAdapter.Date';
}


/**
 * @param {Object<string, *>=} opt_data
 * @param {Object<string, *>=} opt_ijData
 * @param {Object<string, *>=} opt_ijData_deprecated
 * @return {void}
 * @suppress {checkTypes}
 */
function $Numeric(opt_data, opt_ijData, opt_ijData_deprecated) {
  opt_ijData = opt_ijData_deprecated || opt_ijData;
  incrementalDom.elementOpen('p');
    incrementalDom.text('Numeric Field');
  incrementalDom.elementClose('p');
}
exports.Numeric = $Numeric;
if (goog.DEBUG) {
  $Numeric.soyTemplateName = 'ReactFieldAdapter.Numeric';
}


/**
 * @param {Object<string, *>=} opt_data
 * @param {Object<string, *>=} opt_ijData
 * @param {Object<string, *>=} opt_ijData_deprecated
 * @return {void}
 * @suppress {checkTypes}
 */
function $Radio(opt_data, opt_ijData, opt_ijData_deprecated) {
  opt_ijData = opt_ijData_deprecated || opt_ijData;
  incrementalDom.elementOpen('p');
    incrementalDom.text('Radio Field');
  incrementalDom.elementClose('p');
}
exports.Radio = $Radio;
if (goog.DEBUG) {
  $Radio.soyTemplateName = 'ReactFieldAdapter.Radio';
}


/**
 * @param {Object<string, *>=} opt_data
 * @param {Object<string, *>=} opt_ijData
 * @param {Object<string, *>=} opt_ijData_deprecated
 * @return {void}
 * @suppress {checkTypes}
 */
function $Select(opt_data, opt_ijData, opt_ijData_deprecated) {
  opt_ijData = opt_ijData_deprecated || opt_ijData;
  incrementalDom.elementOpen('p');
    incrementalDom.text('Select Field');
  incrementalDom.elementClose('p');
}
exports.Select = $Select;
if (goog.DEBUG) {
  $Select.soyTemplateName = 'ReactFieldAdapter.Select';
}


/**
 * @param {Object<string, *>=} opt_data
 * @param {Object<string, *>=} opt_ijData
 * @param {Object<string, *>=} opt_ijData_deprecated
 * @return {void}
 * @suppress {checkTypes}
 */
function $Text(opt_data, opt_ijData, opt_ijData_deprecated) {
  opt_ijData = opt_ijData_deprecated || opt_ijData;
  incrementalDom.elementOpen('p');
    incrementalDom.text('Text Field');
  incrementalDom.elementClose('p');
}
exports.Text = $Text;
if (goog.DEBUG) {
  $Text.soyTemplateName = 'ReactFieldAdapter.Text';
}


/**
 * @param {Object<string, *>=} opt_data
 * @param {Object<string, *>=} opt_ijData
 * @param {Object<string, *>=} opt_ijData_deprecated
 * @return {void}
 * @suppress {checkTypes}
 */
function $Validation(opt_data, opt_ijData, opt_ijData_deprecated) {
  opt_ijData = opt_ijData_deprecated || opt_ijData;
  incrementalDom.elementOpen('p');
    incrementalDom.text('Validation Field');
  incrementalDom.elementClose('p');
}
exports.Validation = $Validation;
if (goog.DEBUG) {
  $Validation.soyTemplateName = 'ReactFieldAdapter.Validation';
}

exports.render.params = ["fieldType"];
exports.render.types = {"fieldType":"string"};
exports.Checkbox.params = [];
exports.Checkbox.types = {};
exports.CheckboxMultiple.params = [];
exports.CheckboxMultiple.types = {};
exports.Date.params = [];
exports.Date.types = {};
exports.Numeric.params = [];
exports.Numeric.types = {};
exports.Radio.params = [];
exports.Radio.types = {};
exports.Select.params = [];
exports.Select.types = {};
exports.Text.params = [];
exports.Text.types = {};
exports.Validation.params = [];
exports.Validation.types = {};
templates = exports;
return exports;

});

class ReactFieldAdapter extends Component {}
Soy.register(ReactFieldAdapter, templates);
export { ReactFieldAdapter, templates };
export default templates;
/* jshint ignore:end */
