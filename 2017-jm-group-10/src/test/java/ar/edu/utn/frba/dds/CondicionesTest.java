package ar.edu.utn.frba.dds;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ar.edu.utn.frba.dds.expresion.ExpresionCuenta;
import ar.edu.utn.frba.dds.metodologia.Comparador;
import ar.edu.utn.frba.dds.metodologia.ComparadorAntiguedad;
import ar.edu.utn.frba.dds.metodologia.ComparadorDesempenio;
import ar.edu.utn.frba.dds.metodologia.CondicionAntiguedad;
import ar.edu.utn.frba.dds.metodologia.CondicionConsistenciaTiempo;
import ar.edu.utn.frba.dds.metodologia.CondicionGeneral;
import ar.edu.utn.frba.dds.metodologia.CondicionCompararValor;
import ar.edu.utn.frba.dds.metodologia.Metodologia;
import ar.edu.utn.frba.dds.metodologia.TipoOperacion;
import ar.edu.utn.frba.dds.metodologia.Comparador.Comparadores;
import ar.edu.utn.frba.dds.metodologia.TipoOperacion.Operaciones;
import ar.edu.utn.frba.dds.modelo.Balance;
import ar.edu.utn.frba.dds.modelo.Empresa;
import ar.edu.utn.frba.dds.modelo.Indicador;
import ar.edu.utn.frba.dds.modelo.TipoDeCuenta;

public class CondicionesTest {

	Empresa empresa;
	Balance balance;
	List<Balance> listaBalances;
	Indicador indicador;
	@Before
	public void init(){

		empresa = new Empresa();
		balance = new Balance();
		balance.setBalance_periodo("20170100");
		balance.setBalance_tipoCuenta(TipoDeCuenta.EBITDA);
		balance.setBalance_valor(new Double(25000));
		listaBalances = new ArrayList<Balance>();
		listaBalances.add(balance);
		empresa.setBalances(listaBalances);
		empresa.setEmpresa_anioCreacion(2006);
		indicador = new Indicador("indicador", new ExpresionCuenta(TipoDeCuenta.EBITDA));
	}
	
	@Test
	public void unaEmpresaEsInvertibleConValorSuperior(){
		
		CondicionCompararValor condicionValor = new CondicionCompararValor();
		condicionValor.setInicioPeriodo("20170100");
		condicionValor.setFinPeriodo("20170100");
		Comparador comparador = new Comparador();
		comparador.setComparador(Comparadores.MAYOR);
		condicionValor.setComparador(comparador);
		condicionValor.setValorSuperar(20000);
		Indicador indicador = new Indicador("indicador", new ExpresionCuenta(TipoDeCuenta.EBITDA));
		condicionValor.setIndicador(indicador);
		
		Assert.assertEquals(true, condicionValor.deberiaInvertirEn(empresa));
	}
	
	@Test
	public void unaEmpresaEsInvertibleConValorInferior(){
		
		CondicionCompararValor condicionValor = new CondicionCompararValor();
		condicionValor.setInicioPeriodo("20170100");
		condicionValor.setFinPeriodo("20170100");
		Comparador comparador = new Comparador();
		comparador.setComparador(Comparadores.MENOR);
		condicionValor.setComparador(comparador);
		condicionValor.setValorSuperar(30000);
		condicionValor.setIndicador(indicador);
		
		Assert.assertEquals(true, condicionValor.deberiaInvertirEn(empresa));
	}
	
	@Test
	public void convieneEmpresaConIndicadorMasAlto() {
		Empresa empresa2 = new Empresa();
		Balance balance2 = new Balance();
		balance2.setBalance_periodo("20170100");
		balance2.setBalance_tipoCuenta(TipoDeCuenta.EBITDA);
		balance2.setBalance_valor(new Double(20000));
		List<Balance> listaBalances = new ArrayList<Balance>();
		listaBalances.add(balance2);
		empresa2.setBalances(listaBalances);
		
		ComparadorDesempenio comparadorDesempenio = new ComparadorDesempenio();
		Comparador comparador = new Comparador();
		comparador.setComparador(Comparadores.MAYOR);
		comparadorDesempenio.setComparador(comparador);
		comparadorDesempenio.setIndicador(indicador);
		comparadorDesempenio.setInicioPeriodo("20170100");
		Assert.assertEquals(empresa, comparadorDesempenio.cualEmpresaInvertir(empresa, empresa2));
	}
	
	@Test
	public void convieneEmpresaConIndicadorMasBajo() {
		Empresa empresa2 = new Empresa();
		Balance balance2 = new Balance();
		balance2.setBalance_periodo("20170100");
		balance2.setBalance_tipoCuenta(TipoDeCuenta.EBITDA);
		balance2.setBalance_valor(new Double(20000));
		List<Balance> listaBalances = new ArrayList<Balance>();
		listaBalances.add(balance2);
		empresa2.setBalances(listaBalances);
		
		ComparadorDesempenio comparadorDesempenio = new ComparadorDesempenio();
		Comparador comparador = new Comparador();
		comparador.setComparador(Comparadores.MENOR);
		comparadorDesempenio.setComparador(comparador);
		comparadorDesempenio.setIndicador(indicador);
		comparadorDesempenio.setInicioPeriodo("20170100");
		Assert.assertEquals(empresa2, comparadorDesempenio.cualEmpresaInvertir(empresa, empresa2));
	}
	
	@Test
	public void compararDesempenioEmpresasIgualesDevuelvePrimera() {
		Empresa empresa2 = new Empresa();
		Balance balance2 = new Balance();
		balance2.setBalance_periodo("20170100");
		balance2.setBalance_tipoCuenta(TipoDeCuenta.EBITDA);
		balance2.setBalance_valor(new Double(25000));
		List<Balance> listaBalances = new ArrayList<Balance>();
		listaBalances.add(balance2);
		empresa2.setBalances(listaBalances);
		
		
		ComparadorDesempenio comparadorDesempenio = new ComparadorDesempenio();
		Comparador comparador = new Comparador();
		comparador.setComparador(Comparadores.IGUAL);
		comparadorDesempenio.setComparador(comparador);
		comparadorDesempenio.setIndicador(indicador);
		comparadorDesempenio.setInicioPeriodo("20170100");
		comparadorDesempenio.cualEmpresaInvertir(empresa, empresa2);
		Assert.assertEquals(empresa, comparadorDesempenio.cualEmpresaInvertir(empresa, empresa2));
	}
	
	@Test
	public void esConsistentementeCrecienteEnPeriodo(){
		Balance balance2 = new Balance();
		balance2.setBalance_periodo("20170600");
		balance2.setBalance_tipoCuenta(TipoDeCuenta.EBITDA);
		balance2.setBalance_valor(new Double(26000));
		listaBalances.add(balance2);
		empresa.setBalances(listaBalances);
		
		CondicionConsistenciaTiempo condicionCreciente = new CondicionConsistenciaTiempo();
		condicionCreciente.setInicioPeriodo("20170100");
		condicionCreciente.setFinPeriodo("20170600");
		Comparador comparador = new Comparador();
		comparador.setComparador(Comparadores.MAYOR);
		condicionCreciente.setComparador(comparador);
		Indicador indicador = new Indicador("indicador", new ExpresionCuenta(TipoDeCuenta.EBITDA));
		condicionCreciente.setIndicador(indicador);
		Assert.assertTrue(condicionCreciente.deberiaInvertirEn(empresa));
	}
	
	@Test
	public void cumpleLongevidadTaxativa(){
		CondicionAntiguedad condicionAntiguedad = new CondicionAntiguedad();
		balance.setBalance_periodo("200706");
		listaBalances.add(balance);
		empresa.setBalances(listaBalances);
		condicionAntiguedad.setAniosNecesarios(10);
		Assert.assertTrue(condicionAntiguedad.deberiaInvertirEn(empresa));
	}
	
	@Test
	public void cumpleLongetividadComparativa(){
		Empresa empresa2 = new Empresa();
		Balance balance2 = new Balance();
		empresa2.setEmpresa_anioCreacion(2003);
		balance2.setBalance_periodo("20130600");
		balance2.setBalance_tipoCuenta(TipoDeCuenta.EBITDA);
		balance2.setBalance_valor(new Double(26000));
		List<Balance> listaBalances2 = new ArrayList<Balance>();
		listaBalances2.add(balance2);
		empresa2.setBalances(listaBalances2);
		ComparadorAntiguedad comparadorAntiguedad = new ComparadorAntiguedad();
		Comparador comparador = new Comparador();
		comparador.setComparador(Comparadores.MAYOR);
		comparadorAntiguedad.setComparador(comparador);
		Assert.assertEquals(empresa2, comparadorAntiguedad.cualEmpresaInvertir(empresa, empresa2));
	}
	
	@Test
	public void cumpleCondicionSumatoria(){
		listaBalances.add(balance);
		empresa.setBalances(listaBalances);
		CondicionGeneral condicionGeneral = new CondicionGeneral();
		Comparador comparador = new Comparador();
		comparador.setComparador(Comparadores.MAYOR);
		condicionGeneral.setComparador(comparador);
		condicionGeneral.setValorASuperar(40000);
		condicionGeneral.setIndicador(indicador);
		TipoOperacion tipo = new TipoOperacion();
		tipo.setTipoOperacion(Operaciones.SUMATORIA);
		condicionGeneral.setTipoOperacion(tipo);
		Assert.assertTrue(condicionGeneral.deberiaInvertirEn(empresa));
	}
	
	@Test
	public void cumpleCondicionPromedio(){
		Balance balance2 = new Balance();

		balance2.setBalance_periodo("20130600");
		balance2.setBalance_tipoCuenta(TipoDeCuenta.EBITDA);
		balance2.setBalance_valor((double)15000);
		listaBalances.add(balance2);
		empresa.setBalances(listaBalances);
		CondicionGeneral condicionGeneral = new CondicionGeneral();

		Comparador comparador = new Comparador();
		comparador.setComparador(Comparadores.MAYOR);
		condicionGeneral.setComparador(comparador);
		condicionGeneral.setValorASuperar(19999);
		condicionGeneral.setIndicador(indicador);
		TipoOperacion tipo = new TipoOperacion();
		tipo.setTipoOperacion(Operaciones.PROMEDIO);
		condicionGeneral.setTipoOperacion(tipo);
		Assert.assertTrue(condicionGeneral.deberiaInvertirEn(empresa));
	}
	
	@Test
	public void cumpleCondicionMediana(){
		Balance balance2 = new Balance();
		balance2.setBalance_periodo("20130600");
		balance2.setBalance_tipoCuenta(TipoDeCuenta.EBITDA);
		balance2.setBalance_valor((double)15000);
		Balance balance3 = new Balance();
		balance3.setBalance_periodo("20150600");
		balance3.setBalance_tipoCuenta(TipoDeCuenta.EBITDA);
		balance3.setBalance_valor((double)20000);
		listaBalances.add(balance2);
		listaBalances.add(balance3);
		empresa.setBalances(listaBalances);
		CondicionGeneral condicionGeneral = new CondicionGeneral();

		Comparador comparador = new Comparador();
		comparador.setComparador(Comparadores.MAYOR);
		condicionGeneral.setComparador(comparador);
		condicionGeneral.setValorASuperar(19999); //deberia dar 20000 la mediana de los tres valores
		condicionGeneral.setIndicador(indicador);
		TipoOperacion tipo = new TipoOperacion();
		tipo.setTipoOperacion(Operaciones.MEDIANA);
		condicionGeneral.setTipoOperacion(tipo);
		Assert.assertTrue(condicionGeneral.deberiaInvertirEn(empresa));
	}
	
	@Test
	public void esMetodologia() {
		Metodologia metodologia = new Metodologia();
		CondicionAntiguedad condicionAntiguedad = new CondicionAntiguedad();
		condicionAntiguedad.setAniosNecesarios(10);
		condicionAntiguedad.setNombreCondicion("longetividad");
		metodologia.setNombre("metodo1");
		metodologia.agregarCondicion(condicionAntiguedad);
		Assert.assertEquals(1, metodologia.getCondicionesTaxativas().size());	
	}
	
}
