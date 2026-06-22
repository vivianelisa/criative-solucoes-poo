import entidades.Chamado;
import entidades.Cliente;
import entidades.CriticidadeChamado;
import entidades.Funcionario;
import entidades.Ocorrencia;
import entidades.StatusChamado;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class GestaoChamados {

    private List<Chamado> listaChamados;
    private List<Cliente> listaClientes;
    private List<Funcionario> listaFuncionarios;
    private BufferedReader scanner;
    private Login.UsuarioLogado usuarioLogado;

    public GestaoChamados(List<Chamado> listaChamados, List<Cliente> listaClientes, 
                          List<Funcionario> listaFuncionarios, BufferedReader scanner) {
        this.listaChamados = listaChamados;
        this.listaClientes = listaClientes;
        this.listaFuncionarios = listaFuncionarios;
        this.scanner = scanner;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        List<Chamado> listaChamados = GerenciadorArquivo.carregar();
        List<Cliente> listaClientes = new ArrayList<>();
        List<Funcionario> listaFuncionarios = new ArrayList<>();
        executar(listaChamados, listaClientes, listaFuncionarios, scanner);
    }

    public static void executar(List<Chamado> listaChamados, List<Cliente> listaClientes,
                                List<Funcionario> listaFuncionarios, BufferedReader scanner) throws IOException {
        System.out.println("\n╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                    ║");
        System.out.println("║                  SISTEMA DE GESTÃO DE CHAMADOS                     ║");
        System.out.println("║                        CRIATIVE SOLUÇÕES                           ║");
        System.out.println("║                                                                    ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");

        Login.exibirCredenciaisDisponveis();

        Login login = new Login(scanner, listaFuncionarios);
        Login.UsuarioLogado usuarioLogado = login.autenticar();

        if (usuarioLogado == null) {
            System.out.println("Programa encerrado.\n");
            return;
        }

        GestaoChamados sistema = new GestaoChamados(listaChamados, listaClientes, listaFuncionarios, scanner);
        sistema.usuarioLogado = usuarioLogado;

        if (usuarioLogado.isSuperUser()) {
            sistema.menuSuperUsuario();
        } else if (usuarioLogado.isFuncionario()) {
            sistema.menuFuncionario();
        }

        System.out.println("\nAté logo!\n");
    }

    // ==================== CRIAR CHAMADO ====================
    private void criarChamado() throws IOException {
        System.out.println("\n╔═════════════════════════════════════════════════════════╗");
        System.out.println("║                   CRIAR NOVO CHAMADO                    ║");
        System.out.println("╚═════════════════════════════════════════════════════════╝\n");

        System.out.print("Título do chamado: ");
        String titulo = scanner.readLine().trim();

        System.out.print("Descrição: ");
        String descricao = scanner.readLine().trim();

        System.out.print("Obra/Local: ");
        String obra = scanner.readLine().trim();

        System.out.print("Nome do cliente: ");
        String nomeCliente = scanner.readLine().trim();

        System.out.print("ID do cliente: ");
        String idCliente = scanner.readLine().trim();

        System.out.println("\nSelecione a criticidade:");
        System.out.println("01 - BAIXA (Problema menor)");
        System.out.println("02 - MEDIA (Problema moderado)");
        System.out.println("03 - ALTA (Problema grave)");
        System.out.print("\nEscolha: ");
        
        String opcaoCrit = scanner.readLine().trim();
        CriticidadeChamado criticidade = CriticidadeChamado.BAIXA;
        switch (opcaoCrit) {
            case "2":
                criticidade = CriticidadeChamado.MEDIA;
                break;
            case "3":
                criticidade = CriticidadeChamado.ALTA;
                break;
            default:
                criticidade = CriticidadeChamado.BAIXA;
        }

        // Criar novo chamado
        Chamado novoChamado = new Chamado(titulo, descricao, idCliente, nomeCliente, obra, criticidade);
        listaChamados.add(novoChamado);
        
        // Salvar em arquivo
        GerenciadorArquivo.salvar(listaChamados);

        System.out.println("\nChamado criado com sucesso!");
        System.out.println("═════════════════════════════════════════════════════════════════");
        System.out.println("ID do Chamado: " + novoChamado.getId());
        System.out.println("Título: " + novoChamado.getTitulo());
        System.out.println("Cliente: " + novoChamado.getNomeCliente());
        System.out.println("Criticidade: " + novoChamado.getCriticidade());
        System.out.println("Status: " + novoChamado.getStatus());
        System.out.println("═════════════════════════════════════════════════════════════════\n");
    }

    // ==================== LISTAR CHAMADOS ====================
    private void listarChamados() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                          LISTA DE CHAMADOS                            ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════════╝\n");

        if (listaChamados.isEmpty()) {
            System.out.println("Nenhum chamado cadastrado no sistema.\n");
            return;
        }

        System.out.println("Total de chamados: " + listaChamados.size() + "\n");
        System.out.println("┌────┬──────────┬────────────────────┬────────────────┬─────────────┐");
        System.out.println("│ #  │ ID       │ Cliente            │ Status         │ Criticidade │");
        System.out.println("├────┼──────────┼────────────────────┼────────────────┼─────────────┤");
        
        for (int i = 0; i < listaChamados.size(); i++) {
            Chamado c = listaChamados.get(i);
            System.out.printf("│ %2d │ %-8s │ %-18s │ %-14s │ %-11s │\n",
                i + 1, c.getId(), c.getNomeCliente(), c.getStatus(), c.getCriticidade());
        }
        System.out.println("└────┴──────────┴────────────────────┴────────────────┴─────────────┘\n");
    }

    // ==================== REGISTRAR OCORRÊNCIA ====================
    private void registrarOcorrencia(Chamado c) throws IOException {
        if (c.getStatus() == StatusChamado.ENCERRADO) {
            System.out.println("\nErro: Nao e possivel registrar ocorrencias em chamados encerrados.\n");
            return;
        }

        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║               REGISTRAR NOVA OCORRENCIA              ║");
        System.out.println("╚══════════════════════════════════════════════════════╝\n");

        System.out.print("Local/Obra onde ocorreu o problema: ");
        String local = scanner.readLine().trim();

        System.out.print("Titulo da ocorrencia: ");
        String titulo = scanner.readLine().trim();

        System.out.println("\nTipo da ocorrencia:");
        System.out.println("1 - Defeito estrutural");
        System.out.println("2 - Problema eletrico");
        System.out.println("3 - Problema hidraulico");
        System.out.println("4 - Acabamento");
        System.out.println("5 - Outro");
        System.out.print("Escolha: ");
        String opcaoTipo = scanner.readLine().trim();

        String tipo;
        switch (opcaoTipo) {
            case "1": tipo = "Defeito estrutural"; break;
            case "2": tipo = "Problema eletrico";  break;
            case "3": tipo = "Problema hidraulico"; break;
            case "4": tipo = "Acabamento";          break;
            default:  tipo = "Outro";               break;
        }

        System.out.print("Descricao detalhada do problema: ");
        String descricao = scanner.readLine().trim();

        String descricaoCompleta = "[" + tipo + "] " + titulo + " | Local: " + local + " | " + descricao;

        Ocorrencia nova = new Ocorrencia(descricaoCompleta, c.getIdCliente(), c.getNomeCliente());
        c.getOcorrencias().add(nova);
        GerenciadorArquivo.salvar(listaChamados);

        System.out.println("\nOcorrencia registrada com sucesso!");
        System.out.println("ID da ocorrencia: " + nova.getId());
        System.out.println("Tipo            : " + tipo);
        System.out.println("Titulo          : " + titulo);
        System.out.println("Local           : " + local);
        System.out.println("Descricao       : " + descricao + "\n");
    }

    // ==================== RESPONDER OCORRÊNCIA ====================
    private void responderOcorrencia(Chamado c) throws IOException {
        if (c.getOcorrencias().isEmpty()) {
            System.out.println("\nNenhuma ocorrencia registrada para responder.\n");
            return;
        }

        c.exibirOcorrencias();

        System.out.print("\nDigite o ID da ocorrencia para responder (ou Enter para voltar): ");
        String idOcorrencia = scanner.readLine().trim();
        if (idOcorrencia.isEmpty()) return;

        for (Ocorrencia o : c.getOcorrencias()) {
            if (o.getId().equals(idOcorrencia)) {
                System.out.print("Sua resposta: ");
                String resposta = scanner.readLine().trim();
                o.setResposta(resposta);

                System.out.print("Agendar vistoria? (s/n): ");
                if (scanner.readLine().trim().equalsIgnoreCase("s")) {
                    o.agendarVistoria();
                    System.out.println("Vistoria agendada!");
                }

                GerenciadorArquivo.salvar(listaChamados);
                System.out.println("Ocorrencia respondida com sucesso!\n");
                return;
            }
        }
        System.out.println("Ocorrencia nao encontrada!\n");
    }

    // ==================== MENU FUNCIONÁRIO ====================
    private void menuFuncionario() throws IOException {
        boolean ativo = true;
        while (ativo) {
            System.out.println("\n╔══════════════════════════════════════════════════════╗");
            System.out.println("║                   MENU FUNCIONARIO                   ║");
            System.out.println("╚══════════════════════════════════════════════════════╝");
            System.out.println("\nUsuario: " + usuarioLogado.usuario + "\n");
            System.out.println("1 - Criar novo chamado");
            System.out.println("2 - Listar chamados");
            System.out.println("3 - Gerenciar chamado");
            System.out.println("4 - Dashboard de indicadores");
            System.out.println("0 - Logout\n");

            System.out.print("Escolha uma opcao: ");
            String opcao = scanner.readLine().trim();

            switch (opcao) {
                case "1":
                    criarChamado();
                    break;
                case "2":
                    listarChamados();
                    break;
                case "3":
                    selecionarEGerenciarChamado(false);
                    break;
                case "4":
                    new Dashboard(listaChamados).exibir();
                    scanner.readLine();
                    break;
                case "0":
                    System.out.println("\nLogout realizado com sucesso!\n");
                    ativo = false;
                    break;
                default:
                    System.out.println("\nOpcao invalida!\n");
            }
        }
    }

    // ==================== MENU SUPERUSUÁRIO ====================
    private void menuSuperUsuario() throws IOException {
        boolean ativo = true;
        while (ativo) {
            System.out.println("\n╔════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                        MENU SUPERUSUARIO                           ║");
            System.out.println("╚════════════════════════════════════════════════════════════════════╝");
            System.out.println("\nUsuario: " + usuarioLogado.usuario + " (Admin)\n");
            System.out.println("1 - Criar novo chamado");
            System.out.println("2 - Listar chamados");
            System.out.println("3 - Gerenciar chamado");
            System.out.println("4 - Dashboard de indicadores");
            System.out.println("0 - Logout\n");

            System.out.print("Escolha uma opcao: ");
            String opcao = scanner.readLine().trim();

            switch (opcao) {
                case "1":
                    criarChamado();
                    break;
                case "2":
                    listarChamados();
                    break;
                case "3":
                    selecionarEGerenciarChamado(true);
                    break;
                case "4":
                    new Dashboard(listaChamados).exibir();
                    scanner.readLine();
                    break;
                case "0":
                    System.out.println("\nLogout realizado com sucesso!\n");
                    ativo = false;
                    break;
                default:
                    System.out.println("\nOpcao invalida!\n");
            }
        }
    }

    // ==================== SELECIONAR E GERENCIAR CHAMADO ====================
    private void selecionarEGerenciarChamado(boolean isSuperUsuario) throws IOException {
        listarChamados();
        if (listaChamados.isEmpty()) return;

        System.out.print("Digite o ID do chamado: ");
        String idBuscado = scanner.readLine().trim();

        Chamado chamado = null;
        for (Chamado c : listaChamados) {
            if (c.getId().equals(idBuscado)) {
                chamado = c;
                break;
            }
        }

        if (chamado == null) {
            System.out.println("\nChamado nao encontrado!\n");
            return;
        }

        menuGerenciarChamado(chamado, isSuperUsuario);
    }

    // ==================== SUBMENU GERENCIAR CHAMADO ====================
    private void menuGerenciarChamado(Chamado c, boolean isSuperUsuario) throws IOException {
        boolean ativo = true;
        while (ativo) {
            System.out.println("\n╔══════════════════════════════════════════════════════╗");
            System.out.println("║                  GERENCIAR CHAMADO                   ║");
            System.out.println("╚══════════════════════════════════════════════════════╝");
            System.out.println("Chamado #" + c.getId() + " | " + c.getNomeCliente()
                    + " | Status: " + c.getStatus() + " | Criticidade: " + c.getCriticidade());
            System.out.println("Tecnico: " + c.getTecnico() + " | Ocorrencias: " + c.getOcorrencias().size() + "\n");

            System.out.println("1 - Ver detalhes");
            System.out.println("2 - Editar dados");
            System.out.println("3 - Avancar etapa");
            System.out.println("4 - Atribuir tecnico");
            System.out.println("5 - Definir criticidade");
            System.out.println("6 - Anexar documento");
            System.out.println("7 - Encerrar chamado");
            System.out.println("8 - Gerenciar ocorrencias");
            if (isSuperUsuario) {
                System.out.println("9 - Reverter chamado encerrado");
                System.out.println("10 - Deletar chamado");
            }
            System.out.println("0 - Voltar\n");

            System.out.print("Escolha: ");
            String opcao = scanner.readLine().trim();

            switch (opcao) {
                case "1":
                    System.out.println("\n" + c);
                    c.exibirOcorrencias();
                    break;
                case "2":
                    editarChamado(c);
                    break;
                case "3":
                    avancarEtapa(c);
                    break;
                case "4":
                    atribuirTecnico(c);
                    break;
                case "5":
                    definirCriticidade(c);
                    break;
                case "6":
                    anexarDocumento(c);
                    break;
                case "7":
                    encerrarChamado(c);
                    if (c.getStatus() == StatusChamado.ENCERRADO) ativo = false;
                    break;
                case "8":
                    menuOcorrencias(c);
                    break;
                case "9":
                    if (isSuperUsuario) { reverterChamado(c); }
                    else System.out.println("\nOpcao invalida!\n");
                    break;
                case "10":
                    if (isSuperUsuario) {
                        if (deletarChamado(c)) ativo = false;
                    } else System.out.println("\nOpcao invalida!\n");
                    break;
                case "0":
                    ativo = false;
                    break;
                default:
                    System.out.println("\nOpcao invalida!\n");
            }
        }
    }

    // ==================== EDITAR CHAMADO ====================
    private void editarChamado(Chamado c) throws IOException {
        if (c.getStatus() == StatusChamado.ENCERRADO) {
            System.out.println("Erro: Este chamado ja esta encerrado!\n");
            return;
        }

        System.out.println("\n╔════════════════════════════════════════════════════╗");
        System.out.println("║                   EDITAR CHAMADO                   ║");
        System.out.println("╚════════════════════════════════════════════════════╝\n");
        System.out.println("Deixe em branco para manter o valor atual.\n");

        System.out.print("Novo titulo [" + c.getTitulo() + "]: ");
        String titulo = scanner.readLine().trim();
        if (!titulo.isEmpty()) c.setTitulo(titulo);

        System.out.print("Nova descricao [" + c.getDescricao() + "]: ");
        String descricao = scanner.readLine().trim();
        if (!descricao.isEmpty()) c.setDescricao(descricao);

        System.out.print("Nova obra [" + c.getObra() + "]: ");
        String obra = scanner.readLine().trim();
        if (!obra.isEmpty()) c.setObra(obra);

        GerenciadorArquivo.salvar(listaChamados);
        System.out.println("\nChamado atualizado com sucesso!\n");
    }

    // ==================== AVANÇAR ETAPA ====================
    private void avancarEtapa(Chamado c) throws IOException {
        if (c.getStatus() == StatusChamado.ENCERRADO) {
            System.out.println("Erro: Este chamado ja esta encerrado!\n");
            return;
        }
        StatusChamado statusAnterior = c.getStatus();
        c.avancarEtapa();
        if (c.getStatus() != statusAnterior) {
            GerenciadorArquivo.salvar(listaChamados);
            System.out.println("\nStatus atualizado: " + statusAnterior + " -> " + c.getStatus() + "\n");
        }
    }

    // ==================== ATRIBUIR TÉCNICO ====================
    private void atribuirTecnico(Chamado c) throws IOException {
        System.out.print("Nome do tecnico: ");
        String tecnico = scanner.readLine().trim();
        c.setTecnico(tecnico);
        GerenciadorArquivo.salvar(listaChamados);
        System.out.println("\nTecnico " + tecnico + " atribuido ao chamado #" + c.getId() + "\n");
    }

    // ==================== DEFINIR CRITICIDADE ====================
    private void definirCriticidade(Chamado c) throws IOException {
        System.out.println("\n1 - BAIXA  |  2 - MEDIA  |  3 - ALTA");
        System.out.print("Escolha: ");
        String opcao = scanner.readLine().trim();
        switch (opcao) {
            case "2": c.setCriticidade(CriticidadeChamado.MEDIA); break;
            case "3": c.setCriticidade(CriticidadeChamado.ALTA);  break;
            default:  c.setCriticidade(CriticidadeChamado.BAIXA); break;
        }
        GerenciadorArquivo.salvar(listaChamados);
        System.out.println("Criticidade definida: " + c.getCriticidade() + "\n");
    }

    // ==================== ANEXAR DOCUMENTO ====================
    private void anexarDocumento(Chamado c) throws IOException {
        if (c.getStatus() == StatusChamado.ENCERRADO) {
            System.out.println("Erro: Este chamado ja esta encerrado!\n");
            return;
        }
        System.out.print("Descricao do documento: ");
        String descricao = scanner.readLine().trim();
        System.out.print("Caminho do arquivo: ");
        String caminho = scanner.readLine().trim();

        java.io.File arquivo = new java.io.File(caminho);
        if (arquivo.exists()) {
            c.getAnexos().add("[" + descricao + "] " + caminho);
            GerenciadorArquivo.salvar(listaChamados);
            System.out.println("Anexo adicionado com sucesso!\n");
        } else {
            System.out.println("Arquivo nao encontrado!\n");
        }
    }

    // ==================== ENCERRAR CHAMADO ====================
    private void encerrarChamado(Chamado c) throws IOException {
        if (c.podeEncerrar()) {
            c.setStatus(StatusChamado.ENCERRADO);
            GerenciadorArquivo.salvar(listaChamados);
            System.out.println("\nChamado encerrado com sucesso!\n");
        }
    }

    // ==================== REVERTER CHAMADO (superusuário) ====================
    private void reverterChamado(Chamado c) throws IOException {
        if (c.getStatus() != StatusChamado.ENCERRADO) {
            System.out.println("Este chamado nao esta encerrado!\n");
            return;
        }
        c.setStatus(StatusChamado.PAUSADO);
        GerenciadorArquivo.salvar(listaChamados);
        System.out.println("Chamado revertido para PAUSADO!\n");
    }

    // ==================== DELETAR CHAMADO (superusuário) ====================
    private boolean deletarChamado(Chamado c) throws IOException {
        System.out.print("Tem certeza que deseja deletar? (s/n): ");
        if (scanner.readLine().trim().equalsIgnoreCase("s")) {
            listaChamados.remove(c);
            GerenciadorArquivo.salvar(listaChamados);
            System.out.println("Chamado #" + c.getId() + " deletado com sucesso!\n");
            return true;
        }
        return false;
    }

    // ==================== SUBMENU OCORRÊNCIAS ====================
    private void menuOcorrencias(Chamado c) throws IOException {
        boolean ativo = true;
        while (ativo) {
            System.out.println("\n╔══════════════════════════════════════════════════════╗");
            System.out.println("║               GERENCIAR OCORRENCIAS                  ║");
            System.out.println("╚══════════════════════════════════════════════════════╝");
            System.out.println("Chamado #" + c.getId() + " | " + c.getNomeCliente()
                    + " | Ocorrencias: " + c.getOcorrencias().size() + "\n");
            System.out.println("1 - Registrar nova ocorrencia");
            System.out.println("2 - Listar ocorrencias");
            System.out.println("3 - Responder ocorrencia");
            System.out.println("0 - Voltar\n");
            System.out.print("Escolha: ");
            String opcao = scanner.readLine().trim();

            switch (opcao) {
                case "1":
                    registrarOcorrencia(c);
                    break;
                case "2":
                    if (c.getOcorrencias().isEmpty()) {
                        System.out.println("\nNenhuma ocorrencia registrada.\n");
                    } else {
                        c.exibirOcorrencias();
                    }
                    break;
                case "3":
                    responderOcorrencia(c);
                    break;
                case "0":
                    ativo = false;
                    break;
                default:
                    System.out.println("\nOpcao invalida!\n");
            }
        }
    }
}