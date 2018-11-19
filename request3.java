import java.io;

import org.apache.jena.query.*;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

public class projetWebSemantiqueMultiple {

	static String graphe_stackoverflow = "graphe.ttl";
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
		
		String query_string = request3();
	
		
		Query query = QueryFactory.create(query_string);
		try (QueryExecution query_execution = QueryExecutionFactory.create(query, m)) {
    		ResultSet results = query_execution.execSelect() ;
    		ResultSetFormatter.out(System.out, results, query) ;
	    } catch {
			throw new IllegalArgumentException("Requête " + query_string + " invalide");
		}
			
		
	}
	
	/**
	 * Requête : fournit les pays avec leur coefficient salaire_moyen/bonheur par habitant (pour les dévellopeurs (utilisant stack overflow))
	 * @param
	 */
	public request3() {
		return ("PREFIX schema: <http://schema.org/>" +
                "PREFIX geof: <http://www.mindswap.org/2003/owl/geo/geoFeatures20040307.owl#>" +
                "PREFIX op: <http://environement.data.gov.au/def/op#>" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" +
                "PREFIX owl:<http://www.w3.org/2002/07/owl#>" +
                "SELECT DISTINCT(?pays) (xsd:integer(?SalaireMoyen) / xsd:float(AVG(?EconomyGDPPerCapita)) AS ?result)" +
                "WHERE {" +
                "  ?x schema:country ?paysStackOverflow;" +
                "     schema:baseSalary ?salary." +
                "  ?salary schema:estimatedSalary ?ConvertedSalary." +
                "" +
                "  ?y geop:country ?paysHappiness;" +
                "     op:ecoGDPPerCapita ?EconomyGDPPerCapita." +
                "" +
                "  ?paysStackOverflow owl:sameAs ?paysHappiness." +
                "  BIND(xsd:integer(AVG(?ConvertedSalary)) AS ?SalaireMoyen)" +
                "}");
	}
	

}
	
	
