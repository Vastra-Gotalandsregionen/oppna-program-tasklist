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
package se.vgregion.portal.tasklist.controllers;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.ui.ModelMap;
import org.springframework.validation.ObjectError;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import se.vgregion.portal.tasklist.domain.Task;
import se.vgregion.portal.tasklist.services.TaskListService;

/**
 * @author jonas liljenfeldt
 * @author david bennehult
 * 
 */
public class TaskListViewController {
    /**
     * Default error message for DataAccessException
     */
    public static final String ERROR_WHEN_ACCESSING_DATA_SOURCE = "Error when accessing data source";

    /**
     * View tasks page name.
     */
    public static final String VIEW_TASKS = "viewTasks";

    @se.vgregion.portal.tasklist.services.Logger
    private Logger logger;

    @Autowired
    private TaskListService taskListService;

    /**
     * @param taskListService
     *            the taskListService to set
     */
    public void setTaskListService(TaskListService taskListService) {
        this.taskListService = taskListService;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Autowired
    private PortletConfig portletConfig;

    public void setPortletConfig(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }

    @RenderMapping
    public String viewTaskList(ModelMap model, RenderRequest request, RenderResponse response,
            PortletPreferences preferences) {
        logger.debug("Creating database structure...");

        ResourceBundle bundle = portletConfig.getResourceBundle(response.getLocale());

        Map<String, ?> userInfo = (Map<String, ?>) request.getAttribute(PortletRequest.USER_INFO);
        String userId = "";
        if (userInfo != null) {
            userId = (String) userInfo.get(PortletRequest.P3PUserInfos.USER_LOGIN_ID.toString());
        }
        List<Task> taskList;
        try {
            taskList = taskListService.getTaskList(userId);
            model.addAttribute("taskList", taskList);
        } catch (DataAccessException dataAccessException) {
            ObjectError objectError;
            if (bundle != null) {
                objectError = new ObjectError("DataAccessError", bundle.getString("error.DataAccessError"));
            } else {
                objectError = new ObjectError("DataAccessError", ERROR_WHEN_ACCESSING_DATA_SOURCE);
            }
            logger.error("Error when trying to fetch tasks for user " + userId + ".", dataAccessException);
            model.addAttribute("errors", objectError);
        }
        return VIEW_TASKS;
    }
}
