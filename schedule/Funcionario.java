package schedule;

import java.time.Duration;

public class Funcionario 
implements Comparable <Funcionario>
{
	int ID;
	String name;
	Duration time_worked;
	
	public Funcionario(int ID_in, String name_in) {
		ID = ID_in;
		name = name_in;
		time_worked = Duration.ZERO;
	}
	
	@Override
	public int compareTo (Funcionario other){
		return Long.compare(time_worked.getSeconds(), 
					other.time_worked.getSeconds());
	}
	
	public String getTimeWorked(){
	    return String.format("%02d:%02d:%02d",
	    		time_worked.getSeconds() / 3600,
	    		(time_worked.getSeconds() % 3600) / 60,
	    		time_worked.getSeconds() % 60);
	}
}
