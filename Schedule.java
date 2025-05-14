package schedule3;

import java.util.*;
import java.time.*;
import java.io.*;

class Schedule {
    LinkedHashMap <LocalDate, Day> calendar;
    LocalDateTime today;
    LocalTime[] start = null;
    LocalTime[] end = null;
    boolean bounded = true;
    int EventCount;

    public Schedule () 
    {
    	calendar = new LinkedHashMap<>();
    	today = LocalDateTime.now();
    	EventCount = 0;
    	bounded = false;
    }
    
    
    public Schedule (LocalDateTime t)
    {
    	calendar = new LinkedHashMap<>();
    	today = t;
    	EventCount = 0;
    }
    
    public Schedule (LocalTime start_in, LocalTime end_in)
    {
    	calendar = new LinkedHashMap<>();
    	today = LocalDateTime.now();
    	
		start = new LocalTime[7];
		Arrays.fill(start, start_in);
		
		end = new LocalTime[7];
		Arrays.fill(end, end_in);
    	
    	
    	EventCount = 0;
    }
    
    /*
    public Schedule with (DayOfWeek specific_day, LocalTime another_start, LocalTime another_end)
    {
    	int index = specific_day.getValue() - 1;
    	start[index] = another_start;
    	end[index] = another_end;
    	return this;
    }
    */
    
    public void with (DayOfWeek specific_day, LocalTime another_start, LocalTime another_end)
    {
    	int index = specific_day.getValue() - 1;
    	start[index] = another_start;
    	end[index] = another_end;
    }
    
    
    public void setSpecialDay(LocalDate specialDay, LocalTime specialStart, LocalTime specialEnd) {
    	Day d = calendar.get(specialDay);
    	
    	if (d == null) {
    		calendar.put(specialDay, new Day(specialDay, specialStart, specialEnd));
    	} else {
    		d.Events.clear();
    		d.start = LocalDateTime.of(specialDay, specialStart);
    		d.end = LocalDateTime.of(specialDay, specialEnd);
    	}
    }
    
    
    public void addDay(LocalDate date)
    {	
    	if (!calendar.containsKey(date)) {
    		int day = date.getDayOfWeek().getValue() - 1;
    		if (bounded) {
    			calendar.put(date, new Day(date, start[day], end[day]));
    		} else {
    			calendar.put(date, new Day(date, null, null));
    		}
    	}
    }
    
    public void removeDay(LocalDate date)
    {	
    	if (calendar.containsKey(date)) {
    		calendar.remove(date);
    	}
    }
     

    public void addEvent(Event e){
    	
    	
    	if (e.start.isBefore(LocalDateTime.now())) {
    		//System.out.println(e.key + " not added: date already passed");
    		return;
    		//throw new DateTimeException("qwe");
    	}
    	
    	/*
    	Day d = Calendar.get(e.start.toLocalDate());
    	try {
    		d.addEvent(e);
    		
    	} catch (NullPointerException NullDate) {
    		throw new NullPointerException();
    	}
    	*/
    	
    	Day d = calendar.get(e.start.toLocalDate());
    	try {
    		if (bounded) {
    			d.addEvent(e);
    		} else {
    			if (e.end.isBefore(d.end)) {
    				d.addEvent(e);
    			} else {
    				
    				
    				/* TODO */
    				
    				
    			}
    		}
    	} catch (NullPointerException NullDate) {
    		//System.out.println(e.key + " not added: can't find date " + e.start.toLocalDate());
    		throw new NullPointerException();
    	}
    }
    
    public void removeEvent(Event e){
    	Day d = calendar.get(e.start.toLocalDate());
	
    	try {
    		d.Events.remove(e.key);
    		EventCount--;
    		System.out.println("Event successfully removed.\n");   
    	} catch (NullPointerException Nulldate) {
    		//System.out.println("Not removed: can't find date " + e.moment.toLocalDate() + "\n");
    	}
    }
    
    

    public Event get(int key)
    {
    	Event r;
    	for (Day d : calendar.values()){
    		r = d.Events.get(key);
    		if (r != null){
    			return r;
    		}
    	}
    	return null;
    }
    
    public void print_file(String s){
    	try {
    		FileWriter fout = new FileWriter(s + ".txt");
    		for (Day d : calendar.values()){
    			String interval = String.format("[%02d:%02d ~ %02d:%02d]",
    					d.start.getHour(), d.start.getMinute(),
    					d.end.getHour(), d.end.getMinute());
    			fout.write("///////////////   " + d.toString() + interval + "   ///////////////" + "\n");
    			for (Event e : d.Events.values()){
    				fout.write(e.toString() + "\n");
    			}
    			fout.write("\n");
    			}
    		fout.write("\n\n");
    		fout.close();
    	} catch (IOException e){}
    }
    
    
    public void update()
    {
    	LocalDate now = LocalDate.now();
    	if (today.toLocalDate().isBefore(now)) {
    		Iterator <Day> iterator = calendar.values().iterator();
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
    LocalDateTime start;
    LocalDateTime end;
    LinkedHashMap <Integer, Event> Events;
    boolean bounded = true;
  
    public Day (LocalDate d_in) {
    	date = d_in;
    	Events = new LinkedHashMap<>();
    }
    
    public Day (LocalDate d_in, LocalTime start_in, LocalTime end_in) {
    	date = d_in;
    	
    	if (start_in == null && end_in == null) {
    		bounded = false;
    		start = LocalDateTime.of(d_in, LocalTime.MIN);
    		end = LocalDateTime.of(d_in, LocalTime.MAX);
    	} else {
    		start = LocalDateTime.of(d_in, start_in);
    		end = LocalDateTime.of(d_in, end_in);
    	}
    	Events = new LinkedHashMap<>();
    }
    
    
    public void addEvent(Event e) {
    	
    	/*
		System.out.println();
		System.out.println(start + " " + end);
		System.out.println(e.start + " " + e.end);
		System.out.println();
    	 */
        
    	if (bounded) {
	    	if (e.start.isBefore(start) ||
	    		e.end.isAfter(end))
	    	{
	    		//System.out.println(e.key + " not added: out of bounds");
	    		return;
	    	}
    	} else {
    		/*
    		 * 
    		 * TODO
    		 * 
    		 * 
    		 */
    	}
    	
    	

    	Events.put(e.key, e);
    }

    public String toString(){
    	return String.format("%02d/%02d/%02d",
			     date.getDayOfMonth(),
			     date.getMonthValue(),
			     date.getYear());
    }
}


class Event {
    int key;
    LocalDateTime start;
    LocalDateTime end;
    Duration duration;
  
    public Event(int key_in, LocalDateTime start_in, Duration du_in){
    	key = key_in;
    	
    	start = start_in;
    	end = start_in.plus(du_in);
    	duration = du_in;
    }
    
    public Event(int key_in, LocalDateTime start_in, LocalDateTime end_in){
    	key = key_in;
    	
    	start = start_in;
    	end = end_in;
    	duration = Duration.between(start_in, end_in);
    }
    

    public String toString(){
    	return String.format("e%03d [%02d:%02d ~ %02d:%02d]", key,
    			start.toLocalTime().getHour(),
    			start.toLocalTime().getMinute(),
    			end.toLocalTime().getHour(),
    			end.toLocalTime().getMinute());
    }
}


