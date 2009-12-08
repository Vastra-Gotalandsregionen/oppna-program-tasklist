<%--

    Copyright 2009 Västra Götalandsregionen

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
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions'%>

<!-- These YUI dependencies should be retrieved from another source if "vgr theme" is not present in your portal. -->
<script type="text/javascript" src="/vgr-theme/javascript/yui/yahoo-dom-event.js"></script>
<script type="text/javascript" src="/vgr-theme/javascript/yui/container-min.js"></script>
<script type="text/javascript" src="/vgr-theme/javascript/yui/event.js"></script>
<script type="text/javascript" src="/vgr-theme/javascript/yui/connection.js"></script>

<style type="text/css">
  <%@include file ="/style/styles.css" %>
</style>

<portlet:actionURL escapeXml="false" var="formAction" />
<portlet:resourceURL id="save" escapeXml="false" var="saveResource"/>
<portlet:resourceURL id="delete" escapeXml="false" var="deleteResource"/>

<script type="text/javascript">
	//<!--
	function init() {
		// Build overlay1 based on markup, initially hidden, fixed to the center of the viewport, and 300px wide
		myOverlay = new YAHOO.widget.Overlay("myOverlay", {
			fixedcenter : true,
			visible : false,
			width : "300px"
		});
        myOverlay.render(document.body);
	}
	YAHOO.util.Event.addListener(window, "load", init);

    function prepareEdit(taskId, description, priority, dueDate) {
      document.getElementById('taskId').value = taskId;
      document.getElementById('description').value = description;
      document.getElementById('priority').value = priority;
      document.getElementById('dueDate').value = dueDate;
      myOverlay.show();
    }

    function updateTask() {
      myOverlay.hide();
      var postData = "taskId=" + document.getElementById('taskId').value + 
      "&description=" + document.getElementById('description').value + 
      "&priority=" + document.getElementById('priority').value + 
      "&dueDate=" + document.getElementById('dueDate').value; 
      var sUrl = '${saveResource}';
      var request = YAHOO.util.Connect.asyncRequest('POST', sUrl, callback, postData); 
    }

    function deleteTask(taskId) {
        var postData = "taskId=" + taskId; 
        var sUrl = '${deleteResource}';
        var request = YAHOO.util.Connect.asyncRequest('POST', sUrl, callback, postData); 
    }

    function saveTask(taskId, description, priority, dueDate, status) {
    	  alert("status:" + status);
  	 	  var postData = "taskId=" + taskId + 
          "&description=" + description + 
          "&priority=" + priority + 
          "&dueDate=" + dueDate;
          if (status == true) {
    		  postData += '&status=CLOSED';
          } else {
        	  postData += '&status=OPEN';
          }     
          var sUrl = '${saveResource}';
          var request = YAHOO.util.Connect.asyncRequest('POST', sUrl, callback, postData); 
    }


  var handleSuccess = function(o) { 
    if(o.responseText !== undefined){
     document.getElementById('taskList').innerHTML = o.responseText;
    } 
  }; 

  var handleFailure = function(o) { 
     if(o.responseText !== undefined){ 
       alert("update failure!");
     } 
  }; 
  
  var callback = { 
    success:handleSuccess, 
    failure: handleFailure, 
    argument: ['foo','bar'] 
  };
	  
	//-->
</script>

<div class="yui-skin-sam">

<div id="taskList">
  <ul class="list tasks">
    <c:forEach items="${taskList}" var="task">
      <li>
          <input type="checkbox" onclick="alert(this.checked);saveTask('${task.taskId}', '${task.description}', '${task.priority}', '${task.dueDate}' , this.checked);" ${task.status == 'CLOSED' ? 'checked="true"' : ''} "/> ${task.description} <img src="/vgr-theme/i/prio-${task.priority}.gif" /> <br />
          <a onclick="deleteTask('${task.taskId}');"><img src="/vgr-theme/i/icons/delete.png" /></a> <a onclick="prepareEdit('${task.taskId}', '${task.description}', '${task.priority}', '${task.dueDate}');" href="#"><img src="/vgr-theme/i/icons/pencil.png" /></a> ${task.dueDate} 
      </li>
    </c:forEach>
  </ul>
</div>
<a href="#" onclick="prepareEdit('','','','');">Lägg till ny uppgift</a>

<div id="myOverlay" style="visibility: hidden;">
<div class="hd">header</div>
<div class="bd">
  <form>
    <input type="hidden" id="taskId" name="id"/>
    <input type="text" id="description" name="description"/>
    <input type="text" id="priority" name="priority"/>
    <input type="text" id="dueDate" name="dueDate"/>
    <input type="button" onclick="updateTask();" value="save" />
  </form>
</div>
<div class="ft">footer</div>
</div>

</div>
