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

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import se.vgregion.portal.tasklist.domain.Task;

/**
 *
 */
public class TaskListServiceImplTest {

    /**
     * 
     */
    private static final String userId = "1";
    private TaskListServiceImpl taskListServiceImpl;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        taskListServiceImpl = new TaskListServiceImpl();
    }

    /**
     * Test method for
     * {@link se.vgregion.portal.tasklist.service.TaskListServiceImpl#getTaskList(java.lang.String)}.
     */
    @Test
    @Ignore
    public void testGetTaskList() {
        List<Task> taskList = taskListServiceImpl.getTaskList(userId);
        // assertNotNull(taskList);
    }
}
