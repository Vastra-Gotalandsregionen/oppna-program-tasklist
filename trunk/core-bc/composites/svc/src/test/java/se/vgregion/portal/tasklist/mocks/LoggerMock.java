/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

/**
 * 
 */
package se.vgregion.portal.tasklist.mocks;

import org.slf4j.Logger;
import org.slf4j.Marker;

/**
 * 
 */
public class LoggerMock implements Logger {

    private String message;

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void debug(String msg) {
        message = msg;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void debug(String format, Object arg) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void debug(String format, Object[] argArray) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void debug(String msg, Throwable t) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void debug(Marker marker, String msg) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void debug(String format, Object arg1, Object arg2) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void debug(Marker marker, String format, Object arg) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void debug(Marker marker, String format, Object[] argArray) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void error(String msg) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void error(String format, Object arg) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void error(String format, Object[] argArray) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void error(String msg, Throwable t) {
        message = msg;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void error(Marker marker, String msg) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void error(String format, Object arg1, Object arg2) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void error(Marker marker, String format, Object arg) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void error(Marker marker, String format, Object[] argArray) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void error(Marker marker, String msg, Throwable t) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getName() {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void info(String msg) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void info(String format, Object arg) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void info(String format, Object[] argArray) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void info(String msg, Throwable t) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void info(Marker marker, String msg) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void info(String format, Object arg1, Object arg2) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void info(Marker marker, String format, Object arg) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void info(Marker marker, String format, Object[] argArray) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void info(Marker marker, String msg, Throwable t) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isDebugEnabled() {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isDebugEnabled(Marker marker) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isErrorEnabled() {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isErrorEnabled(Marker marker) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isInfoEnabled() {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isInfoEnabled(Marker marker) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isTraceEnabled() {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isTraceEnabled(Marker marker) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isWarnEnabled() {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isWarnEnabled(Marker marker) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void trace(String msg) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void trace(String format, Object arg) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void trace(String format, Object[] argArray) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void trace(String msg, Throwable t) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void trace(Marker marker, String msg) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void trace(String format, Object arg1, Object arg2) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void trace(Marker marker, String format, Object arg) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void trace(Marker marker, String format, Object[] argArray) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void warn(String msg) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void warn(String format, Object arg) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void warn(String format, Object[] argArray) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void warn(String msg, Throwable t) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void warn(Marker marker, String msg) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void warn(String format, Object arg1, Object arg2) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void warn(Marker marker, String format, Object arg) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void warn(Marker marker, String format, Object[] argArray) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }
}
