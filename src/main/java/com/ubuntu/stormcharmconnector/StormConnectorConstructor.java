/**
 * 
 */
package com.ubuntu.stormcharmconnector;

import org.yaml.snakeyaml.constructor.Constructor;
import java.util.HashMap;

import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.Node;
/**
 * @author maarten
 *
 */
public class StormConnectorConstructor extends Constructor {
	

	
	    private HashMap<String,Class<?>> classMap = new HashMap<String,Class<?>>();

	       public StormConnectorConstructor(Class<? extends Object> theRoot) {
	           super( theRoot );
	           classMap.put( StormConnector.class.getName(), StormConnector.class );
	           classMap.put( Type.class.getName(), Type.class );
	           classMap.put( Instance.class.getName(), Instance.class );

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
