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
package se.vgregion.portal.tasklist;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.ReadOnlyException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.portlet.MockPortletPreferences;
import org.springframework.mock.web.portlet.MockRenderRequest;
import org.springframework.mock.web.portlet.MockRenderResponse;
import org.springframework.ui.ModelMap;

import se.vgregion.portal.tasklist.domain.Task;
import se.vgregion.portal.tasklist.mocks.LoggerMock;
import se.vgregion.portal.tasklist.service.TaskListService;

/**
 * @author jonas
 * 
 */
public class TaskListViewControllerTest {

    /**
     * @author jonas
     * 
     */
    public class MockTaskListService implements TaskListService {

        /**
         * @inheritDoc
         */
        @Override
        public List<Task> getTaskList(String userId) {
            List<Task> tasks = new ArrayList<Task>();
            if ("1".equals(userId)) {
                tasks.add(new Task());
            }
            return tasks;
        }
    }

    private static final String USER_ID = String.valueOf(1);

    private TaskListViewController taskListViewController;
    private LoggerMock logger;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        taskListViewController = new TaskListViewController();
        logger = new LoggerMock();
        taskListViewController.setLogger(logger);
        taskListViewController.setTaskListService(new TaskListViewControllerTest.MockTaskListService());
    }

    /**
     * Test method for
     * {@link se.vgregion.portal.tasklist.TaskListViewController#viewTaskList(org.springframework.ui.ModelMap, javax.portlet.RenderRequest, javax.portlet.RenderResponse, javax.portlet.PortletPreferences)}
     * .
     * 
     * @throws ReadOnlyException
     */
    @Test
    public void testViewTaskList() throws ReadOnlyException {

        ModelMap mockModelMap = new ModelMap();
        MockRenderRequest mockPortletRequest = getMockRenderRequest();
        MockRenderResponse mockPortletResponse = new MockRenderResponse();
        MockPortletPreferences mockPortletPreferences = new MockPortletPreferences();

        String viewTaskList = taskListViewController.viewTaskList(mockModelMap, mockPortletRequest,
                mockPortletResponse, mockPortletPreferences);

        assertEquals(1, ((List<Task>) mockModelMap.get("taskList")).size());
        assertEquals(TaskListViewController.VIEW_TASKS, viewTaskList);
    }

    private MockRenderRequest getMockRenderRequest() throws ReadOnlyException {
        MockRenderRequest mockRenderRequest = new MockRenderRequest();
        // Create user login id attribute.
        Map<String, String> userInfo = new HashMap<String, String>();
        userInfo.put(PortletRequest.P3PUserInfos.USER_LOGIN_ID.toString(), USER_ID);
        mockRenderRequest.setAttribute(PortletRequest.USER_INFO, userInfo);
        return mockRenderRequest;
    }

}
