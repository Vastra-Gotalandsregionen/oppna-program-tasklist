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
package se.vgregion.portal.tasklist.services;

import java.util.List;

import se.vgregion.portal.tasklist.domain.Task;

/**
 *
 */
public interface TaskListService {

    /**
     * Retrieve tasks for specified user.
     * 
     * @param userId
     *            User to retrieve tasks for.
     * @return task list for specified user
     */
    List<Task> getTaskList(String userId);

    /**
     * Persist new task.
     * 
     * @param task
     *            to be added
     * @return true if persistence was successful, false otherwise.
     */
    boolean addTask(Task task);

    /**
     * Update existing task.
     * 
     * @param task
     *            To be updated.
     * @return True if task was updated, false otherwise.
     */
    boolean updateTask(Task task);

    /**
     * Delete existing task.
     * 
     * @param taskId
     *            Task id of task to be deleted.
     * @return true if task is successfully deleted, false otherwise.
     */
    boolean deleteTask(long taskId);
}
