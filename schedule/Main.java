package schedule;

import java.util.*;
import java.time.*;
import javax.swing.*;
import java.sql.*;
import java.lang.reflect.*;

public class Main {
	static String QUERY1 = "SELECT ID, NAME, HOURS_WORKED FROM Funcionarios";	
	static String QUERY2 = "SELECT ID, NAME FROM Moradores";	
	//static String QUERY3 = "SELECT ID, IDENTIFIER, START, END, RESERVE_DURATION, ANTECIPATION";	
	static String QUERY4 = "SELECT ID, IDENTIFIER, CLASS, START, END FROM Instalacoes";
	
	public static void main(String args[]) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/residential", root, user);
		

		
		HashMap <Integer, ReservableObject> instalacoes = new HashMap<>();
		HashMap <Integer, Morador> moradores = new HashMap<>();
		PriorityQueue <Funcionario> funcionarios = new PriorityQueue<>();


		
		Statement stmt = con.createStatement();
		
		
		ResultSet RS1 = stmt.executeQuery(QUERY1);
		while (RS1.next()) {
			Funcionario f = new Funcionario(RS1.getInt("ID"), RS1.getString("NAME"));
			funcionarios.add(f);
		}
		
		
		
		ResultSet RS2= stmt.executeQuery(QUERY2);
		while (RS2.next()) {
			Morador m = new Morador(RS2.getInt("ID"), RS2.getString("NAME"));
			moradores.put(m.ID, m);
		}
		

		
		ResultSet RS3 = stmt.executeQuery(QUERY4);
		Object o;
		LocalTime start_in, end_in;
		Class<?> clazz;
		String cz, identifier;
		int id;
		while (RS3.next()) {
			try {
				cz = RS3.getString("CLASS");
				clazz = Class.forName("schedule." + cz);
				id = RS3.getInt("ID");
				//identifier = RS3.getString("IDENTIFIER");

				try {
					start_in = RS3.getTime("START").toLocalTime();
					end_in = RS3.getTime("END").toLocalTime();
					Constructor<?> cstructor = clazz.getConstructor(LocalTime.class, LocalTime.class);
					o = cstructor.newInstance(start_in, end_in);
					instalacoes.put(id,(ReservableObject) o);

					//polimorfismo satânico
					
				} catch (IllegalArgumentException | NullPointerException | NoSuchMethodException exc){
					Constructor<?> cstructor = clazz.getConstructor();
					o = cstructor.newInstance();
					instalacoes.put(id,(ReservableObject) o);
				}		
			} catch (
					ClassNotFoundException |
					IllegalAccessException |
					IllegalArgumentException|
					InstantiationException|
					NoSuchMethodException|
					InvocationTargetException e) 
		      {
		        System.out.println(e);
		      }
		}//while rs3
		


		
		Iterator <ReservableObject> ite = instalacoes.values().iterator();
		ReservableObject rob;
		while (ite.hasNext()) {
			rob = ite.next();
			//System.out.println(ob);
			rob.addDay(LocalDate.now(), 7);
			rob.print_file(rob.toString());
		}
		//só checando se deu certo
		



		//criando uma reserva para o Morador ID = 1 na data 21/05/2025 às 08:00, com duração de 50 minutos
		Reserve rsv = new Reserve(
				moradores.get(1),
				123,
				LocalDateTime.of(2025, 5, 21, 8, 0),
				Duration.ofMinutes(50));
		
		//adicionando essa reserva à instalação de ID = 2 (uma quadra)
		ReservableObject inst = instalacoes.get(2);
		inst.addReserve(rsv);
		//classe Reserva checa se há overlap com outra reserva
		//classe Day checa se a reserva estoura os limites do dia
		//classe Schedule checa se a reserva está sendo adicionada numa data existente


		inst.print_file(inst.toString());
		
		
	}//main
}
