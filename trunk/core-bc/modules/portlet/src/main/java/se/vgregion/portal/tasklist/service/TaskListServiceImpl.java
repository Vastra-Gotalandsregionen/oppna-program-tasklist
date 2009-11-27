/**
 * Copyright 2009 Västra Götalandsregionen
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
 */
/**
 * 
 */
package se.vgregion.portal.tasklist.service;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import se.vgregion.portal.tasklist.domain.Task;

/**
 * @author jonas
 * 
 */
public class TaskListServiceImpl implements TaskListService {
    @se.vgregion.portal.tasklist.service.Logger
    private Logger logger;
    @Autowired
    private SimpleJdbcTemplate simpleJdbcTemplate;

    /**
     * @param simpleJdbcTemplate
     *            the simpleJdbcTemplate to set
     */
    public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
        this.simpleJdbcTemplate = simpleJdbcTemplate;
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<Task> getTaskList(String userId) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }
}
