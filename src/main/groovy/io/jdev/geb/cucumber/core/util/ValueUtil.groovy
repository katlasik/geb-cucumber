/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 the original author or authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.jdev.geb.cucumber.core.util

import geb.content.SimplePageContent
import geb.navigator.Navigator
import io.jdev.geb.cucumber.core.CheckedDecoder
import org.openqa.selenium.support.ui.Select

class ValueUtil {

    static String getValue(def field) {
        if(field instanceof String) {
            // someone calling text() in their geb content definition
            field
        } else if((field instanceof Navigator && field.is('select')) || (field instanceof SimplePageContent && field.navigator.is('select'))) {
            // get the text for the selected option, rather than its id value
            new Select(field.getElement(0)).firstSelectedOption.text.trim()
        } else {
            def value = field.value()
            if(!value) {
                // maybe a plain div rather than input field
                value = field.text()
            }
            value?.trim()
        }
    }

    static void hasValue(def field, def expectedValue) {
        if(expectedValue instanceof CheckedDecoder.CheckedState) {
            boolean isChecked = field.value() != false
            boolean wantChecked = expectedValue == CheckedDecoder.CheckedState.checked
            assert isChecked == wantChecked
        } else {
            String fieldValue = getValue(field)
            assert fieldValue == expectedValue as String
        }
    }

    static void enterValue(def field, def value) {
        if(value instanceof CheckedDecoder.CheckedState) {
            boolean isChecked = field.value() != false
            boolean wantChecked = value == CheckedDecoder.CheckedState.checked
            if(isChecked != wantChecked) {
                field.click()
            }
        } else {
            field.value(value)
        }
    }

}
