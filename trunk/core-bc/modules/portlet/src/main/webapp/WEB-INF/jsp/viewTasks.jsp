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

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions'%>

<script type="text/javascript" src="/vgr-theme/javascript/yui/yahoo-dom-event.js"></script>
<script type="text/javascript" src="/vgr-theme/javascript/yui/container-min.js"></script>

<link type="text/css" rel="stylesheet" href="/vgr-theme/javascript/yui/assets/calendar.css" />

<style type="text/css">
    <%@ include file="/style/styles.css" %>
</style>

<portlet:actionURL escapeXml="false" var="formAction" />
<portlet:resourceURL id="save" escapeXml="false" var="saveResource" />
<portlet:resourceURL id="delete" escapeXml="false" var="deleteResource" />

<script type="text/javascript"><!--
   function init() {
      // Build overlay1 based on markup, initially hidden, fixed to the center of the viewport, and 200px wide
      myOverlay = new YAHOO.widget.Overlay("myOverlayId", {
          context:["taskList","tl","bl", ["beforeShow", "windowResize"]],  
          visible : false,
          width : "220px"
      });
      myOverlay.render(document.body);
   }
   YAHOO.util.Event.addListener(window, "load", init);

    function prepareEdit(taskId, description, priority, dueDate) {
      document.getElementById('taskId').value = taskId;
      document.getElementById('description').value = description;
      document.getElementById('priority').value = priority;

      for (var i=0;i<document.taskForm.priority.options.length;i++) {
    	    if (document.taskForm.priority.options[i].value == priority)
    	        document.taskForm.priority.options[i].selected = true;
      }
    	      
      //document.getElementById('myOverlay').style.display = 'inline';
      document.getElementById('dueDate').value = dueDate;
      myOverlay.show();
    }

    function updateTask() {
        var ok=true;

		// Remove illegal characters
       	document.getElementById('description').value = cleanString(document.getElementById('description').value);
        if(document.getElementById('description').value==""){
            alert("Beskrivning saknas!");
            ok=false;
        }
        if(document.getElementById('dueDate').value==""){
            alert("Datum saknas!");
            ok=false;
        }
        if(! ok) return;

      var postData = "taskId=" + document.getElementById('taskId').value + 
      "&description=" + document.getElementById('description').value + 
      "&priority=" + document.getElementById('priority').value + 
      "&dueDate=" + document.getElementById('dueDate').value; 
      var sUrl = '${saveResource}';
      var request = YAHOO.util.Connect.asyncRequest('POST', sUrl, callback, postData); 
    }

	function cleanString(inputString) {
		var outputString = inputString.replace("'", "").replace("\"", "");
		return outputString;
	}
    
    function cancel() {
        myOverlay.hide();
    }

    function deleteTask(taskId) {
        var postData = "taskId=" + taskId; 
        var sUrl = '${deleteResource}';
        var request = YAHOO.util.Connect.asyncRequest('POST', sUrl, callback, postData); 
    }

    function saveTask(taskId, description, priority, dueDate, status) {
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
    myOverlay.hide();
  }; 

  var handleFailure = function(o) { 
     if(o.responseText !== undefined){ 
       alert("update failure!");
     } 
     myOverlay.hide();
  }; 
  
  var callback = { 
    success:handleSuccess, 
    failure: handleFailure, 
    argument: ['foo','bar'] 
  };

  YAHOO.util.Event.onDOMReady(function(){

      var Event = YAHOO.util.Event,
          Dom = YAHOO.util.Dom,
          dialog,
          calendar;

      var showBtn = Dom.get("dueDate");

      Event.on(showBtn, "click", function() {

          // Lazy Dialog Creation - Wait to create the Dialog, and setup document click listeners, until the first time the button is clicked.
          if (!dialog) {

              // Hide Calendar if we click anywhere in the document other than the calendar
              Event.on(document, "click", function(e) {
                  var el = Event.getTarget(e);
                  var dialogEl = dialog.element;
                  if (el != dialogEl && !Dom.isAncestor(dialogEl, el) && el != showBtn && !Dom.isAncestor(showBtn, el)) {
                      dialog.hide();
                  }
              });

              function resetHandler() {
                  // Reset the current calendar page to the select date, or 
                  // to today if nothing is selected.
                  var selDates = calendar.getSelectedDates();
                  var resetDate;
      
                  if (selDates.length > 0) {
                      resetDate = selDates[0];
                  } else {
                      resetDate = calendar.today;
                  }
      
                  calendar.cfg.setProperty("pagedate", resetDate);
                  calendar.render();
              }
      
              function closeHandler() {
                  dialog.hide();
              }

              dialog = new YAHOO.widget.Dialog("container", {
                  visible:false,
                  underlay:"none",
                  width:'230px',
                  context:["dueDate", "tr", "br"],
                  //buttons:[ {text:"Återställ", handler: resetHandler, isDefault:true}, {text:"Stäng", handler: closeHandler}],
                  draggable:true,
                  close:true
              });
              
              dialog.setHeader('Välj ett datum');
              dialog.setBody('<div id="cal"></div>');
              dialog.render(document.body);

              dialog.showEvent.subscribe(function() {
                  if (YAHOO.env.ua.ie) {
                      // Since we're hiding the table using yui-overlay-hidden, we 
                      // want to let the dialog know that the content size has changed, when
                      // shown
                      dialog.fireEvent("changeContent");
                  }
              });
          }

          // Lazy Calendar Creation - Wait to create the Calendar until the first time the button is clicked.
          if (!calendar) {
              calendar = new YAHOO.widget.Calendar("cal", {
                  iframe:false,          // Turn iframe off, since container has iframe support.
                  hide_blank_weeks:true  // Enable, to demonstrate how we handle changing height, using changeContent
                   });
              calendar.cfg.setProperty("WEEKDAYS_SHORT", ["Sö", "Må", "Ti", "On", "To", "Fr", "Lö"]);
              calendar.cfg.setProperty("MONTHS_LONG",    ["Januari", "Februari", "Mars", "April", "Maj", "Juni", "Juli", "Augusti", "September", "Oktober", "November", "December"]);   
                              
              calendar.render();

              calendar.selectEvent.subscribe(function() {
                  if (calendar.getSelectedDates().length > 0) {

                      var selDate = calendar.getSelectedDates()[0];

                      // Pretty Date Output, using Calendar's Locale values: Friday, 8 February 2008
                      var wStr = calendar.cfg.getProperty("WEEKDAYS_LONG")[selDate.getDay()];
                      var dStr = selDate.getDate();
                      var mStr = (selDate.getMonth()+1);
                      var yStr = selDate.getFullYear();
						if(dStr<10){
							dStr="0"+dStr;
						}
						if(mStr<10){
							mStr="0"+mStr;
						}
                      Dom.get("dueDate").value = yStr + "-" + mStr + "-" + dStr;
                      //Dom.get("date").value = wStr + ", " + dStr + " " + mStr + " " + yStr;
                  } else {
                      Dom.get("dueDate").value = "";
                  }
                  dialog.hide();
              });

              calendar.renderEvent.subscribe(function() {
                  // Tell Dialog it's contents have changed, which allows 
                  // container to redraw the underlay (for IE6/Safari2)
                  dialog.fireEvent("changeContent");
              });
          }

          var seldate = calendar.getSelectedDates();

          if (seldate.length > 0) {
              // Set the pagedate to show the selected date if it exists
              calendar.cfg.setProperty("pagedate", seldate[0]);
              calendar.render();
          }
          dialog.show();
      });
  });

	//--></script>

<div class="yui-skin-sam">

<div id="taskList">
<ul class="list tasks">
  <c:forEach items="${taskList}" var="task">
    <li ${task.status== 'CLOSED' ? 'class="done"' : ''}><input type="checkbox" class="todo"
      onclick="saveTask('${task.taskId}', '${task.description}', '${task.priority}', '${task.dueDate}' , this.checked);"
      ${task.status== 'CLOSED' ? 'checked="true" ' : ''} "/> <label class="descriptionLabel">
    &#160;${task.description}</label> <img class="prioImage" src="/vgr-theme/i/prio-${task.priority}.gif" alt="Prioritet: ${task.priority}"/> <br />
     <div ${task.status== 'CLOSED' ? 'class="hidden"' : ''}>
      <span>
      <a onclick="prepareEdit('${task.taskId}', '${task.description}', '${task.priority}', '${task.dueDate}');" class="editTask" href="#"><img src="/vgr-theme/i/icons/pencil.png" /></a>
      <a onclick="deleteTask('${task.taskId}');" href="#"><img src="/vgr-theme/i/icons/delete.png" /></a> 
      &#160;&#160;${task.dueDate}
      </span>
      </div>
    </li>
  </c:forEach>
</ul>
</div>
  <a href="#" onclick="prepareEdit('','','','');">Lägg till ny uppgift</a> 
<!--<a href="#" id="prepareLink">Lägg till ny uppgift</a> -->

<div id="myOverlayId" style="visibility:hidden;">
<fieldset><legend>Lägg till/ändra uppgift</legend>

<form id="taskForm" name="taskForm">
<table id="taskTable">
  <tr>
    <td><input type="hidden" id="taskId" name="taskId" /> <label for="description">Beskrivning:<em>*</em></label>
    </td>
    <td><input type="text" id="description" name="description" size="10" /></td>
  </tr>
  <tr>
    <td><label for="dueDate">Klart datum:<em>*</em></label></td>
    <td>
    <div class="box">
    <div class="datefield"><input type="text" id="dueDate" name="dueDate" value="" readonly="true" size="10"
      class="readOnly" /></div>
    </div>
    </td>
  </tr>
  <tr>
    <td><label for="priority">Prioritet:</label></td>
    <td><SELECT id="priority">
      <OPTION VALUE="LOW">Låg
      <OPTION VALUE="MEDIUM">Medium
      <OPTION VALUE="HIGH">Hög
    </SELECT></td>
  </tr>
  <tr>
    <td><input type="button" onclick="updateTask();" value="Spara" /></td>
    <td><input type="button" onclick="cancel();" value="Avbryt" /></td>
  </tr>
</table>
</form>
</fieldset>
</div>