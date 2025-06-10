package residential3;

import java.util.*;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.lang.reflect.*;


public class Manager {
	Connection con;
	
	HashMap <Integer, Facility> facilities;
	HashMap <Integer, Resident> residents;
	HashMap <Integer, Event> events;
	PriorityQueue <Employee> employees;
	
	public Manager(Connection con_in) throws SQLException, Exception {
		con = con_in;
		
		facilities = new HashMap<Integer, Facility>();
		residents = new HashMap<Integer, Resident>();
		events = new HashMap <Integer, Event>();
		employees = new PriorityQueue<Employee>();

		
		Statement stmt = con.createStatement();

		ResultSet RS1 = stmt.executeQuery("SELECT ID, NAME, WEEKLY_HOURS FROM Employees");
		while (RS1.next()) {
			Employee f = new Employee(RS1.getInt("ID"), RS1.getString("NAME"), RS1.getString("WEEKLY_HOURS"));
			employees.add(f);
		}
		
		ResultSet RS2 = stmt.executeQuery("SELECT ID, NAME FROM Residents");
		while (RS2.next()) {
			Resident m = new Resident(RS2.getInt("ID"), RS2.getString("NAME"));
			residents.put(m.ID, m);
		}
		
		
		ResultSet RS3 = stmt.executeQuery("SELECT ID, IDENTIFIER, CLASS FROM Facilities");
		Object o;
		Class<?> clazz;
		int ID;
		String IDENTIFIER, CLASS;
		while (RS3.next()) {
			ID = RS3.getInt("ID");
			IDENTIFIER = RS3.getString("IDENTIFIER");
			CLASS = RS3.getString("CLASS");
			
			try {
				clazz = Class.forName("residential3." + CLASS);
				Constructor<?> cstructor = clazz.getConstructor(int.class, String.class);
				o = cstructor.newInstance(ID, IDENTIFIER);
				facilities.put(ID,(Facility) o);
				Facility f = facilities.get(ID);
				f.addDay(LocalDate.now(), f.antecipation);
			} catch (
					ClassNotFoundException |
					IllegalAccessException |
					InstantiationException|
					NoSuchMethodException|
					InvocationTargetException e) 
			{
				System.out.println(e);
			}
			
		}
		
		ResultSet RS4 = stmt.executeQuery("SELECT * FROM Reserves");
		while (RS4.next()) {
			Reserve rsv = new Reserve(RS4.getInt("ID"), 
					residents.get(RS4.getInt("RESIDENT_ID")), 
					(Reservable)facilities.get(RS4.getInt("FACILITY_ID")),
					RS4.getDate("DATE").toLocalDate(), 
					RS4.getTime("START").toLocalTime());
			events.put(rsv.key, rsv);
			
			
			Statement stmt2 = con.createStatement();
			ResultSet RS5 = stmt2.executeQuery(String.format("SELECT * FROM Facilities WHERE ID = %d", RS4.getInt("FACILITY_ID")));
			while (RS5.next()) {
				Reservable r = (Reservable)facilities.get(RS5.getInt("ID"));
				r.addReserve(rsv);
			}
		}
		
		ResultSet RS6 = stmt.executeQuery("SELECT * FROM Supervisions");
		while (RS6.next()) {
			Supervision spv = new Supervision(RS6.getInt("ID"), 
					(Supervised)facilities.get(RS6.getInt("FACILITY_ID")),
					RS6.getDate("DATE").toLocalDate(), 
					RS6.getTime("START").toLocalTime());
			events.put(spv.key, spv);
			
			
			Statement stmt2 = con.createStatement();
			ResultSet RS7 = stmt2.executeQuery(String.format("SELECT * FROM Facilities WHERE ID = %d", RS6.getInt("FACILITY_ID")));
			while (RS7.next()) {
				Supervised spvd = (Supervised)facilities.get(RS7.getInt("ID"));
				spvd.addEvent(spv);
			}
		}
	}
	
	
	
	public void make_random_events() throws SQLException, Exception {
		Random r = new Random();
		
		int year, month, day, hour, minute, second;
		int fclt;
		Reservable rsvable;
		Supervised spvd;
		Employee emp;
		Resident rsdt;
		
		outerloop: for (int i = 1; i < 100; i++) {
			year = 2025;
			month = 6;
			day = LocalDate.now().getDayOfMonth() + r.nextInt(21);
			hour = 6 + r.nextInt(16);
			minute = 5 * r.nextInt(11);
			second = 0;
			fclt = 1 + r.nextInt(26);
			rsdt = residents.get(1 + r.nextInt(20));
			
			try {
				if (fclt <= 11) {
					rsvable = (Reservable) facilities.get(fclt);
					Reserve rsv = new Reserve(i, rsdt, rsvable, LocalDateTime.of(year, month, day, hour, minute, second));
					//rsvable.addReserve(rsv);
					add_Reserve(rsv);
					
					System.out.printf("%d %s %s %02d/%02d/%02d %02d:%02d:%02d\n",
							i, rsdt.name, rsvable.identifier, year, month, day, hour, minute, second);
					
				} else if (fclt <= 21) {
					rsvable = (Reservable) facilities.get(fclt);
					Reserve rsv = new Reserve(i, rsdt, rsvable, LocalDate.of(year, month, day));
					add_Reserve(rsv);
					
					System.out.printf("%d %s %s %02d/%02d/%02d\n",
							i, rsdt.name, rsvable.identifier, year, month, day);
				} else {
					i--;
					/*
					spvd = (Supervised) facilities.get(fclt);
					
					LocalDate date = LocalDate.of(year, month, day);
					Supervision spv = new Supervision(i, spvd, LocalDate.of(year, month, day));
					try {
						assign(spv);
						System.out.printf("%d %s %s %02d/%02d/%02d\n",
								i, spv.emp.name, spvd.identifier, year, month, day);
					} catch (EmployeeException e) {
						i--;
						continue outerloop;
					}
					*/
				}
				
			} catch (ScheduleException |
					ReservableException |
					SupervisedException |
					NullPointerException|
					EmployeeException e) {
				System.out.printf("%d %s %02d/%02d/%02d %02d:%02d:%02d\n", i, facilities.get(fclt).identifier, 
						day, month, year, hour, minute, second);
				System.out.println(e.getMessage());
				i--;
				continue outerloop;
			}
			
		}
		
		
		

	}
	
	public void drop_events() throws SQLException {
		Statement stmt = con.createStatement();
		stmt.execute("DELETE FROM Reserves");
		stmt.execute("DELETE FROM Supervisions");
	}
	
	
	public void add_Reserve(Reserve rsv) throws SQLException, Exception {
		Statement stmt = con.createStatement();
		String QUERY = String.format("SELECT * FROM Reserves WHERE RESIDENT_ID = %d AND FACILITY_ID = %d", rsv.rsdt.ID, rsv.rsvable.ID);
		ResultSet RS = stmt.executeQuery(QUERY);		
		Reservable rsvable = (Reservable) facilities.get(rsv.rsvable.ID);

		int weekly_count = 0, daily_count = 0; 
		while(RS.next()) {
			if (RS.getDate("DATE").toLocalDate().equals(rsv.start.toLocalDate())) {
				daily_count++;
			}
			weekly_count++;
		}
		
		if (weekly_count < rsvable.weekly_RPP) {
			if (daily_count < rsvable.daily_RPP) {
				events.put(rsv.key, rsv);
				rsvable.addReserve(rsv);
				
				stmt.execute(String.format(
						"INSERT INTO Reserves VALUES (%d, '%d-%d-%d', '%d:%d:%d', '%d:%d:%d', %d, %d)",
						rsv.key, rsv.start.getYear(), rsv.start.getMonthValue(), rsv.start.getDayOfMonth(),
						rsv.start.getHour(), rsv.start.getMinute(), rsv.start.getSecond(),
						rsv.end.getHour(), rsv.end.getMinute(), rsv.end.getSecond(),
						rsv.rsdt.ID, rsvable.ID));
						
			} else {
				throw new ReservableException("Event not added: too much daily reserves");
			}
		} else {
			throw new ReservableException("Event not added: too much weekly reserves");
		}
		
	}
	
	/*
	public void remove_Reserve(Reserve rsv) {
		
	}
	
	public Reserve search_Reserve(int rsv_ID) {
		return reserves.get(rsv_ID);
	}
	*/
	
	
	//add_facility
	//remove_facility
	//search_facility
	//edit_facility
	
	//add_resident
	//remove_resident
	//search_resident
	//edit_resident
	
	//add_employee
	//remove_employee
	//search_employee
	//edit_employee
	
	//add_reserve
	//remove_reserve
	//search_reserve
	//edit_reserve
	
	//update
	
	
	public void assign (Supervision spv) throws SQLException, Exception {
		Supervised spvd = (Supervised) facilities.get(spv.spvd.ID);
		Employee emp = employees.poll();
		Statement stmt = con.createStatement();
		
		try {
			if (emp.time_worked.compareTo(Duration.ofHours(40)) < 0) {
				emp.time_worked = emp.time_worked.plus(spvd.supervisionDuration);
				spv.emp = emp;
				spvd.addEvent(spv);
				events.put(spv.key, spv);
				employees.add(emp);
				
				stmt.execute(String.format("UPDATE Employees SET WEEKLY_HOURS = '%d:%d:%d' WHERE ID = %d", 
						(emp.time_worked.getSeconds() / 3600),
			    		(emp.time_worked.getSeconds() % 3600) / 60,
			    		emp.time_worked.getSeconds() % 60,
		
			    		emp.ID));
				stmt.execute(String.format("INSERT INTO Supervisions VALUES (%d, %d, %d, '%d-%d-%d', '%d:%d:%d')", spv.key, emp.ID, spvd.ID,
						spv.start.getYear(), spv.start.getMonthValue(), spv.start.getDayOfMonth(),
						spv.start.getHour(), spv.start.getMinute(), spv.start.getSecond()));
			} else {
				employees.add(emp);
				throw new EmployeeException("Event not added: couldn't assign employee.");
			}
		} catch (NullPointerException e) {
			throw new EmployeeException("Event not added: couldn't assign employee.");
		}
			
	}
	
	public void reset() throws SQLException {
		Statement stmt = con.createStatement();
		stmt.execute(String.format("UPDATE Employees SET WEEKLY_HOURS = '%d:%d:%d'", 0, 0, 0));
	}
	
}
