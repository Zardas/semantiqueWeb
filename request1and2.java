import java.io.InputStream;

import org.apache.jena.query.*;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

public class request1and2 {

	static String graphe = "result.ttl";
	
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
		
		String query_string = request2();
	
		
		Query query = QueryFactory.create(query_string);
		try (QueryExecution query_execution = QueryExecutionFactory.create(query, m)) {
    		ResultSet results = query_execution.execSelect() ;
    		ResultSetFormatter.out(System.out, results, query) ;
	    }
			
		
	}
	
	/**
	 * Requête : fournit les pays avec des devellopeurs travaillant dans des entreprises de plus de 10.000 employés
	 * @paramxsd:integer(AVG(?ConvertedSalary)
	 */
	public static String request1() {
		return (  "PREFIX schema:<http://schema.org/>"
				+ "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "SELECT distinct ?Country "
				+ "WHERE {"
				+ "	?PersonId rdf:type schema:Person." 
				+ " ?PersonId schema:country ?Country."
				+ " ?PersonId schema:numberOfEmployees ?nbEmployees."
				+ " FILTER(?nbEmployees = \"10,000 or more employees\")"
				+ "}"
			   );
	}
	
	/**
	 * Requête : fournit les IDE utilisés par les informaticiens du Paraguay ainsi que leurs id
	 * @param
	 */
	 public static String request2() {
		 return(  "PREFIX schema:<http://schema.org/>" 
				+ "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
		 		+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" 
				+ "PREFIX cwmo:<http://purl/org/cwmo/>"
				+ "SELECT ?PersonId ?ide "
				+ "WHERE {"
				+ 		"?PersonId rdf:type schema:Person."
				+		"?PersonId schema:country ?Country. "
				+		"?PersonId cwmo:tool ?ide."
				+ "FILTER(?Country = \"Paraguay\")" 
			+"}");
	}
	

}
	
	
