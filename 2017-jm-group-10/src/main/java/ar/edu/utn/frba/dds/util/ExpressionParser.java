package ar.edu.utn.frba.dds.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ar.edu.utn.frba.dds.expresion.Expresion;
import ar.edu.utn.frba.dds.expresion.ExpresionCompuesta;
import ar.edu.utn.frba.dds.expresion.ExpresionConstante;
import ar.edu.utn.frba.dds.expresion.ExpresionCuenta;
import ar.edu.utn.frba.dds.expresion.ExpresionIndicador;
import ar.edu.utn.frba.dds.expresion.ExpresionSimple;
import ar.edu.utn.frba.dds.expresion.Operacion;
import ar.edu.utn.frba.dds.modelo.TipoDeCuenta;
import ar.edu.utn.frba.dds.repositorios.RepositorioIndicadores;
import ar.edu.utn.frba.dds.util.exceptions.InvalidTokenException;
import ar.edu.utn.frba.dds.util.exceptions.SyntaxErrorException;
import ar.edu.utn.frba.dds.util.exceptions.TypeExpresionException;

public class ExpressionParser {

	private static String patternParser = "\\+|\\-|\\*|\\/|[0-9]+(\\.[0-9]*)?|[a-zA-Z]+[a-zA-Z0-9]*";
	private static Pattern evaluationPattern = Pattern.compile(patternParser);

	private static String patternVariable = "^\\-?[a-zA-Z]+[a-zA-Z0-9]*$";
	private static String patternNumber = "^\\-?[0-9]+(\\.[0-9]*)?$";

	private static Map<String, Operator> ops = new HashMap<String, Operator>() {
		{
			put("+", Operator.ADD);
			put("-", Operator.SUBSTRACT);
			put("*", Operator.MULTIPLY);
			put("/", Operator.DIVIDE);

		}
	};

	private Boolean isOperator(String token) {
		if (token == null)
			return true; // Para el caso puntual de que la primera constante sea
							// negativa
		else
			return ExpressionParser.ops.containsKey(token);
	}

	private Boolean isVariable(String token) {
		return token.matches(ExpressionParser.patternVariable);
	}

	private Boolean isNumber(String token) {
		return token.matches(ExpressionParser.patternNumber);
	}

	private String nextToken(Matcher expressionMatch) throws InvalidTokenException {
		String nextToken = null;
		String currToken = null;

		// TOOD: Se puede implementar una funcion que verifique el match y haga el find, y explote aparte
		
		// Para el caso de que el primer lexema sea un signo "-"
		try {
			currToken = expressionMatch.group();
		} catch (IllegalStateException e) {
			currToken = null;
		}
		if (expressionMatch.find()) {
			nextToken = expressionMatch.group();
			if ((isOperator(nextToken)) && (nextToken.compareTo("-") == 0) && (isOperator(currToken))
					&& (expressionMatch.find())) {
				nextToken += expressionMatch.group();
//			} else if ((!isNumber(nextToken)) || (!isVariable(nextToken))) {
//				// ERROR: Solo se paso un operador
//				// el token no es ni un numero, ni una variable, ni un operador
//				throw new InvalidTokenException();
			}
		} else {
			// ERROR: No se pudo parsear expresion
			// se acabaron las expresiones para parsear
			throw new InvalidTokenException();
		}
		return nextToken;
	}

	private Expresion parseExpresion(String currToken, String proxToken, Expresion resultExpresion) throws TypeExpresionException {
		if ((resultExpresion == null) && (proxToken == null)) {
			return getFromToken(currToken);
		}
		if (resultExpresion.getClass() != ExpresionCompuesta.class) {
			ExpresionSimple expresionI = (ExpresionSimple) resultExpresion;
			Operacion operacion = new Operacion(ops.get(currToken));
			Expresion expresionD = getFromToken(proxToken);
			return new ExpresionCompuesta(expresionI, operacion, expresionD);
			// TODO: en caso de que no sea de tipo simple la expresion (null por ejemplo)
			// Explotar por el aire, algo muy FEO paso!!
		}
		if ( ((ExpresionCompuesta)resultExpresion).getOp().getOperador().precedence >= ops.get(currToken).precedence ) {
			ExpresionCompuesta expresionI = (ExpresionCompuesta)resultExpresion;
			Operacion operacion = new Operacion(ops.get(currToken));
			Expresion expresionD = getFromToken(proxToken);
			return new ExpresionCompuesta(expresionI, operacion, expresionD);
		} else {
			Operacion operacion = new Operacion(ops.get(currToken));
			Expresion expresionD = getFromToken(proxToken);
			ExpresionCompuesta inferior = new ExpresionCompuesta(((ExpresionCompuesta)resultExpresion).getOperando2(),operacion, expresionD);
			return new ExpresionCompuesta(((ExpresionCompuesta)resultExpresion).getOperando1(), ((ExpresionCompuesta)resultExpresion).getOp(), inferior);
		}
	}
	
	public Expresion buildExpressionFrom(String expresion) throws InvalidTokenException, SyntaxErrorException, TypeExpresionException {
		Expresion resultExpression = null;
		Matcher expressionMatch = ExpressionParser.evaluationPattern.matcher(expresion);
		String currToken = "";
		String proxToken = "";

		currToken = nextToken(expressionMatch);
		resultExpression = parseExpresion(currToken, null, resultExpression);

		// TODO: Evaluar gramatica para que no vengan dos CONSTANTES Juntas Ej 8 y PEPE (No hay operacion entre Constantes)
		// Los casos de dos operaciones juntas ++ *+, se encarga nextToken de explotar ya que puede ser valida con un *- +-
//        System.out.println("Token: " + currToken);
	      // TODO: Contar caracteres y compara contra length para lexema no valido (Por ej: &), ya que el rerex lo ignora
	      while  (expressionMatch.end() != expresion.length()) {
	        currToken = nextToken(expressionMatch);
//	        System.out.println("Token: " + currToken);
	        proxToken = nextToken(expressionMatch);
	        if (isOperator(currToken) && ( isVariable(proxToken) || isNumber(proxToken) ) ) {
//		        System.out.println("Token: " + proxToken);
	        	resultExpression = parseExpresion(currToken, proxToken, resultExpression);
	        } else {
	        	// ERROR: Paso algo raro en la expresion porque no vino Operacion Operando
	        	// esto es un error syntactico porque me pasaron ** o 89PEPE o algo asi
	        	throw new SyntaxErrorException();
	        }
	      }
	      return resultExpression;
	}

	private Expresion getFromToken(String token) {
		if (isNumber(token)) {
			return new ExpresionConstante(Integer.parseInt(token));
		} else if (isVariable(token)) {
			ExpresionConstante expresionI = new ExpresionConstante(-1);
			Operacion operacion = new Operacion(Operator.MULTIPLY);
			Expresion expresionD = null;
			if (RepositorioIndicadores.obtenerSiExiste(token) != null) {
				expresionD = new ExpresionIndicador(RepositorioIndicadores.obtenerSiExiste(token));
			} else {
				expresionD = new ExpresionCuenta(TipoDeCuenta.valueOf(token));
			}
			if (token.matches("^[\\-].*")) {
				return new ExpresionCompuesta(expresionI, operacion, expresionD);
			} else {
				return expresionD;
			}
		} else {
			// ERROR el token no es ni variable ni constante, me pasaron cualquier token HDP!
			return null;
		}
	}

}
