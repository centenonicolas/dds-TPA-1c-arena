package ar.edu.utn.frba.dds.vista;

import java.awt.Color;

import org.uqbar.arena.widgets.Button;
import org.uqbar.arena.widgets.Label;
import org.uqbar.arena.widgets.Panel;
import org.uqbar.arena.widgets.Selector;
import org.uqbar.arena.widgets.tables.Column;
import org.uqbar.arena.widgets.tables.Table;
import org.uqbar.arena.windows.SimpleWindow;
import org.uqbar.arena.windows.WindowOwner;

import ar.edu.utn.frba.dds.controlador.CuentaViewModel;
import ar.edu.utn.frba.dds.modelo.Balance;
import ar.edu.utn.frba.dds.modelo.Empresa;

public class ConsultaCuentaWindow extends SimpleWindow<CuentaViewModel> {
	
	public ConsultaCuentaWindow(WindowOwner parent, CuentaViewModel cuentaModel) {
		super(parent, cuentaModel);
	}

	@Override
	protected void addActions(Panel actionsPanel) {
		new Button(actionsPanel).setCaption("Salir").onClick(this::close);
	}

	@Override
	protected void createFormPanel(Panel form) {
		this.setTitle("Consulta Cuentas");
		new Label(form)
		.setText("Empresa")
		.setBackground(Color.ORANGE);
		
		Selector<Empresa> selectorEmpresa = new Selector<Empresa>(form).allowNull(true);
		selectorEmpresa.bindValueToProperty("empresaSeleccionada");
		selectorEmpresa.bindItemsToProperty("empresas").adaptWith(Empresa.class, "empresa_nombre");
		selectorEmpresa.setWidth(280);
				
		// Tabla de Balances
		Table<Balance> tableBalances = new Table<Balance>(form, Balance.class);
		tableBalances.setHeight(300);
		tableBalances.bindItemsToProperty("balancesEmpresaSeleccionada");
		tableBalances.bindValueToProperty("balanceSeleccionadoEmpresaSeleccionada");
		
		Column<Balance> columnaPeriodo = new Column<Balance>(tableBalances);
		columnaPeriodo.setTitle("Periodo");
		columnaPeriodo.bindContentsToProperty("balance_periodo");
		
		Column<Balance> columnaFrecuencia = new Column<Balance>(tableBalances);
		columnaFrecuencia.setTitle("Frecuencia");
		columnaFrecuencia.bindContentsToProperty("balance_frecuencia");
		
		Column<Balance> columnaValor = new Column<Balance>(tableBalances);
		columnaValor.setTitle("Valor");
		columnaValor.bindContentsToProperty("balance_valor").setTransformer(new DoubleStringTransformer());
		columnaValor.setWeight(32);
		
		Column<Balance> columnaTipoCuenta = new Column<Balance>(tableBalances);
		columnaTipoCuenta.setTitle("Tipo de Cuenta");
		columnaTipoCuenta.bindContentsToProperty("balance_tipoCuenta");
		
		//Valores Cuentas

		new Label(form)
		.setText("Valor Balances:")
		.setBackground(Color.GREEN);
		Label valorTotal = new Label(form);
		valorTotal.bindValueToProperty("valorBalances").setTransformer(new DoubleStringValueTransformer());
		
	}

}
