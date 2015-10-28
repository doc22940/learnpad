/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.learnpad.ontology.recommender;

import java.util.ArrayList;
import java.util.List;


import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;

import eu.learnpad.ontology.persistence.FileOntAO;
import eu.learnpad.ontology.persistence.OntAO;
import eu.learnpad.or.rest.data.BusinessActor;
import eu.learnpad.or.rest.data.Experts;
import eu.learnpad.or.rest.data.Recommendations;

/**
 *
 * @author sandro.emmenegger
 */
public class Recommender {
    
    private static final Recommender instance = new Recommender();
    private OntAO ontAO;
    
    private Recommender() {
    	this.ontAO = new FileOntAO();
    }
    
    public static Recommender getInstance(){
        return instance;
    }
    
    public Recommendations getRecommendations(String modelSetId, String artifactId, String userId){
        Recommendations recommends = new Recommendations();
        Experts experts = new Experts();
        experts.addAllBusinessActors(suggestExpertsWithSameRole(modelSetId, artifactId, userId));
//        experts.addAllBusinessActors(suggestExpertMostOftenExecutedTask(modelSetId, artifactId, userId));
        recommends.setExperts(experts);
        return recommends;
    }
        
    public List<BusinessActor> suggestExpertsWithSameRole(String modelSetId, String artifactId, String userId){
        return getExperts("expertsWithSameRole", modelSetId, artifactId, userId);
    }
    
    public List<BusinessActor> suggestExpertMostOftenExecutedTask(String modelSetId, String artifactId, String userId){
        return getExperts("expertMostOftenExecutedTask", modelSetId, artifactId, userId);
    }

    private List<BusinessActor> getExperts(String queryName, String modelSetId, String artifactId, String userId) {
        RecommenderQuery queryObj = QueryMap.getQuery(queryName);
        List<BusinessActor> experts = new ArrayList<>();
        Query query = QueryFactory.create(queryObj.getQueryString());
        //TODO replace if execution data is available.        
//        OntModel model = ontAO.getExecutionData(modelSetId);
        OntModel model = ontAO.getInferencer(modelSetId).getModel();
        QueryExecution qexec = null;
        QuerySolutionMap initialBinding = new QuerySolutionMap();
        initialBinding.add("userId", model.createTypedLiteral(userId));
        
        try {
            qexec = QueryExecutionFactory.create(query, model, initialBinding);
            ResultSet results = qexec.execSelect();
            for (; results.hasNext();) {
                QuerySolution soln = results.nextSolution();
                BusinessActor expert = new BusinessActor();
                Resource businessActor = soln.getResource("businessActor");
                expert.setUri(businessActor.getURI());
                expert.setName(getLiteralString(soln, "otherPerformerName"));
                expert.setEmail(getLiteralString(soln, "email"));
                expert.setPhoneNumber(getLiteralString(soln, "phone"));
                expert.setRole(getLiteralString(soln, "roleName"));
                expert.setDescription(queryObj.getDescription());
                experts.add(expert);
            }
        } finally {
            if (qexec != null) {
                qexec.close();
            }
        }
        return experts;
    }
    
    private String getLiteralString(QuerySolution soln, String varName){
        Literal lit = soln.getLiteral(varName);
        if(lit != null){
            return lit.getString();
        }
        return null;
    }
    
    

}
