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
package se.vgregion.portal.tasklist.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.ReadOnlyException;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.WriterAppender;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.mock.web.portlet.MockPortletConfig;
import org.springframework.mock.web.portlet.MockPortletPreferences;
import org.springframework.mock.web.portlet.MockRenderRequest;
import org.springframework.mock.web.portlet.MockRenderResponse;
import org.springframework.mock.web.portlet.MockResourceRequest;
import org.springframework.mock.web.portlet.MockResourceResponse;
import org.springframework.ui.ModelMap;
import org.springframework.validation.ObjectError;

import se.vgregion.portal.tasklist.domain.Priority;
import se.vgregion.portal.tasklist.domain.Status;
import se.vgregion.portal.tasklist.domain.Task;
import se.vgregion.portal.tasklist.mocks.LoggerMock;
import se.vgregion.portal.tasklist.services.TaskListService;

/**
 * @author jonas
 * @author david
 */
public class TaskListViewControllerTest {

    public static final String ERROR_DATA_ACCESS_ERROR_VALUE = "Kunde ej få kontakt med databasen. Var god försök senare.";
    public static final String ERROR_DATA_ACCESS_ERROR = "error.DataAccessError";
    private static final String USER_ID = String.valueOf(1);

    private TaskListViewController taskListViewController;
    private LoggerMock logger;
    private MockPortletConfig mockPortletConfig;
    private ModelMap mockModelMap;
    private MockRenderRequest mockPortletRequest;
    private MockRenderResponse mockPortletResponse;
    private MockResourceRequest mockResourceRequest;
    private MockResourceResponse mockResourceResponse;
    private MockPortletPreferences mockPortletPreferences;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        mockPortletConfig = new MockPortletConfig();
        mockPortletConfig.setResourceBundle(new Locale("sv"), new ResourceBundleMock());

        taskListViewController = new TaskListViewController();
        taskListViewController.setTaskListService(new TaskListViewControllerTest.MockTaskListService());
        taskListViewController.setPortletConfig(mockPortletConfig);
        mockModelMap = new ModelMap();
        mockPortletRequest = getMockRenderRequest();
        mockPortletResponse = new MockRenderResponse();
        mockPortletResponse.setLocale(new Locale("sv"));
        mockResourceRequest = getMockResourceRequest();
        mockResourceResponse = new MockResourceResponse();
        mockPortletPreferences = new MockPortletPreferences();
    }

    /**
     * Test method for
     * {@link se.vgregion.portal.tasklist.controllers.TaskListViewController#viewTaskList(org.springframework.ui.ModelMap, javax.portlet.RenderRequest, javax.portlet.RenderResponse, javax.portlet.PortletPreferences)}
     * .
     * 
     * @throws ReadOnlyException
     */
    @Test
    public void testViewTaskList() throws ReadOnlyException {
        String viewTaskList = taskListViewController.viewTaskList(mockModelMap, mockPortletRequest,
                mockPortletResponse, mockPortletPreferences);

        assertEquals(1, ((List<Task>) mockModelMap.get("taskList")).size());
        assertEquals(TaskListViewController.VIEW_TASKS, viewTaskList);
    }

    @Test
    public void testViewTaskListWithUserIdNull() {
        MockRenderRequest mockPortletRequest = new MockRenderRequest();
        MockRenderResponse mockPortletResponse = new MockRenderResponse();
        String viewTaskList = taskListViewController.viewTaskList(mockModelMap, mockPortletRequest,
                mockPortletResponse, mockPortletPreferences);
        assertEquals(0, ((List<Task>) mockModelMap.get("taskList")).size());
        assertEquals(TaskListViewController.VIEW_TASKS, viewTaskList);
    }

    @Test
    public void getGetTaskListDataAccessException() {
        StringWriter logWriter = getLoggerView(Level.ERROR);
        prepareTaskViewListControllerForDataAccessExceptionThrowing();
        String viewTaskListReturnPageName = taskListViewController.viewTaskList(mockModelMap, mockPortletRequest,
                mockPortletResponse, mockPortletPreferences);
        ObjectError objectError = (ObjectError) mockModelMap.get("errors");
        assertEquals(ERROR_DATA_ACCESS_ERROR_VALUE, objectError.getDefaultMessage());
        assertEquals(TaskListViewController.VIEW_TASKS, viewTaskListReturnPageName);
        assertTrue(logWriter.toString().contains("Error when trying to fetch tasks for user " + USER_ID + "."));
    }

    @Test
    public void getGetTaskListDataAccessExceptionWithoutResourceBundle() {
        prepareTaskViewListControllerForDataAccessExceptionThrowing();
        mockPortletConfig.setResourceBundle(new Locale("sv"), null);
        String viewTaskListReturnPageName = taskListViewController.viewTaskList(mockModelMap, mockPortletRequest,
                mockPortletResponse, mockPortletPreferences);
        ObjectError objectError = (ObjectError) mockModelMap.get("errors");
        assertEquals(TaskListViewController.ERROR_WHEN_ACCESSING_DATA_SOURCE, objectError.getDefaultMessage());
        assertEquals(TaskListViewController.VIEW_TASKS, viewTaskListReturnPageName);
    }

    @Test
    public void testHandleRequest() throws IOException {
        MockTaskListService mockTaskListService = new TaskListViewControllerTest.MockTaskListService();
        taskListViewController.setTaskListService(mockTaskListService);
        // Test add task
        String description = "add description";
        String priority = "LOW";
        String dueDate = "2009-12-31";
        String statusAdd = "OPEN";
        String statusUpdate = "CLOSED";
        taskListViewController.handleRequest(mockResourceRequest, mockResourceResponse, "", description, priority,
                dueDate, statusAdd);
        // Get task that should have been updated
        Task taskShatShouldHaveBeenAdded = getTestTaskShatShouldHaveBeenAdded(description, dueDate);
        assertEquals(taskShatShouldHaveBeenAdded, mockTaskListService.getTaskList(USER_ID).get(0));

        // Test updated task
        taskListViewController.handleRequest(mockResourceRequest, mockResourceResponse, "1", description,
                priority, dueDate, statusUpdate);
        // Get task that should have been updated
        Task taskShatShouldHaveBeenUpdated = getTestTaskShatShouldHaveBeenUpdated("1", description, dueDate);
        assertEquals(taskShatShouldHaveBeenUpdated, mockTaskListService.getTaskList(USER_ID).get(0));
    }

    @Test
    public void testDeleteTask() throws NumberFormatException, IOException {
        taskListViewController.deleteTask(mockResourceRequest, mockResourceResponse, Long.parseLong("1"));
        assertEquals("text/xml", mockResourceResponse.getContentType());
    }

    /**
     * @param description
     * @param dueDate
     * @return
     */
    private Task getTestTaskShatShouldHaveBeenUpdated(String taskId, String description, String dueDate) {
        Task taskThatShouldHaveBeenUpdated = new Task();
        taskThatShouldHaveBeenUpdated.setTaskId(Long.parseLong(taskId));
        taskThatShouldHaveBeenUpdated.setDescription(description);
        taskThatShouldHaveBeenUpdated.setPriority(Priority.LOW);
        taskThatShouldHaveBeenUpdated.setUserId(USER_ID);
        Date dueDateObj = null;
        try {
            dueDateObj = new SimpleDateFormat("yyyy-MM-dd").parse(dueDate);
        } catch (ParseException e) {
            logger.warn("Invalid due date.");
        }
        taskThatShouldHaveBeenUpdated.setDueDate(new java.sql.Date(dueDateObj.getTime()));
        taskThatShouldHaveBeenUpdated.setStatus(Status.CLOSED);
        return taskThatShouldHaveBeenUpdated;
    }

    /**
     * @param description
     * @param dueDate
     */
    private Task getTestTaskShatShouldHaveBeenAdded(String description, String dueDate) {
        Task taskThatShouldHaveBeenAdded = new Task();
        taskThatShouldHaveBeenAdded.setDescription(description);
        taskThatShouldHaveBeenAdded.setPriority(Priority.LOW);
        taskThatShouldHaveBeenAdded.setUserId(USER_ID);
        Date dueDateObj = null;
        try {
            dueDateObj = new SimpleDateFormat("yyyy-MM-dd").parse(dueDate);
        } catch (ParseException e) {
            logger.warn("Invalid due date.");
        }
        taskThatShouldHaveBeenAdded.setDueDate(new java.sql.Date(dueDateObj.getTime()));
        taskThatShouldHaveBeenAdded.setStatus(Status.OPEN);
        return taskThatShouldHaveBeenAdded;
    }

    private void prepareTaskViewListControllerForDataAccessExceptionThrowing() {
        MockTaskListService mockTaskListService = new TaskListViewControllerTest.MockTaskListService();
        mockTaskListService.throwDataAccessException = true;
        taskListViewController.setTaskListService(mockTaskListService);
    }

    private MockResourceRequest getMockResourceRequest() throws ReadOnlyException {
        MockResourceRequest mockResourceRequest = new MockResourceRequest();
        // Create user login id attribute.
        Map<String, String> userInfo = new HashMap<String, String>();
        userInfo.put(PortletRequest.P3PUserInfos.USER_LOGIN_ID.toString(), USER_ID);
        mockResourceRequest.setAttribute(PortletRequest.USER_INFO, userInfo);
        return mockResourceRequest;
    }

    private MockRenderRequest getMockRenderRequest() throws ReadOnlyException {
        MockRenderRequest mockRenderRequest = new MockRenderRequest();
        // Create user login id attribute.
        Map<String, String> userInfo = new HashMap<String, String>();
        userInfo.put(PortletRequest.P3PUserInfos.USER_LOGIN_ID.toString(), USER_ID);
        mockRenderRequest.setAttribute(PortletRequest.USER_INFO, userInfo);
        return mockRenderRequest;
    }

    private StringWriter getLoggerView(Level logLevel) {
        Logger logger = Logger.getLogger(TaskListViewController.class);
        logger.setLevel(logLevel);
        final StringWriter writer = new StringWriter();
        Appender appender = new WriterAppender(new SimpleLayout(), writer);
        logger.addAppender(appender);
        return writer;
    }

    static class ResourceBundleMock extends ListResourceBundle {
        private Object[][] contents = new Object[][] { { ERROR_DATA_ACCESS_ERROR, ERROR_DATA_ACCESS_ERROR_VALUE } };

        @Override
        protected Object[][] getContents() {
            return contents;
        }
    }

    static class MockTaskListService implements TaskListService {
        private boolean throwDataAccessException;
        private List<Task> taskList = new ArrayList<Task>();

        /**
         * @param throwDataAccessException
         *            the throwDataAccessException to set
         */
        public void setThrowDataAccessException(boolean throwDataAccessException) {
            this.throwDataAccessException = throwDataAccessException;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public List<Task> getTaskList(String userId) {
            if (throwDataAccessException) {
                throw new DataAccessException("Fake error") {
                    private static final long serialVersionUID = 1L;
                };
            }

            if ("1".equals(userId)) {
                taskList.add(new Task());
            }
            return taskList;
        }

        /**
         * @inheritDoc
         */
        @Override
        public boolean addTask(Task task) {
            taskList.add(task);
            return true;
        }

        /**
         * @inheritDoc
         */
        @Override
        public boolean deleteTask(long taskId) {
            if (taskList.size() > 0) {
                taskList.remove(0);
            }
            return true;
        }

        /**
         * @inheritDoc
         */
        @Override
        public boolean updateTask(Task task) {
            taskList.set(0, task);
            return true;
        }
    }
}
