import entidades.Cliente;
import entidades.Funcionario;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SistemaCadastro {

    public static void main(String[] args) throws IOException {
        BufferedReader scanner = new BufferedReader(
            new InputStreamReader(System.in, StandardCharsets.UTF_8));
        List<Cliente> clientes = GerenciadorCadastro.carregarClientes();
        List<Funcionario> funcionarios = GerenciadorCadastro.carregarFuncionarios();
        executar(clientes, funcionarios, scanner, "superuser");
    }

    public static void executar(List<Cliente> listaClientes, List<Funcionario> listaFuncionarios,
                                BufferedReader scanner, String tipoUsuario) throws IOException {

        boolean isSuperUser = tipoUsuario.equals("superuser");
        boolean ativo = true;

        while (ativo) {
            System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
            System.out.println("║                   SISTEMA DE CADASTRO                          ║");
            System.out.println("╚════════════════════════════════════════════════════════════════╝");
            System.out.println("\n1 - Cadastrar cliente");
            System.out.println("2 - Editar cliente");
            System.out.println("3 - Listar clientes");
            if (isSuperUser) {
                System.out.println("4 - Cadastrar funcionário");
                System.out.println("5 - Editar funcionário");
                System.out.println("6 - Listar funcionários");
            }
            System.out.println("0 - Voltar\n");

            System.out.print("Escolha: ");
            String opcao = scanner.readLine().trim();

            switch (opcao) {
                case "1":
                    cadastrarCliente(listaClientes, scanner);
                    break;
                case "2":
                    editarCliente(listaClientes, scanner);
                    break;
                case "3":
                    listarClientes(listaClientes);
                    break;
                case "4":
                    if (isSuperUser) cadastrarFuncionario(listaFuncionarios, scanner);
                    else System.out.println("\nAcesso negado!\n");
                    break;
                case "5":
                    if (isSuperUser) editarFuncionario(listaFuncionarios, scanner);
                    else System.out.println("\nAcesso negado!\n");
                    break;
                case "6":
                    if (isSuperUser) listarFuncionarios(listaFuncionarios);
                    else System.out.println("\nAcesso negado!\n");
                    break;
                case "0":
                    ativo = false;
                    break;
                default:
                    System.out.println("\nOpção inválida!\n");
            }
        }
    }

    // ==================== CADASTRAR CLIENTE ====================
    private static void cadastrarCliente(List<Cliente> listaClientes, BufferedReader scanner) throws IOException {
        System.out.println("\n--- Cadastro de Cliente ---\n");
        boolean continuar = true;
        while (continuar) {
            String nome = lerNome(scanner);
            String cpf  = lerCpf(scanner);
            System.out.print("Gênero: ");
            String genero = scanner.readLine();
            int idade = lerIdade(scanner);
            System.out.print("Endereço: ");
            String endereco = scanner.readLine();
            System.out.print("Complemento: ");
            String complemento = scanner.readLine();
            String telefone = lerTelefone(scanner);

            if (idade < 18) {
                System.out.println("\nCADASTRO NÃO REALIZADO! Idade mínima: 18 anos.\n");
            } else {
                listaClientes.add(new Cliente(nome, cpf, genero, idade, endereco, complemento, telefone));
                GerenciadorCadastro.salvarClientes(listaClientes);
                System.out.println("Cliente cadastrado!\n");
            }
            continuar = confirmarSaida(scanner, "Deseja cadastrar outro cliente? (s/n): ");
        }
    }

    // ==================== EDITAR CLIENTE ====================
    private static void editarCliente(List<Cliente> listaClientes, BufferedReader scanner) throws IOException {
        if (listaClientes.isEmpty()) {
            System.out.println("\nNenhum cliente cadastrado.\n");
            return;
        }
        listarClientes(listaClientes);
        System.out.print("Digite o ID do cliente para editar: ");
        String id = scanner.readLine().trim();

        for (int i = 0; i < listaClientes.size(); i++) {
            Cliente c = listaClientes.get(i);
            if (c.getId().equals(id)) {
                System.out.println("\nDeixe em branco para manter o valor atual.\n");

                System.out.print("Nome [" + c.getNome() + "]: ");
                String nome = scanner.readLine().trim();

                System.out.print("Gênero [" + c.getGenero() + "]: ");
                String genero = scanner.readLine().trim();

                System.out.print("Endereço [" + c.getEndereco() + "]: ");
                String endereco = scanner.readLine().trim();

                System.out.print("Complemento [" + c.getComplemento() + "]: ");
                String complemento = scanner.readLine().trim();

                System.out.print("Telefone [" + c.getTelefone() + "]: ");
                String telefone = scanner.readLine().trim();

                Cliente atualizado = new Cliente(
                    nome.isEmpty()        ? c.getNome()        : nome,
                    c.getCpf(),
                    genero.isEmpty()      ? c.getGenero()      : genero,
                    c.getIdade(),
                    endereco.isEmpty()    ? c.getEndereco()    : endereco,
                    complemento.isEmpty() ? c.getComplemento() : complemento,
                    telefone.isEmpty()    ? c.getTelefone()    : telefone
                );
                atualizado.setId(c.getId());
                listaClientes.set(i, atualizado);
                GerenciadorCadastro.salvarClientes(listaClientes);
                System.out.println("\nCliente atualizado com sucesso!\n");
                return;
            }
        }
        System.out.println("\nCliente não encontrado!\n");
    }

    // ==================== CADASTRAR FUNCIONÁRIO ====================
    private static void cadastrarFuncionario(List<Funcionario> listaFuncionarios, BufferedReader scanner) throws IOException {
        System.out.println("\n--- Cadastro de Funcionário ---\n");
        boolean continuar = true;
        while (continuar) {
            String nome = lerNome(scanner);
            String cpf  = lerCpf(scanner);
            System.out.print("Gênero: ");
            String genero = scanner.readLine();
            int idade = lerIdade(scanner);
            System.out.print("Endereço: ");
            String endereco = scanner.readLine();
            System.out.print("Complemento: ");
            String complemento = scanner.readLine();
            String telefone = lerTelefone(scanner);

            if (idade < 18) {
                System.out.println("\nCADASTRO NÃO REALIZADO! Idade mínima: 18 anos.\n");
            } else {
                listaFuncionarios.add(new Funcionario(nome, cpf, genero, idade, endereco, complemento, telefone));
                GerenciadorCadastro.salvarFuncionarios(listaFuncionarios);
                System.out.println("Funcionário cadastrado!\n");
            }
            continuar = confirmarSaida(scanner, "Deseja cadastrar outro funcionário? (s/n): ");
        }
    }

    // ==================== EDITAR FUNCIONÁRIO ====================
    private static void editarFuncionario(List<Funcionario> listaFuncionarios, BufferedReader scanner) throws IOException {
        if (listaFuncionarios.isEmpty()) {
            System.out.println("\nNenhum funcionário cadastrado.\n");
            return;
        }
        listarFuncionarios(listaFuncionarios);
        System.out.print("Digite o ID do funcionário para editar: ");
        String id = scanner.readLine().trim();

        for (int i = 0; i < listaFuncionarios.size(); i++) {
            Funcionario f = listaFuncionarios.get(i);
            if (f.getId().equals(id)) {
                System.out.println("\nDeixe em branco para manter o valor atual.\n");

                System.out.print("Nome [" + f.getNome() + "]: ");
                String nome = scanner.readLine().trim();

                System.out.print("Gênero [" + f.getGenero() + "]: ");
                String genero = scanner.readLine().trim();

                System.out.print("Endereço [" + f.getEndereco() + "]: ");
                String endereco = scanner.readLine().trim();

                System.out.print("Complemento [" + f.getComplemento() + "]: ");
                String complemento = scanner.readLine().trim();

                System.out.print("Telefone [" + f.getTelefone() + "]: ");
                String telefone = scanner.readLine().trim();

                Funcionario atualizado = new Funcionario(
                    nome.isEmpty()        ? f.getNome()        : nome,
                    f.getCpf(),
                    genero.isEmpty()      ? f.getGenero()      : genero,
                    f.getIdade(),
                    endereco.isEmpty()    ? f.getEndereco()    : endereco,
                    complemento.isEmpty() ? f.getComplemento() : complemento,
                    telefone.isEmpty()    ? f.getTelefone()    : telefone
                );
                atualizado.setId(f.getId());
                if (f.getUsuario() != null) atualizado.setUsuario(f.getUsuario());
                if (f.getSenha()   != null) atualizado.setSenha(f.getSenha());
                listaFuncionarios.set(i, atualizado);
                GerenciadorCadastro.salvarFuncionarios(listaFuncionarios);
                System.out.println("\nFuncionário atualizado com sucesso!\n");
                return;
            }
        }
        System.out.println("\nFuncionário não encontrado!\n");
    }

    private static void listarClientes(List<Cliente> lista) {
        System.out.println("\n--- Clientes Cadastrados ---");
        if (lista.isEmpty()) { System.out.println("Nenhum cliente cadastrado.\n"); return; }
        lista.stream().filter(c -> c.getIdade() >= 18).forEach(System.out::println);
    }

    private static void listarFuncionarios(List<Funcionario> lista) {
        System.out.println("\n--- Funcionários Cadastrados ---");
        if (lista.isEmpty()) { System.out.println("Nenhum funcionário cadastrado.\n"); return; }
        lista.forEach(System.out::println);
    }

    private static String lerNome(BufferedReader scanner) throws IOException {
        while (true) {
            System.out.print("Nome completo: ");
            String nome = scanner.readLine();
            if (nome.matches(".*\\d.*")) System.out.println("Nome inválido! Use apenas letras.");
            else return nome;
        }
    }

    private static int lerIdade(BufferedReader scanner) throws IOException {
        while (true) {
            System.out.print("Idade: ");
            try {
                int idade = Integer.parseInt(scanner.readLine());
                if (idade < 0) System.out.println("Idade inválida.");
                else return idade;
            } catch (NumberFormatException e) {
                System.out.println("Erro: Digite um número válido.");
            }
        }
    }

    public static String lerCpf(BufferedReader scanner) throws IOException {
        while (true) {
            System.out.print("CPF (Ex: 123.456.789-00): ");
            String cpf = scanner.readLine().trim();
            if (cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) return cpf;
            System.out.println("CPF inválido! Use o formato 000.000.000-00.");
        }
    }

    public static String lerTelefone(BufferedReader scanner) throws IOException {
        while (true) {
            System.out.print("Telefone (Ex: 81912345678): ");
            String tel = scanner.readLine().trim();
            if (tel.matches("\\d{11}")) return tel;
            System.out.println("Telefone inválido! Digite DDD + número (11 dígitos).");
        }
    }

    public static boolean confirmarSaida(BufferedReader scanner, String mensagem) throws IOException {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.readLine().trim().toLowerCase();
            if (entrada.equals("s")) return true;
            if (entrada.equals("n")) return false;
            System.out.println("Erro! Digite apenas 's' ou 'n'.\n");
        }
    }
}