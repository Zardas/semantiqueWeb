
import java.io.InputStream;

import org.apache.jena.query.*;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

public class request3 {

	static String graphe_stackoverflow = "result.ttl";
	static String graphe_happiness = "happiness_report.ttl";
	
	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		Model m_stackoverflow = ModelFactory.createDefaultModel();
		Model m_happiness = ModelFactory.createDefaultModel();
		Model m = ModelFactory.createDefaultModel();

		InputStream stream_stackoverflow = FileManager.get().open(graphe_stackoverflow);
		//On vérifie que le fichier existe
		if(stream_stackoverflow == null) {
			throw new IllegalArgumentException("Le fichier " + graphe_stackoverflow + " n'existe pas");
		}
		InputStream stream_happiness = FileManager.get().open(graphe_happiness);
		//On vérifie que le fichier existe
		if(stream_happiness == null) {
			throw new IllegalArgumentException("Le fichier " + graphe_happiness + " n'existe pas");
		}
		
		m_stackoverflow.read(stream_stackoverflow, null, "Turtle");
		m_happiness.read(stream_happiness, null, "Turtle");

		m.add(m_stackoverflow);
		m.add(m_happiness);
		
		String query_string = request3_return();
	
		
		Query query = QueryFactory.create(query_string);
		try (QueryExecution query_execution = QueryExecutionFactory.create(query, m)) {
    		ResultSet results = query_execution.execSelect() ;
    		ResultSetFormatter.out(System.out, results, query) ;
	    }
			
		
	}
	
	/**
	 * Requête : fournit les 10 utilisateurs de StackOverflow les mieux payés habitant dans les pays ayant l'indice economique le plus élévé.
	 * @param
	 */
	public static String request3_return() {
		return (  "PREFIX schema: <http://schema.org/>" 
                + "PREFIX op: <http://environment.data.gov.au/def/op#>"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
                + "PREFIX owl:<http://www.w3.org/2002/07/owl#>"
                + "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                + "PREFIX dbo: <http://dbpedia.org/ontology/>"
                + "PREFIX rank: <http://dbpedia.org/ontology/rank#>"
                
                + "SELECT ?PersonId ?pays ?HappyMoney ?ConvertedSalary"
                
                + " WHERE {"
	            + "		   ?PersonId rdf:type schema:Person." 
	            +" 		   ?PersonId schema:country ?pays."
	            +"		   ?PersonId schema:numberOfEmployees ?nbEmployees."
	            +"		   ?PersonId schema:estimatedSalary ?ConvertedSalary."
	            +" 		   ?y dbo:country ?paysHappiness;"
	            +"     	      op:ecoGDPperCapita ?HappyMoney."
	            +" 		   FILTER(?nbEmployees = \"10,000 or more employees\") "
	            +" 	   	   FILTER(lcase(str(?pays)) = lcase(str(?paysHappiness)))"
	            +"		  }"
	            +" GROUP BY ?PersonId ?HappyMoney ?pays ?ConvertedSalary"
                +" ORDER BY DESC(?ConvertedSalary) DESC(?HappyMoney)"
                +" LIMIT 20"
                );
	}

}
	
	
