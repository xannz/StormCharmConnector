/**
 * 
 */
package com.ubuntu.stormdeployer;

import org.yaml.snakeyaml.constructor.Constructor;
import java.util.HashMap;

import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.Node;
/**
 * @author maarten
 *
 */
public class TopologiesConstructor extends Constructor {
	

	
	    private HashMap<String,Class<?>> classMap = new HashMap<String,Class<?>>();

	       public TopologiesConstructor(Class<? extends Object> theRoot) {
	           super( theRoot );
	           classMap.put( Topologies.class.getName(), Topologies.class );
	           classMap.put( Topology.class.getName(), Topology.class );
	           classMap.put( DataSource.class.getName(), DataSource.class );
	           classMap.put( Parameter.class.getName(), Parameter.class );
	       }

	       /*
	        * This is a modified version of the Constructor. Rather than using a class loader to
	        * get external classes, they are already predefined above. This approach works similar to
	        * the typeTags structure in the original constructor, except that class information is
	        * pre-populated during initialization rather than runtime.
	        *
	        * @see org.yaml.snakeyaml.constructor.Constructor#getClassForNode(org.yaml.snakeyaml.nodes.Node)
	        */
	        protected Class<?> getClassForNode(Node node) {
	            String name = node.getTag().substring(node.getTag().lastIndexOf(':')+1);
	            Class<?> cl = classMap.get( name );
	            if ( cl == null )
	                throw new YAMLException( "Class not found: " + name );
	            else
	                return cl;
	        }
	
}
