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

<link type="text/css" rel="stylesheet" href="/vgr-theme/javascript/yui/assets/calendar.css"/>

<style type="text/css">
    <%@ include file="/style/styles.css" %>
</style>

<portlet:actionURL escapeXml="false" var="formAction"/>
<portlet:resourceURL id="save" escapeXml="false" var="saveResource"/>
<portlet:resourceURL id="delete" escapeXml="false" var="deleteResource"/>

<script type="text/javascript"><!--
try {
    function init() {
        // Build overlay1 based on markup, initially hidden, fixed to the center of the viewport, and 200px wide
        AUI().use("overlay", "node-base", "dom", function(Y) {
            try {
                window.myOverlay = new Y.Overlay({
                    srcNode: "#myOverlayId",
                    context:["taskList","tl","bl", ["beforeShow", "windowResize"]],
                    visible : false,
                    width : "220px"
                });
                myOverlay.show_old = myOverlay.show;
                myOverlay.show = function() {
                    YUI().use("overlay", "node-base", "dom", function(Y) {
                        try {
                            var pos = Y.one('#taskList').getXY();
                            Y.one('#myOverlayId').setXY([pos[0] + 10, pos[1] + 15]);
                        } catch(e) {
                            alert(e.message);
                        }
                    });
                    this.show_old();
                };
                // If the user clicks somewhere outside... close the whole thing.
                Y.on('mousedown', function(e) {
                    AUI().use("overlay", "node-base", "dom", function(Y) {
                        var myOverlayDiv = Y.one('#myOverlayId');
                        if (!myOverlayDiv.contains(e.target)) {
                            myOverlay.hide();
                        }
                    });
                }, document);
                myOverlay.render(document.body);
            } catch(eee) {
                alert('Fel vid dialogskapandet ' + eee.message);
            }
        });
    }


    function runWhenDomReady(func) {
        YUI().use('node-base', function(Y) {
            try {
                Y.on("domready", func);
            } catch(e) {
                alert(e.message);
            }
        });
    }

    runWhenDomReady(init);

    function mkAlloyCallendar() {
        AUI().ready('aui-calendar', function(A) {
            window.calendar1 = new A.Calendar({
                trigger: '#dueDate',
                dateFormat: '%Y-%m-%d',
                setValue: true,
                selectMultipleDates: false,
                on: {
                    select: function(event) {
                        var normal = event.date.normal;
                        var detailed = event.date.detailed;
                        var formatted = event.date.formatted;
                    }
                }
            })
                    .render();
            //calendar1.cfg.setProperty("WEEKDAYS_SHORT", ["Sö", "Må", "Ti", "On", "To", "Fr", "Lö"]);
            //calendar1.cfg.setProperty("MONTHS_LONG",    ["Januari", "Februari", "Mars", "April", "Maj", "Juni", "Juli", "Augusti", "September", "Oktober", "November", "December"]);
            setTimeout(function() {
                calendar1.toggle();
            }, 1000);
            A.on('mousedown', function() {
                A.CalendarManager.hideAll()
            }, document);
        });
    }

    mkAlloyCallendar();

    function prepareEdit(taskId, description, priority, dueDate) {
        document.getElementById('taskId').value = taskId;
        document.getElementById('description').value = description;
        document.getElementById('priority').value = priority;

        for (var i = 0; i < document.taskForm.priority.options.length; i++) {
            if (document.taskForm.priority.options[i].value == priority)
                document.taskForm.priority.options[i].selected = true;
        }
        document.getElementById('dueDate').value = dueDate;
        window.myOverlay.show();
    }

    function updateTask() {
        var ok = true;

        // Remove illegal characters
        document.getElementById('description').value = cleanString(document.getElementById('description').value);
        if (document.getElementById('description').value == "") {
            alert("Beskrivning saknas!");
            ok = false;
        }
        if (document.getElementById('dueDate').value == "") {
            alert("Datum saknas!");
            ok = false;
        } else {
            var dateString = document.getElementById('dueDate').value;
            var dateParts = dateString.split('-');
            if (dateParts.length < 3) {
                ok = false;
            } else {
                var dt = new Date(dateParts[0], dateParts[1], dateParts[2]);
                if (dt == 'NaN' || dt == 'Invalid Date') {
                    ok = false;
                }
            }
            if (!ok) {
                alert(dateString + " är inte ett giltigt datum! Förväntat format: YYYY-MM-DD");
                document.getElementById('dueDate').focus();
            }
        }
        if (! ok) return;

        var postData = "taskId=" + document.getElementById('taskId').value +
                "&description=" + document.getElementById('description').value +
                "&priority=" + document.getElementById('priority').value +
                "&dueDate=" + document.getElementById('dueDate').value;
        var sUrl = '${saveResource}';

        ajax(sUrl, postData, callback);
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
        var request = ajax(sUrl, postData, callback);
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
        var request = ajax(sUrl, postData, callback);
    }

    var handleSuccess = function(id, o) {
        if (o.responseText != undefined) {
            document.getElementById('taskList').innerHTML = o.responseText;
            YUI().use('node-base', 'dom', function(Y) {
                try {
                    var items = Y.all('.tasks li');
                    var hits = Y.all('#portlet_TaskList_WAR_tasklistportlet .portlet-topper .portlet-title .portlet-title-text');
                    if (hits.size() > 0) {
                        var heading = hits.item(0);
                        var html = heading.get('innerHTML');
                        html = html.replace(new RegExp('[0-9]+'), items.size() + '');
                        heading.set('innerHTML', html);
                    }
                } catch(e) {
                    alert('handleSuccess error \n' + e.message + '\n\n');
                }
            });
        }
        myOverlay.hide();
    };

    var handleFailure = function(o) {
        if (o.responseText != undefined) {
            alert("update failure!");
        }
        myOverlay.hide();
    };

    var callback = {
        success:handleSuccess,
        failure: handleFailure,
        argument: ['foo','bar']
    };

} catch(whatIsWrong) {
    alert('whatIsWrong ' + whatIsWrong.message);
}

/**
 url = the path to the server resouce to be prosessed.
 data = the data as a string concatenatet
 callback = an objcect with two callback functions, success and failure.
 */
function ajax(url, data, callback) {
    try {
        YUI().use("io-base", "node-base", "dom", function(Y) {
            var cfg = {
                method: "POST",
                data: data
            };
            if (!callback) alert('The entire callback was null!');
            if (callback.success) {
                Y.on('io:success', callback.success);
            }
            Y.on('io:failure', callback.failure);
            var request = Y.io(url, cfg);
        });
    } catch(e) {
        alert('Error in ajax ' + e.message);
    }
}
//-->
</script>

<div class="yui-skin-sam">
    <div id="taskList">
        <ul class="list tasks">
            <c:forEach items="${taskList}" var="task">
                <li ${task.status== 'CLOSED' ? 'class="done"' : ''}>
                    <input type="checkbox" class="todo"
                           onclick="saveTask('${task.taskId}', '${task.description}', '${task.priority}', '${task.dueDate}' , this.checked);" ${task.status== 'CLOSED' ? 'checked="true"' : ''} />
                    <label class="descriptionLabel">&#160;${task.description}</label>
                    <img class="prioImage" src="/vgr-theme/i/prio-${task.priority}.gif"
                         alt="Prioritet: ${task.priority}" title="Prioritet: ${task.priority}"/>
                    <br/>

                    <div ${task.status== 'CLOSED' ? 'class="hidden"' : ''}>
      <span>
        <a onclick="prepareEdit('${task.taskId}', '${task.description}', '${task.priority}', '${task.dueDate}');"
           class="editTask" href="#"><img src="/vgr-theme/i/icons/pencil.png" alt="Ändra uppgift"
                                          title="Ändra uppgift"/></a><a onclick="deleteTask('${task.taskId}');"
                                                                        href="#"><img
              src="/vgr-theme/i/icons/delete.png" alt="Ta bort uppgift"
              title="Ta bort uppgift"/></a>&#160;${task.dueDate}
      </span>
                    </div>
                </li>
            </c:forEach>
        </ul>
    </div>

    <a href="#" onclick="prepareEdit('','','','');">Lägg till ny uppgift</a>

    <div id="myOverlayId" style="position:absolute; top:-1000px;">
        <fieldset class="yui3-widget-bd portlet">
            <legend>Lägg till/ändra uppgift</legend>
            <form id="taskForm" name="taskForm">
                <table id="taskTable">
                    <tr>
                        <td><input type="hidden" id="taskId" name="taskId"/> <label
                                for="description">Beskrivning:<em>*</em></label>
                        </td>
                        <td><input type="text" id="description" name="description" size="10"/></td>
                    </tr>
                    <tr>
                        <td><label for="dueDate">Klart datum:<em>*</em></label></td>
                        <td>
                            <div class="box">
                                <div class="datefield"><input type="text" id="dueDate" name="dueDate" value="" size="10"
                                                              class="readOnly"/></div>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td><label for="priority">Prioritet:</label></td>
                        <td><select id="priority">
                            <option value="LOW">Låg</option>
                            <option value="MEDIUM" selected="selected">Medium</option>
                            <option value="HIGH">Hög</option>
                        </select></td>
                    </tr>
                    <tr>
                        <td><input type="button" onclick="updateTask();" value="Spara"/></td>
                        <td><input type="button" onclick="cancel();" value="Avbryt"/></td>
                    </tr>
                </table>
            </form>
        </fieldset>
    </div>
</div>