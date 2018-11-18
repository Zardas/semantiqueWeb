
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class projetWebSemantique {

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
	    } catch {
			throw new IllegalArgumentException("Requête " + query_string + " invalide");
		}
			
		
	}
	
	/**
	 * Requête : fournit les pays avec des devellopeurs travaillant dans des entreprises de plus de 10.000 employés
	 * @param
	 */
	public request1() {
		return ("SELECT ?country {" +
				"?Respondent <https://schema.org/Country> ?Country;" +
				"?Respondent <http://schema.org/numberOfEmployees> '10,000 or more employees'" +
			"}");
	}
	
	/**
	 * Requête : fournit les IDE utilisés par les informaticiens du Paraguay ainsi que leurs id
	 * @param
	 */
	 public request2() {
		 return("SELECT DISTINCT(?ide) ?respondent2 {" +
				"?respondent <https://schema.org/Country> 'Paraguay'";
				"?respondent <http://purl/org/cwmo/tool> ?ide".
				BIND(<http://www.w3.org/2001/XMLSchema#integer>(?respondent) AS ?respondent2)
			"}");
	}
	

}
	
	
