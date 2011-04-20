AUI().add('vgr-tasklist',function(A) {
	var Lang = A.Lang,
		isArray = Lang.isArray,
		isDate = Lang.isDate,
		isFunction = Lang.isFunction,
		isNull = Lang.isNull,
		isObject = Lang.isObject,
		isString = Lang.isString,
		isUndefined = Lang.isUndefined,
		getClassName = A.ClassNameManager.getClassName,
		concat = function() {
			return Array.prototype.slice.call(arguments).join(SPACE);
		},
		
		ADD_TASK_NODE = 'addTaskNode',
		BODY_CONTENT = 'bodyContent',
		CANCEL_TASK_EDIT_BTN = 'cancelTaskEditBtn',
		CONTENT_BOX = 'contentBox',
		CSS_DONE = 'done',
		CSS_DUE_DATE = 'cssDueDate',
		CSS_EDIT_CONTROLS = 'cssEditControls',
		CSS_PRIORITY_HIGH = 'cssPriorityHigh',
		CSS_PRIORITY_LOW = 'cssPriorityLow',
		CSS_PRIORITY_MEDIUM = 'cssPriorityMedium',
		CSS_PRIORITY_PREFIX = 'cssPriorityPrefix',
		DATES = 'dates',
		EDIT_TASK_FORM = 'editTaskForm',
		NAME = 'vgr-tasklist',
		NS = 'vgr-tasklist',
		SAVE_TASK_BTN = 'saveTaskBtn',
		TASK_DESCRIPTION = 'taskDescription',
		TASK_DUE_DATE = 'taskDueDate',
		TASK_ID = 'taskId',
		TASK_ID_PREFIX = 'taskIdPrefix'
		TASK_PRIORITY = 'taskPriority',
		TASK_STATUS = 'taskStatus',
		TXT_ADD_TASK = 'txtAddTask',
		TXT_CHANGE_TASK = 'txtChangeTask',
		URL_DELETE = 'urlDelete',
		URL_SAVE = 'urlSave'
	;

	var VGRTasklist = A.Component.create(
			{
				ATTRS: {
					addTaskNode: {
						setter: A.one
					},
					cancelTaskEditBtn: {
						setter: A.one
					},
					cssDueDate: {
						value: '.task-due-date'
					},
					cssEditControls: {
						value: '.task-edit-controls'
					},
					cssPriorityHigh: {
						value: 'priority-high'
					},
					cssPriorityMedium: {
						value: 'priority-medium'
					},
					cssPriorityPrefix: {
						value: 'priority-'
					},
					cssPriorityLow: {
						value: 'priority-low'
					},
					
					editTaskForm: {
						setter: A.one
					},
					saveTaskBtn: {
						setter: A.one
					},
					taskIdPrefix: {
						value: ''
					},
					urlDelete: {
						value: ''
					},
					urlSave: {
						value: ''
					}
				},
				EXTENDS: A.Component,
				NAME: NAME,
				NS: NS,
				prototype: {
					editOverlay: null,
					hasBoundDialogComponents: false,
					taskFormDatepicker: null,
					initializer: function(config) {
						var instance = this;
						
						// Init debugger console
						//instance._initConsole();
						
						// Define some phrases
						instance.set(TXT_ADD_TASK, 'L&auml;gg till uppgift');
						instance.set(TXT_CHANGE_TASK, '&Auml;ndra uppgift');
					},
					
					renderUI: function() {
						var instance = this;
					},
	
					bindUI: function() {
						var instance = this;
						
						// Bind add task link click event
						var addTaskNode = instance.get(ADD_TASK_NODE);
						addTaskNode.on('click', instance._onAddTaskNodeClick, instance);
						
						instance._bindTaskListComponents();
					},
					
					_bindTaskListComponents: function() {
						var instance = this;
						
						var contentBox = instance.get(CONTENT_BOX);
						
						// Bind edit task link click event
						var editTaskNodes = contentBox.all('li.edit-task a');
						editTaskNodes.on('click', instance._onEditTaskNodeClick, instance);
						
						// Bind delete task link click event
						var deleteTaskNodes = contentBox.all('li.delete-task a');
						deleteTaskNodes.on('click', instance._onDeleteTaskClick, instance);
						
						// Bind status checkbox check event
						var statusCheckboxes = contentBox.all('input.todo');
						statusCheckboxes.on('click', instance._onStatusToggle, instance);
					},
					
					_bindDialogComponents: function() {
						var instance = this;
						
						if(!instance.hasBoundDialogComponents) {
							var dialog = instance.editOverlay;
							var taskId = dialog.get(TASK_ID);
							var taskForm = instance.get(EDIT_TASK_FORM);
							
							var cancelBtn = instance.get(CANCEL_TASK_EDIT_BTN);
							var saveBtn = instance.get(SAVE_TASK_BTN);

							// Bind dialog buttons
							cancelBtn.on('click', instance._onCancelTaskEditClick, instance);
							saveBtn.on('click', instance._onSaveTaskClick, instance);
						}
						
						instance.hasBoundDialogComponents = true;
					},
					
				    _cleanString: function(str) {
				    	
				    	var output = str.replace("'", "").replace("\"", ""); 
				    	
				        return output;
				    },
					
					_cleanTaskData: function(taskData) {
						var instance = this;
						
						// Clean task description
						taskData.description = instance._cleanString(taskData.description);
						
						return taskData;
					},
					
					_createDatepicker: function(triggerNode, renderToNode) {
						var instance = this;
						
						var datepickerConfig = {
							locale: 'sv-se',
							calendar: {
								dateFormat: '%Y-%m-%d',
								dates: [],
								firstDayOfWeek: 1,
								selectMultipleDates: false
							},
							on: {
								'show': function(e) {
									var datepicker = this;
									
									var trigger = datepicker.get('trigger');
									var triggerValueList = trigger.get('value');
									var triggerValue = (triggerValueList.length > 0) ? triggerValueList[0] : '';
									var date = A.DataType.Date.parse(triggerValue);
									
									if(isDate(date)) {
										datepicker.calendar.set(DATES, [ date ]);
									}
								}
							},
							setValue: true
						};
								
						var datepicker = new A.DatePicker(
							A.merge(datepickerConfig, {
								trigger: triggerNode
							})
						).render();
						
						return datepicker;
					},
					
					_createEditOverlay: function(taskId) {
						var instance = this;

						var dialogOptions = {
							bodyContent: instance.get(EDIT_TASK_FORM),
							centered: true,
							constrain2view: true,
							draggable: true,
							height: 270,
							modal: true,
							stack: true,
							width: 300
						};
						
						var dialogTitle = instance.get(TXT_CHANGE_TASK);
						
						if(isNull(taskId)) {
							dialogTitle = instance.get(TXT_ADD_TASK);
						}
						
						instance.editOverlay = new A.Dialog(
							A.merge(dialogOptions, {
								title: dialogTitle
							})
						);
						
						instance.editOverlay.set(TASK_ID, taskId);
						
						instance.editOverlay.on('visibleChange', instance._onDialogToggle, instance);
						instance.editOverlay.on('render', instance._onDialogRender, instance);
						
						instance.editOverlay.render();
					},
					
					_deleteTask: function(taskId) {
						var instance = this;
						
				        var postData = {
			        		taskId: taskId
				        }

						var deleteIO = A.io.request(instance.get(URL_DELETE), {
							autoLoad : false,
							cache: false,
							data: postData,
							dataType: 'html',
							method : 'POST'
						});
						
						// Attach success handler to io request
				        deleteIO.on('success', function(e, id, xhr, taskId) {
				        	var instance = this;
				        	
				        	var instance = this;
				        	
				        	var responseHTML = xhr.responseText;
				        	instance._updateTaskListHtml(responseHTML, false);
				        }, instance, taskId);
				        
				        deleteIO.start();
					},

					_extractTaskId: function(taskNodeId) {
						var instance = this;
						return taskNodeId.replace(instance.get(TASK_ID_PREFIX), '');
					},
					
					_getTaskNode: function(taskId) {
						var instance = this;
						
						var taskNode = null;
						
						taskNode = A.one('#' + instance.get(TASK_ID_PREFIX) + taskId);
						
						return taskNode;
					},
					
					_getTaskNodeId: function(childNode) {
						var instance = this;
						var taskNodeId = '';

						var listItems = childNode.ancestors('li.task-item');
						var taskNode = listItems.item(0);
						
						var taskNodeId = taskNode.getAttribute('id');
						
						return taskNodeId;
					},
					
					_initConsole: function() {
						var instance = this;
						
						new A.Console({
							//height: '250px',
							newestOnTop: false,
							style: 'block',
							visible: true//,
							//width: '600px'
						}).render();
					},

					_onAddTaskNodeClick: function(e) {
						var instance = this;
						
						e.halt();
						
						instance._createEditOverlay(taskId=null);
					},
					
					_onCancelTaskEditClick: function(e) {
						var instance = this;
						
						e.halt();
						
						instance.editOverlay.close();
					},
					
					_onDeleteTaskClick: function(e) {
						
						var instance = this;
						
						e.halt();
						
						var taskNodeId = instance._getTaskNodeId(e.currentTarget);
						var taskNode = A.one('#' + taskNodeId)
						var taskId = instance._extractTaskId(taskNodeId);
						
						instance._deleteTask(taskId);
					},
					
					_onDialogRender: function(e) {
						var instance = this;
						
						var taskForm = instance.get(EDIT_TASK_FORM);
						taskForm.plug(A.LoadingMask, {
							background: '#555',
							strings: { loading: 'Sparar uppgift.' }
						});
						
						instance._updateDialogForm();
						instance._bindDialogComponents();
					},
					
					_onDialogToggle: function(e) {
						var instance = this;
						
						var visible = e.newVal;
						
						if(visible) {
							instance._updateDialogForm();
						}
					},
					
					_onEditTaskNodeClick: function(e) {
						var instance = this;
						
						e.halt();
						
						var taskNodeId = instance._getTaskNodeId(e.currentTarget);
						var taskId = instance._extractTaskId(taskNodeId);
						
						instance._showEditOverlay(taskId=taskId);
					},
					
					_onSaveTaskClick: function(e) {
						var instance = this;
						
						e.halt();
						
						var taskForm = instance.get(EDIT_TASK_FORM);
						var taskNode = instance._getTaskNode(taskId);
						
						var taskId = taskForm.one('#taskId').val();
						
						var taskDescription = taskForm.one('#description').val();
						var taskDueDate = taskForm.one('#dueDate').val();
						var taskPriority = taskForm.one('#priority').val();
						
						var taskData = {
							taskId: taskId,
							description: taskDescription,
							dueDate: taskDueDate,
							priority: taskPriority
						};
						
						taskData = instance._cleanTaskData(taskData);
						
						var isValid = instance._validateTaskData(taskData);

						if(isValid) {
							instance._persistTask(taskData);
						}
					},
					
					_onStatusToggle: function(e) {
						var instance = this;
						
						var checkbox = e.currentTarget;
						var taskNodeId = instance._getTaskNodeId(checkbox);
						var taskNode = A.one('#' + taskNodeId);
						
						var taskId = instance._extractTaskId(taskNodeId);
						var taskDescription = taskNode.one('.task-label').html();
						var taskDueDate = taskNode.one('.task-due-date').html();
						var taskPriority = taskNode.one('.task-priority').html();
						
						var taskData = {
							taskId: taskId, 
							description: taskDescription,
							priority: taskPriority,
							dueDate: taskDueDate,
							status: checkbox.get('checked')
						};
						
						instance._persistTask(taskData);
					},
					
					 // Persists a task to backend (updates or creates)
					 // taskData is an object with taskId, description, priority, dueData, status
					_persistTask: function(taskData) {
						var instance = this;
						
						A.log('_persistTask started.');
						
						var statusTxt = taskData.status ? 'CLOSED' : 'OPEN';
						
				        var postData = {
			        		taskId: taskData.taskId,
			        		description: taskData.description,
			        		priority: taskData.priority,
			        		dueDate: taskData.dueDate,
			        		status: statusTxt
				        }
				        
						var updateIO = A.io.request(instance.get(URL_SAVE), {
							autoLoad : false,
							cache: false,
							data: postData,
							dataType: 'html',
							method : 'POST'
						});
				        
						// Attach success handler to io request
				        updateIO.on('success', function(e, id, xhr) {
				        	var instance = this;
				        	
				        	A.log('updateIO on success callback.');
				        	
				        	var taskForm = instance.get(EDIT_TASK_FORM);
				        	taskForm.loadingmask.hide();
				        	
				        	var responseHTML = xhr.responseText;
				        	instance._updateTaskListHtml(responseHTML, true);
				        }, instance);
				        
				        var taskForm = instance.get(EDIT_TASK_FORM);
				        taskForm.loadingmask.show();
				        
				        updateIO.start();
				        A.log('updateIO started.');
					},
					
					_showAlert: function(title, msg) {
						var instance = this;
						
						/*
						var bodyContent = '<p>' + msg + '</msg>';
						
						var dialogOptions = {
							bodyContent: 'N&aring;got gick fel.',
							centered: true,
							constrain2view: true,
							destroyOnClose: true,
							draggable: false,
							height: 200,
							modal: false,
							resizable: false,
							stack: true,
							title: 'Fel',
							width: 200
						};
							
						var alertOverlay = new A.Dialog(
							A.merge(dialogOptions, {
								bodyContent: bodyContent,
								title: title
							})
						);
						
						alertOverlay.render();
						*/
						
						// Alert should be replaced with aui form validation.
						// Temporary show normal javascript alert
						alert(msg);
					},
					
					_showEditOverlay: function(taskId) {
						var instance = this;
						
						// Create an edit task overlay if one doesn't exist
						if(isNull(instance.editOverlay)) {
							instance._createEditOverlay(taskId=taskId);
						}
						// Else show the dialog and update dialog taskId value
						else {
							instance.editOverlay.set(TASK_ID, taskId);
							instance.editOverlay.show();
						}
					},
					
					_updateDialogForm: function() {
						var instance = this;

						var dialog = instance.editOverlay;
						var taskId = dialog.get(TASK_ID);
						var taskForm = instance.get(EDIT_TASK_FORM);
						var taskNode = instance._getTaskNode(taskId);
						
						var taskDescription = '';
						var taskDueDate = '';
						var taskPriority = '';
						
						if(!isNull(taskNode)) {
							taskDescription = taskNode.one('.task-label').html();
							taskDueDate = taskNode.one('.task-due-date').html();
							taskPriority = taskNode.one('.task-priority').html();
						}
						
						if(isNull(instance.taskFormDatepicker)) {
							var dateInput = taskForm.one('#dueDate');
							var dateParent = dateInput.ancestor('.aui-field-element');
							
							instance.taskFormDatepicker = instance._createDatepicker(dateInput, dateParent); 
						}

						taskForm.one('#taskId').val(taskId);
						taskForm.one('#description').val(taskDescription);
						taskForm.one('#dueDate').val(taskDueDate);
						
						var priorityOptions = taskForm.all('#priority option');
						
						var optionsContext = {
								instance: instance,
								taskPriority: taskPriority
						};
						
						priorityOptions.each(function(val, index, array) {
							var context = this;
							var instance = context.instance;
							var taskPriority = context.taskPriority;
							
							var optionsNode = val;
							var optionValue = optionsNode.val();
							
							if(optionValue == taskPriority) {
								optionsNode.setAttribute('selected', 'selected');
							}
							else {
								optionsNode.removeAttribute('selected');
							}
							
						}, optionsContext);
						
					},
					
					_updateTaskListHtml: function(html, closeOverlay) {
						var instance = this;
						
						A.log('_updateTaskListHtml started.');

						var contentBox = instance.get(CONTENT_BOX);
						var taskList = contentBox.one('ul.tasks');
						
						if(taskList) {
							A.log('_updateTaskListHtml found taskList.');
							taskList.purge();
							taskList.remove();
						}
						contentBox.prepend(html);
						
						instance._bindTaskListComponents();
						
						if(closeOverlay) {
							instance.editOverlay.close();
						}
					},					

					_validateTaskData: function(taskData) {
						var instance = this;
						
						var isValid = false;
						var isDescriptionValid = false;
						var isDateValid = false;
						
						// Validate task description
				        if (taskData.description == '') {
				        	instance._showAlert('Fel', 'Beskrivning saknas.');
				            isDescriptionValid = false;
				        }
				        else {
				        	isDescriptionValid = true;
				        }

				        // Validate date
				        if (taskData.dueDate == '') {
				            isDateValid = false;
				            taskData.dueDate = 'BLANK';
				        }
			        	else {
			        		var isDateValid = true;
				            var dateParts = taskData.dueDate.split('-');
				            
				            if (dateParts.length < 3) {
				            	isDateValid = false;
				            }
				            else {
					            var date = new Date(dateParts[0], dateParts[1], dateParts[2]);
					            isDateValid = isDate(date);
				            }
				        }

			            if (!isDateValid) {
			            	//instance._showAlert('Fel', taskData.dueDate + ' &auml;r inte ett giltigt datum! F&ouml;rv&auml;ntat format: YYYY-MM-DD.');
							// Temporary fix with alert
							// &aring; = \u00E5
							// &auml; = \u00E4
							// &ouml; = \u00F6
							//instance._showAlert('Fel', taskData.dueDate + ' är inte ett giltigt datum! Förväntat format: YYYY-MM-DD.');
							instance._showAlert('Fel', taskData.dueDate + ' \u00E4r inte ett giltigt datum! F\u00F6rv\u00E4ntat format: YYYY-MM-DD.');
			            }
			            
			            isValid = isDescriptionValid && isDateValid;
						
						return isValid;
					}
					
				}
			}
	);

	A.VGRTasklist = VGRTasklist;
		
	},1, {
		requires: [
			'aui-base',
			'console',
			'aui-datepicker',
			'aui-event',
			'aui-dialog',
			'aui-io',
			'aui-loading-mask'
      ]
	}
);