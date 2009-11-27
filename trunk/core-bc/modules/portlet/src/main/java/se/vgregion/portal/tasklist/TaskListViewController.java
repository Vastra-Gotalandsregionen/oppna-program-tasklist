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
package se.vgregion.portal.tasklist;

import java.util.List;
import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import se.vgregion.portal.tasklist.domain.Task;
import se.vgregion.portal.tasklist.service.TaskListService;

/**
 * @author jonas liljenfeldt
 * @author david bennehult
 * 
 */
public class TaskListViewController {
    /**
     * View tasks page name.
     */
    public static final String VIEW_TASKS = "viewTasks";

    @se.vgregion.portal.tasklist.service.Logger
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

    @RenderMapping
    public String viewTaskList(ModelMap model, RenderRequest request, RenderResponse response,
            PortletPreferences preferences) {
        logger.debug("Creating database structure...");
        Map<String, ?> userInfo = (Map<String, ?>) request.getAttribute(PortletRequest.USER_INFO);
        String userId = "";
        if (userInfo != null) {
            userId = (String) userInfo.get(PortletRequest.P3PUserInfos.USER_LOGIN_ID.toString());
        }
        List<Task> taskList = taskListService.getTaskList(userId);
        model.addAttribute("taskList", taskList);

        return VIEW_TASKS;
    }
}
