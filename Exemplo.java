package schedule3;


import java.util.*;
import java.time.*;



class Reserve extends Event {
	//Morador tal;
	
    public Reserve (int key_in, LocalDateTime start_in, Duration du_in){
    	super(key_in, start_in, du_in);
    }
    
    @Override
    public String toString(){
    	return String.format("reserve%03d [%02d:%02d ~ %02d:%02d]", key,
    			start.toLocalTime().getHour(),
    			start.toLocalTime().getMinute(),
    			end.toLocalTime().getHour(),
    			end.toLocalTime().getMinute());
    }
}

class ReservableObject extends Schedule {
	
	public ReservableObject(LocalTime start, LocalTime end) {
		super(start, end);
	}
	
	//Essa função verifica se a reserva a ser adicionada tem overlap com outra já existente
	public void addReserve(Reserve r) {
		Day d = this.calendar.get(r.start.toLocalDate());
		Iterator <Event> ite = d.Events.values().iterator();
		Event p;
		while (ite.hasNext()) {
			p = ite.next();
			if (p.start.isAfter(r.start) && p.start.isBefore(r.end)){
				return;
			  }	
			if (r.start.isAfter(p.start) && r.start.isBefore(p.end)){
				return;
			}
		}
		
		this.addEvent(r);
	}
}


class Quiosque extends ReservableObject {
	//int id
	//
	//
	//
	
	public Quiosque(LocalTime start, LocalTime end) {
		super(start, end);
	}
}




public class Exemplo {
	public static void main(String[] args) {
		Quiosque q1 = new Quiosque(LocalTime.of(6, 0), LocalTime.of(22, 0));
		q1.with(DayOfWeek.SATURDAY, LocalTime.of(9, 0), LocalTime.of(15, 0));
		q1.with(DayOfWeek.SUNDAY, LocalTime.of(9, 0), LocalTime.of(15, 0));
		
		for (int i = 0; i < 10; i++) {
			q1.addDay(LocalDate.of(2025, 5, 14).plusDays(i));
		}
		
		//successes
		q1.addReserve(new Reserve(0, LocalDateTime.of(2025, 5, 16, 18, 0), Duration.ofHours(4)));
		q1.addReserve(new Reserve(1, LocalDateTime.of(2025, 5, 15, 14, 0), Duration.ofHours(3)));
		q1.addReserve(new Reserve(2, LocalDateTime.of(2025, 5, 17, 12, 0), Duration.ofHours(3)));
		q1.addReserve(new Reserve(3, LocalDateTime.of(2025, 5, 16, 12, 0), Duration.ofHours(3)));
		q1.addReserve(new Reserve(4, LocalDateTime.of(2025, 5, 19, 15, 0), Duration.ofHours(6)));
		
		
		// failures
		try {
			q1.addReserve(new Reserve(5, LocalDateTime.of(2025, 3, 13, 15, 0), Duration.ofHours(2)));	//	13/03 já passou
		} catch (NullPointerException exc) {};
		
		try {
			q1.addReserve(new Reserve(6, LocalDateTime.of(2025, 5, 19, 17, 0), Duration.ofHours(3)));	//	já reservado
		} catch (NullPointerException exc) {};
		
		try {
			q1.addReserve(new Reserve(7, LocalDateTime.of(2025, 5, 18, 17, 0), Duration.ofHours(6)));	//	domingo apenas 9h ~ 15h
		} catch (NullPointerException exc) {};
		
		try {
			q1.addReserve(new Reserve(8, LocalDateTime.of(2025, 5, 16, 13, 0), Duration.ofHours(6)));	//	já reservado
		} catch (NullPointerException exc) {};
		
		try {
			q1.addReserve(new Reserve(9, LocalDateTime.of(2025, 9, 15, 15, 0), Duration.ofHours(6)));	//	15/09 não existe ainda
		} catch (NullPointerException exc) {};
		
		
		q1.print_file("quiosque1");
		
		
		
		

	}
}
