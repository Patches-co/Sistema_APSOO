package schedule;



public class Morador {
	int ID;
	String name;
	
	public Morador(int ID_in, String name_in) {
		ID = ID_in;
		name = name_in;
	}
	
}


class Dependente extends Morador {
	Morador responsavel;
	
	public Dependente(int ID_in, String name_in, Morador res_in) {
		super(ID_in, name_in);
		responsavel = res_in;
	}
	
}

