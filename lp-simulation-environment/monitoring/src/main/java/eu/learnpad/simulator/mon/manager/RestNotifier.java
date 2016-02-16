package eu.learnpad.simulator.mon.manager;

import java.util.List;
import java.util.Map;

import org.apache.commons.net.ntp.TimeStamp;

import eu.learnpad.exception.LpRestException;
import eu.learnpad.sim.CoreFacade;
import eu.learnpad.simulator.mon.utils.DebugMessages;
import eu.learnpad.simulator.api.impl.SimulatorMonCoreFacadeImpl;
import eu.learnpad.sim.rest.event.impl.SimulationStartEvent;
import eu.learnpad.sim.rest.event.impl.SimulationEndEvent;
import eu.learnpad.sim.rest.event.impl.ProcessStartEvent;
import eu.learnpad.sim.rest.event.impl.ProcessEndEvent;
import eu.learnpad.sim.rest.event.impl.TaskStartEvent;
import eu.learnpad.sim.rest.event.impl.TaskEndEvent;
import eu.learnpad.sim.rest.event.impl.SessionScoreUpdateEvent;

public class RestNotifier extends Thread {
	
	private volatile static CoreFacade platformCoreFacade = new SimulatorMonCoreFacadeImpl(); 
	
	public RestNotifier() { 
	}

	public void run() {
		
	}
	
	public static void notifySimulationStart(Long processTimeStamp, List<String> involvedUsers, String sessionID, String modelSetID) {
		SimulationStartEvent event = new SimulationStartEvent(processTimeStamp, sessionID, involvedUsers, modelSetID);
		try {
			platformCoreFacade.receiveSimulationStartEvent(event);
			DebugMessages.println(TimeStamp.getCurrentTime(), RestNotifier.class.getSimpleName(), "SimulatioStartEvent sent");
		} catch (LpRestException e) {
			DebugMessages.println(TimeStamp.getCurrentTime(), RestNotifier.class.getSimpleName(), "Error in RestNotifier:notifySimulationStart method");
		}
	}
		
	public static void notifySimulationStop(Long processTimeStamp, List<String> involvedUsers, String sessionID, String modelSetID) {
		SimulationEndEvent event = new SimulationEndEvent(processTimeStamp, sessionID, involvedUsers, modelSetID);
		try {
			platformCoreFacade.receiveSimulationEndEvent(event);
			DebugMessages.println(TimeStamp.getCurrentTime(), RestNotifier.class.getSimpleName(), "SimulatioStopEvent sent");
		} catch (LpRestException e) {
			DebugMessages.println(TimeStamp.getCurrentTime(), RestNotifier.class.getSimpleName(), "Error in RestNotifier:notifySimulationStop method");
		}
	}
		
	public static void notifyProcessStart(Long processTimeStamp, List<String> involvedUsers, String sessionID, String modelSetID, String processID, String processDefinitionID) {
		ProcessStartEvent event = new ProcessStartEvent(processTimeStamp, sessionID, involvedUsers, modelSetID, processID, processDefinitionID);
		try {
			platformCoreFacade.receiveProcessStartEvent(event);
			DebugMessages.println(TimeStamp.getCurrentTime(), RestNotifier.class.getSimpleName(), "ProcessStartEvent sent");
		} catch (LpRestException e) {
			DebugMessages.println(TimeStamp.getCurrentTime(), RestNotifier.class.getSimpleName(), "Error in RestNotifier:notifyProcessStart method");
		}
	}
	
	public static void notifyProcessStop(Long processTimeStamp, List<String> involvedUsers, String sessionID, String modelSetID, String processID, String processDefinitionID) {
		ProcessEndEvent event = new ProcessEndEvent(processTimeStamp, sessionID, involvedUsers, modelSetID, processID, processDefinitionID);
		try {
			platformCoreFacade.receiveProcessEndEvent(event);
			DebugMessages.println(TimeStamp.getCurrentTime(), RestNotifier.class.getSimpleName(), "ProcessStopEvent sent");
		} catch (LpRestException e) {
			DebugMessages.println(TimeStamp.getCurrentTime(), RestNotifier.class.getSimpleName(), "Error in RestNotifier:notifyProcessStop method");
		}
	}
	
	public static void notifyTaskStart(Long processTimeStamp, String sessionID, List<String> involvedUsers, String modelSetID, String processID, String taskID, String taskDefinitionID, List<String> assignedUsers) {
		TaskStartEvent event = new TaskStartEvent(processTimeStamp, sessionID, involvedUsers, modelSetID, processID, taskID, taskDefinitionID, assignedUsers);
		try {
			platformCoreFacade.receiveTaskStartEvent(event);
			DebugMessages.println(TimeStamp.getCurrentTime(), RestNotifier.class.getSimpleName(), "TaskStartEvent sent");
		} catch (LpRestException e) {
			DebugMessages.println(TimeStamp.getCurrentTime(), RestNotifier.class.getSimpleName(), "Error in RestNotifier:notifyTaskStart method");
		}
	}
	
	public static void notifyTaskStop(Long processTimeStamp, String sessionID, List<String> involvedUsers, String modelSetID, String processID, String taskID, String taskDefinitionID, List<String> assignedUsers, String completingUserID, Map<String,Object> submittedData) {
		TaskEndEvent event = new TaskEndEvent(processTimeStamp, sessionID, involvedUsers, modelSetID, processID, taskID, taskDefinitionID, assignedUsers, completingUserID, submittedData);
		try {
			platformCoreFacade.receiveTaskEndEvent(event);
			DebugMessages.println(TimeStamp.getCurrentTime(), RestNotifier.class.getSimpleName(), "TaskEndEvent sent");
		} catch (LpRestException e) {
			DebugMessages.println(TimeStamp.getCurrentTime(), RestNotifier.class.getSimpleName(), "Error in RestNotifier:notifyTaskEnd method");
		}
	}
	
	public static void notifySessionScoreUpdate(Long processTimeStamp, String sessionID, List<String> involvedUsers, String modelSetID, String processID, String userID, Long sessionScore) {
		SessionScoreUpdateEvent event = new SessionScoreUpdateEvent(processTimeStamp, sessionID, involvedUsers, modelSetID, processID, userID, sessionScore);
		try {
			platformCoreFacade.receiveSessionScoreUpdateEvent(event);
			DebugMessages.println(TimeStamp.getCurrentTime(), RestNotifier.class.getSimpleName(), "SessionScoreUpdateEvent sent");
		} catch (LpRestException e) {
			DebugMessages.println(TimeStamp.getCurrentTime(), RestNotifier.class.getSimpleName(), "Error in RestNotifier:notifySessionScoreUpdate method");
		}
	}
}