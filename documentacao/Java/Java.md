# 📘 DOCUMENTAÇÃO JAVA

Sistema desktop Java Swing para gestão de eventos acadêmicos, com cadastro de cursos, palestrantes, eventos e controle de inscrições. Utiliza MySQL como banco de dados e segue arquitetura em camadas (DAO, Service, GUI).

---

## 🗂️ Estrutura do Projeto
src/main/java/grupo12/
├── DAO/ → Camada de acesso a dados
├── GUI/ → Interfaces gráficas (Swing)
├── MODEL/ → Entidades do sistema
├── SERVICE/ → Lógica de negócio
├── UTIL/ → Utilitários
├── Main.java → Ponto de entrada
pom.xml → Dependências Maven
uploads/ → Armazenamento de imagens

---

## 🧩 MODELOS (ENTIDADES)
| Classe        | Descrição                                     | Campos Principais                                           |
|---------------|-----------------------------------------------|-------------------------------------------------------------|
| `Cursos`      | Representa cursos acadêmicos                  | `id`, `nome`                                                |
| `Eventos`     | Armazena informações de eventos               | `id`, `nome`, `dataInicio`, `hora`, ...                     |
| `Inscricoes`  | Gerencia participação de usuários em eventos | `id`, `usuario`, `evento`, `dataInscricao`, `aprovado`      |
| `Palestrantes`| Cadastro de palestrantes                      | `id`, `nome`, `miniCurriculo`, `tema`, `fotoUrl`            |
| `Usuarios`    | Usuários do sistema (alunos/professores)      | `id`, `fotoUrl`, `nome`, `email`, `senhaHash`, `curso`      |

---

## 🗃️ Camadas DAO (Acesso a Dados)
**Principais funcionalidades:**
| Classe           | Método-chave                      | Descrição                                       |
|------------------|-----------------------------------|-------------------------------------------------|
| `CursosDao`      | `insert(), update(), selectAll()` | CRUD para cursos                                |
| `EventosDao`     | `existeConflitoDeHorario()`       | Valida sobreposição de horários                 |
| `InscricoesDao`  | `updateStatusAprovacao()`         | Atualiza presença (1) ou falta (0)              |
| `PalestrantesDao`| `buildFromResultSet()`            | Converte ResultSet em objeto `Palestrantes`     |

### 🔎 Exemplo de Insert (EventosDao.java)
```java
public Boolean insert(Eventos evento) {
    String sql = "INSERT INTO eventos (nome, data_inicio, hora, ...) VALUES (?, ?, ...)";
    // Implementação...
}
```

---

## 📄 CAMADA SERVICE (REGRA DE NEGOCIOS)
**VALIDACAO LOGICA**
| SERVICO               |FUNCIONALIDADE               |
|-----------------------|-----------------------------|
| EventosService        |Valida conflitos de horário  |
|                       |Verifica campos obrigatórios |
| InscricoesService     |Aprova/reprova inscrições    |
| PalestrantesService   |Valida mini-currículo e tema |

**Exemplo (EventosService)**:
```java
public Boolean salvar(Eventos evento) {
    if (evento.getNome() == null) {
        System.err.println("Nome é obrigatório!");
        return false;
    }
    return dao.insert(evento);
}
```

# NTERFACES GRAFICAS (GUI)
**TELAS PRINCIPAIS**
|CLASSE             |DESCRIÇÃO                                            |CAMPONENTES-CHAVE                                      |
|-------------------|-----------------------------------------------------|-------------------------------------------------------|
| PrincipalGui      |Menu principal com acesso a todas funcionalidades    |`JMenuBar` com opções de cadastro                        |
| EventosGui        |Cadastro de eventos com upload de foto               |`JTable, JFileChooser, campos de data/hora`            |
| InscricoesGui     |Controle de presença em eventos                      |`Botões`, `Marcar Presença/Falta`, `tabela com status`  |
| PalestrantesGui   |Cadastro de palestrantes com mini-currículo          |`Área de texto para currículo, upload de foto`         |

*FLUXO TÍPICO
1. Usuário seleciona um evento na tabela
2. Campos são preenchidos automaticamente
3. Edição e salvamento via EventosService

# RELACIONAMENTOS ENTRE ENTIDADES
Cursos        ──< Eventos         (1:N)
Palestrantes  ──< Eventos         (1:N)
Eventos       ──< Inscricoes      (1:N)
Usuarios      ──< Inscricoes      (1:N)
Cursos        ──< Usuarios        (1:N)


# ESTRUTURA DO BANCO DE DADOS (MYSQL)
| TABELA        |DESCRIÇÃO                        |CHAVES ESTRANGEIRAS        | 
|---------------|---------------------------------|---------------------------|
| cursos        |Cursos oferecidos                |-                          |
| eventos	      |Eventos com data, hora e local   |`curso_id, palestrante_id` |
| inscricoes    |Relacionamento usuário-eventos   |`usuario_id, evento_id`    |
| palestrantes  |Informações de palestrantes      |-                          |
| usuarios      |Cadastro de usuários             |`curso_id`                 |

# CONFIGURACAO TECNICA
-Java 17 com Swing para interface
-MySQL 8.0 (JDBC)
-Maven para dependências:
  <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>8.0.33</version>
  </dependency>

# UTILITÁRIOS
**FileUtils**
*Copia imagens para a pasta uploads/
*Valida tipos de arquivo (JPG, PNG)
```java
  public static String copiarImagem(File imagem, String destino) throws IOException {
    Path destinoPath = Paths.get(destino, imagem.getName());
    Files.copy(imagem.toPath(), destinoPath);
    return destinoPath.toString();
  }
```
# COMO EXECUTAR
**1. Configure o MySQL em Dao.java:**
*private static final String URL = "jdbc:mysql://localhost:3306/hackata?useTimezone=true&serverTimezone=UTC-3";
*private static final String USER = "root";
*private static final String PASSWORD = "root";

**2. Execute `Main.java`**
**3. Use o menu principal para acessar as funcionalidades** 