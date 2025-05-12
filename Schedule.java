package mySchedule2;

import java.util.*;
import java.time.*;
import java.io.*;

class Schedule {
    LinkedHashMap <LocalDate, Day> Calendar;
    DayBuilder DB;
    LocalDateTime today;
    int EventCount;

    public Schedule () {
    	Calendar = new LinkedHashMap<>();
    	today = LocalDateTime.now();
    	EventCount = 0;
    	DB = new DayBuilder();
    }
    
    
    public Schedule (LocalDateTime t) {
    	Calendar = new LinkedHashMap<>();
    	today = t;
    	EventCount = 0;
    	DB = new DayBuilder();
    }
    
    public Schedule ( 
    		LocalTime d_start, 
    		LocalTime d_end, 
    		Duration... duration) 
    {
    	Calendar = new LinkedHashMap<>();
    	today = LocalDateTime.now();
    	EventCount = 0;
    	DB = new DayBuilder(d_start, d_end, duration);
    }
    
    public Schedule with (
    		DayOfWeek specific_day, 
    	    LocalTime another_start,
    	    LocalTime another_end,
    	    Duration... another_pattern)
    {
    	int index = specific_day.getValue() - 1;
    	DB.start[index] = another_start;
    	DB.end[index] = another_end;
    	DB.weekly_pattern[index] = another_pattern;
    	return this;
    }
    
    
    public void addDay(LocalDate date)
    {	
    	if (!Calendar.containsKey(date)) {
    		Calendar.put(date, new Day(date, DB));
    	}
    }
    
    public void removeDay(LocalDate date)
    {	
    	if (Calendar.containsKey(date)) {
    		Calendar.remove(date);
    	}
    }

    public void add_Event(Event e){
    	
    	if (e.moment.isBefore(LocalDateTime.now())) {
    		return;
    		//throw new DateTimeException("qwe");
    	}
    	
    	
    	Day d = Calendar.get(e.moment.toLocalDate());
    	try {
    		Timeslot t = d.TimeslotTable.get(e.moment.toLocalTime());
    		try {
    			t.events.put(e.key, e);
    			EventCount++;
    			//System.out.println("Event successfully added.\n");
    		} catch (NullPointerException NullTimeslot) {
    			//System.out.println("Not added: can't find timeslot " + e.moment.toLocalTime() + "\n");
    			throw new NullPointerException();
    		}
    	} catch (NullPointerException Nulldate) {
    		//System.out.println("Not added: can't find date " + e.moment.toLocalDate() + "\n");
    		throw new NullPointerException();
    	}
    }
    
    public void remove_Event(Event e){
    	Day d = Calendar.get(e.moment.toLocalDate());
	
    	try {
    		Timeslot t = d.TimeslotTable.get(e.moment.toLocalTime());
    		try {
    			t.events.remove(e.key);
    			EventCount--;
    			System.out.println("Event successfully removed.\n");
    		} catch (NullPointerException NullTimeslot) {
    			//System.out.println("Not removed: can't find timeslot " + e.moment.toLocalTime() + "\n");
    		}	    
    	} catch (NullPointerException Nulldate) {
    		//System.out.println("Not removed: can't find date " + e.moment.toLocalDate() + "\n");
    	}
    }
    
    

    public Event get(int key){
    	Event r;
    	for (Day d : Calendar.values()){
    		//System.out.println(d.date.toLocalDate());
    		for (Timeslot t : d.TimeslotTable.values()){
    			//System.out.println(t.time);
    			r = t.events.get(key);
    			if (r != null){
    				return r;
    			}
    		}
    	}
    	return null;
    }
    
    public void print_file(String s){
    	try {
    		FileWriter fout = new FileWriter(s + ".txt");
    		for (Day d : Calendar.values()){
    			fout.write("///////////////   " + d.toString() + "   ///////////////" + "\n");
    			for (Timeslot t : d.TimeslotTable.values()){
    				fout.write(t.toString() + "\t");
    				for (Event e : t.events.values()){
    					fout.write(e.toString() + " ");
    				}
    				fout.write("\n");
    			}
    			fout.write("\n\n");
    		}
    		fout.close();
    	} catch (IOException e){}
    }
    
    
    public void update() {
    	LocalDate now = LocalDate.now();
    	if (today.toLocalDate().isBefore(now)) {
    		Iterator <Day> iterator = Calendar.values().iterator();
    		Day d = iterator.next();
    		while (!d.date.isEqual(now)) {
    			
    			iterator.remove();
    			d = iterator.next();
    		}
    		LocalDate last = null;
    		int count = 0;
    		while (iterator.hasNext()) {
    			last = iterator.next().date;
    			count++;
    		}
    		for (int i = 1; i < count-1; i++) {
    			addDay(last.plusDays(i));
    		}
    	}
    }
    
    
    
}

class Day {
    LocalDate date;
    LinkedHashMap <LocalTime, Timeslot> TimeslotTable;
  
    public Day (LocalDate d_in, DayBuilder db) {
    	date = d_in;
    	TimeslotTable = new LinkedHashMap<>();
    	
    	int NVPFD = date.getDayOfWeek().getValue() - 1;		//Numeric value of the pattern for this day, -1 for array processing
    	Duration[] PFTD = db.weekly_pattern[NVPFD];			//Pattern for this day
    	
    	LocalDateTime start = LocalDateTime.of(date, db.start[NVPFD]);    	
    	LocalDateTime end = LocalDateTime.of(date, db.end[NVPFD]).minus(PFTD[0]);
    	//System.out.println(start + " " + end);
    	
    	
    	LocalTime t = db.start[NVPFD];
    	
    	int index = 0;
    	while (start.isBefore(end) || start.isEqual(end)){
    		TimeslotTable.put(t, new Timeslot(t));
    		//System.out.println(t);
    		t = t.plus(PFTD[index]);
    		start = start.plus(PFTD[index]);
    		index = (index + 1) % PFTD.length;
    	}
    }

    public String toString(){
    	return String.format("%02d/%02d/%02d",
			     date.getDayOfMonth(),
			     date.getMonthValue(),
			     date.getYear());
    }
}

class DayBuilder {
	LocalTime[] start, end;
	Duration[][] weekly_pattern;
	
	public DayBuilder()
	{
		start = new LocalTime[7];
		Arrays.fill(start, LocalTime.of(0, 0, 0));
		
		end = new LocalTime[7];
		Arrays.fill(end, LocalTime.MAX);
		
	    weekly_pattern = new Duration[7][];
	    Arrays.fill(weekly_pattern, new Duration[] {Duration.ofHours(1)});
	}
	
	 
	public DayBuilder(
			LocalTime start_in,
			LocalTime end_in,
			Duration... pattern)
	{
		start = new LocalTime[7];
		Arrays.fill(start, start_in);
		
		end = new LocalTime[7];
		Arrays.fill(end, end_in);
		
	    weekly_pattern = new Duration[7][];
	    Arrays.fill(weekly_pattern, pattern);
	}
	
	public DayBuilder with(DayOfWeek d, Duration... pattern)
	{
		weekly_pattern[d.getValue()-1] = pattern;
	    return this;
	}
	
	public DayBuilder with(
			DayOfWeek specific_day, 
			LocalTime specific_start,
			LocalTime specific_end,
			Duration... specific_pattern)
	  {
			      int index = specific_day.getValue() - 1;
			      start[index] = specific_start;
			      end[index] = specific_end;
			      weekly_pattern[index] = specific_pattern;
			      return this;
	  }
}


class Timeslot {
    LocalTime time;
    HashMap <Integer, Event> events;
  
    public Timeslot(LocalTime t_in){
    	this.time = t_in;
    	this.events = new HashMap<>();
    }
    
    public String toString(){
    	return String.format("%02d:%02d:%02d",
			     time.getHour(),
			     time.getMinute(),
			     time.getSecond());
    }
}

class Event {
    int key;
    LocalDateTime moment;
  
    public Event(int key_in, LocalDateTime moment_in){
    	key = key_in;
    	moment = moment_in;
    }

    public String toString(){
    	return String.format("e%03d", key);
    }
}


