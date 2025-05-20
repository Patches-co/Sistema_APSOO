package schedule;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;


class Manutencao extends Event{
	Funcionario funcio;
	
	public Manutencao(int key_in, LocalDateTime start_in, Duration du_in, Funcionario funcio_in) {
		super(key_in, start_in, du_in);
		funcio = funcio_in;
	}
}



class Quiosque extends ReservableObject {
		
	public Quiosque(LocalTime start, LocalTime end) {
		super(start, end);
	}
	
}


class Quadra extends ReservableObject {
	
	public Quadra(LocalTime start, LocalTime end) {
		super(start, end);
	}
	
}



class PiscinaOlimpica extends ReservableObject {
	
	public PiscinaOlimpica(LocalTime start, LocalTime end) {
		super(start, end);
	}
	
	@Override
	public void addReserve(Reserve r) {
		if (r.funcio != null) {
			addReserve(r);
		}
	}
	

}

class SalaoFestas extends ReservableObject {
	Funcionario manut;
	
	public SalaoFestas() {};
	
	@Override
	public void addReserve(Reserve r) {
		
		addReserve(r);
		
		Day d = calendar.get(r.start.toLocalDate());
		if (d.Events.containsKey(r.key)) {
			Manutencao m = new Manutencao(r.key, r.end, Duration.ofHours(4), manut);
		}
	}
	
	
}









