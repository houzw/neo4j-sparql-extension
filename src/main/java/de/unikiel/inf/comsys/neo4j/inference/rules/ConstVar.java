package de.unikiel.inf.comsys.neo4j.inference.rules;

import org.openrdf.model.BNode;
import org.openrdf.model.Literal;
import org.openrdf.model.Value;
import org.openrdf.query.algebra.Var;

/**
 * Implements a constant Sesame algebra variable value.
 * 
 * The sesame query model doesn't provide a seperate class for constant
 * values as literals or URIs. Instead a anonymouse instance of the
 * {@link Var} class is used with a special unique name.
 * 
 * Unfortunately the method that translates SPARQL algebra syntax to constant
 * instances of {@link Var} is not public.
 * As transformations may need to create new constant algebra expressions
 * the code was taken from the Sesame implementation, so that the generated
 * unique names will correspond to the unique names generated by Sesame.
 * See reference below for the original method.
 * @see <a href="https://github.com/ansell/openrdf-sesame/blob/dc36dbf7a487421c8c1cc143cf735f710c5b347c/core/queryparser/sparql/src/main/java/org/openrdf/query/parser/sparql/TupleExprBuilder.java#L313">
 * createConstVar method in original implementation</a>
 */
public class ConstVar extends Var {
	
	public ConstVar(Value value) {
		if (value == null) {
			throw new IllegalArgumentException("value can not be null");
		}

		String uniqueStringForValue = value.stringValue();

		if (value instanceof Literal) {
			uniqueStringForValue += "-lit";

			// we need to append datatype and/or language tag to ensure a unique var name (see SES-1927)
			Literal lit = (Literal) value;
			if (lit.getDatatype() != null) {
				uniqueStringForValue += "-" + lit.getDatatype().stringValue();
			}
			if (lit.getLanguage() != null) {
				uniqueStringForValue += "-" + lit.getLanguage();
			}
		} else if (value instanceof BNode) {
			uniqueStringForValue += "-node";
		} else {
			uniqueStringForValue += "-uri";
		}
		setName("-const-" + uniqueStringForValue);
		setConstant(true);
		setAnonymous(true);
		setValue(value);
	}
}
