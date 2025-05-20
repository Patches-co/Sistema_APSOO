package schedule;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;

class Reserve extends Event {
	Morador tal;
	Funcionario funcio;
	
    public Reserve (Morador tal_in, int key_in, LocalDateTime start_in, Duration du_in){
    	super(key_in, start_in, du_in);
    	tal = tal_in;
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

public class ReservableObject extends Schedule {
	//int ID;
	//String identifier;
	
	
	public ReservableObject() {};
	
	public ReservableObject(LocalTime start, LocalTime end) {
		super(start, end);
		//ID = ID_in;
		//identifier = identifier_in;
	}
	
	//Essa função verifica se a reserva a ser adicionada tem overlap com outra já existente
	public void addReserve(Reserve r) {
		Day d = calendar.get(r.start.toLocalDate());
		Iterator <Event> ite = d.Events.values().iterator();
		Event p;
		while (ite.hasNext()) {
			p = ite.next();
			if ((p.start.isAfter(r.start) && p.start.isBefore(r.end)) || 
				(r.start.isAfter(p.start) && r.start.isBefore(p.end)))
			{
				System.out.println(r.key + " not added: already reserved");
				return;
			}	
			
			/*
			if (r.start.isAfter(p.start) && r.start.isBefore(p.end)){
				System.out.println(r.key + " not added: already reserved");
				return;
			}
			*/
		}
		
		addEvent(r);
	}
}
