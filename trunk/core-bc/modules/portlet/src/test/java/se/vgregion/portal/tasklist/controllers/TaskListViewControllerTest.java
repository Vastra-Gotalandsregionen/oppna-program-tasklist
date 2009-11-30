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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.ReadOnlyException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.mock.web.portlet.MockPortletConfig;
import org.springframework.mock.web.portlet.MockPortletPreferences;
import org.springframework.mock.web.portlet.MockRenderRequest;
import org.springframework.mock.web.portlet.MockRenderResponse;
import org.springframework.ui.ModelMap;
import org.springframework.validation.ObjectError;

import se.vgregion.portal.tasklist.domain.Task;
import se.vgregion.portal.tasklist.mocks.LoggerMock;
import se.vgregion.portal.tasklist.services.TaskListService;

/**
 * @author jonas
 * 
 */
public class TaskListViewControllerTest {

    private static final String USER_ID = String.valueOf(1);

    public static final String ERROR_DATA_ACCESS_ERROR_VALUE = "Kunde ej få kontakt med databasen. Var god försök senare.";

    public static final String ERROR_DATA_ACCESS_ERROR = "error.DataAccessError";

    private TaskListViewController taskListViewController;
    private LoggerMock logger;
    private MockPortletConfig mockPortletConfig;
    private ModelMap mockModelMap;
    private MockRenderRequest mockPortletRequest;
    private MockRenderResponse mockPortletResponse;
    private MockPortletPreferences mockPortletPreferences;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        mockPortletConfig = new MockPortletConfig();
        mockPortletConfig.setResourceBundle(new Locale("sv"), new ResourceBundleMock());

        taskListViewController = new TaskListViewController();
        logger = new LoggerMock();
        taskListViewController.setLogger(logger);
        taskListViewController.setTaskListService(new TaskListViewControllerTest.MockTaskListService());
        taskListViewController.setPortletConfig(mockPortletConfig);
        mockModelMap = new ModelMap();
        mockPortletRequest = getMockRenderRequest();
        mockPortletResponse = new MockRenderResponse();
        mockPortletResponse.setLocale(new Locale("sv"));
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
    public void getGetTaskListDataAccessException() {
        prepareTaskViewListControllerForDataAccessExceptionThrowing();
        String viewTaskListReturnPageName = taskListViewController.viewTaskList(mockModelMap, mockPortletRequest,
                mockPortletResponse, mockPortletPreferences);
        ObjectError objectError = (ObjectError) mockModelMap.get("errors");
        assertEquals(ERROR_DATA_ACCESS_ERROR_VALUE, objectError.getDefaultMessage());
        assertEquals(TaskListViewController.VIEW_TASKS, viewTaskListReturnPageName);
        assertEquals("Error when trying to fetch tasks for user " + USER_ID + ".", logger.getMessage());
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

    /**
     * 
     */
    private void prepareTaskViewListControllerForDataAccessExceptionThrowing() {
        MockTaskListService mockTaskListService = new TaskListViewControllerTest.MockTaskListService();
        mockTaskListService.throwDataAccessException = true;
        taskListViewController.setTaskListService(mockTaskListService);
    }

    private MockRenderRequest getMockRenderRequest() throws ReadOnlyException {
        MockRenderRequest mockRenderRequest = new MockRenderRequest();
        // Create user login id attribute.
        Map<String, String> userInfo = new HashMap<String, String>();
        userInfo.put(PortletRequest.P3PUserInfos.USER_LOGIN_ID.toString(), USER_ID);
        mockRenderRequest.setAttribute(PortletRequest.USER_INFO, userInfo);
        return mockRenderRequest;
    }

    class ResourceBundleMock extends ListResourceBundle {
        private Object[][] contents = new Object[][] { { ERROR_DATA_ACCESS_ERROR, ERROR_DATA_ACCESS_ERROR_VALUE } };

        @Override
        protected Object[][] getContents() {
            return contents;
        }
    }

    /**
     * @author jonas
     * 
     */
    public class MockTaskListService implements TaskListService {

        private boolean throwDataAccessException;

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

            List<Task> tasks = new ArrayList<Task>();
            if ("1".equals(userId)) {
                tasks.add(new Task());
            }
            return tasks;
        }

        /**
         * @inheritDoc
         */
        @Override
        public boolean addTask(Task task) {
            throw new UnsupportedOperationException("TODO: Implement this method");
        }
    }
}
