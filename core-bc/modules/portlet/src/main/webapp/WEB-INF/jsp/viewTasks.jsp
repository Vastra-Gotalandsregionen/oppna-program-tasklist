<%--

    Copyright 2010 Västra Götalandsregionen

      This library is free software; you can redistribute it and/or modify
      it under the terms of version 2.1 of the GNU Lesser General Public
      License as published by the Free Software Foundation.

      This library is distributed in the hope that it will be useful,
      but WITHOUT ANY WARRANTY; without even the implied warranty of
      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
      GNU Lesser General Public License for more details.

      You should have received a copy of the GNU Lesser General Public
      License along with this library; if not, write to the
      Free Software Foundation, Inc., 59 Temple Place, Suite 330,
      Boston, MA 02111-1307  USA


--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions' %>

<portlet:actionURL escapeXml="false" var="formAction"/>
<portlet:resourceURL id="save" escapeXml="false" var="saveResource"/>
<portlet:resourceURL id="delete" escapeXml="false" var="deleteResource"/>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/vgr-tasklist.js"></script>

<div id="taskList" class="task-list-wrap">
	<ul class="list tasks">
		<c:forEach items="${taskList}" var="task">
			<li id="task_${task.taskId}" class="task-item priority-${fn:toLowerCase(task.priority)} ${task.status== 'CLOSED' ? 'done' : ''}">
				<input type="checkbox" class="todo" ${task.status== 'CLOSED' ? 'checked="true"' : ''} />
				<label class="task-label">${task.description}</label>
				<div ${task.status== 'CLOSED' ? 'class="task-edit-controls aui-helper-hidden"' : 'class="task-edit-controls"'}>
					<ul class="task-edit-controls-list clearfix">
						<li class="edit-task">
	       					<a href="#">
	       						<span>&Auml;ndra uppgift</span>
	       					</a>
						</li>
						<li class="delete-task">
	       					<a href="#">
	       						<span>Ta bort uppgift</span>
	       					</a>
						</li>
						<li class="task-due-date">${task.dueDate}</li>
						<li class="task-priority aui-helper-hidden">${task.priority}</li>
					</ul>
				</div>
               </li>
		</c:forEach>
	</ul>
	
	<div class="add-wrap">
		<a id="<portlet:namespace />addTask" href="#">Lägg till ny uppgift</a>
	</div>
	
	<div class="aui-helper-hidden">
		<form method="post" name="editTaskForm" id="<portlet:namespace />editTaskForm" class="aui-form" action="">
			<input id="taskId" name="taskId" type="hidden" value="" />
			<fieldset class="aui-fieldset aui-w100">
			<div class="aui-fieldset-content aui-column-content">
				<span class="aui-field aui-field-text">
					<span class="aui-field-content">
						<label for="description" class="aui-field-label">Beskrivning *</label>
						<span class="aui-field-element">
							<input type="text" value="" style="width: 150px;" name="description" id="description" class="aui-field-input aui-field-input-text">
						</span>
					</span>
				</span>
				<span class="aui-field aui-field-text">
					<span class="aui-field-content">
						<label for="dueDate" class="aui-field-label">Klart datum *</label>
						<span class="aui-field-element">
							<input type="text" value="" style="width: 150px;" name="dueDate" id="dueDate" class="aui-field-input aui-field-input-text">
						</span>
					</span>
				</span>
				<span class="aui-field aui-field-select aui-field-menu">
					<span class="aui-field-content">
						<label for="priority" class="aui-field-label">Prioritet</label>
						<span class="aui-field-element ">
							<select name="priority" id="priority" class="aui-field-input aui-field-input-select aui-field-input-menu">
								<option value="LOW">L&aring;g</option>
								<option value="MEDIUM">Medium</option>
								<option value="HIGH">H&ouml;g</option>
							</select>
						</span>
					</span>
				</span>
			</div>
			</fieldset>
			<fieldset class="aui-fieldset aui-w100">
				<div class="aui-dialog-button-container">
					<a href="#" class="aui-dialog-button link-button" id="<portlet:namespace />saveTaskBtn">Spara</a>
					<a href="#" class="aui-dialog-button link-button" id="<portlet:namespace />cancelTaskEditBtn">Avbryt</a>
				</div>		
			</fieldset>
		</form>
	</div>
	
</div>

<script type="text/javascript">
	AUI().ready('vgr-tasklist', function(A) {
		var vgrTasklist = new A.VGRTasklist({
			addTaskNode: '#<portlet:namespace />addTask',
			cancelTaskEditBtn: '#<portlet:namespace />cancelTaskEditBtn',
			contentBox: '#taskList',
			cssDueDate: '.task-due-date',
			cssEditControls: '.task-edit-controls',
			editTaskForm: '#<portlet:namespace />editTaskForm',
			saveTaskBtn: '#<portlet:namespace />saveTaskBtn',
			portletNamespace: '<portlet:namespace />',
			taskIdPrefix: 'task_',
			urlDelete: '${deleteResource}',
			urlSave: '${saveResource}'
		}).render();
	});
</script>