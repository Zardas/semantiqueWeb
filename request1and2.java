import java.io.InputStream;

import org.apache.jena.query.*;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

public class request1and2 {

	static String graphe = "graphe.ttl";
	
	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		Model m = ModelFactory.createDefaultModel();
		
		InputStream stream = FileManager.get().open(graphe);
		//On vérifie que le fichier existe
		if(stream == null) {
			throw new IllegalArgumentException("Le fichier " + graphe + " n'existe pas");
		}
		
		m.read(stream, null, "Turtle");
		
		String query_string = request1();
	
		
		Query query = QueryFactory.create(query_string);
		try (QueryExecution query_execution = QueryExecutionFactory.create(query, m)) {
    		ResultSet results = query_execution.execSelect() ;
    		ResultSetFormatter.out(System.out, results, query) ;
	    }
			
		
	}
	
	/**
	 * Requête : fournit les pays avec des devellopeurs travaillant dans des entreprises de plus de 10.000 employés
	 * @param
	 */
	public static String request1() {
		return ("SELECT ?country WHERE {" +
				"?Respondent <https://schema.org/Country> ?Country;" +
				"?Respondent <http://schema.org/numberOfEmployees> '10,000 or more employees'" +
			"}");
	}
	
	/**
	 * Requête : fournit les IDE utilisés par les informaticiens du Paraguay ainsi que leurs id
	 * @param
	 */
	 public static String request2() {
		 return("SELECT DISTINCT(?ide) ?respondent2 WHERE {" +
				"?respondent <https://schema.org/Country> 'Paraguay';" +
				"?respondent <http://purl/org/cwmo/tool> ?ide." +
				"BIND(<http://www.w3.org/2001/XMLSchema#integer>(?respondent) AS ?respondent2)" +
			"}");
	}
	

}
	
	
