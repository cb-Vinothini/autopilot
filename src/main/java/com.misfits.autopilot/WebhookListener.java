package com.misfits.autopilot;

import com.chargebee.org.json.JSONException;
import com.chargebee.org.json.JSONObject;
import com.misfits.autopilot.executor.ChargebeeApi;
import com.misfits.autopilot.models.entity.*;
import com.misfits.autopilot.models.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class WebhookListener {

    @Autowired
    WorkflowRepository workflowRepo;
    @Autowired
    HookRepository hookRepository;
    @Autowired
    CriteriaRepository criteriaRepository;
    @Autowired
    CriteriaGroupRepository criteriaGroupRepository;
    @Autowired
    ActionRepository actionRepository;
    @Autowired
    ActionGroupRepository actionGroupRepository;
    @Autowired
    ChargebeeApi actionCall;

    @RequestMapping(value = "/listener", method= RequestMethod.POST)
    public ResponseEntity listener(@RequestBody String event) throws Exception {
        JSONObject webhookEvent = new JSONObject(event);
        String eventType = webhookEvent.getString("event_type");
        List<Workflow> wfs = workflowRepo.findAll();
        for(Workflow wf : wfs){
            if(wf.getType().equals(Workflow.Type.HOOK)){
                Hook findHook = new Hook();
                findHook.setWorkflowId(wf.getId());
                List<Hook> hooks = hookRepository.findAll(Example.of(findHook));
                if(hooks.stream().anyMatch(hook -> hook.getEventType().name().equals(eventType))){
                    if(criteria(wf, webhookEvent)){
                        ActionGroup findActionGroup = new ActionGroup();
                        findActionGroup.setWorkflowId(wf.getId());
                        List<ActionGroup> actionGroups = actionGroupRepository.findAll(Example.of(findActionGroup));
                        for(ActionGroup group : actionGroups){
                            Optional<Action> action = actionRepository.findById(group.getActionId());
                            if(action.isPresent()){
                                Action act = action.get();
                                actionCall.postApi(act.getName().split("\\.")[0], act, webhookEvent);
                            }
                        }
                    }
                }
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    public boolean criteria(Workflow workflow, JSONObject webhookEvent) throws JSONException {
        JSONObject entities = webhookEvent.getJSONObject("entities");
        CriteriaGroup findCriteriaGroup = new CriteriaGroup();
        findCriteriaGroup.setWorkflowId(workflow.getId());
        List<CriteriaGroup> criteriaGroups = criteriaGroupRepository.findAll(Example.of(findCriteriaGroup));
        for(CriteriaGroup group : criteriaGroups){
            Optional<Criteria> criteria = criteriaRepository.findById(group.getCriteriaId());
            if(criteria.isPresent()){
                List<String> resAttr = Arrays.asList(criteria.get().getName().split("\\."));
                JSONObject value = new JSONObject(criteria.get().getValue());
                Criteria.Operator operator = criteria.get().getOperator();
                Iterator iterator = entities.keys();
                while(iterator.hasNext()){
                    String resourceName = (String) iterator.next();
                    if(resourceName.startsWith(resAttr.get(0))){
                        JSONObject res = entities.getJSONObject(resourceName);
                        JSONObject prevRes = res.getJSONObject("prev_version").getJSONObject(resourceName);
                        for(String att : resAttr.subList(1, resAttr.size())){
                            Object obj = res.opt(att);
                            Object prevObj = prevRes.opt(att);
                            if((obj == null && prevObj != null) ||
                                    (obj != null && prevObj == null) ||
                                    (!obj.equals(prevObj))) {
                                if(!Criteria.Operator.perform(operator, value, obj, prevObj)){
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}