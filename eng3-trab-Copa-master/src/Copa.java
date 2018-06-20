import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Copa {

	Scanner sc;
	private SelecaoFactory factory;

	public Copa() throws ClassNotFoundException, IOException {

		this.factory = SelecaoFactory.getInstance();
		sc = new Scanner(System.in);
	}

	public char selecionarGrupo() {
		char c;
		do {
			System.out.println("Selecione um Grupo de A a H:");
			c = sc.next().charAt(0);
			if (c != 'A' && c != 'B' && c != 'C' && c != 'D' && c != 'E' && c != 'F' && c != 'G' && c != 'H') {
				System.out.println("Você Digitou algo diferente de um caractere A a H, digite novamente!");
			}
		} while (c != 'A' && c != 'B' && c != 'C' && c != 'D' && c != 'E' && c != 'F' && c != 'G' && c != 'H');

		return c;

	}

	public void cadastrarSelecoes() throws FileNotFoundException, IOException {
		int flag = -1;
		char grupo = selecionarGrupo();
		for (int i = 1; i < 5; i++) {
			flag = -1;

			do {
				DAO<Selecao> selecaoDao = new DAO<>("selecao.dat");

				System.out.println("Cadastro da " + i + "ª Seleção ");

				System.out.println("Digite o id da seleção: ");
				long id = Long.parseLong(sc.next());
				if (factory.getSelecao(id) == null) {
					System.out.println("Digite o nome da Seleção: ");
					String nome = sc.next();

					factory.addSelecao(new Selecao(id, nome, grupo));
					selecaoDao.save(new Selecao(id, nome, grupo));
					flag = 0;

				} else {
					System.out.println("id já existente, digite um novo id");
				}
			} while (flag != 0);

		}

	}
	
	
	public void cadastrarJogo()
	{
		Selecao mandante = null;
		int flag = -1;
		char grupo = selecionarGrupo();
		
		System.out.println("Digite a data do jogo: ");
		String dataStr = sc.next();
		
		DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
		Date date = null;
		try {
			date = (Date)formatter.parse(dataStr);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		System.out.println("Digite o estádio do jogo: ");
		String estadio = sc.next();
		
		int flagJogo = -1;
		do
		{
			System.out.println("Lista das Seleções: ");
			try {
				listarSelecoesGrupo(grupo);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.out.println("Seleção Mandante: ");
			System.out.println("Digite o id da seleção: ");
			do
			{
				long id = Long.parseLong(sc.next());
				if (factory.getSelecao(id) != null) {
					flag = 0;
					mandante = factory.getSelecao(id);
				}
				else
				{
					
					System.out.println("id não existente, digite um novo id");
				}
			
			}
			while(flag != 0);
			
			
			Selecao visitante = null;
			flag = -1;
			System.out.println("Lista das Seleções: ");
			try {
				listarSelecoesGrupo(grupo);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.out.println("Seleção Visitante: ");
			System.out.println("Digite o id da seleção: ");
			do
			{
				long id = Long.parseLong(sc.next());
				if (factory.getSelecao(id) != null) {
					flag = 0;
					visitante = factory.getSelecao(id);
				}
				else
				{
					
					System.out.println("id não existente, digite um novo id");
				}
			
			}
			while(flag != 0);
			
			
			if(!mandante.equals(visitante))
			{
				DAO<Jogo> dao = new DAO<>("jogo.dat");
				Jogo jogo = new Jogo(date, estadio, mandante, visitante);
				try {
					dao.save(jogo);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				flagJogo = 0;
				System.out.println("Jogo Cadastrado com Sucesso!");
			}
			else
			{
				System.out.println("Selecao Mandante é a mesma que a visitante, digite novamente!");
			}
	    }
		while(flagJogo != 0);
	}
	
	public void listarJogosGrupo()
	{
		DAO<Jogo> dao = new DAO<>("jogo.dat");
		char grupo = selecionarGrupo();
		try {
			for (Jogo jogo : dao.findAll()) {
				if(jogo.getMandante().getGrupo() == grupo)
				{
					System.out.println(jogo);
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		
		
	}

	public void listarSelecoes() throws FileNotFoundException, ClassNotFoundException, IOException {
		try {
			DAO<Selecao> selecaoDao = new DAO<>("selecao.dat");
			selecaoDao.findAll().forEach(selecao -> System.out.println(selecao));
		} catch (Exception e) {
			e.getMessage();
		}

	}

	public void listarSelecoesGrupo(char grupo) throws FileNotFoundException, ClassNotFoundException, IOException {
		try {
			DAO<Selecao> selecaoDao = new DAO<>("selecao.dat");
			for (int i = 0; i < selecaoDao.findAll().size(); i++) {
				Selecao selecao = selecaoDao.findAll().get(i);
				if (selecao.getGrupo() == grupo) {
					System.out.println(selecao);
				}

			}
		} catch (Exception e) {
			e.getMessage();
		}

	}
	

	public static void main(String[] args) throws ClassNotFoundException, IOException {

		Copa copa2018 = new Copa();
		Scanner sc = new Scanner(System.in);
		int opc = 0;

		while (opc != 5) {
			System.out.println("1.Cadastrar Seleções" + "\n2.Mostrar seleções" + "\n3.Mostrar as seleções pelo grupo"
					+ "\n4.Cadastrar Jogo"	+ "\n5.Mostrar Jogos pelo Grupo" + "\n6.Sair do programa" );
			System.out.println("escolha: ");
			opc = sc.nextInt();
			
			switch(opc) {
			case 1:
				copa2018.cadastrarSelecoes();
				break;
			case 2:
				copa2018.listarSelecoes();
				break;
			case 3:
				copa2018.listarSelecoesGrupo(copa2018.selecionarGrupo());
				break;
			case 4:
				copa2018.cadastrarJogo();
				break;
			case 5:
				copa2018.listarJogosGrupo();
				break;
			case 6:
				break;
			default:
				System.out.println("Erro: digite uma opção válida");
				break;
			}

		}

	}

}
