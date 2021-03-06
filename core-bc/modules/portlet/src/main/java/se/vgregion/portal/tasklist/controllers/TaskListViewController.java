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

package se.vgregion.portal.tasklist.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import se.vgregion.portal.tasklist.domain.Priority;
import se.vgregion.portal.tasklist.domain.Status;
import se.vgregion.portal.tasklist.domain.Task;
import se.vgregion.portal.tasklist.services.TaskListService;

/**
 * Displays and allows editing of tasks.
 * 
 * @author jonas liljenfeldt
 * @author david bennehult
 */
@Controller
@RequestMapping("VIEW")
public class TaskListViewController {

    /**
     * Default error message for DataAccessException.
     */
    public static final String ERROR_WHEN_ACCESSING_DATA_SOURCE = "Error when accessing data source";

    /**
     * View tasks page name.
     */
    public static final String VIEW_TASKS = "viewTasks";

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskListViewController.class);

    @Autowired
    private TaskListService taskListService = null;

    @Autowired
    private PortletConfig portletConfig = null;

    public void setTaskListService(TaskListService taskListService) {
        this.taskListService = taskListService;
    }

    public void setPortletConfig(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }

    /**
     * Shows active tasks for user.
     * 
     * @param model
     *            ModelMap
     * @param request
     *            RenderRequest
     * @param response
     *            RenderResponse
     * @param preferences
     *            PortletPreferences
     * @return View name.
     */
    @RenderMapping()
    public String viewTaskList(ModelMap model, RenderRequest request, RenderResponse response,
            PortletPreferences preferences) {
        LOGGER.debug("Creating database structure...");

        ResourceBundle bundle = portletConfig.getResourceBundle(response.getLocale());

        @SuppressWarnings("unchecked")
        Map<String, ?> attributes = (Map<String, ?>) request.getAttribute(PortletRequest.USER_INFO);
        String userId = getUserId(attributes);

        List<Task> taskList = null;
        if (!"".equals(userId)) {
            try {
                taskList = taskListService.getTaskList(userId);
                // Set number of tasks in TaskList portlet header.
                if (bundle != null) {
                    response.setTitle(bundle.getString("javax.portlet.title") + " (" + taskList.size() + ")");
                }
            } catch (DataAccessException dataAccessException) {
                ObjectError objectError;
                if (bundle != null) {
                    objectError = new ObjectError("DataAccessError", bundle.getString("error.DataAccessError"));
                } else {
                    objectError = new ObjectError("DataAccessError", ERROR_WHEN_ACCESSING_DATA_SOURCE);
                }
                LOGGER.error("Error when trying to fetch tasks for user " + userId + ".", dataAccessException);
                model.addAttribute("errors", objectError);
            }
        } else {
            taskList = new ArrayList<Task>();
        }

        model.addAttribute("taskList", taskList);
        return VIEW_TASKS;
    }

    private String getUserId(Map<String, ?> attributes) {
        String userId = "";
        if (attributes != null) {
            userId = (String) attributes.get(PortletRequest.P3PUserInfos.USER_LOGIN_ID.toString());
        }
        return userId;
    }

    /**
     * Insert or update task.
     * 
     * @param request
     *            ResourceRequest
     * @param response
     *            ResourceResponse
     * @param taskId
     *            If empty, task will be added. Updated otherwise.
     * @param description
     *            Task description.
     * @param priority
     *            Task priority.
     * @param dueDate
     *            Task due date.
     * @param status
     *            Task status.
     * @throws IOException
     *             Thrown if IO exception.
     */
    @ResourceMapping("save")
    public void handleRequest(ResourceRequest request, ResourceResponse response,
            @RequestParam(value = "taskId") String taskId,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "priority") String priority, @RequestParam(value = "dueDate") String dueDate,
            @RequestParam(value = "status", required = false) String status) throws IOException {
        @SuppressWarnings("unchecked")
        Map<String, ?> attributes = (Map<String, ?>) request.getAttribute(PortletRequest.USER_INFO);
        String userId = getUserId(attributes);
        if (StringUtils.isBlank(priority)) {
            priority = Priority.MEDIUM.toString();
        }
        // Spring appears to set a non-existing string to the string value "null"
        if(taskId.equals("null")) {
        	taskId = "";
        }
        
        Task task = createTaskFromParams(taskId, description, priority, userId, dueDate, status);

        if (taskId.equals("")) {
            taskListService.addTask(task);
        } else {
            taskListService.updateTask(task);
        }
        List<Task> taskList = taskListService.getTaskList(userId);
        String generateTaskListxml = generateTaskListxml(taskList);
        response.setContentType("text/xml");
        response.getWriter().print(generateTaskListxml);
    }

    /**
     * Delete specified task.
     * 
     * @param request
     *            ResourceRequest
     * @param response
     *            ResourceResponse
     * @param taskId
     *            If empty, task will be added. Updated otherwise.
     * @throws IOException
     *             Thrown if IO exception.
     */
    @ResourceMapping("delete")
    public void deleteTask(ResourceRequest request, ResourceResponse response,
        @RequestParam(value = "taskId") long taskId) throws IOException {
    	
        taskListService.deleteTask(taskId);
        @SuppressWarnings("unchecked")
        Map<String, ?> attributes = (Map<String, ?>) request.getAttribute(PortletRequest.USER_INFO);
        String userId = getUserId(attributes);
        List<Task> taskList = taskListService.getTaskList(userId);
        String generateTaskListxml = generateTaskListxml(taskList);
        response.setContentType("text/xml");
        response.getWriter().print(generateTaskListxml);
    }

    private String generateTaskListxmlOld(List<Task> taskList) {
        StringBuilder sb = new StringBuilder();
        sb.append("<ul class=\"list tasks\">");
        for (Task task : taskList) {
            sb.append("<li");
            if (task.getStatus() == Status.CLOSED) {
                sb.append(" class=\"done\"");
            }
            sb.append(">");
            sb.append("<input type=\"checkbox\" onclick=\"saveTask('" + task.getTaskId() + "', '");
            sb.append(task.getDescription() + "', '" + task.getPriority() + "', '" + task.getDueDate());
            sb.append("' , this.checked);\" ");
            if (task.getStatus() == Status.CLOSED) {
                sb.append(" checked='true'");
            }
            sb.append(" />");
            sb.append("<label class=\"descriptionLabel\"> &#160;" + task.getDescription() + "</label>");
            sb.append("<img class=\"prioImage\" src=\"/vgr-theme/i/prio-" + task.getPriority() + ".gif\" /> ");
            sb.append(" <br />");
            sb.append("<div");
            if (task.getStatus() == Status.CLOSED) {
                sb.append(" class=\"hidden\"");
            }
            sb.append(">");
            sb.append("<a onclick=\"prepareEdit('" + task.getTaskId() + "', '" + task.getDescription() + "', '"
                    + task.getPriority() + "', '" + task.getDueDate() + "');\" class=\"editTask\" href=\"#\">");
            sb.append("<img src=\"/vgr-theme/i/icons/pencil.png\" />");
            sb.append("</a>");
            sb.append("<a onclick=\"deleteTask('" + task.getTaskId() + "');\">");
            sb.append("<img src=\"/vgr-theme/i/icons/delete.png\" />");
            sb.append("</a>&#160;");
            sb.append(task.getDueDate());
            sb.append("</div>");
            sb.append("</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }
    
    private String generateTaskListxml(List<Task> taskList) {
        StringBuilder sb = new StringBuilder();
        sb.append("<ul class=\"list tasks\">");
        for (Task task : taskList) {
            sb.append("<li id=\"task_" + task.getTaskId() + "\"" + " class=\"task-item priority-" + task.getPriority().toString().toLowerCase());
            if (task.getStatus() == Status.CLOSED) {
                sb.append(" done");
            }
            sb.append("\">");
            
            sb.append("<input type=\"checkbox\" class=\"todo\"");
            if (task.getStatus() == Status.CLOSED) {
                sb.append(" checked='true'");
            }
            sb.append(" />");
            sb.append("<label class=\"task-label\">" + task.getDescription() + "</label>");
            
            sb.append("<div class=\"task-edit-controls");
            if (task.getStatus() == Status.CLOSED) {
                sb.append(" aui-helper-hidden");
            }
            sb.append("\">");
            
            sb.append("<ul class=\"task-edit-controls-list clearfix\">");
            
            sb.append("<li class=\"edit-task\">");
            sb.append("<a href=\"#\">");
            sb.append("<span>&Auml;ndra uppgift</span>");
            sb.append("</a>");
            sb.append("</li>");
            
            sb.append("<li class=\"delete-task\">");
            sb.append("<a href=\"#\">");
            sb.append("<span>Ta bort uppgift</span>");
            sb.append("</a>");
            sb.append("</li>");

            sb.append("<li class=\"task-due-date\">" + task.getDueDate() + "</li>");
            
            sb.append("<li class=\"task-priority aui-helper-hidden\">" + task.getPriority() + "</li>");
            
            sb.append("</ul>");
            
            sb.append("</div>");
        }
        sb.append("</ul>");
        return sb.toString();
    }
    

    private Task createTaskFromParams(String taskId, String description, String priority, String userId,
            String dueDate, String status) {
        Task task = new Task();
        if (!taskId.equals("")) {
            task.setTaskId(Long.valueOf(taskId));
        }
        task.setDescription(description);
        task.setPriority(Priority.valueOf(priority));
        task.setUserId(userId);
        Date dueDateObj = null;
        
        try {
            SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy-MM-dd");
            simpleDateformat.setLenient(false);
            dueDateObj = simpleDateformat.parse(dueDate);
        } catch (ParseException e) {
            LOGGER.warn("Invalid due date. Setting today's date.");
            dueDateObj = new Date();
        }
        task.setDueDate(new java.sql.Date(dueDateObj.getTime()));
        if (status != null) {
            task.setStatus(Status.valueOf(status));
        } else {
            task.setStatus(Status.OPEN);
        }
        return task;
    }
}